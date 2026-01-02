package rating.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rating.engine.dto.BillingLineDto;
import rating.engine.mapper.BillingLineMapper;
import rating.engine.persistence.BillingLineEntity;
import rating.engine.persistence.BillingLineRepository;
import rating.engine.persistence.BillingLineSparkRepository;
import rating.engine.persistence.ProductEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.RoundingMode.HALF_UP;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingLineService {

    @Value("${rating-engine.storage.write-engine}")
    private String writeEngine;

    private final ObjectMapper objectMapper;
    private final BillingLineRepository billingLineRepository;
    private final BillingLineSparkRepository billingLineSparkRepository;
    private final BillingLineMapper billingLineMapper;
    private final ProductService productService;

    public void saveBillingLinesBatch(List<String> values) {
        if (values.isEmpty()) {
            return;
        }

        List<BillingLineEntity> entities = new ArrayList<>(values.size());
        int parseErrors = 0;

        for (String value : values) {
            try {
                BillingLineDto billingLineDto = objectMapper.readValue(value, BillingLineDto.class);
                ProductEntity product = productService.getProduct(billingLineDto.getProductId());
                BillingLineEntity billingLineEntity = billingLineMapper.convertToBillingLineEntity(billingLineDto);
                BigDecimal result = billingLineDto.getConsumption()
                        .multiply(product.getPrice())
                        .multiply(product.getCoefficient())
                        .add(product.getMonthlyFee())
                        .setScale(2, HALF_UP);
                billingLineEntity.setPrice(result);
                entities.add(billingLineEntity);
            } catch (Exception e) {
                log.error("Failed to convert billing line", e);
                parseErrors++;
            }
        }

        if (!entities.isEmpty()) {
            if (writeEngine.equalsIgnoreCase("spark")) {
                billingLineSparkRepository.saveAll(entities);
            }
            if (writeEngine.equalsIgnoreCase("trino")) {
                billingLineRepository.saveAll(entities);
            }
            log.info("Saved batch of {} billing lines ({} failed to parse)", entities.size(), parseErrors);
        } else {
            log.warn("No valid billing lines to save from batch of {}", values.size());
        }

    }

}
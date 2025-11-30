package rating.engine.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import rating.engine.service.BillingLineService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BillingLineDataConsumer {

    private final BillingLineService billingLineService;

    @KafkaListener(
            topics = "${spring.kafka.consumer.billing-line-topic}",
            clientIdPrefix = "${spring.kafka.client-id}",
            containerFactory = "billingLineDataKafkaListener"
    )
    public void receiveBillingLineData(List<String> values) {
        billingLineService.saveBillingLinesBatch(values);
    }

}

package rating.engine.persistence;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;


@Data
@Builder
public class BillingLineEntity {

    private Long id;

    private String contractId;

    private Instant startDate;

    private Instant endDate;

    private String productId;

    private BigDecimal price;

    private Instant createdDate;

}

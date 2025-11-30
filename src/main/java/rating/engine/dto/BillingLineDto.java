package rating.engine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rating.engine.persistence.BillingLineStatus;

import java.math.BigDecimal;
import java.time.Instant;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingLineDto {

    private Long id;

    private String contractId;

    private Instant startDate;

    private Instant endDate;

    private String productId;

    private BigDecimal consumption;

    private BillingLineStatus status;

}

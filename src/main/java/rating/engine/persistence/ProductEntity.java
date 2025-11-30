package rating.engine.persistence;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
public class ProductEntity {

    private Long id;

    private String productId;

    private BigDecimal price;

    private String type;

    private BigDecimal coefficient;

    private String formula;

    private BigDecimal monthlyFee;

}

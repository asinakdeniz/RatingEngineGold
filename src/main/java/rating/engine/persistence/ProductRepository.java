package rating.engine.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final JdbcClient jdbcClient;

    public Optional<ProductEntity> findByProductId(String productId) {
        return jdbcClient.sql("""
                        SELECT id, product_id, price, type, 
                               coefficient, formula, monthly_fee
                        FROM rating_engine.product
                        WHERE product_id = :productId
                        """)
                .param("productId", productId)
                .query((rs, rowNum) -> ProductEntity.builder()
                        .id(rs.getLong("id"))
                        .productId(rs.getString("product_id"))
                        .price(rs.getBigDecimal("price"))
                        .type(rs.getString("type"))
                        .coefficient(rs.getBigDecimal("coefficient"))
                        .formula(rs.getString("formula"))
                        .monthlyFee(rs.getBigDecimal("monthly_fee"))
                        .build())
                .optional();
    }

}

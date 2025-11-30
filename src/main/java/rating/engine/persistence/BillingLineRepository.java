package rating.engine.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BillingLineRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<BillingLineEntity> entities) {
        if (entities.isEmpty()) {
            return;
        }

        StringJoiner valuesJoiner = new StringJoiner(", ");
        List<Object> params = new ArrayList<>(entities.size() * 7);

        for (BillingLineEntity entity : entities) {
            valuesJoiner.add("(?, ?, ?, ?, ?, ?, ?)");
            params.add(entity.getId());
            params.add(entity.getContractId());
            params.add(Timestamp.from(entity.getStartDate()));
            params.add(Timestamp.from(entity.getEndDate()));
            params.add(entity.getProductId());
            params.add(entity.getPrice());
            params.add(Timestamp.from(entity.getCreatedDate()));
        }

        String sql = """
                INSERT INTO rating_engine.billing_line_gold 
                (id, contract_id, start_date, end_date, product_id, price, created_date)
                VALUES 
                """ + valuesJoiner;

        jdbcTemplate.update(sql, params.toArray());
    }

}

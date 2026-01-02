package rating.engine.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.analysis.NoSuchTableException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingLineSparkRepository {

    private final SparkSession spark;

    public void saveAll(List<BillingLineEntity> entities) {
        if (entities.isEmpty()) {
            return;
        }

        // Convert entities to Spark Dataset
        Dataset<Row> df = spark.createDataFrame(entities, BillingLineEntity.class)
                .withColumnRenamed("id", "id")
                .withColumnRenamed("contractId", "contract_id")
                .withColumnRenamed("startDate", "start_date")
                .withColumnRenamed("endDate", "end_date")
                .withColumnRenamed("productId", "product_id")
                .withColumnRenamed("price", "price")
                .withColumnRenamed("createdDate", "created_date");

        try {
            df.writeTo("gold.rating_engine.billing_line_gold").append();
        } catch (NoSuchTableException e) {
            // do nothing
        }
    }
}
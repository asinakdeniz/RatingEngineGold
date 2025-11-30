package rating.engine.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rating.engine.dto.BillingLineDto;
import rating.engine.persistence.BillingLineEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = IGNORE
)
public interface BillingLineMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    BillingLineEntity convertToBillingLineEntity(BillingLineDto billingLineDto);

}

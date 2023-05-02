package com.schauzov.crudapp.mapper;

import com.schauzov.crudapp.dto.ProductPriceDTO;
import com.schauzov.crudapp.entity.ProductPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductPriceMapper {
    Set<ProductPriceDTO> toDtoSet(Set<ProductPriceEntity> productPrices);
}

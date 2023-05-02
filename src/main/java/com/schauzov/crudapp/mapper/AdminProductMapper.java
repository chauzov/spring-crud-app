package com.schauzov.crudapp.mapper;

import com.schauzov.crudapp.dto.AdminProductDTO;
import com.schauzov.crudapp.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {ProductInfoMapper.class, ProductPriceMapper.class})
public interface AdminProductMapper {
    AdminProductDTO toDto(ProductEntity product);
}

package com.schauzov.crudapp.mapper;

import com.schauzov.crudapp.dto.ProductInfoDTO;
import com.schauzov.crudapp.entity.ProductInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductInfoMapper {
    Set<ProductInfoDTO> toDtoSet(Set<ProductInfoEntity> productInfo);
}

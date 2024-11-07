package com.smg.product.mapper;

import com.smg.product.dto.ProductCreateDTO;
import com.smg.product.dto.ProductDTO;
import com.smg.product.dto.ProductKafkaDTO;
import com.smg.product.dto.ProductUpdateDTO;
import com.smg.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = ProductMapperConfig.class)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product map(ProductCreateDTO createDTO);

    ProductDTO map(Product product);

    ProductKafkaDTO mapToKafka(Product product);

    List<ProductDTO> map(List<Product> products);

    @Mapping(target = "id", ignore = true)
    void updateProductFromDto(ProductUpdateDTO updateDTO, @MappingTarget Product product);
}
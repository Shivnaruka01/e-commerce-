package com.Springer.Gojo.mapper;

import org.mapstruct.Mapper;

import com.Springer.Gojo.Dto.product.request.ProductRequest;
import com.Springer.Gojo.Dto.product.response.ProductResponse;
import com.Springer.Gojo.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

	ProductResponse toResponse(Product product);

	default Product toEntity(ProductRequest request) {
		return Product.createProduct(request.name(), request.description(), request.price(), request.stock());
	}

	// NOT needed if using Domain Driven Design

//	@Mapping(target = "id", ignore = true)
//	void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}

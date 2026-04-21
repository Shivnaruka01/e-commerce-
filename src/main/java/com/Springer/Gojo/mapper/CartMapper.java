package com.Springer.Gojo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.Springer.Gojo.Dto.Cart.CartItemResponse;
import com.Springer.Gojo.Dto.Cart.CartResponse;
import com.Springer.Gojo.entity.Cart;
import com.Springer.Gojo.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

	@Mapping(target = "cartId", source = "id")
	CartResponse toResponse(Cart cart);

	@Mapping(target = "productId", source = "product.id")
	@Mapping(target = "productName", source = "product.name")
	@Mapping(target = "price", source = "product.price")
	CartItemResponse toItemResponse(CartItem item);
}

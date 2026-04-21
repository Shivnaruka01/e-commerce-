package com.Springer.Gojo.Dto.product.response;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String description, BigDecimal price, int stock) {

}

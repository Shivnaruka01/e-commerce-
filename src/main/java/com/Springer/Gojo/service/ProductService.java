package com.Springer.Gojo.service;

import java.util.List;

import com.Springer.Gojo.Dto.product.request.ProductRequest;
import com.Springer.Gojo.Dto.product.response.ProductResponse;

public interface ProductService {

	ProductResponse createProduct(ProductRequest request);

	List<ProductResponse> getAllProducts();

	ProductResponse getProductById(Long id);

	ProductResponse updateProduct(Long id, ProductRequest request);

	void deleteProduct(Long id);
}

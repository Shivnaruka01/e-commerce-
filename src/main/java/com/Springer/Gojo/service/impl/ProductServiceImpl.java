package com.Springer.Gojo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Springer.Gojo.Dto.product.request.ProductRequest;
import com.Springer.Gojo.Dto.product.response.ProductResponse;
import com.Springer.Gojo.entity.Product;
import com.Springer.Gojo.exception.ResourceNotFoundException;
import com.Springer.Gojo.mapper.ProductMapper;
import com.Springer.Gojo.repository.ProductRepository;
import com.Springer.Gojo.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final ProductMapper mapper;

	@Override
	public ProductResponse createProduct(ProductRequest request) {
		Product product = mapper.toEntity(request);
		return mapper.toResponse(productRepository.save(product));
	}

	@Override
	public List<ProductResponse> getAllProducts() {
		return productRepository.findAll().stream().map(mapper::toResponse).toList();
	}

	@Override
	public ProductResponse getProductById(Long id) {
		/*
		 * Optional<Product> product= repository.findById(id);
		 * 
		 * if(product.isPresent()) { return mapper.toResponse(product.get());
		 * 
		 * } throw new ResourceNotFoundException("Product not found with id: " + id);
		 */

		return productRepository.findById(id).map(mapper::toResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

	}

	@Override
	public ProductResponse updateProduct(Long id, ProductRequest request) {
//		return repository
//						.findById(id)
//						.map(existingProduct -> {
//							mapper.updateProductFromRequest(request, existingProduct);
//							return repository.save(existingProduct);
//						})
//						.map(mapper::toResponse)
//						.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		return productRepository.findById(id).map(existingProduct -> {
			// Domain logic instead of Mapper logic
			existingProduct.updateDetails(request.name(), request.description());
			existingProduct.adjustPrice(request.price());

			if (request.stock() != null) {
				int diff = request.stock() - existingProduct.getStock();

				if (diff > 0)
					existingProduct.restock(diff);
				else if (diff < 0)
					existingProduct.sell(-diff);
			}
			return productRepository.save(existingProduct);

		}).map(mapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

	@Override
	public void deleteProduct(Long id) {

		productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
			throw new ResourceNotFoundException("Product not found with id: " + id);
		});

	}

}

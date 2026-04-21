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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final ProductMapper mapper;

	@Override
	public ProductResponse createProduct(ProductRequest request) {
		Product product = mapper.toEntity(request);
		Product savedProduct = productRepository.save(product);
		log.atInfo()
			.setMessage("New product cataloged")
			.addKeyValue("productId", savedProduct.getId())
			.addKeyValue("name", savedProduct.getName())
			.addKeyValue("initialPrice", savedProduct.getPrice())
			.log();
			return mapper.toResponse(savedProduct);
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

			// Logic: Log old values before updating for an Audit Trail
			log.atInfo()
				.setMessage("Updating product details")
				.addKeyValue("productId", id)
				.addKeyValue("oldPrice", existingProduct.getPrice())
	            .addKeyValue("newPrice", request.price())
	            .log();
			
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

		productRepository.findById(id).ifPresentOrElse(p -> {
			productRepository.delete(p);
			log.atWarn()
				.setMessage("Product removed from catalog")
				.addKeyValue("productId", id)
				.log();
		}, () -> { throw new ResourceNotFoundException("Product not found");
			
		});

	}

}

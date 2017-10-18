package com.store.app.product.services.impl;

import java.util.List;

import javax.validation.Validator;

import com.store.app.common.utils.ValidationUtils;
import com.store.app.product.exception.ProductExistentException;
import com.store.app.product.exception.ProductNotFoundException;
import com.store.app.product.model.Product;
import com.store.app.product.repository.ProductRepository;
import com.store.app.product.services.ProductServices;

public class ProductServicesImpl implements ProductServices {

	ProductRepository productRepository;
	Validator validator;

	@Override
	public Product add(final Product product) {
		validateProduct(product);
		return productRepository.add(product);
	}

	@Override
	public void update(final Product product) {
		validateProduct(product);
		if (!productRepository.existsById(product.getId())) {
			throw new ProductNotFoundException();
		}
		productRepository.update(product);
	}

	@Override
	public Product findById(final Long id) {
		Product product = productRepository.findById(id);
		if (null == product) {
			throw new ProductNotFoundException();
		}
		return product;
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll("name");
	}

	private void validateProduct(final Product product) {
		ValidationUtils.validateEntityFields(validator, product);
		if (productRepository.alreadyExists(product)) {
			throw new ProductExistentException();
		}
	}
}

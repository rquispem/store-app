package com.store.app.product.services.impl;

import java.util.List;

import javax.validation.Validator;

import com.store.app.common.utils.ValidationUtils;
import com.store.app.product.exception.ProductExistentException;
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
		// TODO Auto-generated method stub

	}

	@Override
	public Product findById(final Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	private void validateProduct(final Product product) {
		ValidationUtils.validateEntityFields(validator, product);
		if (productRepository.alreadyExists(product)) {
			throw new ProductExistentException();
		}
	}
}

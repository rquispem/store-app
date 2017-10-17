package com.store.app.product.services.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.store.app.common.exception.FieldNotValidException;
import com.store.app.product.model.Product;
import com.store.app.product.repository.ProductRepository;
import com.store.app.product.services.ProductServices;

public class ProductServicesUTest {

	private ProductServices productServices;
	private ProductRepository productRepository;
	// private Validator validator;

	@Before
	public void init() {
		// validator = Validation.buildDefaultValidatorFactory().getValidator();

		productRepository = mock(ProductRepository.class);
		productServices = new ProductServicesImpl();
	}

	@Test
	public void addProductWithNullName() {
		addProductWithInvalidName(null);
	}

	private void addProductWithInvalidName(final String name) {
		try {
			Product newProduct = new Product();
			newProduct.setName(name);
			productServices.add(newProduct);
			fail("An error should have been thrown");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}

	}
}

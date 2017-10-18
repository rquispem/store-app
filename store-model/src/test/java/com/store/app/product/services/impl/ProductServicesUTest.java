package com.store.app.product.services.impl;

import static com.store.app.commontests.product.ProductForTestRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.store.app.common.exception.FieldNotValidException;
import com.store.app.product.exception.ProductExistentException;
import com.store.app.product.model.Product;
import com.store.app.product.repository.ProductRepository;
import com.store.app.product.services.ProductServices;

public class ProductServicesUTest {

	private ProductServices productServices;
	private ProductRepository productRepository;
	private Validator validator;

	@Before
	public void init() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		productRepository = mock(ProductRepository.class);
		productServices = new ProductServicesImpl();
		((ProductServicesImpl) productServices).validator = validator;
		((ProductServicesImpl) productServices).productRepository = productRepository;
	}

	@Test
	public void addProductWithNullName() {
		addProductWithInvalidName(null);
	}

	@Test
	public void addProductWithShortName() {
		addProductWithInvalidName("A");
	}

	@Test
	public void addProductWithLongName() {
		addProductWithInvalidName("This is a long name that will cause an exception to be thrown");
	}

	@Test(expected = ProductExistentException.class)
	public void addProductWithExistentName() {
		when(productRepository.alreadyExists(diesel())).thenReturn(true);
		productServices.add(diesel());
	}

	@Test
	public void addValidProduct() {
		when(productRepository.alreadyExists(diesel())).thenReturn(false);
		when(productRepository.add(diesel())).thenReturn(productWithId(diesel(), 1l));

		final Product productAdded = productServices.add(diesel());
		assertThat(productAdded.getId(), is(equalTo(1l)));
	}

	private void addProductWithInvalidName(final String name) {
		try {
			final Product newProduct = new Product();
			newProduct.setName(name);
			productServices.add(newProduct);
			fail("An error should have been thrown");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}

	}
}

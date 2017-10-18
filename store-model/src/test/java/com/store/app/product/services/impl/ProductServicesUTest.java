package com.store.app.product.services.impl;

import static com.store.app.commontests.product.ProductForTestRepository.adidas;
import static com.store.app.commontests.product.ProductForTestRepository.diesel;
import static com.store.app.commontests.product.ProductForTestRepository.iphone;
import static com.store.app.commontests.product.ProductForTestRepository.macbook;
import static com.store.app.commontests.product.ProductForTestRepository.nike;
import static com.store.app.commontests.product.ProductForTestRepository.productWithId;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.store.app.common.exception.FieldNotValidException;
import com.store.app.product.exception.ProductExistentException;
import com.store.app.product.exception.ProductNotFoundException;
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

	@Test
	public void addProductWithNullPrice() {
		addProductWithInvalidPrice(null);
	}

	@Test
	public void addProductWithNegativePrice() {
		addProductWithInvalidPrice(-10.0);
	}

	@Test
	public void updateProductWithNullName() {
		updateProductWithInvalidName(null);
	}

	@Test
	public void updateProductWithShortName() {
		updateProductWithInvalidName("A");
	}

	@Test
	public void updateProductWithLongName() {
		updateProductWithInvalidName("This is a long name that will cause an exception to be thrown");
	}

	@Test(expected = ProductExistentException.class)
	public void updateProductWithExistentName() {
		when(productRepository.alreadyExists(productWithId(diesel(), 1l))).thenReturn(true);
		productServices.update(productWithId(diesel(), 1l));
	}

	@Test(expected = ProductNotFoundException.class)
	public void updateProductNotFound() {
		when(productRepository.alreadyExists(productWithId(diesel(), 1l))).thenReturn(false);
		when(productRepository.existsById(1l)).thenReturn(false);
		productServices.update(productWithId(diesel(), 1l));
	}

	@Test
	public void updateValidProduct() {

		when(productRepository.alreadyExists(productWithId(diesel(), 1l))).thenReturn(false);
		when(productRepository.existsById(1l)).thenReturn(true);

		productServices.update(productWithId(diesel(), 1l));

		verify(productRepository).update(productWithId(diesel(), 1l));
	}

	@Test
	public void findProductById() {
		when(productRepository.findById(1l)).thenReturn(productWithId(diesel(), 1l));

		final Product productFound = productServices.findById(1l);

		assertThat(productFound, is(notNullValue()));
		assertThat(productFound.getId(), is(equalTo(1l)));
		assertThat(productFound.getName(), is(equalTo(diesel().getName())));
	}

	@Test(expected = ProductNotFoundException.class)
	public void findProductByIdNotFound() {
		when(productRepository.findById(1l)).thenReturn(null);
		productServices.findById(1l);
	}

	@Test
	public void findAllNoProducts() {
		when(productRepository.findAll("name")).thenReturn(new ArrayList<>());

		List<Product> products = productServices.findAll();
		assertThat(products.isEmpty(), is(equalTo(true)));
	}

	@Test
	public void findAllProducts() {
		when(productRepository.findAll("name"))
				.thenReturn(Arrays.asList(adidas(), diesel(), iphone(), nike(), macbook()));

		List<Product> products = productServices.findAll();

		assertThat(products.size(), is(equalTo(5)));
		assertThat(products.get(0), is(equalTo(adidas())));
		assertThat(products.get(1), is(equalTo(diesel())));
		assertThat(products.get(2), is(equalTo(iphone())));
		assertThat(products.get(3), is(equalTo(nike())));
		assertThat(products.get(4), is(equalTo(macbook())));
	}

	private void updateProductWithInvalidName(final String name) {
		try {
			Product product = diesel();
			product.setName(name);
			productServices.update(product);
			fail("An error should be throw");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is((equalTo("name"))));
		}
	}

	private void addProductWithInvalidPrice(Double price) {
		try {
			Product newProduct = diesel();
			newProduct.setPrice(price);
			productServices.add(newProduct);
			fail("An error should have been thrown");
		} catch (final FieldNotValidException e) {
			System.out.println(e.getFieldName());
			assertThat(e.getFieldName(), is(equalTo("price")));
		}
	}

	private void addProductWithInvalidName(final String name) {
		try {
			final Product newProduct = diesel();
			newProduct.setName(name);
			productServices.add(newProduct);
			fail("An error should have been thrown");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(not(equalTo(null))));
		}
	}
}

package com.store.app.product.resource;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.store.app.product.services.ProductServices;

public class ProductResourceUTest {

	private ProductResource productResource;
	
	@Mock
	private ProductServices productServices;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		
		productResource = new ProductResource();
		productResource.productServices = productServices;
	}
}

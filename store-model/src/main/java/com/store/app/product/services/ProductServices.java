package com.store.app.product.services;

import java.util.List;

import com.store.app.product.model.Product;

public interface ProductServices {

	Product add(Product product);

	void update(Product product);

	Product findById(Long id);

	List<Product> findAll();
}

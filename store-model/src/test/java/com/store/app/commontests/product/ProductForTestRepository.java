package com.store.app.commontests.product;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;

import com.store.app.product.model.Product;

@Ignore
public class ProductForTestRepository {

	public static Product diesel() {
		return new Product("Diesel watch", "Casual Watch", 125.99, 15);
	}

	public static Product nike() {
		return new Product("Nike 90", "Sport trainers", 50.00, 10);
	}

	public static Product adidas() {
		return new Product("Adidas total", "Sport trainers", 75.00, 0);
	}

	public static Product macbook() {
		return new Product("Macbook Pro", "Retina mac 512gb", 1599.00, 6);
	}

	public static Product iphone() {
		return new Product("Iphone", "Iphone X Gray", 999.00, 2);
	}
	
	public static List<Product> allProducts() {
		return Arrays.asList(diesel(), nike(), adidas(), macbook(), iphone());
	}
}

package com.store.app.product.repository;

import static com.store.app.commontests.product.ProductForTestRepository.adidas;
import static com.store.app.commontests.product.ProductForTestRepository.allProducts;
import static com.store.app.commontests.product.ProductForTestRepository.diesel;
import static com.store.app.commontests.product.ProductForTestRepository.iphone;
import static com.store.app.commontests.product.ProductForTestRepository.macbook;
import static com.store.app.commontests.product.ProductForTestRepository.nike;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.store.app.commontests.utils.DBCommandTransactionalExecutor;
import com.store.app.product.model.Product;

public class ProductRepositoryUTest {

	private EntityManagerFactory emf;
	private EntityManager em;
	private ProductRepository productRepository;
	private DBCommandTransactionalExecutor dbCommandExecutor;

	@Before
	public void init() {
		emf = Persistence.createEntityManagerFactory("storePU");
		em = emf.createEntityManager();

		productRepository = new ProductRepository();
		productRepository.setEm(em);

		dbCommandExecutor = new DBCommandTransactionalExecutor(em);
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	@Test
	public void addProductAndFindIt() {

		final Long productAddedId = dbCommandExecutor.executeCommand(() -> {
			return productRepository.add(diesel()).getId();
		});

		assertThat(productAddedId, is(notNullValue()));

		final Product product = productRepository.findById(productAddedId);

		assertThat(product, is(notNullValue()));
		assertThat(product.getName(), is(equalTo(diesel().getName())));
		assertThat(product.getDescription(), is(equalTo(diesel().getDescription())));
		assertThat(product.getPrice(), is(equalTo(diesel().getPrice())));
		assertThat(product.getStock(), is(equalTo(diesel().getStock())));
	}

	@Test
	public void findProductByIdNotFound() {
		Product product = productRepository.findById(999l);
		assertThat(product, is(nullValue()));
	}

	@Test
	public void findProductByIdWithNullId() {
		Product product = productRepository.findById(null);
		assertThat(product, is(nullValue()));
	}

	@Test
	public void updateProduct() {

		final Long productAddedId = dbCommandExecutor.executeCommand(() -> {
			return productRepository.add(diesel()).getId();
		});

		Product productAfterAdd = productRepository.findById(productAddedId);
		assertThat(productAfterAdd.getName(), is(equalTo(diesel().getName())));

		productAfterAdd.setName(iphone().getName());

		dbCommandExecutor.executeCommand(() -> {
			productRepository.update(productAfterAdd);
			return null;
		});

		final Product productAfterUpdate = productRepository.findById(productAddedId);

		assertThat(productAfterUpdate.getName(), is(equalTo(iphone().getName())));
	}

	@Test
	public void findAllProducts() {
		dbCommandExecutor.executeCommand(() -> {
			allProducts().forEach(productRepository::add);
			return null;
		});

		final List<Product> products = productRepository.findAll("name");

		assertThat(products.size(), is(equalTo(5)));
		assertThat(products.get(0).getName(), is(equalTo(adidas().getName())));
		assertThat(products.get(1).getName(), is(equalTo(diesel().getName())));
		assertThat(products.get(2).getName(), is(equalTo(iphone().getName())));
		assertThat(products.get(3).getName(), is(equalTo(macbook().getName())));
		assertThat(products.get(4).getName(), is(equalTo(nike().getName())));
	}
	
	@Test
	public void alreadyExistsForAdd() {
		final Product addedProduct = 
		dbCommandExecutor.executeCommand(() -> {
			return productRepository.add(diesel());
		});
		
		assertThat(productRepository.alreadyExists(addedProduct), is(equalTo(true)));
		assertThat(productRepository.alreadyExists(adidas()), is(equals(false)));
	}

	@Test
	public void existById() {
		final Long addedProductId = dbCommandExecutor.executeCommand(() -> {
			return productRepository.add(diesel()).getId();
		});
		
		assertThat(productRepository.existsById(addedProductId), is(equalTo(true)));
		assertThat(productRepository.existsById(999l), is(equalTo(false)));
	}
}

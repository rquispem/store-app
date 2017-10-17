package com.store.app.product.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.store.app.product.model.Product;

public class ProductRepository {

	private EntityManager em;

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public Product add(Product product) {
		em.persist(product);
		return product;
	}

	public Product findById(Long productId) {
		if (null == productId) {
			return null;
		}
		return em.find(Product.class, productId);
	}

	public void update(Product product) {
		em.merge(product);
	}

	@SuppressWarnings("unchecked")
	public List<Product> findAll(String orderField) {
		return em.createQuery("Select e From Product e Order by e." + orderField).getResultList();
	}

	public boolean alreadyExists(Product product) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("Select 1 from Product e where e.name = :propertyValue");
		if (null != product.getId()) {
			jpql.append(" and e.id = :id");
		}

		final Query query = em.createQuery(jpql.toString());
		query.setParameter("propertyValue", product.getName());
		if (null != product.getId()) {
			query.setParameter("id", product.getId());
		}

		return query.setMaxResults(1).getResultList().size() > 0;
	}

	public boolean existsById(Long productId) {
		return em.createQuery("Select 1 From Product e where e.id = :id").setParameter("id", productId).setMaxResults(1)
				.getResultList().size() > 0;
	}
}

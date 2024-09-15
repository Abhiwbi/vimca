package com.example.vimca.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vimca.GlobalException.MyException;
import com.example.vimca.Model.Product;
import com.example.vimca.Repository.ProductReposoitory;

@Service
public class ProductService {
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ProductReposoitory productRepository;

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	public Product createProduct(Product product) {
		logger.info("Creating new product with name: {}", product.getProductName());
		return productRepository.save(product);
	}

	public Product getProductById(Long productId) {
		logger.info("Fetching product with ID: {}", productId);

		return productRepository.getProductByid(productId, false);
	}

	public Iterable<Product> findAllProduct(boolean isDeleted) {
		Session session = entityManager.unwrap(Session.class);
		Filter filter = session.enableFilter("deletedProductFilter");
		filter.setParameter("isDeleted", isDeleted);
		Iterable<Product> appUserEntities = productRepository.findAll();
		session.disableFilter("deletedProductFilter");
		return appUserEntities;
	}

	public void deleteProductById(Long id) {
		try {
			Product appUser = productRepository.getProductByid(id, false);
			if (appUser == null) {
				throw new MyException("Product not present to delete");
			}
			logger.info("Deleting product with ID: {}", id);

			productRepository.deleteById(id);
		} catch (Exception e) {
			System.err.println("An error occurred while deleting the product: " + e.getMessage());
		}
	}

	public Product updateProduct(Long id, Product product) {
		logger.info("Updating product with ID: {}", id);

		// Fetch the existing product from the repository
		Product existingProduct = productRepository.getProductByid(id, false);

		if (existingProduct != null) {

			// Update fields if the provided values are not null or empty
			if (product.getProductName() != null && !product.getProductName().isEmpty()) {
				existingProduct.setProductName(product.getProductName());
			}
			if (product.getBaseGemAmount() != null&& product.getBonusGemAmount()!=0) {
				existingProduct.setBaseGemAmount(product.getBaseGemAmount());
			}
			if (product.getBonusGemAmount() != null) {
				existingProduct.setBonusGemAmount(product.getBonusGemAmount());
			}
			if (product.getOffer() != null && !product.getOffer().isEmpty()) {
				existingProduct.setOffer(product.getOffer());
			}
			if (product.getPrice() != null && !product.getPrice().isEmpty()) {
				existingProduct.setPrice(product.getPrice());
			}
			if (product.getDescription() != null && !product.getDescription().isEmpty()) {
				existingProduct.setDescription(product.getDescription());
			}
			if(product.getStatus()!=existingProduct.getStatus()) {
				existingProduct.setStatus(product.getStatus());
			}
			  existingProduct.setUpdatedDate(LocalDateTime.now());

			// Save the updated product
			return productRepository.save(existingProduct);
		} else {
			logger.warn("Product with ID: {} not found for update", id);
			return null;
		}
	}

	public List<Product> findAllActiveProduct(boolean b) {
		Boolean status=true;
		return productRepository.findAllActiveProduct(true,false);
	}

}
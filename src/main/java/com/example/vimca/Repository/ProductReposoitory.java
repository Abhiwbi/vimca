package com.example.vimca.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.vimca.Model.Product;

public interface ProductReposoitory  extends JpaRepository<Product, Long>{

	@Query("select d from Product d where d.productId=:productId AND d.deleted=:isDeleted")
	Product getProductByid(Long productId, boolean isDeleted);

	@Query("select d from Product d where d.status=:status AND d.deleted=:isDeleted")
	List<Product> findAllActiveProduct(boolean status, boolean isDeleted);

}
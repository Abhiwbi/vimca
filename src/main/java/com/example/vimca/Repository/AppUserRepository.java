package com.example.vimca.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.vimca.Model.AppUser;


public interface AppUserRepository  extends JpaRepository<AppUser ,Long>{

	@Query("select d from AppUser d where d.appUserId=:id AND d.deleted=:isDeleted")
	public AppUser getUserById(Long id, boolean isDeleted);
	@Query("select d from AppUser d where d.mobile=:mobile AND d.deleted=:isDeleted")
	public AppUser getUserByMobileNumber(String mobile, boolean isDeleted);

	@Query("select d from AppUser d where d.email=:email AND d.deleted=:isDeleted")
	public AppUser getByEmailId(String email, boolean isDeleted);

}
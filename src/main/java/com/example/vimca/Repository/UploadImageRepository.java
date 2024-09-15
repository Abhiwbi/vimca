package com.example.vimca.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.vimca.Model.UploadImage;

public interface UploadImageRepository  extends JpaRepository<UploadImage, Long>{

	@Query("select d from UploadImage d where  d.appUser.appUserId=:appUserId  AND d.deleted=:isDeleted")
	List<UploadImage> findByAppUserId(Long appUserId, boolean isDeleted);

}
package com.ecom.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.file.model.FileStore;

@Repository
public interface FileStoreRepository extends JpaRepository<FileStore, Long>, CrudRepository<FileStore, Long> {
	
	List<FileStore> findByFileIdentity(String identity);
	
	List<FileStore> findByFileIdentityIn(List<String> identites);

	FileStore findByFileIdentityAndOriginalFileName(String identity, String originalFileName);

}

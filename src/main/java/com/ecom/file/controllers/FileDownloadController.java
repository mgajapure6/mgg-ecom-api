package com.ecom.file.controllers;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.app.exceptions.FileStorageException;
import com.ecom.file.model.FileStore;
import com.ecom.file.service.FileStorageService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "File Download API", description = "")
@RestController
@RequestMapping("/api/v1/files")
public class FileDownloadController {
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@GetMapping("/download/{entity}/{identity}/{filename}")
    public ResponseEntity<Resource> loadFileAsResource(@PathVariable("entity") String entity, 
    		@PathVariable("identity") String identity, @PathVariable("filename") String filename) throws FileNotFoundException {
		try {
			System.out.println("entity::"+entity);
			System.out.println("identity::"+identity);
			System.out.println("filename::"+filename);
			FileStore fileStore = fileStorageService.getFileByIdentityAndFileName(identity, filename);
			System.out.println("fileStore::"+fileStore.toString());
	        ByteArrayResource resource = new ByteArrayResource(fileStore.getFile());
	        return ResponseEntity.ok()
	                .contentType(MediaType.IMAGE_JPEG)
	                .contentLength(resource.contentLength())
	                .header(HttpHeaders.CONTENT_DISPOSITION,ContentDisposition.attachment().filename(fileStore.getOriginalFileName())
	                .build().toString())
	                .body(resource);
		} catch (Exception e) {
			throw new FileStorageException("File not found", e);
		}
        
    }

}

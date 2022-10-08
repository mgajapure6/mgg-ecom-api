package com.ecom.product.controllers;

import java.io.FileNotFoundException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ecom.app.exceptions.FileStorageException;
import com.ecom.app.payload.ApiResponse;
import com.ecom.app.payload.PagedResponse;
import com.ecom.app.security.CurrentUser;
import com.ecom.app.security.UserPrincipal;
import com.ecom.app.utils.AppConstant;
import com.ecom.file.model.FileStore;
import com.ecom.file.service.FileIdentityGenerator;
import com.ecom.file.service.FileStorageService;
import com.ecom.product.dto.ProductDTO;
import com.ecom.product.model.Product;
import com.ecom.product.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ptoduct API", description = "")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private FileStorageService fileStorageService;

	@GetMapping
	@PreAuthorize("hasRole('USER') or hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<PagedResponse<Product>> getAllProducts(
			@RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<Product> response = productService.getAllProducts(page, size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/category/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<PagedResponse<ProductDTO>> getProductsByCategory(
			@RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "id") Long categoryId) {
		PagedResponse<ProductDTO> response = productService.getProductsByCategory(categoryId, page, size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/category/{categoryId}/user/{userId}")
	@PreAuthorize("hasRole('USER') or hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<PagedResponse<ProductDTO>> getProductsByCategoryAndUser(
			@RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "categoryId") Long categoryId, @PathVariable(name = "userId") Long userId) {
		PagedResponse<ProductDTO> response = productService.getProductsByCategoryAndUser(categoryId, userId, page,
				size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productRequest,
			@CurrentUser UserPrincipal currentUser) {
		ProductDTO productResponse = productService.addProduct(productRequest, currentUser);
		return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	// @PreAuthorize("hasRole('USER')")
	public ResponseEntity<ProductDTO> getProduct(@PathVariable(name = "id") Long id) {
		ProductDTO productResponse = productService.getProduct(id);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable(name = "id") Long productId,
			@Valid @RequestBody ProductDTO newProductRequest, @CurrentUser UserPrincipal currentUser) {
		ProductDTO productResponse = productService.updateProduct(productId, newProductRequest, currentUser);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable(name = "id") Long id,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = productService.deleteProduct(id, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
	@PostMapping("/uploadfile")
    public ResponseEntity<FileStore> uploadSingleFile(@RequestParam("file") MultipartFile file) {
		if(file.getSize()>1000000) {
			throw new FileStorageException("Unable to store file greater than 1 MB");
		}
		long prodId = 2324;
		try {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	                .path("/api/v1/products/download/"+FileIdentityGenerator.productFileIdentity(prodId)+"/")
	                .toUriString();
			FileStore upfile = fileStorageService.storeFile(file,FileIdentityGenerator.productFileIdentity(prodId),fileDownloadUri);
	        return ResponseEntity.status(HttpStatus.OK).body(upfile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileStorageException("Unable to store file", e);
		}
    }
	
	@GetMapping("/files")
    public ResponseEntity<List<FileStore>> loadAllFiles() throws FileNotFoundException {
        return ResponseEntity.ok()
                .body(fileStorageService.getAllFiles());
    }
	
	@GetMapping("/download/{productIdentity}/{fileName:.+}")
    public ResponseEntity<Resource> loadFileAsResource(@PathVariable("productIdentity") String productIdentity, 
    		@PathVariable("fileName") String fileName) throws FileNotFoundException {
		try {
			FileStore fileStore = fileStorageService.getFileByIdentityAndFileName(productIdentity, fileName);
	        ByteArrayResource resource = new ByteArrayResource(fileStore.getFile());
	        return ResponseEntity.ok()
	                .contentType(MediaType.IMAGE_JPEG)
	                .contentLength(resource.contentLength())
	                .header(HttpHeaders.CONTENT_DISPOSITION,ContentDisposition.attachment().filename(fileStore.getFileName())
	                .build().toString())
	                .body(resource);
		} catch (Exception e) {
			throw new FileStorageException("File not found", e);
		}
        
    }

}

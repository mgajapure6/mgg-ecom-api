package com.ecom.category.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.exceptions.UnauthorizedException;
import com.ecom.app.payload.ApiResponse;
import com.ecom.app.payload.PagedResponse;
import com.ecom.app.security.UserPrincipal;
import com.ecom.app.utils.AppConstant;
import com.ecom.app.utils.AppMessages;
import com.ecom.app.utils.AppUtil;
import com.ecom.category.dto.CategoryRequestDTO;
import com.ecom.category.dto.CategoryResponseDTO;
import com.ecom.category.mapper.CategoryMapper;
import com.ecom.category.model.Category;
import com.ecom.category.repository.CategoryRepository;
import com.ecom.file.service.FileStorageService;
import com.ecom.product.model.Product;
import com.ecom.user.model.RoleName;
import com.ecom.user.model.User;
import com.ecom.user.repository.UserRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private FileStorageService fileStorageService;

	public PagedResponse<CategoryResponseDTO> getAllCategory(Integer page, Integer size) {
		AppUtil.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstant.CREATED_AT);
		Page<Category> categories = categoryRepository.findAll(pageable);
		List<CategoryResponseDTO> content = categories.getNumberOfElements() == 0 ? Collections.emptyList()
				: categories.getContent().stream().map(c -> {
					if(c.getImageName() != null) {
						String identity = AppConstant.CATEGORY.toLowerCase()+"_"+c.getId();
						String imageUrl = AppConstant.IMAGE_DOWNLOAD_BASE_URL+"/"+AppConstant.CATEGORY.toLowerCase()+"/"+identity+"/"+c.getImageName();
						return CategoryMapper.mapToCategoryResponseDTO(c, imageUrl);
					}
					return CategoryMapper.mapToCategoryResponseDTO(c, "");
					
				}).collect(Collectors.toList());
		return new PagedResponse<>(content, categories.getNumber(), categories.getSize(), categories.getTotalElements(),
				categories.getTotalPages(), categories.isLast());
	}

	public CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));

		Category category = CategoryMapper.mapToCategory(categoryRequest);
		category.setUser(user);
		Category newCategory = categoryRepository.save(category);
		CategoryResponseDTO categoryResponse = CategoryMapper.mapToCategoryResponseDTO(newCategory, "");
		if(categoryResponse != null && categoryRequest.getCategoryImage() != null && categoryResponse.getId() > 0) {
			try {
				fileStorageService.storeFile(categoryRequest.getCategoryImage(), AppConstant.CATEGORY.toLowerCase(), categoryResponse.getId(), categoryRequest.getCategoryImage().getOriginalFilename());
				String identity = AppConstant.CATEGORY.toLowerCase()+"_"+categoryResponse.getId();
				String imageUrl = AppConstant.IMAGE_DOWNLOAD_BASE_URL+"/"+AppConstant.CATEGORY.toLowerCase()+"/"+identity+"/"+category.getImageName();
				categoryResponse.setImageUrl(imageUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return categoryResponse;
	}

	public CategoryResponseDTO getCategory(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY, AppConstant.ID, id));
		String identity = AppConstant.CATEGORY.toLowerCase()+"_"+category.getId();
		String imageUrl = AppConstant.IMAGE_DOWNLOAD_BASE_URL+"/"+AppConstant.CATEGORY.toLowerCase()+"/"+identity+"/"+category.getImageName();
		CategoryResponseDTO categoryResponseDTO = CategoryMapper.mapToCategoryResponseDTO(category, imageUrl);
		return categoryResponseDTO;
	}

	public CategoryResponseDTO updateCategory(Long id, @Valid CategoryRequestDTO newCategoryRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));
		
		Category category = categoryRepository.findById(newCategoryRequest.getId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstant.CATEGORY, AppConstant.ID, newCategoryRequest.getId()));
		
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			String oldImageName = category.getImageName();
			Set<Product> products = category.getProducts();
			category = CategoryMapper.mapToCategory(newCategoryRequest);
			category.removeAllProducts(products);
			category.addAllProducts(products);
			category.setUser(user);
			category = categoryRepository.save(category);
			
			try {
				fileStorageService.updateFile(newCategoryRequest.getCategoryImage(), AppConstant.CATEGORY.toLowerCase(), category.getId(), oldImageName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			CategoryResponseDTO categoryResponseDTO = CategoryMapper.mapToCategoryResponseDTO(category, category.getImageName());
			return categoryResponseDTO;
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

	public ApiResponse deleteCategory(Long categoryId, UserPrincipal currentUser) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY, AppConstant.ID, categoryId));
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			category.removeAllProducts(category.getProducts());
			fileStorageService.deleteFile(AppConstant.CATEGORY.toLowerCase(), categoryId, category.getImageName());
			categoryRepository.deleteById(categoryId);
			return new ApiResponse(Boolean.TRUE, AppMessages.CATEGORY_DELETE_SUCCESS);
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

}
package com.ecom.product.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.ecom.app.exceptions.EcommApiException;
import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.exceptions.UnauthorizedException;
import com.ecom.app.payload.ApiResponse;
import com.ecom.app.payload.PagedResponse;
import com.ecom.app.security.UserPrincipal;
import com.ecom.app.utils.AppConstant;
import com.ecom.app.utils.AppMessages;
import com.ecom.app.utils.AppUtil;
import com.ecom.category.model.Category;
import com.ecom.category.repository.CategoryRepository;
import com.ecom.file.service.FileStorageService;
import com.ecom.product.dto.ProductRequestDTO;
import com.ecom.product.dto.ProductResponseDTO;
import com.ecom.product.mapper.ProductMapper;
import com.ecom.product.model.Product;
import com.ecom.product.model.ProductAttribute;
import com.ecom.product.model.ProductPhoto;
import com.ecom.product.repository.ProductAttributeRepository;
import com.ecom.product.repository.ProductPhotoRepository;
import com.ecom.product.repository.ProductRepository;
import com.ecom.user.model.RoleName;
import com.ecom.user.model.User;
import com.ecom.user.repository.UserRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductAttributeRepository productAttributeRepository;

	@Autowired
	private ProductPhotoRepository productPhotoRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileStorageService fileStorageService;

	public PagedResponse<ProductResponseDTO> getAllProducts(Integer page, Integer size) {
		AppUtil.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstant.CREATED_AT);
		Page<Product> products = productRepository.findAll(pageable);
		List<ProductResponseDTO> content = products.getNumberOfElements() == 0 ? Collections.emptyList()
				: products.getContent().stream().map(p -> {
					return ProductMapper.mapProductToProductResponseDTO(p);
				}).collect(Collectors.toList());
		return new PagedResponse<>(content, products.getNumber(), products.getSize(), products.getTotalElements(),
				products.getTotalPages(), products.isLast());
	}

	public PagedResponse<ProductResponseDTO> getProductsByCategory(Long categoryId, Integer page, Integer size) {
		AppUtil.validatePageNumberAndSize(page, size);
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY, AppConstant.ID, categoryId));

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstant.CREATED_AT);
		Page<Product> products = productRepository.findByCategory(category, pageable);
		List<ProductResponseDTO> content = products.getNumberOfElements() == 0 ? Collections.emptyList()
				: products.getContent().stream().map(p -> {
					return ProductMapper.mapProductToProductResponseDTO(p);
				}).collect(Collectors.toList());

		return new PagedResponse<>(content, products.getNumber(), products.getSize(), products.getTotalElements(),
				products.getTotalPages(), products.isLast());
	}

	public PagedResponse<ProductResponseDTO> getProductsByCategoryAndUser(Long categoryId, Long userId, Integer page,
			Integer size) {
		AppUtil.validatePageNumberAndSize(page, size);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, userId));
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY, AppConstant.ID, categoryId));

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstant.CREATED_AT);
		Page<Product> products = productRepository.findByCategoryAndUser(category, user, pageable);
		List<ProductResponseDTO> content = products.getNumberOfElements() == 0 ? Collections.emptyList()
				: products.getContent().stream().map(p -> ProductMapper.mapProductToProductResponseDTO(p))
						.collect(Collectors.toList());

		return new PagedResponse<>(content, products.getNumber(), products.getSize(), products.getTotalElements(),
				products.getTotalPages(), products.isLast());
	}

	public PagedResponse<ProductResponseDTO> getProductsByCreatedBy(String username, int page, int size) {
		AppUtil.validatePageNumberAndSize(page, size);
		User user = userRepository.getUserByName(username);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstant.CREATED_AT);
		Page<Product> products = productRepository.findByCreatedBy(user.getId(), pageable);
		List<ProductResponseDTO> content = products.getNumberOfElements() == 0 ? Collections.emptyList()
				: products.getContent().stream().map(p -> ProductMapper.mapProductToProductResponseDTO(p))
						.collect(Collectors.toList());

		return new PagedResponse<>(content, products.getNumber(), products.getSize(), products.getTotalElements(),
				products.getTotalPages(), products.isLast());
	}

	public ProductResponseDTO addProduct(@Valid ProductRequestDTO productRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, currentUser.getId()));

		Category category = categoryRepository.findById(productRequest.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY, AppConstant.ID,
						productRequest.getCategoryId()));

		if (productRequest.getUserId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

			Product product = ProductMapper.mapToProduct(productRequest);
			product.setUser(user);
			product.setCategory(category);
			product = productRepository.save(product);

			for (ProductAttribute pa : product.getProdAttributes()) {
				pa.setProduct(product);
				productAttributeRepository.save(pa);
			}

			for (ProductPhoto pp : product.getProdPhotos()) {
				pp.setProduct(product);
				productPhotoRepository.save(pp);
			}

			// upload images
			if (productRequest.getProdPhotos().size() > 0) {
				for (ProductPhoto pp : product.getProdPhotos()) {
					try {
						fileStorageService.storeFile(pp.getFile(), AppConstant.PRODUCT.toLowerCase(), product.getId(),
								pp.getImageName());
						// String identity = AppConstant.PRODUCT.toLowerCase()+"_"+product.getId();
						// String imageUrl =
						// AppConstant.IMAGE_DOWNLOAD_BASE_URL+"/"+AppConstant.PRODUCT.toLowerCase()+"/"+identity+"/"+imageFilename;
						// String thumbnailUrl = imageUrl;
						// //AppConstant.IMAGE_DOWNLOAD_BASE_URL+"/"+AppConstant.PRODUCT.toLowerCase()+"/"+identity+"/"+thumbnailFilename;
					} catch (Exception e) {
					}
				}
			}

			ProductResponseDTO productResponse = ProductMapper.mapProductToProductResponseDTO(product);
			return productResponse;
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

	public ProductResponseDTO getProduct(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT, AppConstant.ID, id));
		ProductResponseDTO productResponseDTO = ProductMapper.mapProductToProductResponseDTO(product);
		return productResponseDTO;

	}

	public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO newProductRequest, UserPrincipal currentUser) {

		if (productId != newProductRequest.getId()) {
			throw new EcommApiException(HttpStatus.CONFLICT, "Product ID mismatch");
		}

		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));

		if (newProductRequest.getUserId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_VENDOR.toString()))) {

			// Fetch the product by ID
			Product product = productRepository.findById(newProductRequest.getId())
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT, AppConstant.ID,
							newProductRequest.getId()));

			// Fetch the category by product category ID
			Category category = categoryRepository.findById(newProductRequest.getCategoryId())
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY, AppConstant.ID,
							newProductRequest.getCategoryId()));

			// Get list of old products attributes and remove it from product
			Set<ProductAttribute> oldProdAttrs = new HashSet<>(productAttributeRepository.findByProduct(product));
			product.removeAllProdAttributes(oldProdAttrs);

			// Get list of old products photos and remove it from product
			Set<ProductPhoto> oldProdPhotos = new HashSet<>(productPhotoRepository.findByProduct(product));
			product.removeAllProdPhotos(oldProdPhotos);

			product = ProductMapper.mapToProduct(newProductRequest);
			product.setId(newProductRequest.getId());
			product.setUser(user);
			product.setCategory(category);

			for (ProductAttribute pa : product.getProdAttributes()) {
				pa.setProduct(product);
				productAttributeRepository.save(pa);
			}

			for (ProductPhoto pp : product.getProdPhotos()) {
				pp.setProduct(product);
				productPhotoRepository.save(pp);
			}

			oldProdAttrs.stream().filter(opa -> opa.getProduct() == null)
					.forEach(opa -> productAttributeRepository.delete(opa));
			oldProdPhotos.stream().filter(opp -> opp.getProduct() == null)
					.forEach(opp -> productPhotoRepository.delete(opp));

			product = productRepository.save(product);

			ProductResponseDTO productResponse = ProductMapper.mapProductToProductResponseDTO(product);
			return productResponse;

		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

	public ApiResponse deleteProduct(Long id, UserPrincipal currentUser) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT, AppConstant.ID, id));
		if (product.getUser().getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_VENDOR.toString()))) {
			List<ProductAttribute> oldProdAttrs = productAttributeRepository.findByProduct(product);
			for (ProductAttribute productAttribute : oldProdAttrs) {
				product.removeProdAttributes(productAttribute);
			}

			List<ProductPhoto> oldProdPhotos = productPhotoRepository.findByProduct(product);
			for (ProductPhoto productPhoto : oldProdPhotos) {
				product.removeProdPhotos(productPhoto);
			}
			productAttributeRepository
					.deleteAllById(oldProdAttrs.stream().map(pa -> pa.getId()).collect(Collectors.toList()));
			productPhotoRepository
					.deleteAllById(oldProdPhotos.stream().map(pp -> pp.getId()).collect(Collectors.toList()));
			productRepository.deleteById(id);
			return new ApiResponse(Boolean.TRUE, AppMessages.PROD_DELETE_SUCCESS);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);

		throw new UnauthorizedException(apiResponse);
	}

}

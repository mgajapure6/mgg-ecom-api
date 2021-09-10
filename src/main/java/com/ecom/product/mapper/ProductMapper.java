package com.ecom.product.mapper;

import java.util.Objects;
import java.util.stream.Collectors;

import com.ecom.product.dto.ProductAttributeDTO;
import com.ecom.product.dto.ProductDTO;
import com.ecom.product.dto.ProductPhotoDTO;
import com.ecom.product.model.Product;
import com.ecom.product.model.ProductAttribute;
import com.ecom.product.model.ProductPhoto;

public class ProductMapper {

	public static Product mapToProduct(ProductDTO newProductRequest) {
		Product product = new Product();
		product.setProdName(newProductRequest.getProdName());
		product.setProdDesc(newProductRequest.getProdDesc());
		product.setProdModel(newProductRequest.getProdModel());
		product.setProdPrice(newProductRequest.getProdPrice());
		product.setProdQuantity(newProductRequest.getProdQuantity());
		product.setProdSku(newProductRequest.getProdSku());
		product.setProdSpecialPrice(newProductRequest.getProdSpecialPrice());
		product.setProdTags(newProductRequest.getProdTags());
		product.setProdAttributes(newProductRequest.getProdAttributes().stream().filter(Objects::nonNull)
				.map(pa -> mapToProductAttribute(pa)).collect(Collectors.toSet()));
		product.setProdPhotos(newProductRequest.getProdPhotos().stream().filter(Objects::nonNull)
				.map(pp -> mapToProductPhoto(pp)).collect(Collectors.toSet()));
		return product;
	}

//	public static Product mapToProduct(ProductDTO newProductRequest, Product product) {
//		product.setProdName(newProductRequest.getProdName());
//		product.setProdDesc(newProductRequest.getProdDesc());
//		product.setProdModel(newProductRequest.getProdModel());
//		product.setProdPrice(newProductRequest.getProdPrice());
//		product.setProdQuantity(newProductRequest.getProdQuantity());
//		product.setProdSku(newProductRequest.getProdSku());
//		product.setProdSpecialPrice(newProductRequest.getProdSpecialPrice());
//		product.setProdTags(newProductRequest.getProdTags());
//		product.setProdAttributes(newProductRequest.getProdAttributes().stream().filter(Objects::nonNull)
//				.map(pa -> mapToProductAttribute(pa)).collect(Collectors.toSet()));
//		product.setProdPhotos(newProductRequest.getProdPhotos().stream().filter(Objects::nonNull)
//				.map(pp -> mapToProductPhoto(pp)).collect(Collectors.toSet()));
//		return product;
//	}

	public static ProductDTO mapToProductDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setProdName(product.getProdName());
		productDTO.setProdDesc(product.getProdDesc());
		productDTO.setProdModel(product.getProdModel());
		productDTO.setProdPrice(product.getProdPrice());
		productDTO.setProdQuantity(product.getProdQuantity());
		productDTO.setProdSku(product.getProdSku());
		productDTO.setProdSpecialPrice(product.getProdSpecialPrice());
		productDTO.setProdTags(product.getProdTags());
		productDTO.setCategoryId(product.getCategory().getId());
		productDTO.setProdAttributes(product.getProdAttributes().stream().filter(Objects::nonNull)
				.map(pa -> mapToProductAttributeDTO(pa)).collect(Collectors.toSet()));
		productDTO.setProdPhotos(product.getProdPhotos().stream().filter(Objects::nonNull)
				.map(pp -> mapToProductPhotoDTO(pp)).collect(Collectors.toSet()));
		productDTO.setUserId(product.getUser().getId());
		return productDTO;
	}

	// ==================================================================================//

	public static ProductAttribute mapToProductAttribute(ProductAttributeDTO newProductAttributeRequest) {
		ProductAttribute productAttribute = new ProductAttribute();
		productAttribute.setId(newProductAttributeRequest.getId());
		productAttribute.setName(newProductAttributeRequest.getName());
		productAttribute.setValue(newProductAttributeRequest.getValue());
		return productAttribute;
	}

	public static ProductAttributeDTO mapToProductAttributeDTO(ProductAttribute productAttribute) {
		ProductAttributeDTO productAttributeDTO = new ProductAttributeDTO();
		productAttributeDTO.setId(productAttribute.getId());
		productAttributeDTO.setName(productAttribute.getName());
		productAttributeDTO.setValue(productAttribute.getValue());
		return productAttributeDTO;
	}

	// =================================================================================//

	public static ProductPhoto mapToProductPhoto(ProductPhotoDTO newProductPhotoRequest) {
		ProductPhoto productPhoto = new ProductPhoto();
		productPhoto.setId(newProductPhotoRequest.getId());
		productPhoto.setTitle(newProductPhotoRequest.getTitle());
		productPhoto.setUrl(newProductPhotoRequest.getUrl());
		productPhoto.setThumbnailUrl(newProductPhotoRequest.getThumbnailUrl());
		return productPhoto;
	}

	public static ProductPhotoDTO mapToProductPhotoDTO(ProductPhoto productPhoto) {
		ProductPhotoDTO productPhotoDTO = new ProductPhotoDTO();
		productPhotoDTO.setId(productPhoto.getId());
		productPhotoDTO.setTitle(productPhoto.getTitle());
		productPhotoDTO.setUrl(productPhoto.getUrl());
		productPhotoDTO.setThumbnailUrl(productPhoto.getThumbnailUrl());
		return productPhotoDTO;
	}
}

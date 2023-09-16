package com.ecom.product.mapper;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ecom.app.utils.AppConstant;
import com.ecom.product.dto.ProductAttributeRequestDTO;
import com.ecom.product.dto.ProductAttributeResponseDTO;
import com.ecom.product.dto.ProductPhotoRequestDTO;
import com.ecom.product.dto.ProductPhotoResponseDTO;
import com.ecom.product.dto.ProductRequestDTO;
import com.ecom.product.dto.ProductResponseDTO;
import com.ecom.product.model.Product;
import com.ecom.product.model.ProductAttribute;
import com.ecom.product.model.ProductPhoto;

public class ProductMapper {

	public static Product mapToProduct(ProductRequestDTO newProductRequest) {
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

	public static ProductAttribute mapToProductAttribute(ProductAttributeRequestDTO newProductAttributeRequest) {
		ProductAttribute productAttribute = new ProductAttribute();
		productAttribute.setId(newProductAttributeRequest.getId());
		productAttribute.setName(newProductAttributeRequest.getName());
		productAttribute.setValue(newProductAttributeRequest.getValue());
		return productAttribute;
	}

	public static ProductPhoto mapToProductPhoto(ProductPhotoRequestDTO newProductPhotoRequest) {
		ProductPhoto productPhoto = new ProductPhoto();
		productPhoto.setId(newProductPhotoRequest.getId());
		productPhoto.setTitle(newProductPhotoRequest.getTitle());
		productPhoto.setImageName(newProductPhotoRequest.getImage().getOriginalFilename());
		productPhoto.setThumbnailImageName("thumb_" + newProductPhotoRequest.getImage().getOriginalFilename());
		productPhoto.setFile(newProductPhotoRequest.getImage());
		return productPhoto;
	}

	// =================================================================================//

	public static ProductResponseDTO mapProductToProductResponseDTO(Product product) {
		ProductResponseDTO productDTO = new ProductResponseDTO();
		productDTO.setId(product.getId());
		productDTO.setProdName(product.getProdName());
		productDTO.setProdDesc(product.getProdDesc());
		productDTO.setProdModel(product.getProdModel());
		productDTO.setProdPrice(product.getProdPrice());
		productDTO.setProdQuantity(product.getProdQuantity());
		productDTO.setProdSku(product.getProdSku());
		productDTO.setProdSpecialPrice(product.getProdSpecialPrice());

		if (product.getProdTags().contains(",")) {
			productDTO.setProdTags(Arrays.asList(product.getProdTags().split(",")));
		} else {
			productDTO.setProdTags(Arrays.asList(product.getProdTags()));
		}

		productDTO.setImages(product.getProdPhotos().stream().filter(Objects::nonNull)
				.map(pp -> mapToProductPhotoResponseDTO(pp, product)).collect(Collectors.toSet()));

		productDTO.setProdAttributes(product.getProdAttributes().stream().filter(Objects::nonNull)
				.map(pa -> mapToProductAttributeResponseDTO(pa)).collect(Collectors.toSet()));

		return productDTO;
	}

	private static ProductPhotoResponseDTO mapToProductPhotoResponseDTO(ProductPhoto pp, Product product) {
		String identity = AppConstant.PRODUCT.toLowerCase() + "_" + product.getId();
		String imageUrl = AppConstant.IMAGE_DOWNLOAD_BASE_URL + "/" + AppConstant.PRODUCT.toLowerCase() + "/" + identity
				+ "/" + pp.getImageName();
		String thumbnailUrl = AppConstant.IMAGE_DOWNLOAD_BASE_URL + "/" + AppConstant.PRODUCT.toLowerCase() + "/"
				+ identity + "/" + pp.getThumbnailImageName();
		ProductPhotoResponseDTO ppDto = new ProductPhotoResponseDTO();
		ppDto.setId(pp.getId());
		ppDto.setCoverImage(pp.getCoverImage());
		ppDto.setTitle(pp.getTitle());
		ppDto.setImageUrl(imageUrl);
		ppDto.setThumbnailUrl(thumbnailUrl);
		return ppDto;
	}

	private static ProductAttributeResponseDTO mapToProductAttributeResponseDTO(ProductAttribute pa) {
		ProductAttributeResponseDTO productAttributeResponseDTO = new ProductAttributeResponseDTO();
		productAttributeResponseDTO.setId(pa.getId());
		productAttributeResponseDTO.setName(pa.getName());
		productAttributeResponseDTO.setValue(pa.getValue());
		return productAttributeResponseDTO;
	}
}

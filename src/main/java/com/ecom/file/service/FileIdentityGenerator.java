package com.ecom.file.service;

public class FileIdentityGenerator {
	
	public static String productFileIdentity(Long productId) {
		return "product_"+productId;
	}
	
	public static String categoryFileIdentity(Long categoryId) {
		return "category_"+categoryId;
	}
	
	public static String userFileIdentity(Long userId) {
		return "user_"+userId;
	}

}

package com.ecom.ui.product.controllers;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.product.dto.ProductDTO;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product UI API", description = "")
@Controller
@RequestMapping("/ui/products")
public class UIProductController {
	
	
	@GetMapping("/create")
	public String loadUI(HttpServletRequest request, Model model) {
		ProductDTO productDTO = new ProductDTO();
		model.addAttribute("productDTO", productDTO);
		return "products/addProductNew";
	}
	
	@ResponseBody
	@PostMapping("/uploadImages")
	public ResponseEntity<String> handleFileUpload(@ModelAttribute("productDTO") ProductDTO productDTO,
			RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {
		 System.out.println("productDTO::"+productDTO.toString());
		 System.out.println("productDTO images size::"+productDTO.getImageFiles().size());
		 if(productDTO.getImageFiles().size()>0) {
			 for (MultipartFile mFile : productDTO.getImageFiles()) {
				 String savePath = "D:\\Mayur\\file_upload_directory\\";
				 File newFile = new File(savePath+mFile.getOriginalFilename());
				 mFile.transferTo(newFile);
			}
		 }
		
		redirectAttributes.addFlashAttribute("message", "Product added successfully");
		return new ResponseEntity<String>("Product added successfully", HttpStatus.OK);
	}
	
	

}

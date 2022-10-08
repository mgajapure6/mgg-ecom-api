package com.ecom.db.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Clock;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.app.exceptions.FileStorageException;
import com.ecom.app.utils.H2DBUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "H2DB API", description = "")
@RestController
@RequestMapping("/api/v1/h2db")
public class H2DBController {

	@Autowired
	private H2DBUtil h2dbUtil;

	@GetMapping("/export")
	public ResponseEntity<Resource> exportH2DB(HttpServletResponse response) throws FileNotFoundException {
		try {
			File file = new File("backup_" + Clock.systemDefaultZone().millis() + ".sql");
			h2dbUtil.exportDB(file);
			FileSystemResource resource = new FileSystemResource(file);
			return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).contentLength(resource.contentLength())
					.header(HttpHeaders.CONTENT_DISPOSITION,
							ContentDisposition.attachment().filename(file.getName()).build().toString())
					.body(resource);
		} catch (Exception e) {
			throw new FileStorageException("File not found", e);
		}
	}

	@PostMapping("/import")
	public ResponseEntity<String> importH2DB(@RequestParam("file") MultipartFile mfile)
			throws IllegalStateException, IOException, SQLException {
		if (!StringUtils.getFilenameExtension(mfile.getOriginalFilename()).equals("sql")) {
			throw new FileStorageException("Unable to import file. Please provide sql file");
		}
		File file = new File(mfile.getOriginalFilename());
		mfile.transferTo(file);
		h2dbUtil.importDB(file);
		return new ResponseEntity<String>("File imported successfully", HttpStatus.OK);

	}

}

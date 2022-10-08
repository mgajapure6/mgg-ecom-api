package com.ecom.file.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.file.model.FileStore;
import com.ecom.file.repository.FileStoreRepository;

@Service
public class FileStorageService {

	@Autowired
	private FileStoreRepository fileStoreRepository;

	public FileStore storeFile(MultipartFile file, String identity, String fileDownloadUri) throws IOException {
		FileStore store = new FileStore();
		store.setFile(file.getBytes());
		store.setFileIdentity(identity);
		store.setFileName(file.getOriginalFilename());
		store.setFileSize(file.getSize());
		store.setFileURI(identity);
		store.setFileURI(fileDownloadUri + file.getOriginalFilename());
		return fileStoreRepository.save(store);
	}

	public List<FileStore> storeAllFile(List<MultipartFile> files, String identity, String fileDownloadUri)
			throws IOException {
		List<FileStore> storeFiles = new ArrayList<>();
		for (MultipartFile file : files) {
			FileStore store = new FileStore();
			store.setFile(file.getBytes());
			store.setFileIdentity(identity);
			store.setFileName(file.getOriginalFilename());
			store.setFileSize(file.getSize());
			store.setFileURI(identity);
			store.setFileURI(fileDownloadUri + "/" + file.getOriginalFilename());
			storeFiles.add(store);
		}
		return fileStoreRepository.saveAll(storeFiles);
	}

	public List<FileStore> getFilesByMultipleIdentity(List<String> identites) {
		return fileStoreRepository.findByFileIdentityIn(identites);
	}

	public List<FileStore> getFilesByIdentity(String identity) {
		return fileStoreRepository.findByFileIdentity(identity);
	}

	public FileStore getFileByIdentityAndFileName(String identity, String fileName) {
		return fileStoreRepository.findByFileIdentityAndFileName(identity, fileName);
	}

	public List<FileStore> getAllFiles() {
		return fileStoreRepository.findAll();
	}

}

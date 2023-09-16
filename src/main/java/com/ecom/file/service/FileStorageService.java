package com.ecom.file.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.app.exceptions.FileStorageException;
import com.ecom.file.model.FileStore;
import com.ecom.file.repository.FileStoreRepository;

@Service
public class FileStorageService {

	@Autowired
	private FileStoreRepository fileStoreRepository;
	
	public List<FileStore> storeMultipleFiles(List<MultipartFile> files, String entity, Long entityId)
			throws IOException {
		List<FileStore> storeFiles = new ArrayList<>();
		for (MultipartFile file : files) {
			storeFiles.add(storeFile(file, entity, entityId, file.getOriginalFilename()));
		}
		return storeFiles;
	}

	public FileStore storeFile(MultipartFile file, String entity, Long entityId, String filename) throws IOException {
		if(file != null && entity !=  null && entityId != null) {
			if(file.getSize()>1000000) {
				throw new FileStorageException("Unable to store file greater than 1 MB");
			}
			String identity = entity.toLowerCase()+"_"+entityId;
			FileStore store = new FileStore();
			store.setFile(file.getBytes());
			store.setFileIdentity(identity);
			store.setOriginalFileName(filename);
			store.setFileSize(file.getSize());
			store.setEntity(entity);
			store.setEntityId(entityId);
			return fileStoreRepository.save(store);
		}
		return null;
	}
	
	public FileStore updateFile(MultipartFile file, String entity, Long entityId, String filename) throws IOException {
		if(file != null && entity !=  null && entityId != null) {
			if(file.getSize()>1000000) {
				throw new FileStorageException("Unable to store file greater than 1 MB");
			}
			String identity = entity.toLowerCase()+"_"+entityId;
			FileStore store = fileStoreRepository.findByFileIdentityAndOriginalFileName(identity, filename);
			store.setFile(file.getBytes());
			store.setFileIdentity(identity);
			store.setOriginalFileName(file.getOriginalFilename());
			store.setFileSize(file.getSize());
			store.setEntity(entity);
			store.setEntityId(entityId);
			return fileStoreRepository.save(store);
		}
		return null;
	}

	public boolean deleteFile(String entity, Long entityId, String filename){
		if(entity !=  null && entityId != null) {
			String identity = entity.toLowerCase()+"_"+entityId;
			FileStore store = fileStoreRepository.findByFileIdentityAndOriginalFileName(identity, filename);
			try {
				fileStoreRepository.delete(store);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}


	public List<FileStore> getFilesByMultipleIdentity(List<String> identites) {
		return fileStoreRepository.findByFileIdentityIn(identites);
	}

	public List<FileStore> getFilesByIdentity(String identity) {
		return fileStoreRepository.findByFileIdentity(identity);
	}

	public FileStore getFileByIdentityAndFileName(String identity, String fileName) {
		return fileStoreRepository.findByFileIdentityAndOriginalFileName(identity, fileName);
	}

	public List<FileStore> getAllFiles() {
		return fileStoreRepository.findAll();
	}

}

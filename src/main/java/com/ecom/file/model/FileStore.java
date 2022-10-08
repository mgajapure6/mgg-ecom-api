package com.ecom.file.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table
public class FileStore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String fileName;

	@Column
	private String fileType;

	@Column
	private Long fileSize;

	@Column
	private String filePath;

	@Column
	private String fileURI;

	@Column
	private String fileIdentity;

	@Lob
	@Column
	private byte[] file;

	public FileStore(Long id, String fileName, String fileType, Long fileSize, String filePath, String fileURI,
			String fileIdentity, byte[] file) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.filePath = filePath;
		this.fileURI = fileURI;
		this.fileIdentity = fileIdentity;
		this.file = file;
	}

	public FileStore() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileURI() {
		return fileURI;
	}

	public void setFileURI(String fileURI) {
		this.fileURI = fileURI;
	}

	public String getFileIdentity() {
		return fileIdentity;
	}

	public void setFileIdentity(String fileIdentity) {
		this.fileIdentity = fileIdentity;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

}

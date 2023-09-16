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
	private String originalFileName;

	@Column
	private String fileType;

	@Column
	private Long fileSize;

	@Column
	private String fileIdentity;

	@Column
	private String entity;

	@Column
	private Long entityId;

	@Lob
	@Column
	private byte[] file;

	public FileStore() {
		super();
	}

	public FileStore(Long id, String originalFileName, String fileType, Long fileSize, String fileIdentity,
			String entity, Long entityId, byte[] file) {
		super();
		this.id = id;
		this.originalFileName = originalFileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.fileIdentity = fileIdentity;
		this.entity = entity;
		this.entityId = entityId;
		this.file = file;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
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

	public String getFileIdentity() {
		return fileIdentity;
	}

	public void setFileIdentity(String fileIdentity) {
		this.fileIdentity = fileIdentity;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

}

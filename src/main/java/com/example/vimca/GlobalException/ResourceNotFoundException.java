package com.example.vimca.GlobalException;

public class ResourceNotFoundException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	private String resouceName;
	private String fieldName;
	private Long fieldValue;

	public ResourceNotFoundException(String resouceName, String fieldName, Long fieldValue) {
		super(String.format("%s not found with %s:%s", resouceName, fieldName, fieldValue));
		this.resouceName = resouceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getResouceName() {
		return resouceName;
	}

	public void setResouceName(String resouceName) {
		this.resouceName = resouceName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Long getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Long fieldValue) {
		this.fieldValue = fieldValue;
	}

}

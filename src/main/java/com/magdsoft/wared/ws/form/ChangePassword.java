package com.magdsoft.wared.ws.form;

public class ChangePassword {
	private String newPassword;
	private String currentPassword;
	private String apiToken;
	private String verficationCode;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	/**
	 * @return the verficationCode
	 */
	public String getVerficationCode() {
		return verficationCode;
	}

	/**
	 * @param verficationCode the verficationCode to set
	 */
	public void setVerficationCode(String verficationCode) {
		this.verficationCode = verficationCode;
	}
	
	

}

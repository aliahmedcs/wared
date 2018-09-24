package com.magdsoft.wared.ws.form;

public class GetHistory {

private String apiToken;
private Integer running;
private Integer page;

public Integer getPage() {
	return page;
}
public void setPage(Integer page) {
	this.page = page;
}
public String getApiToken() {
	return apiToken;
}
public void setApiToken(String apiToken) {
	this.apiToken = apiToken;
}
public Integer getRunning() {
	return running;
}
public void setRunning(Integer running) {
	this.running = running;
}


}

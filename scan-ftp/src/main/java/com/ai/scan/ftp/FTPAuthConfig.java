package com.ai.scan.ftp;

public class FTPAuthConfig {
	private String hostname;
	private int port;
	private String username;
	private String password;
	private String pathname;
	
	public FTPAuthConfig(){
		
	}
	
	public FTPAuthConfig(String hostname, int port, String username, String password, String pathname) {
		super();
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
		this.pathname = pathname;
	}


	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPathname() {
		return pathname;
	}
	public void setPathname(String pathname) {
		this.pathname = pathname;
	}
	@Override
	public String toString() {
		return "FTPAuthConfig [hostname=" + hostname + ", port=" + port + ", username=" + username + ", password="
				+ password + ", pathname=" + pathname + "]";
	}
}

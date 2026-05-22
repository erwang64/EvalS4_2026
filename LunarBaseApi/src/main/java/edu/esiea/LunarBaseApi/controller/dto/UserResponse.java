package edu.esiea.LunarBaseApi.controller.dto;

import edu.esiea.LunarBaseApi.enumeration.Role;

public class UserResponse {
	
	private int userId;
	private String username;
	private String email;
	private Role role;
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	

	

}

package edu.esiea.LunarBaseApi.controller.dto;


import edu.esiea.LunarBaseApi.controller.dto.error.ResourceType;
import edu.esiea.LunarBaseApi.enumeration.Role;

public class UserRequest  extends AbstractRequest {
	
	private String username;
	private String password;
	private String email;
	private Role role;
	
	public UserRequest() {
		super(ResourceType.USER);
	}

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public Role getRole() { return role; }
	public void setRole(Role role) { this.role = role; }

}

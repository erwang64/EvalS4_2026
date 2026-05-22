package edu.esiea.LunarBaseApi.controller.dto;

import edu.esiea.LunarBaseApi.enumeration.CrewrRole;

public class CrewMemberResponse {
	
	private int crewMemberId;
    private String firstName;
    private String lastName;
    private CrewrRole crewRole;
    private SpaceSuitResponse spaceSuit;
    
	public int getCrewMemberId() {
		return crewMemberId;
	}
	public void setCrewMemberId(int crewMemberId) {
		this.crewMemberId = crewMemberId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public CrewrRole getCrewRole() {
		return crewRole;
	}
	public void setCrewRole(CrewrRole crewRole) {
		this.crewRole = crewRole;
	}
	public SpaceSuitResponse getSpaceSuit() {
		return spaceSuit;
	}
	public void setSpaceSuit(SpaceSuitResponse spaceSuit) {
		this.spaceSuit = spaceSuit;
	}
    
    
    

}

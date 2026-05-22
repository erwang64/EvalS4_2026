package edu.esiea.LunarBaseApi.controller.dto;

import edu.esiea.LunarBaseApi.controller.dto.error.ResourceType;
import edu.esiea.LunarBaseApi.enumeration.CrewrRole;

public class CrewMemberRequest extends AbstractRequest {

	private String firstName;
    private String lastName;
    private CrewrRole crewRole;
    private SpaceSuitRequest spaceSuit;
    private Integer lunarBaseId;

    public CrewMemberRequest() {
        super(ResourceType.CREW_MEMBER);
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

	public SpaceSuitRequest getSpaceSuit() {
		return spaceSuit;
	}

	public void setSpaceSuit(SpaceSuitRequest spaceSuit) {
		this.spaceSuit = spaceSuit;
	}

	public Integer getLunarBaseId() {
		return lunarBaseId;
	}

	public void setLunarBaseId(Integer lunarBaseId) {
		this.lunarBaseId = lunarBaseId;
	}
	
	
    
    
    
}

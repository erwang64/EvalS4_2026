package edu.esiea.LunarBaseApi.model;

import java.util.List;

import edu.esiea.LunarBaseApi.enumeration.CrewrRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CrewMember")
public class CrewMember {
	
	@Id
	@Column(name = "CrewMemberId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int crewMemberId;
	
	@Column(name = "FirstName", nullable = false)
	private String FirstName;
	
	@Column(name = "LastName", nullable = false)
	private String LastName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "CrewrRole")
	private CrewrRole crewRole;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "space_suit_id", referencedColumnName = "SpaceSuitId")
    private SpaceSuit spaceSuit;

	public int getCrewMemberId() {
		return crewMemberId;
	}

	public void setCrewMemberId(int crewMemberId) {
		this.crewMemberId = crewMemberId;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public CrewrRole getCrewRole() {
		return crewRole;
	}

	public void setCrewRole(CrewrRole crewRole) {
		this.crewRole = crewRole;
	}

	public SpaceSuit getSpaceSuit() {
		return spaceSuit;
	}

	public void setSpaceSuit(SpaceSuit spaceSuit) {
		this.spaceSuit = spaceSuit;
	}
	
	

}

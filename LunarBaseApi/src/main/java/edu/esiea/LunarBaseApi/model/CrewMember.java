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
    @JoinColumn(name = "space_suit_id", referencedColumnName = "id")
    private SpaceSuit spaceSuit;

}

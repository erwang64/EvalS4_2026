package edu.esiea.LunarBaseApi.controller.dto;

import java.util.List;

public class LunarBaseResponse {
	
	private int lunarBaseId;
    private String name;
    private int posX;
    private int posY;
    private int maximalCapacity;
    private int currentCrewCount;
    private List<Integer> crewIds;
    
    
	public int getLunarBaseId() {
		return lunarBaseId;
	}
	public void setLunarBaseId(int lunarBaseId) {
		this.lunarBaseId = lunarBaseId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getMaximalCapacity() {
		return maximalCapacity;
	}
	public void setMaximalCapacity(int maximalCapacity) {
		this.maximalCapacity = maximalCapacity;
	}
	public int getCurrentCrewCount() {
		return currentCrewCount;
	}
	public void setCurrentCrewCount(int currentCrewCount) {
		this.currentCrewCount = currentCrewCount;
	}
	public List<Integer> getCrewIds() {
		return crewIds;
	}
	public void setCrewIds(List<Integer> crewIds) {
		this.crewIds = crewIds;
	}
    
    
    


    

}

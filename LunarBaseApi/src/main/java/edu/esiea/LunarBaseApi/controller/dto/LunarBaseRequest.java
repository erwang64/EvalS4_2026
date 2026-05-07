package edu.esiea.LunarBaseApi.controller.dto;

import edu.esiea.LunarBaseApi.controller.dto.error.ResourceType;

public class LunarBaseRequest extends AbstractRequest{
	
	
	private String name;
	private String sector;
    private Integer posX;
    private Integer posY;
    private Integer maximalCapacity;
    
    public LunarBaseRequest() {
        super(ResourceType.LUNAR_BASE); 
    }
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPosX() {
		return posX;
	}
	public void setPosX(Integer posX) {
		this.posX = posX;
	}
	public Integer getPosY() {
		return posY;
	}
	public void setPosY(Integer posY) {
		this.posY = posY;
	}
	public Integer getMaximalCapacity() {
		return maximalCapacity;
	}
	public void setMaximalCapacity(Integer maximalCapacity) {
		this.maximalCapacity = maximalCapacity;
	}

	public String getSector() {
		return sector;
	}


	public void setSector(String sector) {
		this.sector = sector;
	}
	
	
    
    

    
    
    
    
    

}

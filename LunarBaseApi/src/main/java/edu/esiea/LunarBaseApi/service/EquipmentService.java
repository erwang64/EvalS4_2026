package edu.esiea.LunarBaseApi.service;

import org.springframework.stereotype.Service;

import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.Equipment;
import edu.esiea.LunarBaseApi.model.LunarBase;
import edu.esiea.LunarBaseApi.repository.EquipmentRepository;
import edu.esiea.LunarBaseApi.repository.LunarBaseRepository;

@Service
public class EquipmentService {

	private final EquipmentRepository equipmentRepo;
    private final LunarBaseRepository lunarBaseRepo;

    public EquipmentService(EquipmentRepository equipmentRepo, LunarBaseRepository lunarBaseRepo) {
        this.equipmentRepo = equipmentRepo;
        this.lunarBaseRepo = lunarBaseRepo;
    }

    public Equipment addEquipment(Equipment newEquipment, Integer lunarBaseId) throws ServiceException {
        if (lunarBaseId != null && lunarBaseId > 0) {
            LunarBase base = lunarBaseRepo.findById(lunarBaseId)
                    .orElseThrow(() -> new ServiceException("Impossible d'affecterLa base n'existe pas"));
            base.getEquipments().add(newEquipment);
            lunarBaseRepo.save(base);
            
            return newEquipment;
        }
        return equipmentRepo.save(newEquipment);
    }
    
    public Equipment getEquipmentById(int id) throws ServiceException {
        return equipmentRepo.findById(id)
                .orElseThrow(() -> new ServiceException("Équipement non trouvé avec l'ID"));
    }

    public Iterable<Equipment> getAllEquipments() {
        return equipmentRepo.findAll();
    }

    public Equipment updateEquipment(int id, Equipment equipmentDetails) throws ServiceException {
        Equipment existingEquipment = getEquipmentById(id);
        
        existingEquipment.setNameEquiment(equipmentDetails.getNameEquiment());
        existingEquipment.setEquipmentStatus(equipmentDetails.getEquipmentStatus());
        
        return equipmentRepo.save(existingEquipment);
    }

    public void deleteEquipment(int id) throws ServiceException {
        Equipment existingEquipment = getEquipmentById(id);
        equipmentRepo.delete(existingEquipment);
    }
}
package edu.esiea.LunarBaseApi.service;

import org.springframework.stereotype.Service;

import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.LunarBase;
import edu.esiea.LunarBaseApi.repository.LunarBaseRepository;

@Service
public class LunarBaseService {
	
	private final LunarBaseRepository repo;
	
	public LunarBaseService( LunarBaseRepository repo) {
		this.repo = repo;
	}
	
	//Crée une base
	public LunarBase createLunarBase(final LunarBase baseToCreate) throws ServiceException {
		
		if (baseToCreate.getLunarBaseId() > 0) {
			throw new ServiceException("Impossible de créer un base dont l'ID est défini.");
		}
		
		if (repo.findByName(baseToCreate.getName()).isPresent()) {
			throw new ServiceException("Erreur : Une base avec le meme nom  existe déjà.");
		}
		
		// 3. Règle métier : la position (X, Y) doit être libre
		if (repo.findByPosXAndPosY(baseToCreate.getPosX(), baseToCreate.getPosY()).isPresent()) {
			throw new ServiceException("Erreur : Les coordonnées sont déjà occupées.");
		}
		
		// Si toutes les règles sont respectées, on sauvegarde
		return this.repo.save(baseToCreate);
	}
	
	
	// Get by ID
	public LunarBase getBaseById(final int id) throws ServiceException {
	    return repo.findById(id)
	        .orElseThrow(() -> new ServiceException("La base est introuvable sur la Lune !"));
	}
	
	// Get all base
	public Iterable<LunarBase> getAllBases() {
		return repo.findAll();
	}
	
	// modifier une base 
	public LunarBase updateLunarBase(int id, LunarBase baseToUpdate) throws ServiceException {
		
		// 1. On vérifie que la base à modifier existe bien
		LunarBase existingBase = repo.findById(id)
				.orElseThrow(() -> new ServiceException("Impossible de modifier elle n'existe pas."));
		
		// 2. Règle métier : Si on change le nom, on vérifie que le nouveau nom n'est pas déjà pris par UNE AUTRE base
		if (!existingBase.getName().equals(baseToUpdate.getName()) && repo.findByName(baseToUpdate.getName()).isPresent()) {
			throw new ServiceException("Le nom est déjà utilisé par une autre base.");
		}
		
		// 3. On applique les modifications (on met à jour le "document officiel")
		existingBase.setName(baseToUpdate.getName());
		existingBase.setPosX(baseToUpdate.getPosX());
		existingBase.setPosY(baseToUpdate.getPosY());
		existingBase.setMaximalCapacity(baseToUpdate.getMaximalCapacity());
		existingBase.setSector(baseToUpdate.getSector());
		
		// 4. On sauvegarde les changements
		return repo.save(existingBase);
	}
		
	
	//Suprime une base
	public void deleteLunarBase(int id) throws ServiceException {
		if (!repo.existsById(id)) {
			throw new ServiceException("Impossible de supprimer n'existe pas");
		}
		repo.deleteById(id);
	}

}

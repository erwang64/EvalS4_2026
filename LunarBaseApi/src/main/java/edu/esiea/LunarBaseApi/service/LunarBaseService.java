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
			throw new ServiceException("Erreur : Les coordonnées [" + baseToCreate.getPosX() + "," + baseToCreate.getPosY() + "] sont déjà occupées.");
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

}

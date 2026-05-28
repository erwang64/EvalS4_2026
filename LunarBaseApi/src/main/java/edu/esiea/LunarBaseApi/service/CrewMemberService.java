package edu.esiea.LunarBaseApi.service;

import org.springframework.stereotype.Service;

import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.CrewMember;
import edu.esiea.LunarBaseApi.model.LunarBase;
import edu.esiea.LunarBaseApi.model.SpaceSuit;
import edu.esiea.LunarBaseApi.repository.CrewMemberRepository;
import edu.esiea.LunarBaseApi.repository.LunarBaseRepository;

@Service
public class CrewMemberService {

	private final CrewMemberRepository crewMemberRepo;
    private final LunarBaseRepository lunarBaseRepo;

    public CrewMemberService(CrewMemberRepository crewMemberRepo, LunarBaseRepository lunarBaseRepo) {
        this.crewMemberRepo = crewMemberRepo;
        this.lunarBaseRepo = lunarBaseRepo;
    }

    public CrewMember addCrewMember(CrewMember newMember, Integer lunarBaseId) throws ServiceException {
        if (lunarBaseId != null && lunarBaseId > 0) {
            LunarBase base = lunarBaseRepo.findById(lunarBaseId)
                    .orElseThrow(() -> new ServiceException("Impossible d'affecter : La base n'existe pas"));
            int currentCrewSize = 0;
            
            if (base.getCrewMembers() != null) {
                currentCrewSize = base.getCrewMembers().size();
            }
            
            if (currentCrewSize >= base.getMaximalCapacity()) {
                throw new ServiceException("Affectation impossible : La base a atteint sa capacité maximale de membres");
            }
            base.getCrewMembers().add(newMember);
            lunarBaseRepo.save(base);
            
            return newMember;
        }
        return crewMemberRepo.save(newMember);
    }
    
    public CrewMember getCrewMemberById(int id) throws ServiceException {
        return crewMemberRepo.findById(id)
                .orElseThrow(() -> new ServiceException("Membre d'équipage non trouvé avec l'ID"));
    }

    public Iterable<CrewMember> getAllCrewMembers() {
        return crewMemberRepo.findAll();
    }

    public CrewMember updateCrewMember(int id, CrewMember memberDetails) throws ServiceException {
        CrewMember existingMember = getCrewMemberById(id); 
        
        existingMember.setFirstName(memberDetails.getFirstName());
        existingMember.setLastName(memberDetails.getLastName());
        existingMember.setCrewRole(memberDetails.getCrewRole());
        existingMember.getSpaceSuit().setSize(memberDetails.getSpaceSuit().getSize());
        existingMember.getSpaceSuit().setModel(memberDetails.getSpaceSuit().getModel());
        
        return crewMemberRepo.save(existingMember);
    }

    public void deleteCrewMember(int id) throws ServiceException {
        CrewMember existingMember = getCrewMemberById(id);
        crewMemberRepo.delete(existingMember);
    }
}
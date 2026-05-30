package edu.esiea.LunarBaseApi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.esiea.LunarBaseApi.enumeration.CrewrRole;
import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.CrewMember;
import edu.esiea.LunarBaseApi.model.LunarBase;
import edu.esiea.LunarBaseApi.model.SpaceSuit;
import edu.esiea.LunarBaseApi.repository.CrewMemberRepository;
import edu.esiea.LunarBaseApi.repository.LunarBaseRepository;

@ExtendWith(MockitoExtension.class)
public class CrewMemberServiceTest {

    @Mock
    private CrewMemberRepository crewMemberRepo;

    @Mock
    private LunarBaseRepository lunarBaseRepo;

    @InjectMocks
    private CrewMemberService crewMemberService;

    private CrewMember memberTest;

    @BeforeEach
    void setup() {
        SpaceSuit suit = new SpaceSuit();
        suit.setSize(42);
        suit.setModel("ModèleX");

        memberTest = new CrewMember();
        memberTest.setCrewMemberId(1);
        memberTest.setFirstName("Neil");
        memberTest.setLastName("Armstrong");
        memberTest.setCrewRole(CrewrRole.PILOTE);
        memberTest.setSpaceSuit(suit);
    }

    @Test
    public void testAddCrewMember_SansBase() throws ServiceException {
        when(crewMemberRepo.save(memberTest)).thenReturn(memberTest);

        CrewMember result = crewMemberService.addCrewMember(memberTest, null);

        assertNotNull(result);
        verify(crewMemberRepo).save(memberTest);
    }

    @Test
    public void testAddCrewMember_AvecBase() throws ServiceException {
        LunarBase base = new LunarBase();
        base.setLunarBaseId(10);
        base.setMaximalCapacity(5);
        base.setCrewMembers(new ArrayList<>());

        when(lunarBaseRepo.findById(10)).thenReturn(Optional.of(base));

        CrewMember result = crewMemberService.addCrewMember(memberTest, 10);

        assertNotNull(result);
        assertEquals(1, base.getCrewMembers().size());
        verify(lunarBaseRepo).save(base);
    }

    @Test
    public void testAddCrewMember_CapaciteMaximaleAtteinte() {
        LunarBase base = new LunarBase();
        base.setLunarBaseId(10);
        base.setMaximalCapacity(1);
        ArrayList<CrewMember> equipage = new ArrayList<>();
        equipage.add(new CrewMember());
        base.setCrewMembers(equipage);

        when(lunarBaseRepo.findById(10)).thenReturn(Optional.of(base));

        assertThrows(ServiceException.class, () -> {
            crewMemberService.addCrewMember(memberTest, 10);
        }, "On ne peut pas dépasser la capacité maximale de la base");
    }

    @Test
    public void testAddCrewMember_BaseInexistante() {
        when(lunarBaseRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            crewMemberService.addCrewMember(memberTest, 99);
        }, "Affecter un membre à une base inexistante doit être impossible");
    }

    @Test
    public void testGetCrewMemberById_Success() throws ServiceException {
        when(crewMemberRepo.findById(1)).thenReturn(Optional.of(memberTest));

        CrewMember result = crewMemberService.getCrewMemberById(1);

        assertNotNull(result);
        assertEquals("Neil", result.getFirstName());
        verify(crewMemberRepo).findById(1);
    }

    @Test
    public void testGetCrewMemberById_NotFound() {
        when(crewMemberRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            crewMemberService.getCrewMemberById(99);
        }, "Un membre introuvable doit lever une exception");
    }

    @Test
    public void testGetAllCrewMembers() {
        when(crewMemberRepo.findAll()).thenReturn(new ArrayList<CrewMember>());

        crewMemberService.getAllCrewMembers();

        verify(crewMemberRepo).findAll();
    }

    @Test
    public void testUpdateCrewMember_Success() throws ServiceException {
        when(crewMemberRepo.findById(1)).thenReturn(Optional.of(memberTest));
        when(crewMemberRepo.save(memberTest)).thenReturn(memberTest);

        SpaceSuit nouvelleSuit = new SpaceSuit();
        nouvelleSuit.setSize(44);
        nouvelleSuit.setModel("ModèleY");

        CrewMember nouvellesDonnees = new CrewMember();
        nouvellesDonnees.setFirstName("Buzz");
        nouvellesDonnees.setLastName("Aldrin");
        nouvellesDonnees.setCrewRole(CrewrRole.INGENIEUR);
        nouvellesDonnees.setSpaceSuit(nouvelleSuit);

        CrewMember result = crewMemberService.updateCrewMember(1, nouvellesDonnees);

        assertNotNull(result);
        assertEquals("Buzz", result.getFirstName());
        verify(crewMemberRepo).save(memberTest);
    }

    @Test
    public void testUpdateCrewMember_NotFound() {
        when(crewMemberRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            crewMemberService.updateCrewMember(99, memberTest);
        }, "Modifier un membre inexistant doit être impossible");
    }

    @Test
    public void testDeleteCrewMember_Success() throws ServiceException {
        when(crewMemberRepo.findById(1)).thenReturn(Optional.of(memberTest));

        crewMemberService.deleteCrewMember(1);

        verify(crewMemberRepo).delete(memberTest);
    }

    @Test
    public void testDeleteCrewMember_NotFound() {
        when(crewMemberRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            crewMemberService.deleteCrewMember(99);
        }, "Supprimer un membre inexistant doit être impossible");
    }
}

package org.openmrs.module.smsreminder.utils;

import org.openmrs.*;
import org.openmrs.api.*;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nelson.mahumane on 31-08-2015.
 */
public class OpenmrsResources {

public static List<Obs> getPatienteAccept(){
    ConceptService cService= Context.getConceptService();
    LocationService lService=Context.getLocationService();
    EncounterService eService=Context.getEncounterService();
    AdministrationService aService=Context.getAdministrationService();
    ObsService oService=Context.getObsService();

    List<Concept> questions=new ArrayList<Concept>();
    List<Concept> answers=new ArrayList<Concept>();
    List<Location> locations = new ArrayList<Location>();
    //List<Encounter> encounters=new ArrayList<Encounter>();

    Concept accept=cService.getConcept(6309);
    Concept acceptYes=cService.getConcept(6307);
    Location l = lService.getLocation(6);
   // Encounter encounter=eService.getEncounter(34);
   EncounterType encounterType=eService.getEncounterType(34);

    List<OpenmrsConstants.PERSON_TYPE> personTypes=new ArrayList<OpenmrsConstants.PERSON_TYPE>();
    personTypes.add(OpenmrsConstants.PERSON_TYPE.PATIENT);
    questions.add(accept);
    answers.add(acceptYes);
    locations.add(l);
   // encounters.add(encounter);

    List<Obs> acceptPersons=oService.getObservations(null,null, questions, answers, personTypes, null, null, null, null, null, new Date(), false);
    List<Obs> finalList=new ArrayList<Obs>();

    for(Obs obs:acceptPersons){
        if(obs.getEncounter().getEncounterType().getEncounterTypeId().equals(34))
            finalList.add(obs);
    }

if (finalList.isEmpty()||finalList==null)
    System.out.println("O metodo não traz nada.......... para os parametros Accept" );
    else System.out.println("Yes, Accept............goood: "+ finalList.size());


return finalList;

}
    public static List<Obs> getPatientInFilaNextVisit(){

        ConceptService cService= Context.getConceptService();
        LocationService lService=Context.getLocationService();
        EncounterService eService=Context.getEncounterService();
        AdministrationService aService=Context.getAdministrationService();
        ObsService oService=Context.getObsService();

        List<Concept> visits=new ArrayList<Concept>();
        List<Location> locations = new ArrayList<Location>();
        //List<Encounter> encounters=new ArrayList<Encounter>();

        Concept nextVisit=cService.getConcept(5096);
        Location l = lService.getLocation(6);
        EncounterType encounterType=eService.getEncounterType(18);

        Encounter encounter=eService.getEncounter(18);

        List<OpenmrsConstants.PERSON_TYPE> personTypes=new ArrayList<OpenmrsConstants.PERSON_TYPE>();
        personTypes.add(OpenmrsConstants.PERSON_TYPE.PATIENT);
        visits.add(nextVisit);
        locations.add(l);
        //encounters.add(encounter);
        List<Obs> filaPersons=oService.getObservations(null, null, visits, null, personTypes, null, null, null, null, null, new Date(), false);
        List<Obs> finalList=new ArrayList<Obs>();

        for(Obs obs:filaPersons){
            if(obs.getEncounter().getEncounterType().getEncounterTypeId().equals(18))
                finalList.add(obs);
        }

        if (finalList.isEmpty()||finalList==null)
            System.out.println("O metodo não traz nada.......... para os parametros Fila:");
        else System.out.println("Yes, Fila............goood: "+ finalList.size());

return finalList;
    }

    public static List<Obs> getPatientInGeneralNextVisits(){

        ConceptService cService= Context.getConceptService();
        LocationService lService=Context.getLocationService();
        //EncounterService eService=Context.getEncounterService();
        AdministrationService aService=Context.getAdministrationService();
        ObsService oService=Context.getObsService();

        List<Obs> listaTodasProximasVisitas=null;
        List<Concept> visits=new ArrayList<Concept>();
        List<Location> locations = new ArrayList<Location>();
        //List<Encounter> encounters=new ArrayList<Encounter>();
        //List<Encounter> encountersP=new ArrayList<Encounter>();
        Concept nextVisit=cService.getConcept(1410);
        Location l = lService.getLocation(6);
       // Encounter encounter=eService.getEncounter(6);
        //Encounter encounterP=eService.getEncounter(9);
        List<OpenmrsConstants.PERSON_TYPE> personTypes=new ArrayList<OpenmrsConstants.PERSON_TYPE>();
        personTypes.add(OpenmrsConstants.PERSON_TYPE.PATIENT);
        visits.add(nextVisit);
        locations.add(l);
        //encounters.add(encounter);
        //encountersP.add(encounterP);
        List<Obs> geeneralNextVisits=oService.getObservations(null, null, visits, null, personTypes, null, null,null, null, null, new Date(), false);
       // List<Obs> pediatricGeneralPersons=oService.getObservations(null, encountersP, visits, null, personTypes, null, null, 1, null, null, new Date(), false);

        List<Obs> finalList=new ArrayList<Obs>();

        for(Obs obs:geeneralNextVisits){
            if(obs.getEncounter().getEncounterType().getEncounterTypeId().equals(6)
                    ||obs.getEncounter().getEncounterType().getEncounterTypeId().equals(9))
                finalList.add(obs);
        }

        if (finalList.isEmpty()||finalList==null)
            System.out.println("O metodo não traz nada.......... para os parametros Fila:");
        else System.out.println("Yes, Consulta............goood: "+ finalList.size());

        return finalList;
    }
}

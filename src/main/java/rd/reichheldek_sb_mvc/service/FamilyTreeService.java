package rd.reichheldek_sb_mvc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rd.reichheldek_sb_mvc.inmemory_repository.FamilyTreeRepository;
import rd.reichheldek_sb_mvc.model.Family;
import rd.reichheldek_sb_mvc.model.FamilyTree;
import rd.reichheldek_sb_mvc.model.FamilyTreeDTO;
import rd.reichheldek_sb_mvc.model.Individual;
import rd.reichheldek_sb_mvc.model.IndividualsFullDataDTO;
import rd.reichheldek_sb_mvc.utility.FamilyTreeConverter;
import rd.reichheldek_sb_mvc.utility.FileAttributeReader;
import rd.reichheldek_sb_mvc.utility.IFamilyDataParser;

@Service
public class FamilyTreeService {

    @Autowired
    private FamilyTreeRepository repository;

    @Autowired
    private IFamilyDataParser dataParser;

    @Autowired
    private FileAttributeReader fileAttributeReader;

    @Autowired
    private FamilyTreeConverter converter;


    public FamilyTreeDTO getFamilyTreeDTO() {

        FamilyTreeDTO familyTreeDTO = repository.getFamilyTreeDTO();

        if(familyTreeDTO == null || this.isNewerDataAvailable()) {         
            this.parseFamilyTreeAndUpdateDatabase();  
            familyTreeDTO = repository.getFamilyTreeDTO();
        }
        
        return familyTreeDTO;
    }


    public IndividualsFullDataDTO getIndividualsFullDataDTO(String id) {

        FamilyTree familyTree = repository.getFamilyTree();
        if(familyTree == null || this.isNewerDataAvailable()) {
            this.parseFamilyTreeAndUpdateDatabase();
            familyTree = repository.getFamilyTree();
        }

        //get individual object
        Individual individual = familyTree.getIndividualsLookUpMap().get(id);
        if(individual == null) {
            throw new IllegalArgumentException("Id " + id + " cannot be found.");
        }

        //look up families for family ties
        List<Family> familyTies = new ArrayList<>();
        List<Family> allFamilies = familyTree.getFamilies();

        for(Family family : allFamilies) {

            if((family.getParentIDs() != null && family.getParentIDs().contains( individual.getIndividualID() ))
                || (family.getChildrenIDs() != null && family.getChildrenIDs().contains( individual.getIndividualID() ))) 
            {
                familyTies.add(family);
            }
        }

        //create DTO with converter
        IndividualsFullDataDTO dto = converter.buildIndividualsFullDataDTO(individual, familyTies, familyTree.getIndividualsLookUpMap());

        return dto;
    }


    private boolean isNewerDataAvailable() {
        
        String lastModified = fileAttributeReader.readLastModifiedDate();
        boolean isNewerAvailable = !(lastModified.equals(repository.getLastModifiedDate()));

        if(isNewerAvailable) {
            repository.updateLastModifedDate(lastModified);
        }
        
        return isNewerAvailable;
    }


    public String getLastModifiedDate() {

        String lastModified = repository.getLastModifiedDate();

        if(lastModified == null) {
            isNewerDataAvailable();
            lastModified = repository.getLastModifiedDate();
        }
        
        return lastModified;
    }


    private void parseFamilyTreeAndUpdateDatabase() {

        CompletableFuture<FamilyTree> familyTreePromise = CompletableFuture.supplyAsync(() -> dataParser.parseDataToObject());
        
        try {
            if(familyTreePromise.get().isEmpty() == false) {

                repository.updateFamiltyTree(familyTreePromise.get());

                FamilyTreeDTO familyTreeDTO = converter.convertFamilyTreeToDTO(familyTreePromise.get());
                repository.updateFamiltyTreeDTO(familyTreeDTO);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    
}

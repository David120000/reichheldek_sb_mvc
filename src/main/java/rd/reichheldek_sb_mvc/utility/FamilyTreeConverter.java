package rd.reichheldek_sb_mvc.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import rd.reichheldek_sb_mvc.model.Family;
import rd.reichheldek_sb_mvc.model.FamilyLinkDTO;
import rd.reichheldek_sb_mvc.model.FamilyMemberDTO;
import rd.reichheldek_sb_mvc.model.FamilyTree;
import rd.reichheldek_sb_mvc.model.FamilyTreeDTO;
import rd.reichheldek_sb_mvc.model.FamilyWithMemberNamesDTO;
import rd.reichheldek_sb_mvc.model.Individual;
import rd.reichheldek_sb_mvc.model.IndividualDTO;
import rd.reichheldek_sb_mvc.model.IndividualsFullDataDTO;

@Service
public class FamilyTreeConverter {
    
    public FamilyTreeDTO convertFamilyTreeToDTO(FamilyTree familyTree) {

        List<IndividualDTO> nodes = new ArrayList<>();
        List<FamilyLinkDTO> links = new ArrayList<>();
        FamilyTreeDTO dto = new FamilyTreeDTO(nodes, links);

        // convert individual's data to graph nodes
        for(Individual individual : familyTree.getIndividuals()) {

            IndividualDTO individualDTO = new IndividualDTO(
                individual.getIndividualID(), 
                this.buildIndividualsName(individual), 
                individual.getDateOfBirth(), 
                individual.getDateOfDeath(), 
                this.setGroupIdForIndividualDTO(individual));

            nodes.add(individualDTO);
        }

        // convert family relationship data to graph links/edges
        for(Family family : familyTree.getFamilies()) {

            if(family.getChildrenIDs() != null) {
                for(String parentId : family.getParentIDs()) {
                    
                    for(String childId : family.getChildrenIDs()) {

                        FamilyLinkDTO linkDTO = new FamilyLinkDTO(parentId, childId);
                        links.add(linkDTO);
                    }
                }
            }
            else if(family.getParentIDs() != null) {
                for(int i = family.getParentIDs().size()-1; i > 0; i--) {

                    String sourceId = family.getParentIDs().get(i);
                    String targetId = family.getParentIDs().get(0);

                    FamilyLinkDTO linkDTO = new FamilyLinkDTO(sourceId, targetId);
                    links.add(linkDTO);
                }
            }        
        }

        return dto;
    }


    public IndividualsFullDataDTO buildIndividualsFullDataDTO(
            Individual individual, 
            List<Family> familyTies,
            HashMap<String, Individual> individualsLookUpMap) 
    {
        List<FamilyWithMemberNamesDTO> familiesWithNames = new ArrayList<>();

        for(Family family : familyTies) {

            List<FamilyMemberDTO> parents = new ArrayList<>();
            if(family.getParentIDs() != null) {
                for(String parentId : family.getParentIDs()) {

                    FamilyMemberDTO parent = new FamilyMemberDTO(parentId, this.buildIndividualsName( individualsLookUpMap.get(parentId) ));
                    parents.add(parent);
                }
            }

            List<FamilyMemberDTO> children = new ArrayList<>();
            if(family.getChildrenIDs() != null) {
                for(String childId : family.getChildrenIDs()) {

                    FamilyMemberDTO child = new FamilyMemberDTO(childId, this.buildIndividualsName( individualsLookUpMap.get(childId) ));
                    children.add(child);
                }
            }  

            FamilyWithMemberNamesDTO familyWithNames = new FamilyWithMemberNamesDTO(
                family.getFamilyID(), 
                family.getMarriageDate(), 
                parents, 
                children, 
                family.getComments());

            familiesWithNames.add(familyWithNames);
        }

        IndividualsFullDataDTO dto = new IndividualsFullDataDTO(
            individual.getIndividualID(), 
            this.buildIndividualsName(individual), 
            individual.getDateOfBirth(), 
            individual.getDateOfDeath(), 
            individual.getGender(), 
            individual.getComments(), 
            familiesWithNames);
        
        return dto;
    }


    private String buildIndividualsName(Individual individual) {

        String name = "";

        if(individual.getFirstName().length() > 0) {
            name += individual.getFirstName();
        }

        if(individual.getMiddleName().length() > 0) {
            name += " " + individual.getMiddleName();
        }

        if(individual.getLastName().length() > 0) {
            name += " " + individual.getLastName();
        }
        
        return name;
    }


    private String setGroupIdForIndividualDTO(Individual individual) {
       
        String group = "black";

        if(individual.getDateOfDeath() == null || (individual.getDateOfDeath() != null && individual.getDateOfDeath().length() == 0)) {

            if(individual.getGender().equals("F")) {
                group = "orange";
            }
            else if(individual.getGender().equals("M")) {
                group = "darkblue";
            }
        }
        else {

            if(individual.getGender().equals("F")) {
                group = "lightsalmon";
            }
            else if(individual.getGender().equals("M")) {
                group = "lightseagreen";
            }
        }

        return group;
    }
    
}

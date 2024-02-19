package rd.reichheldek_sb_mvc.model;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FamilyTree {
    
    private List<Individual> individuals;
    private List<Family> families;
    private HashMap<String, Individual> individualsLookUpMap;


    public Individual lookUpIndividualById(String id) {
        return individualsLookUpMap.get(id);
    }

    public boolean isEmpty() {
        return (individuals.size() == 0 || families.size() == 0);
    }

}

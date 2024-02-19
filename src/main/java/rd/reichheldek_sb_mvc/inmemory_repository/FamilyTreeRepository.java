package rd.reichheldek_sb_mvc.inmemory_repository;

import org.springframework.stereotype.Repository;

import rd.reichheldek_sb_mvc.model.FamilyTree;
import rd.reichheldek_sb_mvc.model.FamilyTreeDTO;

@Repository
public class FamilyTreeRepository {

    private FamilyTree familyTree;
    private FamilyTreeDTO familyTreeDTO;
    private String lastModifiedDate;


    public synchronized void updateFamiltyTree(FamilyTree familyTree) {
        this.familyTree = familyTree;
    }

    public FamilyTree getFamilyTree() {
        return this.familyTree;
    }

    public String getObjectIdOfFamilyTree() {
        return this.familyTree.toString();
    }


    public synchronized void updateFamiltyTreeDTO(FamilyTreeDTO familyTreeDTO) {
        this.familyTreeDTO = familyTreeDTO;
    }

    public FamilyTreeDTO getFamilyTreeDTO() {
        return this.familyTreeDTO;
    }

    public synchronized void updateLastModifedDate(String date) {
        this.lastModifiedDate = date;
    }

    public String getLastModifiedDate() {
        return this.lastModifiedDate;
    }


}

package rd.reichheldek_sb_mvc.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class FamilyWithMemberNamesDTO {

    private String familyID;
    private String marriageDate;
    private List<FamilyMemberDTO> parents;
    private List<FamilyMemberDTO> children;
    private String comments;
    
}

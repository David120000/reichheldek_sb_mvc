package rd.reichheldek_sb_mvc.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class FamilyTreeDTO {
    
    private List<IndividualDTO> nodes;
    private List<FamilyLinkDTO> links;

}

package rd.reichheldek_sb_mvc.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Family {
    
    private String familyID;
    private String marriageDate;
    private List<String> parentIDs;
    private List<String> childrenIDs;
    private String comments;

}

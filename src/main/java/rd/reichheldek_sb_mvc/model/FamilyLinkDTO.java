package rd.reichheldek_sb_mvc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class FamilyLinkDTO {

    private String source;
    private String target;
    
}

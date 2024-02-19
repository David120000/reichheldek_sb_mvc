package rd.reichheldek_sb_mvc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class IndividualDTO {
    
    private String id;
    private String name;
    private String birth;
    private String death;
    private String color;

}

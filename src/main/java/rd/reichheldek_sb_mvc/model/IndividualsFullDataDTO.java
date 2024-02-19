package rd.reichheldek_sb_mvc.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class IndividualsFullDataDTO {

    private String id;
    private String name;
    private String birth;
    private String death;
    private String gender;
    private String personalComment;
    private List<FamilyWithMemberNamesDTO> families;

}

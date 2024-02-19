package rd.reichheldek_sb_mvc.utility;

import rd.reichheldek_sb_mvc.model.FamilyTree;

public interface IFamilyDataParser {

    String parseDataToString();
    FamilyTree parseDataToObject();
    
}

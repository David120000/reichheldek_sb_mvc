package rd.reichheldek_sb_mvc.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import rd.reichheldek_sb_mvc.model.Family;
import rd.reichheldek_sb_mvc.model.FamilyTree;
import rd.reichheldek_sb_mvc.model.Individual;

@Service
public class OldGNOFormatParser implements IFamilyDataParser {

    private final Pattern individualIDRegexPattern;
    private final Pattern familyIDRegexPattern;


    public OldGNOFormatParser() {
        individualIDRegexPattern = Pattern.compile("^I[0-9].*");
        familyIDRegexPattern = Pattern.compile("^F[0-9].*");
    }


    @Override
    public String parseDataToString() {
        
        String data = "";

        try {
            BufferedReader buffReader = new BufferedReader(new FileReader("src\\main\\resources\\data\\Reichheldek.txt", Charset.forName("Windows-1250")));
            buffReader.readLine();

            String row = buffReader.readLine();

            while(row != null)  {

                data += row;
                row = buffReader.readLine();
            }

            buffReader.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }

        return data;
    }


    @Override
    public FamilyTree parseDataToObject() {

        List<Individual> individuals = new ArrayList<>();
        List<Family> families = new ArrayList<>();
        HashMap<String, Individual> individualsLookUpMap = new HashMap<>();
        
        FamilyTree familyTree = new FamilyTree(individuals, families, individualsLookUpMap);
        
        try {
            BufferedReader buffReader = new BufferedReader(new FileReader("src\\main\\resources\\data\\Reichheldek.txt", Charset.forName("Windows-1250")));
            buffReader.readLine();

            String row = buffReader.readLine();

            while(row != null)  {

                if(individualIDRegexPattern.matcher(row).matches()) {

                    Individual individual = parseIndividualRow(row.split("\\t"));
                    individuals.add(individual);

                    individualsLookUpMap.put(individual.getIndividualID(), individual);
                }

                if(familyIDRegexPattern.matcher(row).matches()) {

                    Family family = parseFamilyRow(row.split("\\t"));
                    families.add(family);
                }

                row = buffReader.readLine();
            }

            buffReader.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }

        return familyTree;
    }
    

    private Individual parseIndividualRow(String[] dataRow) {

        Individual individual = new Individual();
        individual.setIndividualID(dataRow[0]);
        individual.setFirstName(dataRow[1]);
        individual.setMiddleName(dataRow[2]);
        individual.setLastName(dataRow[3]);
        individual.setGender(dataRow[4]);

        if(dataRow.length >= 6) {
            individual.setDateOfBirth(dataRow[5]);
        }

        if(dataRow.length >= 7) {
            individual.setDateOfDeath(dataRow[6]);
        }

        if(dataRow.length >= 8) {
            individual.setComments(dataRow[7]);
        }

        return individual;
    }


    private Family parseFamilyRow(String[] dataRow) {

        Family family = new Family();
        family.setFamilyID(dataRow[0]);

        if(dataRow.length >= 2) {
            family.setMarriageDate(dataRow[1]);
        }

        if(dataRow.length >= 3) {
            family.setParentIDs(Arrays.asList(dataRow[2].split(",")));
        }

        if(dataRow.length >= 4) {
            family.setChildrenIDs(Arrays.asList(dataRow[3].split(",")));
        }

        if(dataRow.length >= 5) {
            family.setComments(dataRow[4]);
        }

        return family;
    }


}

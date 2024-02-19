package rd.reichheldek_sb_mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import rd.reichheldek_sb_mvc.model.IndividualsFullDataDTO;
import rd.reichheldek_sb_mvc.service.FamilyTreeService;

@RestController
public class DataQueriesRestController {
    
    @Autowired
    private FamilyTreeService familyTreeService;


    @GetMapping("/protected/api/individual/{id}")
    public IndividualsFullDataDTO getIndividualsData(@PathVariable String id) {

        IndividualsFullDataDTO dto = familyTreeService.getIndividualsFullDataDTO(id);

        return dto;
    }
}

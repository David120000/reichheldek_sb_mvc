package rd.reichheldek_sb_mvc.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import rd.reichheldek_sb_mvc.model.FamilyTreeDTO;
import rd.reichheldek_sb_mvc.service.FamilyTreeService;

@Controller
public class FrontendController {

    @Autowired
    private FamilyTreeService familyTreeService;
    

    @GetMapping("/")
    public String loadMainPage() {
        return "index.html";
    }


    @GetMapping("/error")
    public String displayErrorPage() {
        return "error.html";
    }

    
    @GetMapping("/public/test")
    public String parseTestData(Model model) throws InterruptedException, ExecutionException {
        
        FamilyTreeDTO familydata = familyTreeService.getFamilyTreeDTO();
        model.addAttribute("familydata", familydata);

        String lastUpdated = familyTreeService.getLastModifiedDate();
        model.addAttribute("updated", lastUpdated);

        return "familytree.html";
    }

    
    @GetMapping("/protected/reichheldek")
    public String displayFamilyTree(Model model) {
        
        FamilyTreeDTO familydata = familyTreeService.getFamilyTreeDTO();
        model.addAttribute("familydata", familydata);

        String lastUpdated = familyTreeService.getLastModifiedDate();
        model.addAttribute("updated", lastUpdated);

        return "familytree.html";
    }

}

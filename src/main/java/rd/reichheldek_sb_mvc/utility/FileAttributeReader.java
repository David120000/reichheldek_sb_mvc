package rd.reichheldek_sb_mvc.utility;

import java.io.File;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

@Service
public class FileAttributeReader {
    
    public String readLastModifiedDate() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.");
        File file = new File("src\\main\\resources\\data\\Reichheldek.txt");
        
        String lastModifiedDate = format.format(file.lastModified());

        return lastModifiedDate;
    }
}

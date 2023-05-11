package revision;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Cookie {
    String fileName;
    File file;

    // create cookie object
    public Cookie(String fileName) {
        this.fileName = "C:\\Users\\Lydia\\Documents\\Work Matters\\Visa\\NUS\\SDF\\Project\\day06_workshop\\src\\main\\java\\revision\\" + fileName;
        
        // get cookie file
        Path path = Paths.get(this.fileName);
        file = path.toFile();
        
        if (!file.exists()) {
            System.err.println(fileName + " not found");
            System.exit(0);
        }
    }

    public byte[] getFile() throws IOException {
        // get file stream
        InputStream is = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);

        byte[] buffer = new byte[1024];
        bis.read(buffer);

        // for bigger files: 
        //      int size;
        //      while ((size = bis.read(buffer)) != -1) {
        //          bis.read(buffer);
        //      }

        bis.close();
        is.close();

        return buffer;
    }
 
    public String getCookie() throws IOException {
        Reader fr = new FileReader(this.fileName);
        BufferedReader br = new BufferedReader(fr);
        
        String line;

        List<String> list = new ArrayList<>();
        
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        
        br.close();
        fr.close();

        int index = (int)(Math.random() * list.size());
        String cookie = list.get(index);        

        return cookie;
    }


}

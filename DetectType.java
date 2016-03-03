import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.*;
import org.apache.tika.Tika;
import java.util.*;

public class DetectType {

	public static void main( String[] args) {
		System.out.println("hello");
		Tika tika = new Tika();
        String source = "./"; // /home/599/hw1/
		String sourcePath = source + "octet_files_ubuntu_recognized";
        File folder = new File(sourcePath);
        File[] files = folder.listFiles();
        
        String destPath = source + "octet_sorted";
        make_dir(destPath);
        
		String outputPath = source + "octet_sort.sh";
		PrintWriter writer = null;
		try {
            File ofile = new File(outputPath);
            if (ofile.exists()) ofile.delete();
			writer = new PrintWriter(outputPath);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HashMap<String,String> typehash = new HashMap<String,String>();
        for (File file : files) {
            if (file.isDirectory()) continue;
            
            String type = null;
            try {
                type = tika.detect(file);
            } catch(IOException e) {
                e.printStackTrace();
                continue;
            }
            if (type.equals("application/octet-stream")) continue;
            
            if(!typehash.containsKey(type)) {
                make_dir(destPath + "/" + type);
                typehash.put(type, destPath + "/" + type);
            }
            writer.println("mv " + sourcePath + "/"  + file.getName() + " " + typehash.get(type));				
        }
		writer.close();
	}
    
	private static void make_dir(String directory) {
		File theDir = new File(directory);
		if (!theDir.exists()) {
			System.out.println("creating directory: " + directory);
			boolean result = false;

			try {
				theDir.mkdirs();
				result = true;
			} catch(SecurityException se) {
				System.out.println("error creating director");
			}        
			if (result) {    
				System.out.println("DIR created");  
			}
		}
	}
}


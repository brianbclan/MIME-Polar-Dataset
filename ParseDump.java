
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

public class ParseDump {

	public static void main( String[] args)
	{
		Tika tika = new Tika();

		String directory = "/Users/yeh/2016_Spring/599_DC/hw1/categorized";
		String inputfile = "/Users/yeh/2016_Spring/599_DC/hw1/file_list.txt";
		String outputfile ="/Users/yeh/2016_Spring/599_DC/hw1/move_file.sh";
		make_dir( directory );
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outputfile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HashMap typehash = new HashMap();
		try (BufferedReader br = new BufferedReader(new FileReader(inputfile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	//String contentfile = "/Users/yeh/2016_Spring/599_DC/hw1/dump/sciencebase/776BC017553BDFF1ABF01CDE08F87D462B81F5FA51F64899D76DE48DFB971120";
				String type = tika.detect(new File(line));
				System.out.println(line + ": " + type);
				if( !typehash.containsKey(type) )
				{
					make_dir( directory + "/" + type );
					typehash.put(type, directory + "/" + type);
				}
				writer.println( "mv " + line + " " + typehash.get(type) );				
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.close();
		
	}
	private static void make_dir( String directory )
	{
		File theDir = new File(directory);
		if (!theDir.exists()) {
		    System.out.println("creating directory: " + directory);
		    boolean result = false;

		    try{
		        theDir.mkdirs();
		        result = true;
		    } 
		    catch(SecurityException se){
		        System.out.println("error creating director");
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}
	}
}

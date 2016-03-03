/*
Author:Chiung-Lun Hung
Course:CSCI599 MIME
Version:1.0
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class bfa{
	public static void main (String[] args){
		//foldeNname takes foler name from the input of arguments zero
		String folderName = args[0];
		String path = "./categorized_partition%75/application/" + folderName + "/";
		String outputFile = "./BFA_JSON/" + folderName + ".json";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		double[] bytes = new double[256];
		ArrayList<double[]> collect = new ArrayList<double[]>(); 

		for (File file : listOfFiles){
			if (file.isFile()){
				 collect.add(countBytes(path + file.getName()));
				 System.out.println(file.getName());
			}
		}
		
		for (int i = 0; i < collect.size(); i++){
			for (int j = 0; j < collect.get(i).length; j++){
				bytes[j] += collect.get(i)[j];
			}
		}
		
		double max = 0.0;
		for (int i = 0; i < bytes.length; i++){
			bytes[i] = muLaw(bytes[i] / collect.size());
			max = Math.max(max, bytes[i]);
		}

		JSONArray jsa = new JSONArray();
		for (int i = 0; i < bytes.length; i++){
			JSONObject jso = new JSONObject();
			jso.put("byte", new Integer(i));
			jso.put("frequency", new Double(toFourDeci(bytes[i] / max)));
			jsa.add(jso);
		}
		
		try{
			PrintWriter writer = new PrintWriter(outputFile);
			JSONObject jsoR = new JSONObject();
			jsoR.put(folderName, jsa);
			writer.println(jsoR.toJSONString());
			writer.flush();
			writer.close();
		}catch (FileNotFoundException ex){
			ex.printStackTrace();
		}
	
	}

	public static double[] countBytes (String input){
		FileInputStream fileInputStream = null;
	 	File file = new File(input);
		byte[] bFile = new byte[(int)file.length()];
		int[] bytes = new int[256];
		double[] result = new double[256];
		int max = 0;
               
	       	try{
		       	fileInputStream = new FileInputStream(file);
		       	fileInputStream.read(bFile);
		       	fileInputStream.close();

		       	for (int i = 0; i < bFile.length; i++){
				int bNumber = bFile[i] & 0xFF;
				bytes[bNumber]++;
				max = Math.max(max, bytes[bNumber]);
			 }
		}catch(Exception e){
			 e.printStackTrace();
		}

		for (int i = 0; i < bytes.length; i++){
			result[i] = toFourDeci(bytes[i] / (double)max);
		}

		return result;
	}

	public static double log (double input){
		return (double)(Math.log(input) / Math.log(2));
	}

	public static double muLaw (double input){
		int sign = input >= 0 ? 1 : -1;
		int mu = 255;
		double f = sign * ((log(1 + mu * Math.abs(input))) / (log(1 + mu)));

		return toFourDeci(f);
	}

	public static double toFourDeci(double input){
		return Math.round(input * 10000.0) / 10000.0;
	}
}	

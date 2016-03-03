package usc.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class FHT {

	static public void main( String[] args )
	{
		//String directory = "/Users/yeh/2016_Spring/599_DC/hw1/categorized";
		//String[] folders = {"atom+xml","dif+xml","epub+zip","fits","gzip","msword","octet-stream","ogg","pdf","postscript","rdf+xml","rss+xml","rtf","vnd.google-earth.kml+xml","vnd.ms-excel","vnd.oasis.opendocument.text","vnd.rn-realmedia","x-7z-compressed","x-bibtex-text-file","x-bittorrent","x-bzip","x-bzip2","x-compress","x-debian-package","x-elc","x-executable","x-font-ttf","x-grib","x-gtar","x-hdf","xhtml+xml","x-lha","x-matroska","xml","x-msdownload","x-msmetafile","x-rpm","x-sh","x-shockwave-flash","xslt+xml","x-tar","x-tex","x-tika-msoffice","x-tika-ooxml","x-xz","zip","zlib"};
		//String[] folders = { "atom+xml"};
		//String[] folders = { "xml"};
		//String[] folders = {"quicktime"};
		//String[] folders = {"mp4","mpeg","quicktime","x-flv","x-m4v","x-ms-asf","x-msvideo","x-ms-wmv"};
		String[] folders = { "x-matlab","tiff","rfc822"};
		for( int f = 0; f < folders.length; ++f)
		{
			//String inputfile = "/Users/yeh/2016_Spring/599_DC/hw1/" + folders[f] + "_file_list.txt";
			String inputfile = "/home/599/hw1/file_list/" + folders[f] + "_file_list.txt";
			//String outputfile ="/Users/yeh/2016_Spring/599_DC/hw1/" + folders[f] + ".csv";
			String outputfile = "/home/599/hw1/FHT75/" + folders[f] + ".csv";
			int H = 128;
			int[][] table = new int[H][256];
			for( int i = 0; i < H; ++i)
			{
				for( int j = 0; j < 256; ++j )
				{
					table[i][j] = 0;
				}
			}
			//int nr_files = 0;
			int[] nr_files = new int[H];
			try (BufferedReader br = new BufferedReader(new FileReader(inputfile))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			    	FileInputStream fis = new FileInputStream(line);
	//		    	if( file_reader.getEncoding() == "UTF-8")
	//		    	{
	//		    		if( file_reader.)
	//		    	}
			    	//++nr_files;
			    	//System.out.println(line);
			    	byte[] bs =new byte[H];
			    	fis.mark(2);
			    	int bytes_read = fis.read(bs);
			    	if( bytes_read > 1 && bs[0] == -2 && bs[1]== -1)
			    	{
			    		System.out.println("in bytes reset");
			    		fis.reset();
			    		bytes_read = fis.read(bs);
			    	}
					if( bytes_read == 0 ) continue;
					//++nr_files;
			    	for( int j = 0; j < H && j < bytes_read ; ++j )
			    	{
			    		//System.out.printf("j is %d\n", j );
			    		++table[j][bs[j]&0xFF];
			    		++nr_files[j];
			    		//System.out.println(bs[j]+128);
			    	}
				fis.close();
			    }
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(outputfile);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			writer.printf("letter");
			for( int i = 0; i < H; ++i )
			{
				writer.printf(",byte_%d",i);
			}
			writer.printf("\n");
			//int[][] table = new int[H][256];
			for( int i = 0; i < 256; ++i )
			{
				char c = (char)i;
				if( i != 34 && ( Character.isLetterOrDigit(c) || (!Character.isISOControl(c)) ) )
					writer.printf("\"%d (%c)\"", i, c);
				else
					writer.printf("\"%d (%d)\"", i, i);
				for( int j = 0; j < H; ++j )
				{
					if ( nr_files[j] != 0 )
						writer.printf(",%.6f", table[j][i]*1.0/nr_files[j]);
					else
						writer.printf(",%.6f", 0.0);
				}
				writer.print('\n');
			}
			writer.close();
		}
	}
}

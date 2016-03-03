
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ExtractFHT {
	
	
	static public void main( String[] args)
	{
		readCSV();
	}
	public static void printJson( PrintWriter writer, int h, int b, double value)
	{
		//{"value": 0.9385566891880558, "byte1": 0, "byte0": 0},
		writer.printf("{ \"value\": %.8f, \"byte1\": %d, \"byte0\": %d}", value, h, b );
	}
	public static void readCSV()
	{
		String directory = "/Users/yeh/content/FHT75/";
		String[] folders = {"x-matlab", "tiff", "rfc822", "mp4","mpeg","quicktime","x-flv","x-m4v","x-ms-asf","x-msvideo","x-ms-wmv","atom+xml","dif+xml","epub+zip","fits","gzip","msword","octet-stream","ogg","pdf","postscript","rdf+xml","rss+xml","rtf","vnd.google-earth.kml+xml","vnd.ms-excel","vnd.oasis.opendocument.text","vnd.rn-realmedia","x-7z-compressed","x-bibtex-text-file","x-bittorrent","x-bzip","x-bzip2","x-compress","x-debian-package","x-elc","x-executable","x-font-ttf","x-grib","x-gtar","x-hdf","xhtml+xml","x-lha","x-matroska","xml","x-msdownload","x-msmetafile","x-rpm","x-sh","x-shockwave-flash","xslt+xml","x-tar","x-tex","x-tika-msoffice","x-tika-ooxml","x-xz","zip","zlib"};
		//String[] folders = {"atom+xml"};
		//String[] folders = {"x-matlab", "tiff", "rfc822"};
		String outfile = directory + "FHT_result.txt";
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outfile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int H=128;
		for( int f = 0; f < folders.length; ++f)
		{
			String csvFile = directory + folders[f] + ".csv";
			String json_file_name = directory + folders[f] + ".json";
			PrintWriter jwriter = null;
			try {
				jwriter = new PrintWriter(json_file_name);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			jwriter.print('[');
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";
			writer.printf("%s: ", folders[f]);
			try {
				br = new BufferedReader(new FileReader(csvFile));
				line = br.readLine();
				double[][] freqs = new double[256][H];
				int ln = 0;
				while ((line = br.readLine()) != null) {
					// use comma as separator
					String[] freq = null ;
					if ( ln != 44 ) freq = line.split(cvsSplitBy);
					else
					{
						line = "\"44 (-)\"" + line.substring(8); 
						//line = line.substring(5, line.length());
						freq = line.split(cvsSplitBy);
					}
					for( int i = 1; i < freq.length; ++i )
					{
						//System.out.printf("%d %d %s\n",ln, i, freq[i]);
						freqs[ln][i-1] =Double.parseDouble(freq[i]); 
					}
					++ln;
				}
				br.close();
				for( int i = 0; i < H; ++i )
				{
					boolean found = false;
					for( int j = 0; j < 256; ++j )
					{
						if ( freqs[j][i] > 0.95)
						{
							//System.out.printf("Found it %d %d\n", j, i );
							char c = (char) (j);
							if( j != 34 && j != '*' && ( Character.isLetterOrDigit(c) || (!Character.isISOControl(c)) ) )
								writer.printf("%c", j);
							else
								writer.printf("\\x%x", j);
							found = true;
						}
					}
					if( !found )
					{
						writer.print('*');
					}
				}
				writer.print('\n');
				
				for( int i = 0; i < 16; ++i )
				{
					for( int j = 0; j < 256; ++j )
					{
						printJson( jwriter, j, i, freqs[j][i] );
						if( i != 15 || j != 255 )
						{
							jwriter.print(", ");
						}
					}
				}
				jwriter.print(']');
				jwriter.close();
	
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		writer.close();
		System.out.println("Done");
	}
}

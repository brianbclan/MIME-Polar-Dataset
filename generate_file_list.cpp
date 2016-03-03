#include<iostream>
#include<istream>
#include<ostream>
#include<fstream>
#include<string>

using namespace std;

int main()
{
	//string folders[] = {"atom+xml","dif+xml","epub+zip","fits","gzip","msword","octet-stream","ogg","pdf","postscript","rdf+xml","rss+xml","rtf","vnd.google-earth.kml+xml","vnd.ms-excel","vnd.oasis.opendocument.text","vnd.rn-realmedia","x-7z-compressed","x-bibtex-text-file","x-bittorrent","x-bzip","x-bzip2","x-compress","x-debian-package","x-elc","x-executable","x-font-ttf","x-grib","x-gtar","x-hdf","xhtml+xml","x-lha","x-matroska","xml","x-msdownload","x-msmetafile","x-rpm","x-sh","x-shockwave-flash","xslt+xml","x-tar","x-tex","x-tika-msoffice","x-tika-ooxml","x-xz","zip","zlib", ""};
	string path = "/home/599/hw1/categorized_partition%75/video/";
	ofstream out("generate_file_list.sh"); //( path + folder[i] + "_file")
	string folders[] ={"mp4","mpeg","quicktime","x-flv","x-m4v","x-ms-asf","x-msvideo","x-ms-wmv"};
    for( int i = 0; !folders[i].empty(); ++i )
	{
		out << "ls -d -1 "<< path << folders[i] <<"/**" << ">" << folders[i] << "_file_list.txt\n";
 	}

 	out.close();

 	ofstream out2("generate_file_counts.sh");
	for( int i = 0; !folders[i].empty(); ++i )
	{
		out << "wc -l "<< path << folders[i] << ">> filecounts\n";
 	} 	
 	out2.close();
}

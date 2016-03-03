# MIME-Polar-Dataset
_This is a project of CSCI 599 at USC._

* **find polar_dump -exec readlink -f {} \; > file_list.txt** : this file_list will be used as input file for ParseDump
* **ParseDump.java** : call tika::detect() to detect file types of all the files dumped from polar dataset
* **/Users/yeh/2016_Spring/599_DC/hw1/move_file.sh** : output file for ParseDump.java, will move files into categorized/
* **generate_file_list.cpp** : write out shell script to generate file list for different file types
* **generate_file_list.sh** : contains the command to generate file list for different file types (should be executed inside file_list/
* **FHT.java** : read different file_list.txt to calculate FHT of each file and write results into seperate csv file
* **ExtractFHT** : read different FHT.csv files and output to json format for d3 visualization, also it checks if there is any dominant character (95%) for each byte in different file types.
* **FHT/index.html** : main webpage to visualize result of FHT in matrix chart
* **FHT/FHT_Barchart.html** : webpage to visualize result of FHT in bar chart


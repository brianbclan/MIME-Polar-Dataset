# MIME-Polar-Dataset
_This is a project of CSCI 599 at USC._

## Main
* **find polar_dump -exec readlink -f {} \; > file_list.txt**: this file_list will be used as input file for ParseDump
* **ParseDump.java**: call tika::detect() to detect file types of all the files dumped from polar dataset
* **/Users/yeh/2016_Spring/599_DC/hw1/move_file.sh**: output file for ParseDump.java, will move files into categorized/
* **generate_file_list.cpp**: write out shell script to generate file list for different file types
* **generate_file_list.sh**: contains the command to generate file list for different file types (should be executed inside file_list/
* **FHT.java**: read different file_list.txt to calculate FHT of each file and write results into seperate csv file
* **ExtractFHT.java**: read different FHT.csv files and output to json format for d3 visualization, also it checks if there is any dominant character (95%) for each byte in different file types.
* **partitionFiles.py**: used to partition the categorized data into 25% and 75% portions as training and test sets
* **findCrossCorrelation.py**: used to generate the cross correlation matrix for each file type
* **findInputCorrelation.py**: used to generate the correlation matrix of a single input file for each file type
* **stringConverter.py**: used to generate json file for overall MIME type D3 visualization
* **bfa.java**: used to calculate byte frquency for each file type

## FHT
* **FHT/index.html**: main webpage to visualize result of FHT in matrix chart
* **FHT/FHT_Barchart.html**: webpage to visualize result of FHT in bar chart

## BFC
* **BFC/index.html**: main webpage to visualize the correlation matrix obtained from BFC
* **BFC/app.java**: d3 javascript snippet for generating the heat map representation
* **BFC/randomMatrixGenerator.py**: test file that generates sample json file for correlation matrix
* **BFC/BFC_json_cross_correlation**: cross correlation json data
* **BFC/BFC_json_single_input**: single file correaltion json data

## BFA
* **BFA/75%/folder/index.html: main webpage to visualiza the byte frequency from BFA of 75% data
* **BFA/75%/folder/file.json: json file for each type's d3 visualization's webpage of 75% data
* **BFA/25%/folder/index.html: main webpage to visualiza the byte frequency from BFA of 25% data
* **BFA/25%/folder/file.json: json file for each type's d3 visualization's webpage of 25% data

## Pie Chart
* **Pie Chart/index.html: d3 visualization for overall MIME type
* **Pie Chart/mimetype.txt: original overall MIME type json file
* **Pie Chart/mimetypeC.txt: convert to the format that fit the input of pie chart

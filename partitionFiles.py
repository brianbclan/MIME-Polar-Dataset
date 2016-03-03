import os, sys, math
from shutil import copyfile, rmtree
from collections import defaultdict
import random

# write to shell file
def writeToSH(source, dest, f, fileList):
    if not os.path.exists(dest):
        os.makedirs(dest)      
    filesource = os.path.join(source, f)
    filedest = os.path.join(dest, f)
    fileList.write("cp " + filesource + " " + filedest + "\n")

# dfs(source, dest1, dest2, r): go through all the directories given source
#                               partition the files under a directory with a specified ratio r

def dfs(source, dest1, dest2, fileList, r):
    items = [name for name in os.listdir(source)]
    for item in items:
        newpath = os.path.join(source, item)
        newdest1 = os.path.join(dest1, item)
        newdest2 = os.path.join(dest2, item)
        if os.path.isdir(newpath):
            print "Partitioning the folder " + newpath
            # if it is the octet-stream files, then partition them into 4 even parts
            #if item == "octet-stream":
                #items = [name for name in os.listdir(newpath)]
                #random.shuffle(items)
                #size = int(math.floor(len(items)*0.25))
                #start = size*[0, 1, 2, 3]
                #end = size*[1, 2, 3]
                #end += [len(items)]
                #for i in xrange(0,4):
                    #p = items[start[i]: end[i]]
                    #for f in p:
                        #filesource = os.path.join(newpath, f)
                        #filedest = os.path.join(home + "octet_partition" + str(i), f)
                        #copyfile(filesource, filedest)
                #continue
            if newpath != exception:
                dfs(newpath, newdest1, newdest2, fileList, r)
        else:
            # the folder contains only files
            # partition them and return
            numFiles = len(items)
            p1size = int(math.ceil(numFiles*r)) # size of the partition 1 (note p2size would be numFiles - p1size)
            random.shuffle(items)
            p1 = items[:p1size]
            p2 = items[p1size:]
            for f in p1:
                writeToSH(source, dest1, f, fileList)
            for f in p2:            
                writeToSH(source, dest2, f, fileList)
            return


home = '/home/599/hw1/'
#home = './'
filename = home + 'partitionFiles.sh'
if os.path.isfile(filename):
    os.remove(filename)
fileList = open(filename, 'wb')

# specify the file names
source = home + "categorized"
exception = home + "categorized/application"
dest1 = home + "categorized_partition%25"
dest2 = home + "categorized_partition%75"

# remove the old directories
#~ if os.path.isdir(dest1):
    #~ rmtree(dest1)
#~ if os.path.isdir(dest2):
    #~ rmtree(dest2)
    
#for i in xrange(0,4):
    #path = home + "octet_partition" + str(i)
    #if os.path.isdir(path):
        #rmtree(path)
    #os.makedirs(path)
# remove the old files

dfs(source, dest1, dest2, fileList, 0.25)
print "Files in " + source + " have been partitioned"

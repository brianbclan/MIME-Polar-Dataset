import os, sys, math, json
import math, random
from shutil import copyfile, rmtree
from collections import defaultdict

# specify the directory names
source = "./categorized_partition%25"
exception = "./categorized_partition%25/application"
histDir = "./BFA_histogram_json%75/"
jsonDir = "./BFC_json_cross_correlation/"
if os.path.exists(jsonDir):
    rmtree(jsonDir)
os.makedirs(jsonDir)

typeFolders = [name for name in os.listdir(source)]
for typeFolder in typeFolders:
    typeFolderPath = os.path.join(source, typeFolder)
    if typeFolderPath == exception:
        continue
    typenames = [name for name in os.listdir(typeFolderPath)]

    for typename in typenames:
        typeDir = os.path.join(typeFolderPath, typename)
        fnames = [name for name in os.listdir(typeDir)]
        fnum = float(len(fnames))
        
        # initialize the matrix avgDiff for average freq difference and corrStrength for correlation strength
        avgDiff = []
        corrStrength = []
        for i in xrange(256):
            corrStrength += [[]]
            avgDiff += [[]]
            for j in xrange(256):
                corrStrength[i] += [0]
                avgDiff[i] += [0]
        
        # load the average histogram
        avgHistFileName = os.path.join(histDir, typeFolder, typename) + ".json"
        # if the type is in %25 but not in %75, then we skip it
        if not os.path.isfile(avgHistFileName):
            continue
        
        with open(avgHistFileName, 'r') as jsonfile:
            data = json.load(jsonfile)
        avgHist = {}
        for i in xrange(256):
            avgHist[i] = data[i]["frequency"]
        jsonfile.close();
        
        print "Generating cross corr for type: " + typename
        
        # go through and average corr mtx for each file
        for fname in fnames:
            # find the histogram of the picked file
            hist = {}
            for i in xrange(256):
                hist[i] = 0.0
            maxVal = 0.0
            filepath = os.path.join(typeDir, fname)
            f = open(filepath, 'rb')
            while True:
                byte = f.read(1)
                if byte == '':
                    break
                hist[ord(byte)] += 1.0
                maxVal = max([maxVal, hist[ord(byte)]])
            f.close();
            
            # if the file is empty, then don't care about this
            #~ if maxVal == 0.0:
                #~ fnum -= 1
                #~ continue
            
            for i in xrange(256):
                hist[i] = math.log(1 + 255*hist[i]/maxVal)/math.log(256)
            
            # find the correlation matrix
            # find the average freq difference
            for i in xrange(256):
                for j in xrange(256):
                    corrStrength[i][j] += 1 - abs(hist[i] - avgHist[j])
                    avgDiff[i][j] += hist[i] - hist[j]
                    
        corrMtx = []
        for i in xrange(256):
            for j in xrange(0, i):
                corrMtx += [{"byte0": i, "byte1": j, "value": avgDiff[i][j]/fnum}] # take average
            for j in xrange(i, 256):
                corrMtx += [{"byte0": i, "byte1": j, "value": corrStrength[i][j]/fnum}]
        jsonDest = os.path.join(jsonDir, typeFolder)
        if not os.path.exists(jsonDest):
            os.makedirs(jsonDest)                
        jsonFileName = os.path.join(jsonDir, typeFolder, typename +  '.json')
        if os.path.isfile(jsonFileName):
            os.remove(jsonFileName)
        with open(jsonFileName, 'w') as filejson:
            json.dump(corrMtx, filejson)
    
print "Cross correlaton matrix generated for " + source

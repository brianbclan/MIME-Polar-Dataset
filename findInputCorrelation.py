import os, sys, math, json
import math, random
from shutil import copyfile, rmtree
from collections import defaultdict

# specify the directory names
source = "./categorized_partition%25/"
exception = "./categorized_partition%25/application"
histDir = "./BFA_histogram_json%75/"
jsonDir = "./BFC_json_single_input/"
if os.path.exists(jsonDir):
    rmtree(jsonDir)
os.makedirs(jsonDir)

inputRecord = jsonDir + "input.txt"
if os.path.isfile(inputRecord):
    os.remove(inputRecord)
inputRecordFile = open(inputRecord, 'w')

typeFolders = [name for name in os.listdir(source)]
for typeFolder in typeFolders:
    typeFolderPath = os.path.join(source, typeFolder)
    if typeFolderPath == exception:
        continue
    
    typenames = [name for name in os.listdir(typeFolderPath)]
    for typename in typenames:
        typeDir = os.path.join(typeFolderPath, typename)
        fnames = [name for name in os.listdir(typeDir)]
        
        avgHistFileName = os.path.join(histDir, typeFolder, typename) + ".json"
        # if the type is in %25 but not in %75, then we skip it
        if not os.path.isfile(avgHistFileName):
            continue
            
        # load the average histogram
        with open(avgHistFileName, 'r') as jsonfile:
            data = json.load(jsonfile)
        avgHist = {}
        for i in xrange(256):
            avgHist[i] = data[i]["frequency"]    
        
        # randomly pick a file
        i = random.randint(0, len(fnames) - 1)
        filepath = os.path.join(typeDir, fnames[i])
        inputRecordFile.write(typename + " " + fnames[i])
        print "Generating single-input corr for type: " + typename
        
        # find the histogram of the picked file
        hist = {}
        for i in xrange(256):
            hist[i] = 0.0
        maxVal = 0.0
        f = open(filepath, 'rb')
        while True:
            byte = f.read(1)
            if byte == '':
                break
            hist[ord(byte)] += 1.0
            maxVal = max([maxVal, hist[ord(byte)]])
        
        # if the file is empty, then don't care about this
        if maxVal == 0.0:
            continue
        for i in xrange(256):
            hist[i] = math.log(1 + 255*hist[i]/maxVal)/math.log(256)
        
        # find the correlation matrix
        corrStrength = []
        for i in xrange(256):
            corrStrength += [[]]
            for j in xrange(256):
                corrStrength[i] += [1 - abs(hist[i] - avgHist[j])]
        
        # find the average freq difference
        avgDiff = []
        for i in xrange(256):
            avgDiff += [[]]
            for j in xrange(256):
                avgDiff[i] += [hist[i] - hist[j]]
                
        corrMtx = []
        for i in xrange(256):
            for j in xrange(0, i):
                corrMtx += [{"byte0": i, "byte1": j, "value": avgDiff[i][j]}]
            for j in xrange(i, 256):
                corrMtx += [{"byte0": i, "byte1": j, "value": corrStrength[i][j]}]
        jsonDest = os.path.join(jsonDir, typeFolder)
        if not os.path.exists(jsonDest):
            os.makedirs(jsonDest)
        jsonFileName = os.path.join(jsonDir, typeFolder, typename) +  '.json'
        if os.path.isfile(jsonFileName):
            os.remove(jsonFileName)
        with open(jsonFileName, 'w') as filejson:
            json.dump(corrMtx, filejson)
    
print "Correlaton matrix generated for " + source

import os, random, json

matrix = [[random.random() for e in xrange(256)] for e in xrange(256)]
mdict = []
for i in xrange(256):
    for j in xrange(256):
        mdict += [{"byte0": i, "byte1": j, "value": matrix[i][j]}]
mj = {"data": mdict}

if os.path.isfile('data.json'):
    os.remove('data.json')
with open('data.json', 'w') as filejson:
    json.dump(mj, filejson)

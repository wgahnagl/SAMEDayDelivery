from random import * 
from math import *


TRIPS = []
TRIPMOVES = []

BEGIN = 1546300800  # 1 Jan 2019
END =   1554076800  # 1 Apr 2019

class Warehouse:
	def __init__(self, _id):
		self.id = _id
		self.packages = []
		self.lastUpdate = 1546300800

	def deliverSomePackages(self, other):
		### Transfer the packages

		keep = []
		send = []

		for p in self.packages:
			if randint(0, 2) == 0:
				keep.append(p)
			else:
				send.append(p)

		self.packages = keep
		other.packages += send

		### Sync the two warehouses

		maxLastUpdate = max(self.lastUpdate, other.lastUpdate)
		self.lastUpdate = maxLastUpdate
		other.lastUpdate = maxLastUpdate		

		### Choose start and end time

		starttime = randint(maxLastUpdate, maxLastUpdate + 86400)
		endtime = randint(starttime + 10000, starttime + 86400)
		self.lastUpdate = endtime
		other.lastUpdate = endtime

		TRIPS.append([len(TRIPS)+1, starttime, endtime, self.id, other.id, randint(1,200),False])
		[TRIPMOVES.append([len(TRIPS), p]) for p in send]

	def prettyPrintSelf(self):
		print(str(self.id) + " " + str(self.packages))		


def prettyTrip(trip):
	res = str(trip[0])
	for i in range(1, 7):
		res += ","
		res += str(trip[i])
	return res

def prettyMove(move):
	return str(move[0]) + "," + str(move[1])
	

wh = [Warehouse(i) for i in range(1, 201)]

for i in range(0, 500):
	start = randint(0, 199)
	end = start
	while end == start:
		end = randint(0, 199)

	wh[start].deliverSomePackages(wh[end])

f = open("trip.csv", "w")
[f.write( prettyTrip(p) ) for p in TRIPS]
f.close()

f = open("tripPackage.csv", "w")
[f.write( prettyMove(m) ) for m in TRIPMOVES]
f.close()
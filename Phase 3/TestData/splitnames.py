#!/usr/bin/python

with open("customer.csv") as f:
    with open("customerLastFirst.csv", "w") as w:
        bogusFirstLine = f.readline()
    
        for line in f:
            values = line.split(",")
            name = values[1]
            
            parts = name.split(" ", 1)
            newName = parts[1] + "," + parts[0]
            w.write(line.replace(name, newName, 1))
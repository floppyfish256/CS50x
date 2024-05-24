from csv import reader
from sys import argv, exit

# Analyze command line arguments
if len(argv) != 3:
    print("Usage: python dna.py data.csv sequence.txt")
    exit(1)
# Open the CSV file, read contents into memory
my_array = []
with open(argv[1]) as fp:
    line = fp.readline().rstrip('\n')
    while line:
        my_list = line.split(",")
        line = fp.readline().rstrip('\n')
        my_array.append(my_list)

# Find the longest consecutive repeats
# str_list keeps the list of longest runs for STRs
str_list = []
# Max keeps longest run
MAX = 0
txt = open(argv[2], "r")
# comp is the string format of the sequence
comp = (txt.read())
# Repeats for each STR
for STR in my_array[0][1:]:
    # c is length of current run
    c = 0
    i = 0
    # Repeats until STR reaches the end
    while (i < len(comp)-1):
        if comp[i:i+len(STR)] == STR:
            # Adds matching STR to c (counting matches)
            c += 1
            i += len(STR)
        else:
            # Applies c to MAX
            if c > MAX:
                MAX = c
            c = 0
            i += 1
    str_list.append(str(MAX))
    MAX = 0

# Comparing against data
for person in my_array[1:]:
    if person[1:] == str_list:
        print(person[0])
        sys.exit()
print("No match\n")
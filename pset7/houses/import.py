import cs50
import csv
from sys import argv

# Check command-line arguments
if len(argv) != 2:
    print("Usage: python import.py file.csv")
    exit(1)

# Open csv file given by command-line argument

# Create database
# Close file so that you don't keep adding
open(f"students.db", "w").close()
db = cs50.SQL("sqlite:///students.db")
db.execute("CREATE TABLE students(first TEXT, middle TEXT, last TEXT, house TEXT, birth NUMERIC)")

# Open CSV file
with open(argv[1], "r") as characters:

    # Create DictReader
    reader = csv.DictReader(characters)

    # Iterate over CSV file
    for row in reader:

        name = row["name"].split()
        first = name[0]
        if len(name) == 2:
            middle = None
        else:
            middle = name[1]
        last = name[-1]
        house = row["house"]
        birth = row["birth"]
        name = []

        # Insert show
        db.execute("INSERT INTO students (first, middle, last, house, birth) VALUES(?, ?, ?, ?, ?)", first, middle, last, house, birth)
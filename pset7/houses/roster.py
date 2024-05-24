import cs50
from sys import argv

# Check command-line arguments
if len(argv) != 2:
    print("Usage: python roster.py house")
    exit(1)

house = argv[1]
db = cs50.SQL("sqlite:///students.db")
rows = db.execute("SELECT * FROM students WHERE house = ? ORDER BY last, first", house)

for row in rows:
    if row["middle"] == None:
        print(row["first"] + " " + row["last"] + ", born " + str(row["birth"]))
    else:
        print(row["first"] + " " + row["middle"] + " " + row["last"] + ", born " + str(row["birth"]))
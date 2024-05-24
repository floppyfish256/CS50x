from cs50 import get_float

i = 0
change = -1
while change < 0:
    change = get_float("Change owed: ")*100
while change/25 >= 1:
    i += 1
    change -= 25
while change/10 >= 1:
    i += 1
    change -= 10
while change/5 >= 1:
    i += 1
    change -= 5
while change/1 >= 1:
    i += 1
    change -= 1
print(i)
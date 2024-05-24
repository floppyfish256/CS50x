from cs50 import get_int

# Setting the inputs
space = 1
number = 10
# Complicated calculations
while number == 1 or number > 9 or number < 1:
    number = get_int("Height: ") + 1
while space < number:
    print(" "*(number - space - 1), end="")
    print("#"*space)
    space += 1
from cs50 import get_string

text = input("Text: ")
l = len(text)
Letter = 0
Word = 1
Sent = 0
for i in range(l):
    if text[i].isalpha() == True:
        Letter += 1
    if text[i] == " ":
        Word += 1
    if text[i] == "." or text[i] == "!" or text[i] == "?":
        Sent += 1
print(f"Letters: {Letter}")
print(f"Words: {Word}")
print(f"Sentences: {Sent}")
L = Letter/(Word/100)
S = Sent/(Word/100)
index = round(0.0588 * L - 0.296 * S - 15.8)
print(index)
if index < 1:
    print("Before Grade 1")
elif index > 16:
    print("Grade 16+")
else:
    print(f"Grade {index}")
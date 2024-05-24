#include <cs50.h>
#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <math.h>

int count_words(string s);
int count_letters(string s);
int count_sentences(string s);

int main(void)
{
    string text = get_string("Text: ");
    float L = (float)count_letters(text) / ((float)count_words(text) / 100);
    float S = (float)count_sentences(text) / ((float)count_words(text) / 100);
    int r = round(0.0588 * L - 0.296 * S - 15.8);
    if (r < 16 && r >= 1)
    {
        printf("Grade %i\n", r);
    }
    else if (r >= 16)
    {
        printf("Grade 16+\n");
    }
    else
    {
        printf("Before Grade 1\n");
    }

}

//Letters
int count_letters(string s)
{
    int length = strlen(s);
    int letters = 0;
    for (int l = 0; l < length; l++)
    {
        if (isalpha(s[l]))
        {
            letters++;
        }
    }
    return letters;
}

//Words
int count_words(string s)
{
    int length = strlen(s);
    int words = 1;
    for (int l = 0; l < length; l++)
    {
        if (isspace(s[l]))
        {
            words++;
        }
    }
    return words;
}

//Sentences
int count_sentences(string s)
{
    int length = strlen(s);
    int sentences = 0;
    for (int l = 0; l < length; l++)
    {
        if (s[l] == '!' || s[l] == '?' || s[l] == '.')
        {
            sentences++;
        }
    }
    return sentences;
}
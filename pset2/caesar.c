#include <stdio.h>
#include <cs50.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>

//Key
int main(int argc, string argv[])
{
    int key;
    int l = 0;
    if (argc == 2)
    {
        int len = strlen(argv[1]);
        while (l < len)
        {
            if isdigit(argv[1][l])
            {
                l++;
            }
            else
            {
                printf("Usage: ./caesar key\n");
                return 1;
            }
        }
        key = atoi(argv[1]) % 26;
    }
    else
    {
        printf("Usage: ./caesar key\n");
        return 1;
    }

//Get plaintext
    string text = get_string("plaintext: ");

//Encipher
    int len = strlen(text);
    printf("ciphertext: ");
    for (int a = 0; a < len; a++)
    {
        if (isalpha(text[a]) && (islower(text[a])))
        {
            if ((int) text[a] + key > 122)
            {
                text[a] = text[a] + key - 26;
            }
            else
            {
                text[a] = text[a] + key;
            }
        }
        else if (isalpha(text[a]) && (isupper(text[a])))
        {
            if ((int) text[a] + key > 90)
            {
                text[a] = text[a] + key - 26;
            }
            else
            {
                text[a] = text[a] + key;
            }
        }
        else
        {

        }
        printf("%c", text[a]);
    }
    printf("\n");
}
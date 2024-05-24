#include <cs50.h>
#include <stdio.h>

int main(void)
{
    // Counter and number for limit
    int space = 1;
    int number = 10;

    // Repeats until 8 >= x >= 1
    do
    {
        number = get_int("Height: ") + 1;
    }
    while (9 < number || number < 2);

    // Printing the hashes
    do
    {
        int i;
        // Starting at the top center
        for (i = 0; i < (number - space - 1); ++i)
        {
            printf(" ");
        }
        //Top hash
        for (i = 0; i < space; ++i)
        {
            printf("#");
        }
        // Spaces in between
        for (i = 0; i < 2; ++i)
        {
            printf(" ");
        }
        // Other side of the pyramid
        for (i = 0; i < space; ++i)
        {
            printf("#");
        }
        printf("\n");
        space += 1;
    }
    while (space < number);
}
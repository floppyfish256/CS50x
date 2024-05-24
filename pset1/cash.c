#include <cs50.h>
#include <stdio.h>
#include <math.h>

int main()
{
    float change = -1;
    int coins = 0;
    //Ignores this loop if change is 0
    while (change < 0)
    {
        change = get_float("Change:");
    }
    int cents = round(change * 100);

    while (cents > 0)
    {
        if (cents - 25 >= 0)
        {
            cents = cents - 25;
            coins++;
        }
        else if (cents - 10 >= 0)
        {
            cents = cents - 10;
            coins++;
        }
        else if (cents - 5 >= 0)
        {
            cents = cents - 5;
            coins++;
        }
        else
        {
            cents = cents - 1;
            coins++;
        }
    }

    printf("%d \n", coins);
}

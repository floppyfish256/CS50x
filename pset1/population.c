#include <cs50.h>
#include <stdio.h>

int main(void)
{
    // Getting inputs from the user
    int n;
    do
    {
        n = get_int("Start size: ");
    }
    while (n < 9);
    int i;
    do
    {
        i = get_int("End size: ");
    }
    while (i < n);

    // Setting the counter
    int c = 0;

    //Condition only in they are equal
    if (i == n)
    {
        c = -1;
    }

    //Calculations and counting
    do
    {
        n = n + (n / 3) - (n / 4);
        c = c + 1;
    }
    while (i > n);

    // Printing the result
    printf("Years: %i\n", c);
}
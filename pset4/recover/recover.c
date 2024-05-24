#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

int main(int argc, char *argv[])
{
    //Check if it is valid
    if (argc != 2)
    {
        printf("Usage: ./recover image\n");
        return 1;
    }
    //Open memory card
    typedef uint8_t BYTE;
    FILE *f = fopen(argv[1], "r");

    //Read 512 bytes into a buffer
    BYTE buffer[512];
    int i = 0;
    //If start of new JPEG:
    FILE *j;
    while (fread(buffer, 1, 512, f) != 0)
    {
        if (buffer[0] == 0xff && buffer[1] == 0xd8 && buffer[2] == 0xff && (buffer[3] & 0xf0) == 0xe0)
        {
            //If it's the first JPEG
            if (i == 0)
            {
                //Start writing
                char filename[8];
                sprintf(filename, "%03d.jpg", i);
                j = fopen(filename, "w");
            }
            //If it's not them first JPEG
            else
            {
                //Close previous JPEG
                fclose(j);
                //Start writing new JPEG
                char file[8];
                sprintf(file, "%03d.jpg", i);
                j = fopen(file, "w");

            }
            //Write into the pointer to the JPEG
            fwrite(buffer, 1, 512, j);
            i++;
        }
        else
        {
            //If it already found the JPEG:
            if (i > 0)
            {
                fwrite(buffer, 1, 512, j);
            }
        }
    }
    //Close any remaining files
    fclose(j);
}
// Implements a dictionary's functionality

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include "dictionary.h"

// Represents a node in a hash table
typedef struct node
{
    char word[LENGTH + 1];
    struct node *next;
}
node;

// Number of buckets in hash table
const unsigned int N = 45;

// Hash table
node *table[N];

// Returns true if word is in dictionary else false
bool check(const char *word)
{
    // TODO
    node *cursor;
    int bucket = hash(word);
    cursor = table[bucket];
    while (cursor != NULL)
    {
        int i = strcasecmp(word, cursor->word);
        if (i == 0)
        {
            return true;
        }
        else
        {
            cursor = cursor->next;
        }
    }
    return false;
}

// Hashes word to a number
unsigned int hash(const char *word)
{
    // TODO
    int hash = strlen(word);
    return hash;
}

// Loads dictionary into memory, returning true if successful else false
int counter = 0;
bool load(const char *dictionary)
{
    // TODO
    char word[LENGTH + 1];
    FILE *f = fopen(dictionary, "r");
    if (f == NULL)
    {
        return false;
    }

    while (fscanf(f, "%s", word) != EOF)
    {
        node *n = malloc(sizeof(node));
        strcpy(n->word, word);
        n->next = NULL;
        int bucket = hash(word);
        n->next = table[bucket];
        table[bucket] = n;
        counter++;
    }
    fclose(f);
    return true;
}

// Returns number of words in dictionary if loaded else 0 if not yet loaded
unsigned int size(void)
{
    // TODO
    return counter;
}

// Unloads dictionary from memory, returning true if successful else false
bool unload(void)
{
    // TODO
    node *cursor;
    node *tmp;
    //Going down the table
    for (int i = 0; i < N; i++)
    {
        cursor = table[i];
        //Going down the linked lists
        while (cursor != NULL)
        {
            tmp = cursor;
            cursor = cursor->next;
            free(tmp);
        }
    }
    return true;
}

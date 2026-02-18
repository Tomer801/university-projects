#include "cipher.h"

#include <stdio.h>

/// IN THIS FILE, IMPLEMENT EVERY FUNCTION THAT'S DECLARED IN cipher.h.


// See full documentation in header file
#include <stdio.h>

void cipher(char s[], int k) {
    int i = 0;
    k = k % 26;

    while (s[i] != '\0') {
        if ('a' <= s[i] && s[i] <= 'z') {
            s[i] = 'a' + ((s[i] - 'a' + k + 26) % 26);//makes sure that we stay in range of 0-256
        }
        else if ('A' <= s[i] && s[i] <= 'Z') {
            s[i] = 'A' + ((s[i] - 'A' + k + 26) % 26);
        }
        i++;
    }
}

void decipher(char s[], int k) {
    cipher(s, -k);// using chiper by negate the k
}



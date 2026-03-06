#include <stdio.h>
#include <stdlib.h>
#define ARGUMENT_N 5
#define ARGUMENT_D 1



#include "cipher.h"
#include "tests.h"
#include<string.h>
#include <stdio.h>
#define C "cipher"
#define D "decipher"
#define E_1 "The program receives 1 or 4 arguments only.\n"
#define E_2 "Usage: cipher test\n"
#define E_3 "The given command is invalid.\n"
#define E_4 "The given shift value is invalid.\n"
#define E_5 "The given file is invalid.\n"
#define MAX_LENGTH 1024
#define TEST_CASE 2

void tests_c(void);

int main (int argc, char *argv[]) {

  if (argc != ARGUMENT_N){
    if (argc == TEST_CASE) {
      if (strcmp(argv[1], "test")==0){ //chek for 2 arg
        tests_c();
        return EXIT_SUCCESS;
      }


        fprintf(stderr,E_2); //print err when argv[2]!=test
        return EXIT_FAILURE;
      }

    fprintf(stderr,E_1); // argc != 5
    return EXIT_FAILURE;
  }
  if (strcmp(argv[1],C)!=0 && strcmp(argv[1],D)!=0 ) // checks command validity
  {
    fprintf(stderr,E_3);
    return EXIT_FAILURE;
  }
  char *endptr;
  long num = strtol(argv[2], &endptr , 10);//checks k validity
  if (*endptr != '\0') {
    fprintf(stderr,E_4);
    return EXIT_FAILURE;
  }

  FILE *in_file=fopen(argv[3],"r");
  if(in_file==NULL)
  {
    fprintf(stderr,E_5);
    return EXIT_FAILURE;
  }
  FILE *out_file=fopen(argv[4],"w");
  if(out_file==NULL)
  {
    fprintf(stderr,E_5);
    fclose(in_file);
    return EXIT_FAILURE; ;
  }

  char input[MAX_LENGTH]; //read file line by line and write in the out file
  while (fgets(input,MAX_LENGTH,in_file)!=NULL)
  {
    if(strcmp(argv[1], C )==0){cipher(input,num);}
    else if (strcmp(argv[1], D )==0){decipher(input,num);}
    fprintf(out_file,"%s",input);
  }
  fclose(in_file);

  fclose(out_file);//close files
}




void tests_c()
{
  test_cipher_non_cyclic_lower_case_positive_k();
  test_cipher_cyclic_lower_case_special_char_positive_k ();
  test_cipher_non_cyclic_lower_case_special_char_negative_k ();
  test_cipher_cyclic_lower_case_negative_k ();
  test_cipher_cyclic_upper_case_positive_k ();
  test_decipher_non_cyclic_lower_case_positive_k ();
  test_decipher_cyclic_lower_case_special_char_positive_k ();
  test_decipher_non_cyclic_lower_case_special_char_negative_k ();
  test_decipher_cyclic_lower_case_negative_k ();
  test_decipher_cyclic_upper_case_positive_k ();
}

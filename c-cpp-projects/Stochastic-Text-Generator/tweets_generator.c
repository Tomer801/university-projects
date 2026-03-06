//Don't change the macros!
#define FILE_PATH_ERROR "Error: incorrect file path"
#define NUM_ARGS_ERROR "Usage: invalid number of arguments"
#define NUM_ARGS_4 4
#define NUM_ARGS_5 5
#define MAX_LENGTH 1200
#define MAX_TWEET_LENGTH 20

#define DELIMITERS " \n\t\r"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#include "markov_chain.h"

void* copy_string(const void *src) {
    char *str = (char *)src;
    char *cpy_ptr = malloc(strlen(str) + 1);
    if (cpy_ptr == NULL) {
        return NULL;
    }
    memcpy(cpy_ptr, src, strlen(str) + 1);
    return cpy_ptr;
}
void free_string(void *str) {

    free(str);
}
void print_string(const void *str) {
    printf("%s ", (const char*)str);

}
bool compare_string(const void *str1, const void *str2) {
    //fprintf(stderr,"%s ", "hello world");
    return  strcmp((const char*)str1, (const char*)str2);
}
bool is_last_string(const void *str) {
    char *str1 = (char *)str;
    if (str1[strlen(str1)-1] == '.') {
        return 0;
    }
    return 1;
}




//helper to count words amount
int word_in_txt(FILE *fp) {
    int word_count = 0;
    char line[MAX_LENGTH];
    while (fgets(line, MAX_LENGTH, fp) != NULL) {
        char *token = strtok(line, DELIMITERS);
        while (token != NULL) {
            word_count++;
            token = strtok(NULL, DELIMITERS);
        }
    }
    return word_count;

}


int fill_database(FILE *fp,int words_to_read ,MarkovChain *markov_chain) {
    char input[MAX_LENGTH];
     // count loops
    int count = 0;
    // vars to get the markove nodes
    Node *node;
    Node *next_node;
    while (fgets(input, MAX_LENGTH, fp) != NULL ) {
        //add first word of line
        char *token = strtok(input,DELIMITERS);
        // if line is empty
        if (token == NULL) {
            continue;
        }
        node = add_to_database(markov_chain,token);
        if (node == NULL) {
            return 1;
        }
        count++;
        while (( token = strtok(NULL,DELIMITERS))) {

            if (count==words_to_read) {
                return 0;
            }
            // add node to data base
            next_node = add_to_database(markov_chain,token);
            if (next_node == NULL) {
                return 1;
            }
            count++;
            //if end of word dont creat frequency list
            if (node->data->end) {
                node = next_node;
                continue;
            }
            // add next node to prev node
            if(add_node_to_frequency_list(node->data,
                next_node->data)) {
                return 1;

            }
            node = next_node;
        }
    }

    return 0;
}








int main(int argc,char *argv[]) {
    if(argc!=NUM_ARGS_4 && argc!=NUM_ARGS_5 ) {
        printf( NUM_ARGS_ERROR);
        return EXIT_FAILURE;
    }
    MarkovChain *markov_chain2 = malloc(sizeof(MarkovChain));
    if(markov_chain2==NULL) {
        return EXIT_FAILURE;
    }
    // convert args to digit
    long seed = strtol(argv[1],NULL,10);
    long tweets_am = strtol(argv[2],NULL,10);
    srand(seed);
    // allocate memory to database list
    LinkedList* database_list = malloc(sizeof(LinkedList));
    if(database_list==NULL) {
        free(markov_chain2);
        return EXIT_FAILURE;
    }
    //init list
    *database_list = (LinkedList){NULL,NULL,0} ;
    markov_chain2->database=database_list;
    markov_chain2->comp_func = compare_string;
    markov_chain2->copy_func = copy_string;
    markov_chain2->print_func = print_string;
    markov_chain2->free_data= free_string;
    markov_chain2->is_last_func= is_last_string;
    //creat pointer to pointer of markov chain
    MarkovChain **markov_chain ;
    markov_chain = &markov_chain2;

    FILE *in_file=fopen(argv[3],"r");//clossssss!!!!!
    if(in_file==NULL)
    {
        printf(FILE_PATH_ERROR);
        free_database(markov_chain);
        return EXIT_FAILURE;
    }
    //no words to read
    if(argc==NUM_ARGS_4) {//no words to read in arg
        int words_read = word_in_txt(in_file);
        rewind(in_file);
        if(fill_database(in_file,words_read,markov_chain2)) {
            free_database(markov_chain);

            fclose(in_file);
            return EXIT_FAILURE;

        }
        // with words to read
    }else {
        int words_read;
        if(sscanf(argv[4],"%d",&words_read)!=1) {
            free(database_list);
            fclose(in_file);
            return EXIT_FAILURE;
        }// if allocation failed
        if(fill_database(in_file,words_read
            ,markov_chain2)) {
            free_database(markov_chain);
            fclose(in_file);
            return EXIT_FAILURE;
            }
    }
    int tweets = 0;
    // create tweets
    while(tweets_am!=tweets) {
        tweets++;
        MarkovNode *random = get_first_random_node(markov_chain2);
        printf("Tweet %d: ",tweets);
        generate_random_sequence(markov_chain2,random,MAX_TWEET_LENGTH);
        printf("\n");

    }
        fclose(in_file);
        free_database(markov_chain);
        return EXIT_SUCCESS;
    }

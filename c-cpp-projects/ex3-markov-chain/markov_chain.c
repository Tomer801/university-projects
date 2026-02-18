#include "markov_chain.h"
#include <assert.h>
#include <string.h>
#define MAX_TWEET_LENGTH 20
/**
 * Get random number between 0 and max_number [0, max_number).
 * @param max_number
 * @return Random number
 */
int get_random_number(int max_number) {
    return rand() % max_number;
}



Node *get_node_from_database(MarkovChain *markov_chain, const void *data_ptr) {
    if (data_ptr == NULL) {
        return NULL;
    }
    //loop over the list
    Node *chain_ptr = markov_chain->database->first;
        while (chain_ptr != NULL) {
        if (markov_chain->comp_func(chain_ptr->data->data, data_ptr)==0) {
            // return pointer to the node
            return chain_ptr;
        }
        chain_ptr = chain_ptr->next;
    }


    return NULL;
}


Node *add_to_database(MarkovChain *markov_chain, void *data_ptr) {
    //in case that the node not on the list
    if (get_node_from_database(markov_chain, data_ptr) == NULL) {
        //allocate memory for the string

        char *cpy_ptr = markov_chain->copy_func(data_ptr);
        if (cpy_ptr == NULL) {
            return NULL;
        }

        // is ending a sentence
        int end = 0;

        if (!markov_chain->is_last_func(cpy_ptr)) {
            end = 1;
        }
        // not in list so add
        MarkovNode *new_mnode = malloc(sizeof(MarkovNode));
        if (new_mnode == NULL) {
            markov_chain->free_data(cpy_ptr);
            return NULL;
        }
        //init node and add to list
        *new_mnode = (MarkovNode){cpy_ptr,
            NULL, end,0,0};
        if (add(markov_chain->database, new_mnode)) {
            markov_chain->free_data(cpy_ptr);
            free(new_mnode);
            return NULL;
        }
        return get_node_from_database(markov_chain,cpy_ptr);
    }
    return get_node_from_database(markov_chain,data_ptr );
}

int add_node_to_frequency_list(MarkovNode *first_node, MarkovNode *second_node)
{
    // no list yet so allocte memory
    if (first_node->frequency_list == NULL) {
        MarkovNodeFrequency *list_frequency_node =
            malloc(sizeof(MarkovNodeFrequency));
        if (list_frequency_node == NULL) {
            return 1;
        }
        *list_frequency_node = (MarkovNodeFrequency){second_node, 1};
        first_node->frequency_list = list_frequency_node;
        first_node->total ++;
        first_node->diff_words++;
        return 0;
    }
    // check if already in list
    for (int i = 0; i < first_node->diff_words; i++) {

        if (&(first_node->frequency_list[i].markov_node->data)
            == &second_node->data)
            {
            first_node->total++;
            first_node->frequency_list[i].frequency ++;
            return 0;
            }
    }

    // if there is a list but the word not in
    MarkovNodeFrequency *temp = realloc(first_node->frequency_list,
        sizeof(MarkovNodeFrequency) * (first_node->diff_words+1));
    if (temp == NULL) {

        return 1;
    }
    first_node->frequency_list = temp;
    first_node->frequency_list[first_node->diff_words]
     =(MarkovNodeFrequency){second_node, 1};
    first_node->total++;
    first_node->diff_words++;
    return 0;

}


void free_database(MarkovChain **ptr_chain) {

    // Free the linked list of nodes
    if ((*ptr_chain)->database != NULL) {
        Node *current = (*ptr_chain)->database->first;
        while (current != NULL) {
            Node *next = current->next; // Save the next node
            // Free the MarkovNode's frequency list
            if (current->data != NULL) {
                if (current->data->frequency_list != NULL) {
                    // Free the array
                    free(current->data->frequency_list);
                }
                // Free the string data inside the MarkovNode
                (*ptr_chain)->free_data(current->data->data);
                // Free the MarkovNode itself
                free(current->data);
            }
            // Free the current Node
            free(current);
            current = next;
        }
        // Free the LinkedList structure
        free((*ptr_chain)->database);
    }
    // Free the MarkovChain structure itself
    free(*ptr_chain);
    *ptr_chain = NULL;
}
/**
 * Get one random MarkovNode from the given markov_chain's database.
 * @param markov_chain
 * @return the random MarkovNode
 */
MarkovNode * get_first_random_node(MarkovChain *markov_chain) {
    while (1){
        int i = get_random_number(markov_chain->database->size);
        Node *curr = markov_chain->database->first;
        // gets the i'th node
        while (i > 0) {
           curr = curr->next;
            i--;
        }
        // if word is end of sentence
        if (curr->data->end || curr->data->frequency_list == NULL) {
           continue;
        }
        return curr->data;
    }
}
/**
 * Choose randomly the next MarkovNode, depend on it's occurrence frequency.
 * @param cur_markov_node current MarkovNode
 * @return the next random MarkovNode
 */
MarkovNode* get_next_random_node(MarkovNode *cur_markov_node) {
    int i = get_random_number(cur_markov_node->total);
    int z = 0;
    for (int j = 0; j < cur_markov_node->diff_words; j++) {
        z += cur_markov_node->frequency_list[j].frequency;
        // if true return the i'th on frequency list
        if (i < z) {
            return cur_markov_node->frequency_list[j].markov_node;

        }
    }
    return NULL;
}
/**
 * Receive markov_chain, generate and print random sentence out of it. The
 * sentence must have at least 2 words in it.
 * @param first_node markov_node to start with
 * @param  max_length maximum length of chain to generate
 */
void generate_random_sequence(MarkovChain *markov_chain, MarkovNode *
first_node, int max_length) {
    int i = 0;
    // prints first word
    markov_chain->print_func(first_node->data);

    MarkovNode *next = get_next_random_node(first_node);


    while (1) {
        // prints last word
        if(next->end || i == max_length-2) {
            markov_chain->print_func(next->data);
            break;
        }

        markov_chain->print_func(next->data);
        next = get_next_random_node(next);
        i++;
    }

}

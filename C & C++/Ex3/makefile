CC = gcc
CCFLAGS = -std=c99 -Wall -Werror

.PHONY: all clean

all: tweets_generator snakes_and_ladders

# Build tweets_generator executable
tweets_generator: tweets_generator.o markov_chain.o linked_list.o
	$(CC) $(CCFLAGS) tweets_generator.o markov_chain.o linked_list.o -o tweets_generator

tweets_generator.o: tweets_generator.c markov_chain.h linked_list.h
	$(CC) $(CCFLAGS) -c tweets_generator.c -o tweets_generator.o

# Build snakes_and_ladders executable
snakes_and_ladders: snakes_and_ladders.o markov_chain.o linked_list.o
	$(CC) $(CCFLAGS) snakes_and_ladders.o markov_chain.o linked_list.o -o snakes_and_ladders

snakes_and_ladders.o: snakes_and_ladders.c markov_chain.h
	$(CC) $(CCFLAGS) -c snakes_and_ladders.c -o snakes_and_ladders.o

# Compile markov_chain.c
markov_chain.o: markov_chain.c markov_chain.h linked_list.h
	$(CC) $(CCFLAGS) -c markov_chain.c -o markov_chain.o

# Compile linked_list.c
linked_list.o: linked_list.c linked_list.h
	$(CC) $(CCFLAGS) -c linked_list.c -o linked_list.o

# Clean up build artifacts
clean:
	rm -f *.o tweets_generator snakes_and_ladders

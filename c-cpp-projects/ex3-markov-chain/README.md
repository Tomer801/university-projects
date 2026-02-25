# Markov Chain Projects – Intro2CS Assignment

C implementations using Markov Chains for text generation ("tweets") and for simulating the game *Snakes and Ladders*.

---

## Files
- **markov_chain.h / markov_chain.c** – generic Markov Chain data structure and utilities (add to database, frequency lists, random walk, free).【176†files_uploaded_in_conversation】【177†files_uploaded_in_conversation】
- **tweets_generator.c** – builds a Markov Chain from an input text file and generates random "tweets".【175†files_uploaded_in_conversation】
- **snakes_and_ladders.c** – builds a Markov Chain model of the Snakes and Ladders board game and generates random walks from start to finish.【174†files_uploaded_in_conversation】
- **makefile** – build automation.

---

## Build
Compile with a C11 compiler. Example:
```bash
gcc -std=c11 -Wall -Wextra -Werror -O2 tweets_generator.c markov_chain.c -o tweets
gcc -std=c11 -Wall -Wextra -Werror -O2 snakes_and_ladders.c markov_chain.c -o snakes
```

Or simply run:
```bash
make
```

---

## Usage

### Tweets Generator
```
./tweets <seed> <num_tweets> <input_file> [words_to_read]
```
- `<seed>`: random seed (integer).
- `<num_tweets>`: number of random tweets to generate.
- `<input_file>`: path to a text file corpus.
- `[words_to_read]`: optional max number of words to read from the file.

Example:
```bash
./tweets 42 5 corpus.txt 1000
```

Tweets are limited to 20 words max.

### Snakes and Ladders
```
./snakes <seed> <num_walks>
```
- `<seed>`: random seed (integer).
- `<num_walks>`: number of random walks to simulate.

Each walk prints the path from cell 1 until reaching cell 100. Ladders and snakes are modeled as deterministic transitions.【174†files_uploaded_in_conversation】

---

## Implementation Notes
- The Markov Chain stores generic data with function pointers for copy, free, compare, print, and "is last" checks.【177†files_uploaded_in_conversation】
- `tweets_generator.c`:
  - Reads file, tokenizes words, builds chain with word frequencies.
  - Sentence ends when a token ends with `.`.【175†files_uploaded_in_conversation】
- `snakes_and_ladders.c`:
  - Board of 100 cells, transitions for 20 snakes/ladders hardcoded.
  - Dice rolls (1–6) define other transitions.
  - Walk ends when reaching cell 100.【174†files_uploaded_in_conversation】

---

## Example Outputs

**Tweets**:
```
Tweet 1: the cat sat on the mat .
Tweet 2: hello world .
```

**Snakes and Ladders**:
```
Random Walk 1: [1] -> [2] -> [8] -ladder to-> [30] -> ... -> [100]
```

---

## License
Educational use. Add a license if you plan to publish broadly.

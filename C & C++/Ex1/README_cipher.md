# Caesar Cipher (C) – Intro2CS Assignment

Command‑line Caesar cipher encoder/decoder implemented in C, with unit‑tests.

## Files
- `cipher.h` – API for `cipher`/`decipher`.【turn6file3】
- `cipher.c` – implementation of Caesar shift (supports any integer k; wraps mod 26; preserves non‑letters).【turn6file2】
- `tests.h`, `tests.c` – unit tests for typical/edge cases. Run via `test` mode (see below).【turn6file1】【turn6file0】
- `main.c` – CLI driver: validates args, reads input file line‑by‑line (up to 1024 chars), applies cipher/decipher, writes to output file, or runs tests.【turn6file4】

## Build
Use any C11 compiler. Example with `gcc`:
```bash
gcc -std=c11 -Wall -Wextra -Werror -O2 main.c cipher.c tests.c -o caesar
```

## Usage
Two modes are supported:

### 1) Cipher/Decipher mode
```
./caesar <command> <k> <input_file> <output_file>
```
- `<command>`: `cipher` or `decipher`.【turn6file4】
- `<k>`: integer shift (positive/negative/large; normalized inside implementation).【turn6file2】【turn6file4】
- `<input_file>`: path to text file to process.
- `<output_file>`: path to write results.

Example:
```bash
./caesar cipher 3 in.txt out.txt
./caesar decipher -3 out.txt restored.txt
```

**Behavior**
- Only letters `A–Z`/`a–z` are shifted; other chars remain unchanged.【turn6file2】
- Lines are processed up to `MAX_LENGTH = 1024` bytes each.【turn6file4】

**Errors (printed to stderr)**
- Wrong arg count (must be 1 or 4 user args): `The program receives 1 or 4 arguments only.`【turn6file4】
- Wrong test invocation: `Usage: cipher test`【turn6file4】
- Invalid command: `The given command is invalid.`【turn6file4】
- Invalid shift value: `The given shift value is invalid.`【turn6file4】
- Invalid file (open/read/write): `The given file is invalid.`【turn6file4】

### 2) Test mode
Run the built binary with two args to execute unit tests:
```bash
./caesar test
```
The driver calls all test functions defined in `tests.c`.【turn6file4】

## Implementation Notes
- `cipher(char s[], int k)` shifts letters by `k % 26` (safe with negative k), modifies the string in place.【turn6file2】
- `decipher(char s[], int k)` delegates to `cipher(s, -k)`.【turn6file2】

## Example
Input (`in.txt`):
```
Hello, World!  xyz XYZ
```
Command:
```
./caesar cipher 3 in.txt out.txt
```
Output (`out.txt`):
```
Khoor, Zruog!  abc ABC
```

## License
Educational use.

# Bus Lines Sorting (C) – Intro2CS Assignment

Program in C for managing and sorting bus line data using different algorithms.

---

## Files
- `sort_bus_lines.h` – defines `BusLine` struct (name, distance, duration), `SortType` enum, and function prototypes for bubble/quick sort and helpers.【158†files_uploaded_in_conversation】
- `sort_bus_lines.c` – implements bubble sort (by name), quick sort (by distance or duration), and partition/swap helpers.【162†files_uploaded_in_conversation】
- `test_bus_lines.h` – prototypes for validation functions (is_sorted_by_x, is_equal).【160†files_uploaded_in_conversation】
- `test_bus_lines.c` – implementations of validation functions, used for unit testing sorts.【159†files_uploaded_in_conversation】
- `main.c` – command‑line driver: parses bus line input, validates, and calls sorting/tests according to argument.【161†files_uploaded_in_conversation】

---

## Build
Use any C11 compiler. Example with `gcc`:
```bash
gcc -std=c11 -Wall -Wextra -Werror -O2 main.c sort_bus_lines.c test_bus_lines.c -o buslines
```

---

## Usage
Run the program with one argument specifying the mode:
```
./buslines <command>
```
Commands:
- `by_name` – sort by bus name (bubble sort).【161†files_uploaded_in_conversation】  
- `by_distance` – sort by distance (quick sort).【161†files_uploaded_in_conversation】  
- `by_duration` – sort by duration (quick sort).【161†files_uploaded_in_conversation】  
- `test` – run predefined unit tests for all sort methods.【161†files_uploaded_in_conversation】  

Example:
```bash
./buslines by_name
Enter number of lines. Then enter
3
Enter line info. Then enter
line1,100,20
Enter line info. Then enter
line2,200,50
Enter line info. Then enter
line3,150,30
```

Output:
```
line1,100,20
line3,150,30
line2,200,50
```

---

## Input format
Each bus line must be entered as:
```
<name>,<distance>,<duration>
```
- **name**: up to 20 non‑space characters (lowercase letters or digits).【161†files_uploaded_in_conversation】  
- **distance**: integer between 0 and 1000.  
- **duration**: integer between 10 and 100.  

Invalid inputs print error messages and re‑prompt.

---

## Tests
When run in `test` mode, the program:
- Sorts a copy of the array by distance, duration, and name.  
- Uses `is_sorted_by_distance`, `is_sorted_by_duration`, `is_sorted_by_name`, and `is_equal` to validate correctness.【159†files_uploaded_in_conversation】【160†files_uploaded_in_conversation】  
- Prints `PASSED`/`FAILED` messages.

---

## License
Educational use. Add a license if you plan to publish broadly.

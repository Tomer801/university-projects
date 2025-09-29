import ex7_helper


def mult(x: ex7_helper.N, y: int) -> ex7_helper.N:
    """multiplication function using recursion"""
    if y == 0:  # base case
        return 0
    #  return n-1 + x for each iteration
    return ex7_helper.add(mult(x, ex7_helper.subtract_1(y)), x)


def is_even(n: int) -> bool:
    """check if number is even"""
    if n == 0:  # base case if even
        return True
    if n == ex7_helper.subtract_1(0): # base case if odd
        return False
    return is_even(ex7_helper.subtract_1(ex7_helper.subtract_1(n)))


def log_mult(x: ex7_helper.N, y: int) -> ex7_helper.N:
    """doing multiplication with logarithm time"""
    if y == 0:  #  base case
        return 0
    #  caliing the function
    temp_x = log_mult(x, ex7_helper.divide_by_2(y))
    if not ex7_helper.is_odd(y):  #  for even cases
        return ex7_helper.add(temp_x, temp_x)
    else: #  for odd cases
        return ex7_helper.add(ex7_helper.add(temp_x, temp_x), x)





def we_love_b(original_b:int, b:int, x:int)->bool:
    """helper function to keep the original b"""
    if b == x: #  in case that b=x so b^1+x
        return True
    if b > x:  #  if b > x ther is no integer that will work
        return False
    if we_love_b(original_b, log_mult(b, original_b), x):#the recursion loop
        return True
    else:
        return False


def is_power(b: int, x: int) -> bool:
    if x == 1 and b >= 0:  #  if x=1 b^0=1
        return True
    if x > 1 and b == 1:  #  if b = 1  then b^n=1!=x
        return False
    #  calls the recursion
    return we_love_b(b, b, x)




def we_love_s(s, len_s):
    """"helper function that call the recursion function by keeping the original length"""
    if len(s) == 0:   # base case
        return s
    if len_s == 1:
        return s[len(s) - len_s]
    # ading n-1 in revers
    return ex7_helper.append_to_end(we_love_s(s, len_s - 1), s[len(s) - len_s])


def reverse(s: str) -> str:
    """reverse a string"""
    return we_love_s(s, len(s))


def play_hanoi(Hanoi: any, n: int, src: any, dest: any, temp: any) -> any:
    if n <= 0: # for negetiv integer
        return
    if n == 1: # base case
        return Hanoi.move(src, dest)
    else:
        #  doing the n-1 by replacing between dest and temp
        play_hanoi(Hanoi, n - 1, src, temp, dest)
        #  moving the bigest in evry iteretion
        Hanoi.move(src, dest)
        #  bringing back the n-1 disks on top of the big one
        play_hanoi(Hanoi, n - 1, temp, dest, src)


def number_of_ones(n: int) -> int:
    """"counting the number of ones between 1-n"""
    if n == 0:# base case
        return 0
    #caling the function and the helper function that counts for evry number the number of ones
    return number_of_ones(n - 1) + count_ones(n)


def count_ones(n: int) -> int:
    """helper function to count the number of ones"""
    if n == 0: # base case
        return 0
    if n % 10 == 1: # check for each number the number of ones by modul
        return count_ones(n // 10) + 1 # if found 1 adding to the sum of ones
    else:
        return count_ones(n // 10)


def compare_lists(l3, l4, idx=0) -> bool:
    """helper function that compare the  lists"""
    if idx == len(l4) and idx == len(l3):# checking length
        return True
    if idx == len(l4) or idx == len(l3):# reurn fals if not even
        return False
    #  conpare the length of the inner ones by incricing index and using helper function to compare the ints inside
    return compare_lists(l3, l4, idx + 1) and len(l4[idx]) == len(l3[idx]) and compare_ints(l4[idx],l3[idx],0)


def compare_ints(l3, l4, idx1=0) -> bool:
    """helper functio to compare ints"""
    if idx1 == len(l4) and idx1 == len(l3):
        return True
    #checking the length and identical ints
    return l3[idx1] == l4[idx1] and compare_ints(l3, l4, idx1 + 1)


def compare_2d_lists(l1, l2) -> bool:
    """main function that calls the compare functions"""
    return compare_lists(l1, l2,0)




def magic_list(n: int):
    """reating list of lists by adding the previous call to the main list"""
    if n == 0: # base case
        return []
    # adding in evry iteretion the out put of the old on to the main
    # list by calling the funnction inside the append
    magical_list = magic_list(n - 1)
    magical_list.append(magic_list(n - 1))

    return magical_list










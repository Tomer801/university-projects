CXX = g++
CXXFLAGS = -Wall -Wextra -std=c++20 -pthread -O2

SRC = MapReduceFramework.cpp Barrier.cpp
OBJ = $(SRC:.cpp=.o)
LIB = libMapReduceFramework.a

all: $(LIB)

$(LIB): $(OBJ)
	ar rcs $@ $^

%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

clean:
	rm -f $(OBJ) $(LIB)

# Makefile for User-Level Threads Library (uthreads)
# Build static and shared libraries from uthreads.cpp
# Usage:
#   make            # build static lib (libuthreads.a)
#   make shared     # build shared lib  (libuthreads.so)
#   make clean

CXX       ?= g++
CXXFLAGS  ?= -std=c++11 -Wall -Wextra -Werror -O2 -fPIC
AR        ?= ar
ARFLAGS   ?= rcs

SRC  := uthreads.cpp
HDR  := uthreads.h
OBJ  := $(SRC:.cpp=.o)

STATIC_LIB := libuthreads.a
SHARED_LIB := libuthreads.so

.PHONY: all static shared clean

all: static

static: $(STATIC_LIB)

shared: $(SHARED_LIB)

$(OBJ): $(SRC) $(HDR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(STATIC_LIB): $(OBJ)
	$(AR) $(ARFLAGS) $@ $^

$(SHARED_LIB): $(OBJ)
	$(CXX) -shared -o $@ $^

clean:
	$(RM) $(OBJ) $(STATIC_LIB) $(SHARED_LIB)

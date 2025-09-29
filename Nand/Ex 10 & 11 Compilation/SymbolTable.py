"""
This file is part of nand2tetris, as taught in The Hebrew University, and
was written by Aviv Yaish. It is an extension to the specifications given
[here](https://www.nand2tetris.org) (Shimon Schocken and Noam Nisan, 2017),
as allowed by the Creative Common Attribution-NonCommercial-ShareAlike 3.0
Unported [License](https://creativecommons.org/licenses/by-nc-sa/3.0/).
"""
import typing


class SymbolTable:
    """A symbol table that associates names with information needed for Jack
    compilation: type, kind and running index. The symbol table has two nested
    scopes (class/subroutine).
    """

    def __init__(self) -> None:
        """Creates a new empty symbol table."""
        # Class-level scope for STATIC and FIELD variables
        self.class_scope = {}

        # Subroutine-level scope for ARG and VAR variables
        self.subroutine_scope = {}

        # Running indices for each kind of variable
        self.index = {
            "STATIC": 0,
            "FIELD": 0,
            "ARG": 0,
            "VAR": 0
        }

    def start_subroutine(self) -> None:
        """Starts a new subroutine scope (i.e., resets the subroutine's 
        symbol table).
        """
        self.subroutine_scope = {}

        # Reset running indices for ARG and VAR
        self.index["ARG"] = 0
        self.index["VAR"] = 0

    def define(self, name: str, type: str, kind: str) -> None:
        """Defines a new identifier of a given name, type and kind and assigns 
        it a running index. "STATIC" and "FIELD" identifiers have a class scope, 
        while "ARG" and "VAR" identifiers have a subroutine scope.

        Args:
            name (str): the name of the new identifier.
            type (str): the type of the new identifier.
            kind (str): the kind of the new identifier, can be:
            "STATIC", "FIELD", "ARG", "VAR".
        """

        # Validate the kind
        if kind not in ["STATIC", "FIELD", "ARG", "VAR"]:
            raise ValueError(f"Invalid kind: {kind}")

        # Determine the appropriate scope
        if kind in ["STATIC", "FIELD"]:
            scope = self.class_scope
        else:  # kind is "ARG" or "VAR"
            scope = self.subroutine_scope

        # Assign the variable a running index and store it in the scope
        scope[name] = {
            "type": type,
            "kind": kind,
            "index": self.index[kind]
        }

        # Increment the running index for this kind
        self.index[kind] += 1

    def var_count(self, kind: str) -> int:
        """
        Args:
            kind (str): can be "STATIC", "FIELD", "ARG", "VAR".

        Returns:
            int: the number of variables of the given kind already defined in 
            the current scope.
        """

        # Validate the kind
        if kind not in ["STATIC", "FIELD", "ARG", "VAR"]:
            raise ValueError(f"Invalid kind: {kind}")

        # return the index
        return self.index[kind]

    def kind_of(self, name: str) -> str:
        """
        Args:
            name (str): name of an identifier.

        Returns:
            str: the kind of the named identifier in the current scope, or None
            if the identifier is unknown in the current scope.
        """
        if name in self.subroutine_scope:
            return self.subroutine_scope[name]["kind"]

        # Check class scope next
        if name in self.class_scope:
            return self.class_scope[name]["kind"]

        # If not found, return None
        return None

    def type_of(self, name: str) -> str:
        """
        Args:
            name (str):  name of an identifier.

        Returns:
            str: the type of the named identifier in the current scope.
        """

        # Check subroutine scope first
        if name in self.subroutine_scope:
            return self.subroutine_scope[name]["type"]

        # Check class scope next
        if name in self.class_scope:
            return self.class_scope[name]["type"]

        # If not found, return None
        return None

    def index_of(self, name: str) -> int:
        """
        Args:
            name (str):  name of an identifier.

        Returns:
            int: the index assigned to the named identifier.

        Raises:
            KeyError: If the identifier is not found in the symbol table.
        """

        # Check subroutine scope first
        if name in self.subroutine_scope:
            return self.subroutine_scope[name]["index"]

        # Check class scope next
        if name in self.class_scope:
            return self.class_scope[name]["index"]

        # If not found, return None
        raise KeyError(f"Variable '{name}' not found in the symbol table")

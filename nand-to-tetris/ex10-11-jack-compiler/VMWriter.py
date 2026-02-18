"""
This file is part of nand2tetris, as taught in The Hebrew University, and
was written by Aviv Yaish. It is an extension to the specifications given
[here](https://www.nand2tetris.org) (Shimon Schocken and Noam Nisan, 2017),
as allowed by the Creative Common Attribution-NonCommercial-ShareAlike 3.0
Unported [License](https://creativecommons.org/licenses/by-nc-sa/3.0/).
"""
import typing


class VMWriter:
    """
    Writes VM commands into a file. Encapsulates the VM command syntax.
    """

    # Shared mapping for all methods
    segment_mapping = {
        "CONST": "constant",
        "ARG": "argument",
        "LOCAL": "local",
        "STATIC": "static",
        "THIS": "this",
        "THAT": "that",
        "POINTER": "pointer",
        "TEMP": "temp",
    }


    def __init__(self, output_stream: typing.TextIO) -> None:
        """Creates a new file and prepares it for writing VM commands."""
        # Your code goes here!
        # Note that you can write to output_stream like so:
        # output_stream.write("Hello world! \n")
        self.output = output_stream

    def write_push(self, segment: str, index: int) -> None:
        """Writes a VM push command.

        Args:
            segment (str): the segment to push to, can be "CONST", "ARG", 
            "LOCAL", "STATIC", "THIS", "THAT", "POINTER", "TEMP"
            index (int): the index to push to.
        """

        # Validate the segment
        if segment not in self.segment_mapping:
            raise ValueError(f"Invalid segment: {segment}")

        # Write the push command
        self.output.write(f"push {self.segment_mapping[segment]} {index}\n")

    def write_pop(self, segment: str, index: int) -> None:
        """Writes a VM pop command.

        Args:
            segment (str): the segment to pop from, can be "CONST", "ARG", 
            "LOCAL", "STATIC", "THIS", "THAT", "POINTER", "TEMP".
            index (int): the index to pop from.
        """
        if segment not in self.segment_mapping:
            raise ValueError(f"Invalid segment: {segment}")

        self.output.write(f"pop {self.segment_mapping[segment]} {index}\n")

    def write_arithmetic(self, command: str) -> None:
        """Writes a VM arithmetic command.

        Args:
            command (str): the command to write, can be "ADD", "SUB", "NEG", 
            "EQ", "GT", "LT", "AND", "OR", "NOT", "SHIFTLEFT", "SHIFTRIGHT".
        """

        # Map supported commands to their VM equivalents
        valid_commands = {
            "ADD": "add",
            "SUB": "sub",
            "NEG": "neg",
            "EQ": "eq",
            "GT": "gt",
            "LT": "lt",
            "AND": "and",
            "OR": "or",
            "NOT": "not",
            "SHIFTLEFT": "shiftleft",
            "SHIFTRIGHT": "shiftright",
        }

        # Validate the command
        if command not in valid_commands:
            raise ValueError(f"Invalid arithmetic command: {command}")

        # Write the command
        self.output.write(f"{valid_commands[command]}\n")

    def write_label(self, label: str) -> None:
        """Writes a VM label command.

        Args:
            label (str): the label to write.
        """

        # Write the label command
        self.output.write(f"label {label}\n")

    def write_goto(self, label: str) -> None:
        """Writes a VM goto command.

        Args:
            label (str): the label to go to.
        """
        # Write the goto command
        self.output.write(f"goto {label}\n")

    def write_if(self, label: str) -> None:
        """Writes a VM if-goto command.

        Args:
            label (str): the label to go to.
        """
        # Write the if-goto command
        self.output.write(f"if-goto {label}\n")

    def write_call(self, name: str, n_args: int) -> None:
        """Writes a VM call command.

        Args:
            name (str): the name of the function to call.
            n_args (int): the number of arguments the function receives.
            """

        # Validate the function name and argument count
        if not name or not name.isidentifier():
            raise ValueError(f"Invalid function name: {name}")
        if n_args < 0:
            raise ValueError(f"Number of arguments cannot be negative: {n_args}")

        # Write the call command
        self.output.write(f"call {name} {n_args}\n")

    def write_function(self, name: str, n_locals: int) -> None:
        """Writes a VM function command.

        Args:
            name (str): the name of the function.
            n_locals (int): the number of local variables the function uses.
        """

        # Validate the function name and the number of local variables
        if not name or not name.isidentifier():
            raise ValueError(f"Invalid function name: {name}")
        if n_locals < 0:
            raise ValueError(f"Number of local variables cannot be negative: {n_locals}")

        # Write the function command
        self.output.write(f"function {name} {n_locals}\n")

    def write_return(self) -> None:
        """Writes a VM return command."""

        self.output.write("return\n")

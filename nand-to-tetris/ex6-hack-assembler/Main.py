"""
This file is part of nand2tetris, as taught in The Hebrew University, and
was written by Aviv Yaish. It is an extension to the specifications given
[here](https://www.nand2tetris.org) (Shimon Schocken and Noam Nisan, 2017),
as allowed by the Creative Common Attribution-NonCommercial-ShareAlike 3.0
Unported [License](https://creativecommons.org/licenses/by-nc-sa/3.0/).
"""
import os
import sys
import typing
from SymbolTable import SymbolTable
from Parser import Parser
from Code import Code


def assemble_file(input_file: typing.TextIO, output_file: typing.TextIO) -> None:
    """Assembles a single file.

    Args:
        input_file (typing.TextIO): the file to assemble.
        output_file (typing.TextIO): writes all output to this file.
    """
    # Initialize the parser and symbol table
    parser = Parser(input_file)
    symbol_table = SymbolTable()

    # First pass: Add all labels to the symbol table
    rom_address = 0
    while parser.has_more_commands():
        parser.advance()
        if parser.command_type() == "L_COMMAND":
            # Add label to symbol table
            symbol_table.add_entry(parser.symbol(), rom_address)
        elif parser.command_type() in ["A_COMMAND", "C_COMMAND"]:
            rom_address += 1  # Only increment for actual commands

    # Reinitialize parser for second pass
    input_file.seek(0)
    parser = Parser(input_file)

    # Second pass: Translate commands to binary
    ram_address = 16
    while parser.has_more_commands():
        parser.advance()
        if parser.command_type() == "A_COMMAND":
            symbol = parser.symbol()
            if symbol.isdigit():
                # If it's a number, use it directly
                address = int(symbol)
            else:
                # If it's a symbol, look it up or allocate in RAM
                if not symbol_table.contains(symbol):
                    symbol_table.add_entry(symbol, ram_address)
                    ram_address += 1
                address = symbol_table.get_address(symbol)
            # Write the A-instruction as binary
            output_file.write(f"{address:016b}\n")

        elif parser.command_type() == "C_COMMAND":
            # Translate dest, comp, and jump fields
            dest_bin = Code.dest(parser.dest())
            comp_bin = Code.comp(parser.comp())
            jump_bin = Code.jump(parser.jump())
            # Write the C-instruction as binary
            output_file.write(f"111{comp_bin}{dest_bin}{jump_bin}\n")

        # Ignore L_COMMAND in the second pass



if "__main__" == __name__:
    # Parses the input path and calls assemble_file on each input file.
    # This opens both the input and the output files!
    # Both are closed automatically when the code finishes running.
    # If the output file does not exist, it is created automatically in the
    # correct path, using the correct filename.
    if not len(sys.argv) == 2:
        sys.exit("Invalid usage, please use: Assembler <input path>")
    argument_path = os.path.abspath(sys.argv[1])
    if os.path.isdir(argument_path):
        files_to_assemble = [
            os.path.join(argument_path, filename)
            for filename in os.listdir(argument_path)]
    else:
        files_to_assemble = [argument_path]
    for input_path in files_to_assemble:
        filename, extension = os.path.splitext(input_path)
        if extension.lower() != ".asm":
            continue
        output_path = filename + ".hack"
        with open(input_path, 'r') as input_file, \
                open(output_path, 'w') as output_file:
            assemble_file(input_file, output_file)

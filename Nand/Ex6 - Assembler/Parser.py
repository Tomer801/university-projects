"""
This file is part of nand2tetris, as taught in The Hebrew University, and
was written by Aviv Yaish. It is an extension to the specifications given
[here](https://www.nand2tetris.org) (Shimon Schocken and Noam Nisan, 2017),
as allowed by the Creative Common Attribution-NonCommercial-ShareAlike 3.0
Unported [License](https://creativecommons.org/licenses/by-nc-sa/3.0/).
"""
import typing


class Parser:
    """Encapsulates access to the input code. Reads an assembly program
    by reading each command line-by-line, parses the current command,
    and provides convenient access to the commands components (fields
    and symbols). In addition, removes all white space and comments.
    """


    def __init__(self, input_file: typing.TextIO) -> None:
        """Opens the input file and gets ready to parse it.
        Has three variables.
        commands- an array of all commands
        current_command_index- the index of current command
        current_command- the actual command
        """
        #remove lines and spaces
        input_lines = input_file.read().splitlines()

        #remove comments
        self.commands = [line.split("//")[0].strip() for line in input_lines if
                         line.strip() and not line.startswith("//")]

        self.current_command_index = -1  #start before the first command
        self.current_command = None

    def has_more_commands(self) -> bool:
        """Are there more commands in the input?

        Returns:
            bool: True if there are more commands, False otherwise.
        """
        return self.current_command_index < len(self.commands) - 1

    def advance(self) -> None:
        """Reads the next command from the input and makes it the current command.
        Should be called only if has_more_commands() is true.
        """
        if self.has_more_commands():
            self.current_command_index += 1
            self.current_command = self.commands[self.current_command_index]

    def command_type(self) -> str:
        """
        Returns:
            str: the type of the current command:
            "A_COMMAND" for @Xxx where Xxx is either a symbol or a decimal number
            "C_COMMAND" for dest=comp;jump
            "L_COMMAND" (actually, pseudo-command) for (Xxx) where Xxx is a symbol
        """

        if self.current_command.startswith("(") and self.current_command.endswith(")"):
            return "L_COMMAND"
        elif self.current_command.startswith("@"):
            return "A_COMMAND"
        else:
            return "C_COMMAND"

    def symbol(self) -> str:
        """
        Returns:
            str: the symbol or decimal Xxx of the current command @Xxx or
            (Xxx). Should be called only when command_type() is "A_COMMAND" or
            "L_COMMAND".
        """
        if self.command_type() == "A_COMMAND":
            return self.current_command[1:]
        elif self.command_type() == "L_COMMAND":
            return self.current_command[1:-1]

    def dest(self) -> str:
        """
        Returns:
            str: the dest mnemonic in the current C-command. Should be called
            only when commandType() is "C_COMMAND".
        """
        if "=" in self.current_command:
            return self.current_command.split("=")[0]
        return ""

    def comp(self) -> str:
        """
        Returns:
            str: the comp mnemonic in the current C-command. Should be called
            only when commandType() is "C_COMMAND".
        """
        command = self.current_command
        if "=" in command:
            command = command.split("=")[1] #removes the dest
        if ";" in command:
            command = command.split(";")[0] #removes the jump
        return command

    def jump(self) -> str:
        """
        Returns:
            str: the jump mnemonic in the current C-command. Should be called
            only when commandType() is "C_COMMAND".
        """
        if ";" in self.current_command:
            return self.current_command.split(";")[1]
        return ""

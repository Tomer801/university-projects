"""
This file is part of nand2tetris, as taught in The Hebrew University, and
was written by Aviv Yaish. It is an extension to the specifications given
[here](https://www.nand2tetris.org) (Shimon Schocken and Noam Nisan, 2017),
as allowed by the Creative Common Attribution-NonCommercial-ShareAlike 3.0
Unported [License](https://creativecommons.org/licenses/by-nc-sa/3.0/).
"""
import os
import typing

global label_count_call
global label_count_con
class CodeWriter:
    output_stream:typing.TextIO
    label_count_con = 0

    label_count_call = 0
    function_name = None

    current_file_name = None

    """Translates VM commands into Hack assembly code."""

    def __init__(self, output_stream: typing.TextIO) -> None:
        """Initializes the CodeWriter.

        Args:
            output_stream (typing.TextIO): output stream.
        """
        # Your code goes here!
        # Note that you can write to output_stream like so:
        # output_stream.write("Hello world! \n")

        self.output_stream = output_stream




    def write_init(self) -> None:
        """
        Writes the bootstrap code to initialize the VM environment.
        - Sets SP to 256.
        - Calls the Sys.init function to start the program.
        """
        self.output_stream.write("// Bootstrap code\n")
        self.output_stream.write("@256\n")
        self.output_stream.write("D=A\n")
        self.output_stream.write("@SP\n")
        self.output_stream.write("M=D\n")  # SP = 256

        self.write_call("Sys.init", 0)  # Sys.init has 0 arguments

    def set_file_name(self, filename: str) -> None:
        """Informs the code writer that the translation of a new VM file is 
        started.

        Args:
            filename (str): The name of the VM file.
        """
        self.current_file_name = os.path.splitext(os.path.basename(filename))[0]


    def write_arithmetic(self, command: str) -> None:
        """Writes assembly code that is the translation of the given
        arithmetic command. For the commands eq, lt, gt, the function correctly
        compares values and writes the results (-1 for true, 0 for false) to the stack.

        Args:
            command (str): an arithmetic command.
        """
        code = []
        if command in ["add", "sub", "and", "or"]:
            # pop tow last values on stack
            code.extend(["@SP", "AM=M-1", "D=M", "@SP", "A=M-1"])
            if command == "add":
                code.append("M=D+M")
            elif command == "sub":
                code.append("M=M-D")
            elif command == "and":
                code.append("M=D&M")
            elif command == "or":
                code.append("M=D|M")
        elif command in ["neg", "not"]:
            #point last value
            code.extend(["@SP", "A=M-1" ])
            if command == "neg":
                code.append("M=-M")
            elif command == "not":
                code.append("M=!M")
        elif command in ["eq", "lt", "gt"]:
                # Avoid label name clashes
            label_false = f"FALSE_{CodeWriter.label_count_con}"
            label_end = f"END_{CodeWriter.label_count_con}"
            CodeWriter.label_count_con += 1

                # Pop two last values and subtract them
            code.extend([
                "@SP", "AM=M-1", "D=M",  # D = y (second operand)
                "@SP", "A=M-1", "D=M-D",  # D = x - y
                "@SP", "A=M-1", "M=-1"  # Assume true: set stack top to -1
                ])

                # Conditional jump to set false
            if command == "eq":
                code.extend([
                        f"@{label_false}", "D;JNE",  # Jump to FALSE if D != 0
                    ])
            elif command == "lt":
                code.extend([
                    f"@{label_false}", "D;JGE",  # Jump to FALSE if D >= 0
                    ])
            elif command == "gt":
                    code.extend([
                        f"@{label_false}", "D;JLE",  # Jump to FALSE if D <= 0
                    ])

                # Jump to end if condition passed
            code.extend([
                    f"@{label_end}", "0;JMP",  # Jump to END
                    f"({label_false})",  # FALSE label
                    "@SP", "A=M-1", "M=0",  # Set top of stack to 0 (false)
                    f"({label_end})"  # END label
                ])
        elif command == "shiftleft":
            code.extend([ "@SP", "A=M-1", "M=M<<"  ])
        elif command == "shiftright":
            code.extend(["@SP", "A=M-1", "M=M>>"])
        self.output_stream.write("\n".join(code) + "\n")

    def write_push_pop(self, command: str, segment: str, index: int) -> None:
        code = []
        if command == "C_PUSH":
            if segment == "constant":
                # Push constant value
                code.extend([
                    f"@{index}", "D=A",
                    "@SP", "A=M", "M=D",
                    "@SP", "M=M+1"
                ])
            elif segment in ["local", "argument", "this", "that"]:
                base_address = {"local": "LCL", "argument": "ARG", "this": "THIS", "that": "THAT"}
                code.extend([
                    f"@{index}", "D=A", f"@{base_address[segment]}", "A=M+D", "D=M",
                    "@SP", "A=M", "M=D",
                    "@SP", "M=M+1"
                ])
            elif segment == "static":
                code.extend([
                    f"@{self.current_file_name}.{index}", "D=M",
                    "@SP", "A=M", "M=D",
                    "@SP", "M=M+1"
                ])
            elif segment == "temp":
                code.extend([
                    f"@{5 + index}", "D=M",
                    "@SP", "A=M", "M=D",
                    "@SP", "M=M+1"
                ])
            elif segment == "pointer":
                pointer = "THIS" if index == 0 else "THAT"
                code.extend([
                    f"@{pointer}", "D=M",
                    "@SP", "A=M", "M=D",
                    "@SP", "M=M+1"
                ])
        elif command == "C_POP":
            if segment in ["local", "argument", "this", "that"]:
                base = {"local": "LCL", "argument": "ARG", "this": "THIS", "that": "THAT"}[segment]
                code.extend([
                    f"@{index}", "D=A", f"@{base}", "D=M+D",
                    "@R13", "M=D",  # Store target address in R13
                    "@SP", "AM=M-1", "D=M",
                    "@R13", "A=M", "M=D"
                ])
            elif segment == "static":
                code.extend([
                    "@SP", "AM=M-1", "D=M",
                    f"@{self.current_file_name}.{index}", "M=D"
                ])
            elif segment == "temp":
                code.extend([
                    "@SP", "AM=M-1", "D=M",
                    f"@{5 + index}", "M=D"
                ])
            elif segment == "pointer":
                pointer = "THIS" if index == 0 else "THAT"
                code.extend([
                    "@SP", "AM=M-1", "D=M",
                    f"@{pointer}", "M=D"
                ])
        self.output_stream.write("\n".join(code) + "\n")


    # Your code goes here!
    # Note: each reference to "static i" appearing in the file Xxx.vm should
    # be translated to the assembly symbol "Xxx.i". In the subsequent
    # assembly process, the Hack assembler will allocate these symbolic
    # variables to the RAM, starting at address 16.


    def write_label(self, label: str) -> None:
        """Writes assembly code that affects the label command.
        Let "Xxx.foo" be a function within the file Xxx.vm. The handling of
        each "label bar" command within "Xxx.foo" generates and injects the symbol
        "Xxx.foo$bar" into the assembly code stream.
        When translating "goto bar" and "if-goto bar" commands within "foo",
        the label "Xxx.foo$bar" must be used instead of "bar".

        Args:
            label (str): the label to write.
        """
        # This is irrelevant for project 7,
        # you will implement this in project 8!
        label_name = f"{self.function_name}${label}"
        code = [
            f"({label_name})"  # Assembly label syntax
        ]
        self.output_stream.write("\n".join(code) + "\n")


    def write_goto(self, label: str) -> None:
        """Writes assembly code that affects the goto command.

        Args:
            label (str): the label to go to.
        """
        label_name = f"{self.function_name}${label}"
        code = [
            f"@{label_name}",  # Load the address of the label
            "0;JMP"  # Unconditional jump
        ]
        self.output_stream.write("\n".join(code) + "\n")


    def write_if(self, label: str) -> None:
        """Writes assembly code that affects the if-goto command.

        Args:
            label (str): the label to go to.
        """
        label_name = f"{self.function_name}${label}"
        code = [
            "@SP",  # Access the stack pointer
            "AM=M-1",  # Decrement SP and access the top value
            "D=M",  # Store the top stack value in D
            f"@{label_name}",  # Load the address of the label
            "D;JNE"  # Jump to the label if D != 0
        ]
        self.output_stream.write("\n".join(code) + "\n")


    def write_function(self, function_name: str, n_vars: int) -> None:
        """Writes assembly code for a function declaration.

        Args:
            function_name (str): The name of the function.
            n_vars (int): The number of local variables to initialize.
        """
        # Embed the number of variables in the function label
        self.function_name = function_name
        code = [
            f"({function_name})"  # Define the function label with n_vars
        ]
        # Initialize n_vars local variables to 0
        for _ in range(n_vars):
            code.extend([
                "@0", "D=A",  # Load constant 0 into D
                "@SP", "A=M", "M=D",  # Store 0 at the top of the stack
                "@SP", "M=M+1"  # Increment stack pointer
            ])
        self.output_stream.write("\n".join(code) + "\n")

    def write_call(self, function_name: str, n_args: int) -> None:
        """Writes the assembly code for the 'call' command."""

        return_label = f"{function_name}$ret.{CodeWriter.label_count_call}"
        CodeWriter.label_count_call += 1


        code = [
            f"// call {function_name} {n_args}",

            # 1. Push return address
            f"@{return_label}", "D=A",
            "@SP", "A=M", "M=D", "@SP", "M=M+1",

            # 2. Push LCL
            "@LCL", "D=M",
            "@SP", "A=M", "M=D", "@SP", "M=M+1",

            # 3. Push ARG
            "@ARG", "D=M",
            "@SP", "A=M", "M=D", "@SP", "M=M+1",

            # 4. Push THIS
            "@THIS", "D=M",
            "@SP", "A=M", "M=D", "@SP", "M=M+1",

            # 5. Push THAT
            "@THAT", "D=M",
            "@SP", "A=M", "M=D", "@SP", "M=M+1",

            # 6. Reposition ARG = SP - 5 - n_args
            "@SP", "D=M",
            f"@{5 + n_args}", "D=D-A", "@ARG", "M=D",

            # 7. Reposition LCL = SP
            "@SP", "D=M", "@LCL", "M=D",

            # 8. Jump to the function
            f"@{function_name}", "0;JMP",

            # 9. Declare the return label
            f"({return_label})"
        ]
        self.output_stream.write("\n".join(code) + "\n")



    def write_return(self) -> None:
        """Writes assembly code that affects the return command."""
        code = [
            # FRAME = LCL
            "@LCL", "D=M",
            "@R13", "M=D",  # FRAME stored in R13

            # RET = *(FRAME-5)
            "@5", "A=D-A", "D=M",
            "@R14", "M=D",  # RET stored in R14

            # *ARG = pop()
            "@SP", "AM=M-1", "D=M",
            "@ARG", "A=M", "M=D",

            # SP = ARG + 1
            "@ARG", "D=M+1",
            "@SP", "M=D",

            # Restore THAT = *(FRAME-1)
            "@R13", "AM=M-1", "D=M",
            "@THAT", "M=D",

            # Restore THIS = *(FRAME-2)
            "@R13", "AM=M-1", "D=M",
            "@THIS", "M=D",

            # Restore ARG = *(FRAME-3)
            "@R13", "AM=M-1", "D=M",
            "@ARG", "M=D",

            # Restore LCL = *(FRAME-4)
            "@R13", "AM=M-1", "D=M",
            "@LCL", "M=D",

            # Goto RET
            "@R14", "A=M", "0;JMP"
        ]
        self.output_stream.write("\n".join(code) + "\n")

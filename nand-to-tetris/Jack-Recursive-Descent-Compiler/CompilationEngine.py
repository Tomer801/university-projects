"""
This file is an educational adaptation derived from nand2tetris.
Original authorship by Aviv Yaish, with specifications by Schocken and Nisan.
Licensed under Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported.
"""
import typing
from JackTokenizer import JackTokenizer


class CompilationEngine:
    """
    Processes a JackTokenizer to emit parsed structures into an output stream.
    """

    def __init__(self, tokenizer: "JackTokenizer", vm_writer: "VMWriter", symbol_table: "SymbolTable") -> None:
        self.tokenizer = tokenizer
        self.vm_writer = vm_writer
        self.symbol_table = symbol_table


    def compile_class(self) -> None:
        """Compiles a complete class."""
        # Advance the tokenizer to the first token
        if not self.tokenizer.token:
            self.tokenizer.advance()

        # Get the class name
        self.tokenizer.advance()  # Skip "class"
        self.class_name = self.tokenizer.token  # Store the class name
        self.tokenizer.advance()  # Advance past the class name
        self.tokenizer.advance()  # Skip "{"

        # Compile class variable declarations
        while self.tokenizer.token in {"field", "static"}:
            self.compile_class_var_dec()

        # Compile subroutines
        while self.tokenizer.token in {"constructor", "function", "method"}:
            self.compile_subroutine()

        # Advance past the closing brace
        self.tokenizer.advance()  # Skip "}"

    def compile_class_var_dec(self) -> None:
        """Compiles a static declaration or a field declaration."""

        # Determine the kind ("STATIC" or "FIELD")
        kind = self.tokenizer.token.upper()  # "static" or "field"
        self.tokenizer.advance()

        # Get the type of the variable
        var_type = self.tokenizer.token  # e.g., "int", "boolean", "MyClass"
        self.tokenizer.advance()

        # Process variable names
        while True:
            # Define the variable in the SymbolTable
            name = self.tokenizer.token
            self.symbol_table.define(name, var_type, kind)
            self.tokenizer.advance()

            # If the next token is a comma, continue processing names
            if self.tokenizer.token == ",":
                self.tokenizer.advance()
            else:
                break

        # Expect and skip the semicolon
        if self.tokenizer.token == ";":
            self.tokenizer.advance()

    def compile_subroutine(self) -> None:
        """Compiles a method, function, or constructor."""
        # Reset the subroutine scope
        self.symbol_table.start_subroutine()

        # Get the subroutine type: "constructor", "function", or "method"
        subroutine_type = self.tokenizer.token
        self.tokenizer.advance()

        # Get the return type: "void" or a type
        return_type = self.tokenizer.token
        self.tokenizer.advance()

        # Get the subroutine name
        subroutine_name = self.tokenizer.token
        self.tokenizer.advance()

        # Fully qualified function name: <class_name>.<subroutine_name>
        function_name = f"{self.class_name}.{subroutine_name}"

        # Advance past the opening parenthesis
        self.tokenizer.advance()  # Skip "("

        # Compile parameter list
        self.compile_parameter_list()

        # Advance past the closing parenthesis
        self.tokenizer.advance()  # Skip ")"

        # Handle constructor or method specifics
        if subroutine_type == "method":
            self.vm_writer.write_push("argument", 0)  # Push `this`
            self.vm_writer.write_pop("pointer", 0)  # Set `this` to `pointer 0`
        elif subroutine_type == "constructor":
            field_count = self.symbol_table.var_count("FIELD")
            self.vm_writer.write_push("constant", field_count)  # Allocate memory
            self.vm_writer.write_call("Memory.alloc", 1)
            self.vm_writer.write_pop("pointer", 0)

        # Handle subroutine body
        self.tokenizer.advance()  # Skip "{"
        self.compile_var_dec()  # Define local variables

        # Write the function declaration
        n_locals = self.symbol_table.var_count("VAR")
        self.vm_writer.write_function(function_name, n_locals)

        # Compile the body statements
        self.compile_statements()

        # Advance past the closing brace
        self.tokenizer.advance()  # Skip "}"

    def compile_parameter_list(self) -> None:
        """Compiles a (possibly empty) parameter list."""
        if self.tokenizer.token == ")":
            return  # Empty parameter list

        while True:
            # Parse parameter type and name
            param_type = self.tokenizer.token  # e.g., "int", "boolean", or class name
            self.tokenizer.advance()
            param_name = self.tokenizer.token  # Parameter name
            self.tokenizer.advance()

            # Define the parameter in the SymbolTable
            self.symbol_table.define(param_name, param_type, "ARG")

            # Check for more parameters
            if self.tokenizer.token != ",":
                break  # End of parameter list
            self.tokenizer.advance()  # Skip the comma

    def compile_var_dec(self) -> None:
        """Compiles a var declaration."""
        while self.tokenizer.token == "var":
            self.tokenizer.advance()  # Skip "var"

            # Parse the type of the variable
            var_type = self.tokenizer.token  # e.g., "int", "boolean", or class name
            self.tokenizer.advance()

            # Parse variable names
            while True:
                # Define the variable in the SymbolTable
                var_name = self.tokenizer.token
                self.symbol_table.define(var_name, var_type, "VAR")
                self.tokenizer.advance()

                # If there are more variables, skip the comma
                if self.tokenizer.token != ",":
                    break
                self.tokenizer.advance()

            # Skip the semicolon
            self.tokenizer.advance()

    def compile_statements(self) -> None:
        """Compiles a sequence of statements."""
        while self.tokenizer.token in {"let", "if", "while", "do", "return"}:
            getattr(self, f"compile_{self.tokenizer.token}")()

    def compile_do(self) -> None:
        """Compiles a do statement."""
        self.tokenizer.advance()  # Skip "do"

        # Parse the subroutine name
        subroutine_name = self.tokenizer.token
        self.tokenizer.advance()

        # Check if the subroutine is prefixed with a class/variable name
        if self.tokenizer.token == ".":
            self.tokenizer.advance()  # Skip "."
            prefix = subroutine_name
            subroutine_name = f"{prefix}.{self.tokenizer.token}"
            self.tokenizer.advance()

        # Handle method calls
        is_method = "." in subroutine_name or subroutine_name in self.symbol_table.class_scope
        if is_method:
            if prefix in self.symbol_table.class_scope:
                # Push the base address of the object
                self.vm_writer.write_push(self.symbol_table.kind_of(prefix), self.symbol_table.index_of(prefix))

        # Advance past the opening parenthesis
        self.tokenizer.advance()  # Skip "("

        # Compile the argument list and count arguments
        n_args = self.compile_expression_list()

        # Advance past the closing parenthesis
        self.tokenizer.advance()  # Skip ")"

        # Adjust argument count for methods
        if is_method:
            n_args += 1  # Include the `this` pointer

        # Emit the call
        self.vm_writer.write_call(subroutine_name, n_args)

        # Discard the return value
        self.vm_writer.write_pop("temp", 0)

        # Advance past the semicolon
        self.tokenizer.advance()  # Skip ";"

    def compile_let(self) -> None:
        """Compiles a let statement."""
        self.tokenizer.advance()  # Skip "let"

        # Get the variable name
        var_name = self.tokenizer.token
        self.tokenizer.advance()

        # Handle array access
        is_array = False
        if self.tokenizer.token == "[":
            is_array = True
            self.tokenizer.advance()  # Skip "["
            self.compile_expression()  # Compile the index expression
            self.vm_writer.write_push(self.symbol_table.kind_of(var_name), self.symbol_table.index_of(var_name))
            self.vm_writer.write_arithmetic("add")  # Compute base + offset
            self.tokenizer.advance()  # Skip "]"

        # Skip "=" and compile the right-hand side expression
        self.tokenizer.advance()  # Skip "="
        self.compile_expression()
        self.tokenizer.advance()  # Skip ";"

        if is_array:
            # Store the result in the array
            self.vm_writer.write_pop("temp", 0)  # Temporarily hold the value
            self.vm_writer.write_pop("pointer", 1)  # Set `that` to the target address
            self.vm_writer.write_push("temp", 0)  # Push the value back
            self.vm_writer.write_pop("that", 0)  # Store the value in the array
        else:
            # Simple variable assignment
            self.vm_writer.write_pop(self.symbol_table.kind_of(var_name), self.symbol_table.index_of(var_name))

    def compile_expression(self) -> None:
        """Compiles an expression."""
        # Compile the first term
        self.compile_term()

        # Process additional terms and operators
        while self.tokenizer.token in {"+", "-", "*", "/", "&", "|", "<", ">", "="}:
            # Get the operator and advance
            operator = self.tokenizer.token
            self.tokenizer.advance()

            # Compile the next term
            self.compile_term()

            # Emit the VM command for the operator
            if operator == "+":
                self.vm_writer.write_arithmetic("add")
            elif operator == "-":
                self.vm_writer.write_arithmetic("sub")
            elif operator == "*":
                self.vm_writer.write_call("Math.multiply", 2)
            elif operator == "/":
                self.vm_writer.write_call("Math.divide", 2)
            elif operator == "&":
                self.vm_writer.write_arithmetic("and")
            elif operator == "|":
                self.vm_writer.write_arithmetic("or")
            elif operator == "<":
                self.vm_writer.write_arithmetic("lt")
            elif operator == ">":
                self.vm_writer.write_arithmetic("gt")
            elif operator == "=":
                self.vm_writer.write_arithmetic("eq")

    def compile_term(self) -> None:
        """Compiles a term."""
        token_type = self.tokenizer.token_type()

        if token_type == "INT_CONST":
            # Integer constant
            value = self.tokenizer.int_val()
            self.vm_writer.write_push("constant", value)
            self.tokenizer.advance()
        elif token_type == "STRING_CONST":
            # String constant
            string = self.tokenizer.string_val()
            self._compile_string_constant(string)
            self.tokenizer.advance()
        elif self.tokenizer.token in {"true", "false", "null", "this"}:
            # Keywords
            self._compile_keyword_constant(self.tokenizer.token)
            self.tokenizer.advance()
        elif token_type == "IDENTIFIER":
            # Variable, array access, or subroutine call
            self._compile_identifier_term()
        elif self.tokenizer.token == "(":
            # Parenthesized expression
            self.tokenizer.advance()  # Skip "("
            self.compile_expression()
            self.tokenizer.advance()  # Skip ")"
        elif self.tokenizer.token in {"-", "~"}:
            # Unary operator
            operator = self.tokenizer.token
            self.tokenizer.advance()
            self.compile_term()
            self.vm_writer.write_arithmetic("neg" if operator == "-" else "not")

    def compile_expression_list(self) -> int:
        n_args = 0  # Initialize argument count

        if self.tokenizer.token != ")":  # Check for non-empty list
            while True:
                self.compile_expression()  # Compile the next expression
                n_args += 1  # Increment the count for each expression

                if self.tokenizer.token != ",":  # No more arguments
                    break
                self.tokenizer.advance()  # Skip the comma

        return n_args

    def compile_return(self) -> None:
        """Compiles a return statement."""
        self.tokenizer.advance()  # Skip "return"

        if self.tokenizer.token != ";":
            # Compile the expression to return
            self.compile_expression()
        else:
            # Push 0 for void returns
            self.vm_writer.write_push("constant", 0)

        # Emit the return command
        self.vm_writer.write_return()

        # Advance past the semicolon
        self.tokenizer.advance()  # Skip ";"

    def _compile_string_constant(self, string: str) -> None:
        """Generates VM code for string constants."""
        self.vm_writer.write_push("constant", len(string))
        self.vm_writer.write_call("String.new", 1)
        for char in string:
            self.vm_writer.write_push("constant", ord(char))
            self.vm_writer.write_call("String.appendChar", 2)

    def _compile_keyword_constant(self, keyword: str) -> None:
        """Generates VM code for keyword constants."""
        if keyword in {"null", "false"}:
            self.vm_writer.write_push("constant", 0)
        elif keyword == "true":
            self.vm_writer.write_push("constant", 0)
            self.vm_writer.write_arithmetic("not")
        elif keyword == "this":
            self.vm_writer.write_push("pointer", 0)

    def _compile_identifier_term(self) -> None:
        """Handles variable, array, or subroutine calls."""
        name = self.tokenizer.token
        self.tokenizer.advance()

        if self.tokenizer.token == "[":
            # Array access: varName[expression]
            self.tokenizer.advance()  # Skip "["
            self.compile_expression()
            self.tokenizer.advance()  # Skip "]"
            self.vm_writer.write_push(self.symbol_table.kind_of(name), self.symbol_table.index_of(name))
            self.vm_writer.write_arithmetic("add")
            self.vm_writer.write_pop("pointer", 1)
            self.vm_writer.write_push("that", 0)
        elif self.tokenizer.token in {".", "("}:
            # Subroutine call
            self._compile_subroutine_call(name)
        else:
            # Simple variable
            self.vm_writer.write_push(self.symbol_table.kind_of(name), self.symbol_table.index_of(name))


    def compile_if(self) -> None:
        """Compiles an if statement."""
        # Generate unique labels
        label_else = f"IF_ELSE_{id(self)}"
        label_end = f"IF_END_{id(self)}"

        self.tokenizer.advance()  # Skip "if"
        self.tokenizer.advance()  # Skip "("
        self.compile_expression()  # Compile the condition
        self.tokenizer.advance()  # Skip ")"

        # If condition is false, jump to ELSE
        self.vm_writer.write_arithmetic("not")
        self.vm_writer.write_if(label_else)

        # Compile the 'if' body
        self.tokenizer.advance()  # Skip "{"
        self.compile_statements()
        self.tokenizer.advance()  # Skip "}"

        # Jump to END after the 'if' block
        self.vm_writer.write_goto(label_end)

        # Write the ELSE label
        self.vm_writer.write_label(label_else)

        # Compile the 'else' body (if present)
        if self.tokenizer.token == "else":
            self.tokenizer.advance()  # Skip "else"
            self.tokenizer.advance()  # Skip "{"
            self.compile_statements()
            self.tokenizer.advance()  # Skip "}"

        # Write the END label
        self.vm_writer.write_label(label_end)

    def compile_while(self) -> None:
        """Compiles a while statement."""
        # Generate unique labels
        label_start = f"WHILE_START_{id(self)}"
        label_end = f"WHILE_END_{id(self)}"

        # Write the loop's start label
        self.vm_writer.write_label(label_start)

        self.tokenizer.advance()  # Skip "while"
        self.tokenizer.advance()  # Skip "("
        self.compile_expression()  # Compile the condition
        self.tokenizer.advance()  # Skip ")"

        # If the condition is false, jump to the end
        self.vm_writer.write_arithmetic("not")
        self.vm_writer.write_if(label_end)

        # Compile the loop body
        self.tokenizer.advance()  # Skip "{"
        self.compile_statements()
        self.tokenizer.advance()  # Skip "}"

        # Jump back to the start of the loop
        self.vm_writer.write_goto(label_start)

        # Write the end label
        self.vm_writer.write_label(label_end)

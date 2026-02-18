"""
This file is an adaptation for educational purposes, derived from nand2tetris.
Original authorship credited to Aviv Yaish and specifications by Schocken and Nisan.
Licensed under Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported.
"""
import typing


class JackTokenizer:

    # Define keywords and symbols relevant to the Jack programming language
    KEYWORDS = {
        "class", "constructor", "function", "method", "field", "static", "var",
        "int", "char", "boolean", "void", "true", "false", "null", "this",
        "let", "do", "if", "else", "while", "return"
    }

    SYMBOLS = set("{}()[],.+-*/&|<>=~^#;")

    def __init__(self, input_stream: typing.TextIO) -> None:
        self.tokens = self._process_input(input_stream.read())
        self.token_index = -1
        self.token = None  # Initialize token as None

    def _process_input(self, data: str) -> list:
        """
        Tokenizes the input data by removing comments and splitting into valid tokens.

        Args:
            data (str): The raw input data from the file.

        Returns:
            list: A list of processed tokens.
        """
        tokens, in_block_comment = [], False
        for line in data.splitlines():
            stripped_line = line.strip()
            if in_block_comment:
                if "*/" in stripped_line:
                    in_block_comment = False
                    stripped_line = stripped_line.split("*/", 1)[1].strip()
                else:
                    continue

            while "/*" in stripped_line:
                before, after = stripped_line.split("/*", 1)
                stripped_line = before.strip()
                if "*/" in after:
                    stripped_line += " " + after.split("*/", 1)[1].strip()
                else:
                    in_block_comment = True
                    break

            stripped_line = stripped_line.split("//", 1)[0].strip()
            if stripped_line:
                tokens.extend(self._tokenize_line(stripped_line))
        return tokens

    def _tokenize_line(self, line: str) -> list:
        """
        Splits a single line into tokens.

        Args:
            line (str): A single line of Jack code.

        Returns:
            list: Tokens extracted from the line.
        """
        token_list, current_token, inside_string = [], "", False
        for char in line:
            if inside_string:
                current_token += char
                if char == '"':
                    token_list.append(current_token)
                    current_token, inside_string = "", False
            elif char == '"':
                if current_token:
                    token_list.append(current_token)
                current_token, inside_string = '"', True
            elif char in self.SYMBOLS:
                if current_token:
                    token_list.append(current_token)
                token_list.append(char)
                current_token = ""
            elif char.isspace():
                if current_token:
                    token_list.append(current_token)
                current_token = ""
            else:
                current_token += char
        if current_token:
            token_list.append(current_token)
        return token_list

    def has_more_tokens(self) -> bool:
        """
        Checks if there are more tokens to process.

        Returns:
            bool: True if more tokens are available, False otherwise.
        """
        return self.token_index < len(self.tokens) - 1

    def advance(self) -> None:
        """
        Moves to the next token, making it the current token.
        """
        if self.has_more_tokens():
            self.token_index += 1

    def token_type(self) -> str:
        """
        Determines the type of the current token.

        Returns:
            str: Token type ("KEYWORD", "SYMBOL", "IDENTIFIER", "INT_CONST", "STRING_CONST").
        """
        current_token = self.tokens[self.token_index]
        if current_token in self.KEYWORDS:
            return "KEYWORD"
        elif current_token in self.SYMBOLS:
            return "SYMBOL"
        elif current_token.isdigit() and 0 <= int(current_token) <= 32767:
            return "INT_CONST"
        elif current_token.startswith('"') and current_token.endswith('"'):
            return "STRING_CONST"
        else:
            return "IDENTIFIER"

    def keyword(self) -> str:
        if self.token_type() == "KEYWORD":
            return self.tokens[self.token_index]

    def symbol(self) -> str:
        if self.token_type() == "SYMBOL":
            return {"<": "&lt;", ">": "&gt;", "&": "&amp;"}.get(self.tokens[self.token_index], self.tokens[self.token_index])

    def identifier(self) -> str:
        if self.token_type() == "IDENTIFIER":
            return self.tokens[self.token_index]

    def int_val(self) -> int:
        if self.token_type() == "INT_CONST":
            return int(self.tokens[self.token_index])

    def string_val(self) -> str:
        if self.token_type() == "STRING_CONST":
            return self.tokens[self.token_index][1:-1]

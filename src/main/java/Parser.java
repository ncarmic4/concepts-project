import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {

    private int lineIndex = 0;
    private int tokenIndex = 0;
    private List<Token> currentTokens = new ArrayList<>();
    private final List<Variable> identifiers = new ArrayList<>();
    private final Stack<Token> tokenStack = new Stack<>();

    public Parser(List<Line> lines) throws IOException {
        for (Line l : lines) {
            currentTokens = l.getLexemes();
            tokenIndex = 0;
            StringBuilder line = new StringBuilder();
            while (tokenIndex < currentTokens.size()) {
                Token current = currentTokens.get(tokenIndex);
                lineIndex = current.getLine();
                line.append("--------------------------------------------------------------------------------------\n");
                line.append("Parsed ").append(lineIndex).append(": ").append(l.getValue()).append("\n");
                line.append("--------------------------------------------------------------------------------------")
                        .append("\n");
                parseKeyword(current, line);
                printList(line.toString());
            }
        }
    }

    /**
     * Get the next token and ensure it is of a certain type.
     * @param val The expected type of the token.
     * @return The next token in the list.
     */
    private Token nextToken(String val) {
        tokenIndex++;
        Token next = tokenIndex == currentTokens.size() ? null : currentTokens.get(tokenIndex);
        checkNull(val, next);
        if (!val.isEmpty() && next.getSubType() != val)
            throwExpectedException(val, next);
        return next;
    }

    /**
     * Get the next token and ensure it is of a certain category.
     * @param options Categorical list of possibilities
     * @param val Expected type of the token (category name).
     * @return The next token in the list.
     */
    private Token nextToken(List<String> options, String val) {
        tokenIndex++;
        Token next = tokenIndex == currentTokens.size() ? null : currentTokens.get(tokenIndex);
        checkNull(val, next);
        if (options.contains(next.getSubType())) {
            return next;
        }

        throwExpectedException(val, next);
        return null;
    }

    /**
     * Check if the current token is the last token in the last.
     */
    private void checkIsLast() {
        Token token = currentTokens.get(tokenIndex);
        if (tokenIndex != currentTokens.size() - 1) {
            throwExpectedException(token.getValue() + " should be the last item on the line", token);
        }
        tokenIndex++;
    }

    /**
     * Either push or pop to the stack for blocks. For example,
     * Push a open parenthesis, and when a close parenthesis is parsed,
     * pop the open one. This helps track the syntax of blocks.
     * @param token The token to evaluate.
     * @param related The related type of the open/close token.
     * @param isStart True if it begins a block.
     */
    public void modifyStack(Token token, String related, boolean isStart) {
        if (isStart) {
            tokenStack.push(token);
        } else {
            if (tokenStack.peek().getSubType().equals(related)) {
                tokenStack.pop();
            } else {
                throwException("invalid " + related + "/" + token.getSubType() + " block", token);
            }
        }
    }

    /**
     * Return a variable from the identifiers list.
     * @param token The token that holds the name of the identifier.
     * @return The variable of the identifier.
     */
    public Variable getIdentifier(Token token) {
        for (Variable v : identifiers) {
            if (v.getIdentifier().equals(token.getValue())) {
                return v;
            }
        }
        return null;
    }

    /**
     * Giant switch statement to parse a line.
     * @param token The first token of the line.
     */
    private void parseKeyword(Token token, StringBuilder output) {
        switch (token.getSubType()) {
            case "import": {
                nextToken("literal_string");
                checkIsLast();
                output.append(outputAction("Recognized valid import"));
                break;
            }
            case "implementations": {
                checkIsLast();
                output.append(outputAction("Defining implementations"));
                break;
            }
            case "variables": {
                checkIsLast();
                output.append(outputAction("Defining variables"));
                break;
            }
            case "define": {
                Token id = nextToken("identifier");
                nextToken("of");
                nextToken("type");
                Token type = nextToken(LexicalRules.TYPES, "variable_type");
                Variable var = new Variable(id.getValue(), "", Variable.Type.valueOf(type.getValue().toUpperCase()), Variable.Purpose.VARIABLE);
                identifiers.add(var);
                checkIsLast();
                output.append(outputAction("Defined variable " + id.getValue() + " of type " + type.getValue()));
                break;
            }
            case "set": {
                Token id = nextToken("identifier");
                nextToken("assignment");
                parseVariable(output);
                checkIsLast();
                output.append(outputAction("Assigned variable " + id.getValue() + ""));
                break;
            }
            case "function": {
                modifyStack(token, "endfun", true);
                Token id = nextToken("identifier");
                nextToken("return");
                nextToken("type");
                Token type = nextToken(LexicalRules.TYPES, "type");
                Variable var = new Variable(id.getValue(), id.getValue(), Variable.Type.valueOf(type.getSubType().toUpperCase()), Variable.Purpose.FUNCTION);
                identifiers.add(var);
                StringBuilder out = new StringBuilder();
                out.append("Defined function ").append(id.getValue()).append(" with return type ").append(type.getValue());
                output.append(outputAction(out.toString()));
                if (currentTokens.get(tokenIndex + 1).getSubType().equals("parameters")) {
                    nextToken("parameters");
                    while (tokenIndex < currentTokens.size() - 2) {
                        out = new StringBuilder();
                        Token idParam = nextToken("identifier");
                        nextToken("of");
                        nextToken("type");
                        Token typeParam = nextToken(LexicalRules.TYPES, "type");
                        Variable varParam = new Variable(idParam.getValue(), idParam.getValue(), Variable.Type.valueOf(typeParam.getSubType().toUpperCase()), Variable.Purpose.VARIABLE);
                        identifiers.add(varParam);
                        out.append("parameter ").append(id.getValue()).append(" of type ").append(typeParam.getValue());
                        output.append(outputAction(out.toString()));
                    }
                }
                nextToken("is");
                checkIsLast();
                output.append(outputAction("Opening function block"));
                break;
            }
            case "endfun": {
                modifyStack(token, "function", false);
                nextToken("identifier");
                checkIsLast();
                output.append(outputAction("Closing valid function/endfun block"));
                break;
            }
            case "begin": {
                modifyStack(token, "exit", true);
                checkIsLast();
                output.append(outputAction("Opening begin block"));
                break;
            }
            case "exit": {
                modifyStack(token, "begin", false);
                checkIsLast();
                output.append(outputAction("Closing valid begin/exit block"));
                break;
            }
            case "while": {
                modifyStack(token, "endwhile", true);
                nextToken("identifier");
                nextToken(LexicalRules.CONDITIONAL_OPERATORS, "conditional_operator");
                nextToken("literal_integer");
                nextToken("do");
                checkIsLast();
                output.append(outputAction("Opening while block"));
                break;
            }
            case "endwhile": {
                modifyStack(token, "while", false);
                checkIsLast();
                output.append(outputAction("Closing valid while/endwhile block"));
                break;
            }
            case "if": {
                modifyStack(token, "endif", true);
                parseVariable(output);
                nextToken(LexicalRules.CONDITIONAL_OPERATORS, "conditional_operator");
                parseVariable(output);
                nextToken("then");
                checkIsLast();
                output.append(outputAction("Opening if block"));
                break;
            }
            case "endif": {
                modifyStack(token, "if", false);
                checkIsLast();
                output.append(outputAction("Closing valid if/endif block"));
                break;
            }
            case "else": {
                modifyStack(token, "if", false);
                // create a dummy if token and push it to the stack
                modifyStack(new Token(Token.Type.KEYWORD, "", "if", 0, 0, 0), "endif", true);
                checkIsLast();
                output.append(outputAction("Closing valid if/endif block"));
                output.append(outputAction("Opening if block"));
                break;
            }
            case "display": {
                parseVariable(output);
                checkIsLast();
                output.append(outputAction("Displaying a variable"));
                break;
            }
            case "return": {
                parseVariable(output);
                checkIsLast();
                output.append(outputAction("Returning a variable"));
                break;
            }
            default:
                throwException("unrecognized word", token);
        }
    }

    /**
     * Parse a function call: function(param1, param2)
     */
    private void parseFunction(StringBuilder output) {
        if (tokenIndex >= currentTokens.size())
            return;

        StringBuilder params = new StringBuilder("Parameters of call: [ ");
        Token openPar = nextToken("open_par");
        modifyStack(openPar, "close_par", true);
        while (true) {
            tokenIndex++;
            Token next = currentTokens.get(tokenIndex);
            if (next.getSubType().equals("close_par")) {
                modifyStack(next, "open_par", false);
                break;
            } else if (next.getSubType().equals("comma")) {
                tokenIndex++;
            } else if (LexicalRules.VARIABlES.contains(next.getSubType())) {
                params.append(next.getValue()).append(" ");
                parseVariable(output);
                tokenIndex--;
            }
        }
        params.append("]");
        output.append(outputAction("Recognized function call"));
        output.append(outputAction(params.toString()));
    }

    /**
     * Parse a variabled: myVar; myVar + 1; 1 + myVar
     */
    private void parseVariable(StringBuilder output) {
        tokenIndex++;
        if (tokenIndex >= currentTokens.size())
            return;

        StringBuilder out = new StringBuilder();

        // get the first identifier - either a var or a literal
        Token identifier = currentTokens.get(tokenIndex);
        Variable var = getIdentifier(identifier);
        if (!LexicalRules.VARIABlES.contains(identifier.getSubType())
            && var == null) {
            return;
        } else if (var != null) {
            if (var.getPurpose().equals(Variable.Purpose.FUNCTION)) {
                parseFunction(output);
                return;
            }
        }

        tokenIndex++;
        if (tokenIndex >= currentTokens.size()) {
            tokenIndex--;
            return;
        }

        Token operator = currentTokens.get(tokenIndex);
        if (!LexicalRules.ARITHMETIC_OPERATORS.contains(operator.getSubType())) {
            tokenIndex--;
            return;
        }

        parseVariable(output);
    }

    /**
     * Check if a token is null.
     * @param type Type of the token.
     * @param next The token to be checked.
     */
    private void checkNull(String type, Token next) {
        if (next == null) {
            throwExpectedException(type, next);
        }
    }

    /**
     * Throw an exception when a token does not match the expected.
     * @param expected Expected type of the token.
     * @param token Actual token object.
     */
    private void throwExpectedException(String expected, Token token) {
        String out;
        if (token == null) {
            out = "ERROR: expected " + expected + " at " + (lineIndex + 1);
        } else {
            out = "ERROR: expected " + expected + ", but got " + token.getSubType()
                    + ". Object at " + (lineIndex + 1) + ":" + (token.getStart() + 1) + " - " + token.getValue();
        }
        throw new IllegalArgumentException(out);
    }

    /**
     * Throw an exception with a custom error.
     * @param error Error to print out.
     * @param token Token where the error occurred.
     */
    private void throwException(String error, Token token) {
        throw new IllegalArgumentException("ERROR: " + error + " at " + (lineIndex + 1) + ":" + (token.getStart() + 1));
    }

    /**
     * Print an action under the line output.
     * @param action The custom action string.
     */
    private String outputAction(String action) {
        return "     * " + action + ".\n";
    }

    /**
     * Print output to file.
     * @throws IOException when error occurred.
     */
    public void printList(String line) throws IOException {
        // Print per line
        PrintWriter printWriterLine = new PrintWriter(new FileWriter("src/main/out/parser_results_line.txt", true));
        printWriterLine.write(line);
        System.out.println(line);
        printWriterLine.close();
    }
}

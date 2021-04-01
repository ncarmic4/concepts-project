/*
 * File:    LexicalDictionary.java
 * Author:  Nicholas Carmichael
 * School:  Kennesaw State University, CCSE
 * Course:  CS4308 - 01 - Prof. J M Garrido
 */

import java.io.IOException;
import java.util.ArrayList;

/**
 * A class that holds tokens for analyzing an input string. Each token has a type (keyword, operator, etc.),
 * a value, and a regex. If there is no regex, the regex = value. This regex is used to match a string, and
 * the value is used to refer to the regex. For instance, it is more easier to understand a call to
 * LexicalDictionary.getRegex("comment_block_begin") than to specify the confusing regex more than once.
 */
public class LexicalDictionary {

    public static ArrayList<Token> TOKENS = new ArrayList<>();

    /**
     * Initializes list with recognized tokens. Sorted in order of priority.
     * Literals should be extracted first, since they can contain operators in string form, etc.
     */
    public static void init() {
        /* -------------------------------- LITERALS --------------------------------- */
        add(Token.Type.LITERAL, "literal_string", "\".+?\"");
        add(Token.Type.LITERAL, "literal_bool", "(true|false)");
        add(Token.Type.LITERAL, "literal_double", "(?<!\\S)[1-9]+[.]\\d+(?!\\S+)");
        add(Token.Type.LITERAL, "literal_integer",    "(?<!\\S)-?\\d+(?!\\S+)");
        add(Token.Type.LITERAL, "literal_import", "<.*>");
        add(Token.Type.LITERAL, "literal_param", "(?<=[(\\[<])\\d+(?=[)\\]>])");

        /* -------------------------------- COMMENTS --------------------------------- */
        add(Token.Type.PUNCTUATION, "comment_line",        "//.*|\\/\\*(.*\\*/)");
        add(Token.Type.PUNCTUATION, "comment_block_begin", "\\/\\*.*");
        add(Token.Type.PUNCTUATION, "comment_block_end",   ".*\\*/");

        /* -------------------------------- OPERATORS -------------------------------- */
        add(Token.Type.OPERATOR, "less/equal", "<=");
        add(Token.Type.OPERATOR, "more/equal", ">=");
        add(Token.Type.OPERATOR, "equal",      "==");
        add(Token.Type.OPERATOR, "not equal",  "~=");
        add(Token.Type.OPERATOR, "assignment", "=");
        add(Token.Type.OPERATOR, "pointer",    "->");
        add(Token.Type.OPERATOR, "less",       "[<]");
        add(Token.Type.OPERATOR, "more",       "[>]");
        add(Token.Type.OPERATOR, "add",        "[+]");
        add(Token.Type.OPERATOR, "subtract",   "[-]");
        add(Token.Type.OPERATOR, "multiply",   "[*]");
        add(Token.Type.OPERATOR, "divide",     "[/]");
        add(Token.Type.OPERATOR, "exponent",   "[\\^]");

        /* -------------------------------- PUNCTUATION ------------------------------ */
        add(Token.Type.PUNCTUATION, "comma",         "[,]");
        add(Token.Type.PUNCTUATION, "period",        "[.]");
        add(Token.Type.PUNCTUATION, "open_par",      "[\\(]");
        add(Token.Type.PUNCTUATION, "close_par",     "[\\)]");
        add(Token.Type.PUNCTUATION, "open_bracket",  "[\\[]");
        add(Token.Type.PUNCTUATION, "close_bracket", "[\\]]");
        add(Token.Type.PUNCTUATION, "new_line",      "[\\r\\n]+");

        /* --------------------------------- KEYWORDS -------------------------------- */
        add(Token.Type.KEYWORD, "specifications");
        add(Token.Type.KEYWORD, "variables");
        add(Token.Type.KEYWORD, "parameters");
        add(Token.Type.KEYWORD, "references");
        add(Token.Type.KEYWORD, "declarations");
        add(Token.Type.KEYWORD, "implementations");

        add(Token.Type.KEYWORD, "void");
        add(Token.Type.KEYWORD, "integer");
        add(Token.Type.KEYWORD, "double");
        add(Token.Type.KEYWORD, "bool");
        add(Token.Type.KEYWORD, "string");

        add(Token.Type.KEYWORD, "import");
        add(Token.Type.KEYWORD, "define");
        add(Token.Type.KEYWORD, "is");
        add(Token.Type.KEYWORD, "of");
        add(Token.Type.KEYWORD, "type");
        add(Token.Type.KEYWORD, "exit");
        add(Token.Type.KEYWORD, "set");
        add(Token.Type.KEYWORD, "display");
        add(Token.Type.KEYWORD, "true");
        add(Token.Type.KEYWORD, "false");

        add(Token.Type.KEYWORD, "endstruct");
        add(Token.Type.KEYWORD, "struct");
        add(Token.Type.KEYWORD, "endif");
        add(Token.Type.KEYWORD, "if");
        add(Token.Type.KEYWORD, "else");
        add(Token.Type.KEYWORD, "endwhile");
        add(Token.Type.KEYWORD, "while");
        add(Token.Type.KEYWORD, "function");
        add(Token.Type.KEYWORD, "endfun");
        add(Token.Type.KEYWORD, "endrepeat");
        add(Token.Type.KEYWORD, "repeat");
        add(Token.Type.KEYWORD, "return");

        add(Token.Type.KEYWORD, "begin");
        add(Token.Type.KEYWORD, "do");
        add(Token.Type.KEYWORD, "then");
        add(Token.Type.KEYWORD, "forward");
        add(Token.Type.KEYWORD, "until");

        add(Token.Type.IDENTIFIER, "identifier", "[_a-zA-Z][_a-zA-Z0-9]*");
    }

    public static void add(Token.Type type, String value) {
        add(type, value, value);
    }

    public static void add(Token.Type type, String value, String regex) {
        TOKENS.add(new Token(type, value, regex));
    }

    /**
     * Return the regular expression for a key.
     * @param key value to search for.
     * @return regex.
     * @throws IOException when value cannot be found.
     */
    public static String getRegex(String key) throws IOException {
        for (Token token : TOKENS) {
            if (token.getValue().equals(key)) {
                return token.getRegex();
            }
        }
        throw new IOException("No pattern for supplied key: " + key);
    }
}

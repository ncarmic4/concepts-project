/*
 * File:    Token.java
 * Author:  Nicholas Carmichael
 * School:  Kennesaw State University, CCSE
 * Course:  CS4308 - 01 - Prof. J M Garrido
 */
public class Token implements Comparable<Token> {
    private final Type type;
    private String subType;
    private final String value;
    private final String regex;

    private int start;
    private int end;

    private int referenceIndex;

    /**
     * Used for implementation of the LexicalAnalyzer, to store information
     * on the tokens that will be used in searching the code.
     * @param type Type of the token.
     * @param value Name of the token.
     * @param regex Regular expression that matches a lexeme.
     */
    public Token(Type type, String value, String regex) {
        this.type = type;
        this.value = value;
        this.regex = regex;
    }

    /**
     * Used for creating lexemes.
     * @param type Type of the token.
     * @param value Value of the lexeme.
     * @param subType Name of the token.
     * @param start Column start value.
     * @param end Column end value.
     */
    public Token(Type type, String value, String subType, int start, int end) {
        this.type = type;
        this.value = value;
        this.regex = value;
        this.subType = subType;
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getValue() {
        return value;
    }

    public String getRegex() {
        return regex;
    }

    public Type getType() {
        return type;
    }

    public int getReferenceIndex() {
        return referenceIndex;
    }

    public void setReferenceIndex(int referenceIndex) {
        this.referenceIndex = referenceIndex;
    }

    /**
     * An enum for identifying the type of token.
     */
    public enum Type {
        LITERAL,
        KEYWORD,
        OPERATOR,
        PUNCTUATION,
        IDENTIFIER,
        UNKNOWN
    }

    @Override
    public int compareTo(Token compare) {
        return Integer.compare(start, compare.start);
    }

    /**
     * Compare the state of two tokens. Literal has a higher priority than keyword, etc.
     * @param compare The object to compare to.
     * @return -1 if less than, 0 if equal, 1 if greater than.
     */
    public int compareState(Token compare) {
        return -1 * type.compareTo(compare.getType());
    }

    @Override
    public String toString() {
        return String.format("%-16s%-31s%32s", type.toString(), value, subType);
    }
}

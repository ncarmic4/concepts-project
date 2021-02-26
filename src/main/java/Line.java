/*
 * File:    Line.java
 * Author:  Nicholas Carmichael
 * School:  Kennesaw State University, CCSE
 * Course:  CS4308 - 01 - Prof. J M Garrido
 */
import java.util.ArrayList;
import java.util.Collections;

/**
 * A class that represents each line of code, or input string sent to the scanner.
 * Determines conflicts between tokens and allows for nice output (print line then the tokens for that line).
 * Rather than sorting a long list of tokens, each line can be sorted and the lists merged.
 */
public class Line {
    private final int lineNumber;
    private final String value;
    private final ArrayList<Token> lexemes = new ArrayList<>();

    private static final char DIVIDER_CHAR = '-';
    private static final int DIVIDER_LENGTH = 86;

    public Line(int lineNumber, String value) {
        this.lineNumber = lineNumber;
        this.value = value;
    }

    /**
     * Tokenize a specific word, adding it to the list of lexemes.
     * @param lexeme the word to be tokenized.
     */
    public void tokenize(Token lexeme) {
        for (int i = 0; i < lexemes.size(); i++) {
            Token compare = lexemes.get(i);
            if (lexeme.getStart() == compare.getStart() && lexeme.getEnd() == compare.getEnd()) {
                // if lexemes are the same word
                if (lexeme.compareState(compare) == 1) {
                    lexemes.set(i, lexeme);
                }
                return;
            } else if (lexeme.getStart() <= compare.getStart() && lexeme.getEnd() >= compare.getEnd()) {
                // if lexeme encompasses another lexeme, EX: keyword in a literal string
                lexemes.set(i, lexeme);
                return;
            } else if (lexeme.getStart() >= compare.getStart() && lexeme.getEnd() <= compare.getEnd()) {
                // if lexeme is encompassed by another lexeme
                return;
            }
        }
        lexemes.add(lexeme);
    }

    /**
     * Sort the list in order based on column number.
     * @return Sorted list.
     */
    public ArrayList<Token> sortList() {
        Collections.sort(lexemes);
        return lexemes;
    }

    @Override
    public String toString() {
        // build divider
        StringBuilder d = new StringBuilder();
        for (int i = 0; i < DIVIDER_LENGTH; i++) {
            d.append(DIVIDER_CHAR);
        }
        String divider = d.toString();

        // build line output
        StringBuilder s = new StringBuilder();
        s.append(divider).append("\n").append(lineNumber).append(" * ").append(value.trim()).append("\n")
                .append(divider).append("\n");
        for (Token lexeme : lexemes) {
            s.append("     * ").append(lexeme).append("\n");
        }
        return s.toString();
    }
}
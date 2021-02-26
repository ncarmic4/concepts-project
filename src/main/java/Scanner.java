/*
 * File:    Scanner.java
 * Author:  Nicholas Carmichael
 * School:  Kennesaw State University, CCSE
 * Course:  CS4308 - 01 - Prof. J M Garrido
 */
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scanner processes input strings into lexemes. These are stored in lists to be used later in the
 * interpretation process.
 */
public class Scanner {

    private final ArrayList<Token> identifiers = new ArrayList<>(); // all identifiers present
    private final ArrayList<Token> symbols = new ArrayList<>();    // every token except for identifiers

    private final ArrayList<Line> lines = new ArrayList<>();
    private boolean comment = false;

    /**
     * Process an SCL file line by line, tokenizing it.
     * @param path the path to the file.
     * @throws IOException when file cannot be found or does not have .scl extension.
     */
    public void processFile(String path) throws IOException {
        if (!path.endsWith(".scl")) {
            throw new IOException("Incorrect file extension. *.scl required.");
        }

        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        int lineNumber = 0;
        while ((line = br.readLine()) != null) {
            line = removeComments(line).trim();
            if (!line.equals("")) {
                processLine(line, lineNumber);
            }
            lineNumber++;
        }
        br.close();
        printList();
    }

    /**
     * Process a line by splitting it into lexemes.
     * @param str Line to be processed.
     * @param lineNumber Number of the line.
     */
    public void processLine(String str, int lineNumber) {
        Line line = new Line(lineNumber, str);
        for (Token token : LexicalDictionary.TOKENS) {
            String regex = token.getRegex();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);

            while (matcher.find()) {
                Token lexeme = new Token(token.getType(), str.substring(matcher.start(), matcher.end()),
                        token.getValue(), matcher.start(), matcher.end());
                line.tokenize(lexeme);
            }
        }
        lines.add(line);

        // add token to corresponding list
        for (Token token : line.sortList()) {
            token((token.getType() == Token.Type.IDENTIFIER ? identifiers : symbols), token);
        }
    }

    /**
     * Add the lexeme in the corresponding identifier or symbol list.
     * @param list List to add to.
     * @param token Token to add.
     */
    private void token(ArrayList<Token> list, Token token) {
        // check if token exists already
        for (int i = 0; i < list.size(); i++) {
            if (token.getValue().equals(list.get(i).getValue())) {
                token.setReferenceIndex(i);
                return;
            }
        }
        token.setReferenceIndex(list.size());
        list.add(token);
    }

    /**
     * Remove all single line, block comments from the line.
     * @param str line to be adjusted.
     * @return adjusted line without comments.
     * @throws IOException when comment regex cannot be found.
     */
    public String removeComments(String str) throws IOException {
        String commentLine = LexicalDictionary.getRegex("comment_line");
        String commentBlockBegin = LexicalDictionary.getRegex("comment_block_begin");
        String commentBlockEnd = LexicalDictionary.getRegex("comment_block_end");
        str = str.replaceAll(commentLine, "");
        if (str.matches(commentBlockBegin)) {
            comment = true;
            str = str.replaceAll(commentBlockBegin, "");
        }
        if (str.matches(commentBlockEnd)) {
            comment = false;
            str = str.replaceAll(commentBlockEnd, "");
        }
        if (comment) {
            str = "";
        }
        return str;
    }

    /**
     * Display the line in token form.
     */
    public void printList() throws IOException {
        // Print per line
        PrintWriter printWriterLine = new PrintWriter("src/main/out/results_line.txt");
        for (Line line : lines) {
            printWriterLine.write(line.toString());
            System.out.println(line);
        }
        printWriterLine.close();

        // Print lists
        PrintWriter printWriterList = new PrintWriter("src/main/out/results_list.txt");
        printWriterList.write("IDENTIFIERS:\n");
        for (Token token : identifiers) {
            printWriterList.write(token.getReferenceIndex() + " - " + token + "\n");
        }

        printWriterList.write("\n\nSYMBOLS:\n");
        for (Token token : symbols) {
            printWriterList.write(token.getReferenceIndex() + " - " + token + "\n");
        }
        printWriterList.close();
    }
}

/*
 * File:    Main.java
 * Author:  Nicholas Carmichael
 * School:  Kennesaw State University, CCSE
 * Course:  CS4308 - 01 - Prof. J M Garrido
 */
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        // uncomment the following line for testing purposes
        // (for instance, running from IDE rather than manual compilation)
        // args = new String[] { "src/main/resources/subset.scl" };

        // check args
        if (args.length == 0) {
            System.out.println("Usage:\n  Main [file1] [file2] ...\n  Main src/main/resources/subset.scl");
            System.exit(1);
        }

        // init components
        LexicalDictionary.init();
        Scanner scanner = new Scanner();

        // process each file
        for (String s : args) {
            File file = new File(s);
            scanner.processFile(file.getAbsolutePath());
        }
    }
}

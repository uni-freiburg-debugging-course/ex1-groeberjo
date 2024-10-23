import java.util.Scanner;
import java.io.File;

/**
 * Reads file in args and prints the output for each line in the file
 */
public class Main {
    public static void main(String[] args) {
        Lexer l = new Lexer();
        try {
            // Read file in filepath line by line, parse and evaluate it and then print the output
            String filepath = args[0];
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNextLine()) {
                String nxt = scanner.nextLine();
                System.out.println(l.lexing(nxt));
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

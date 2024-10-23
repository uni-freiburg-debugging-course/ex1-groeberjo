import java.util.Scanner;

/**
 * Lexer is able to build an AST for given input in the form "(simplify (op num1 num2))" where
 * op is one of +,- or *. num1 and num2 are either 32-bit integers or an expression like (op num1 num2)
 * themselves.
 * Build the AST by calling the lexing method which calls a TreeParser in the end to return the
 * evaluated expression for the input.
 */
public class Lexer {
    // Counts opened and closed brackets
    private int opened;
    private int closed;
    private TreeNode<String> rootNode;
    private TreeNode<String> currentOperator;

    public Lexer() {}

    // Builds AST for input and calls parser to return result for the expression in the input
    public String lexing(String toLex) throws Exception {
        // Set open and close counter to zero
        opened = 0;
        closed = 0;

        // Tokenize input string by using a Scanner
        Scanner scanner = new Scanner(toLex.replaceAll(".$", ""));
        if (!("(simplify".equals(scanner.next())) || !(toLex.endsWith(")"))){
            scanner.close();
            throw new Exception("Input has to begin with simplify statement.");
        }
        // Handle first operator and put it as root
        String root = scanner.next();
        if (root.matches("^\\([\\+\\-\\*]$")) {
            opened++;
            rootNode = new TreeNode<String>(root.replaceAll("\\(", ""));
            currentOperator = rootNode;
        } else {
            scanner.close();
            throw new Exception("First operator wrong.");
        }
        // Handle rest of input string
        while (scanner.hasNext()) {
            addSafe(scanner.next());
        }

        if (!(opened == closed)) {
            scanner.close();
            throw new Exception("Wrong number of brackets");
        }

        scanner.close();

        // parse built tree and output result as string
        final TreeParser pars = new TreeParser();
        final TreeNode<String> resultTree = pars.parseTree(currentOperator);
        if (resultTree.data.matches("^\\d+$")) {
            return resultTree.data;
        } else if (resultTree.data.matches("^-\\d+$")) {
            return ("(" + "- " + resultTree.data.replaceAll("-", "") + ")");
        } else {
            throw new Exception("Something went wrong with parsing tree");
        }
    }

    // Safely add content to AST
    public void addSafe(String content) throws Exception{
        // Case input is a number that may or may not have closing brackets in the end
        if (content.matches("^\\d+\\)*$")) {

            // Current operator has no operands yet
            if (currentOperator.children.isEmpty()) {
                if ((content.matches("^\\d+\\)+$") && "-".equals(currentOperator.data)) || 
                    content.matches("^\\d+$")) {
                    currentOperator.addChild(content.replaceAll("\\)+", ""));
                    closeBrackets(content);
                }

            // Current operator has exactly one operand so far
            } else if (currentOperator.children.size() == 1 && content.matches("^\\d+\\)+$")) {
                currentOperator.addChild(content.replaceAll("\\)+", ""));
                closeBrackets(content);
            } else {
                throw new Exception ("Wrong number of inputs.");
            }
        } 

        // Case input is an operator
        else if (content.matches("^\\([\\+\\-\\*]$") && currentOperator.children.size() < 2) {
            currentOperator.addChild(content.replaceAll("\\(", ""));
            currentOperator = currentOperator.children.getLast();
            opened++;
        }

        // No other cases are desired input
        else {
            throw new Exception("Undesired input");
        }
    }

    // Correctly close all brackets for content and return new current operator
    public void closeBrackets(String content) throws Exception {
        String brackets = content.replaceAll("\\d+", "");
        if (brackets.length() + closed > opened) {
            throw new Exception("There are too many closing brackets.");
        }
        for (int i = 0; i < brackets.length(); i++) {
            if (!(currentOperator.parent == null && brackets.length() == i + 1 && opened == closed + 1)) {
                currentOperator = currentOperator.parent;
                closed++;
            } else {
                closed++;
            }
        }
    }
}

/**
 * TreeParser is able to evaluate an AST that was built by Lexer.
 * Call parseTree() to get the result for the input AST.
 * Note: The output is a single node whose data is the result. For negative values the
 * data in the node displays -number. This is obviously not the correct form to display
 * negative values, however this is handled by lexing() in Lexer which should be the only
 * method to call parseTree().
 */
public class TreeParser {
    public TreeParser(){}

    // Recursively works in a DFS manner to evaluate the input tree
    public TreeNode<String> parseTree(TreeNode<String> tree) throws Exception {
        // Base case no1, tree has no children
        if (tree.children.isEmpty()) {
            return tree;
        }

        // Base case no2, display negative integers as one node
        else if ("-".equals(tree.data) && tree.children.size() == 1 &&
                 tree.children.getFirst().data.matches("^\\d+$")) {
                    if (tree.parent == null) {
                        return new TreeNode<String>("-" + tree.children.getFirst().data);
                    } else {
                        TreeNode<String> negetiveInt = new TreeNode<String>("-" + tree.children.getFirst().data);
                        tree.parent.children.set(tree.parent.children.indexOf(tree), negetiveInt);
                        return parseTree(tree.parent);
                    }
        }

        // Go right if right operand is another operator
        else if (!tree.children.getLast().data.matches("^-?\\d+$")) {
            return parseTree(tree.children.getLast());
        }

        // Go left if right operand is just a number
        else if (!tree.children.getFirst().data.matches("^-?\\d+$")) {
            return parseTree(tree.children.getFirst());
        }

        // Case we have both operands as numbers 
        else if (tree.data.matches("^[+\\-\\*]$")) {
            int result = evaluate(tree.data, Integer.parseInt(tree.children.getFirst().data),
                                  Integer.parseInt(tree.children.getLast().data));
            TreeNode<String> resultTree = new TreeNode<String>(String.valueOf(result));
            if (tree.parent == null) {
                tree = resultTree;
                return tree;
            } else {
                tree.parent.children.set(tree.parent.children.indexOf(tree), resultTree);
                return parseTree(tree.parent);
            }
            
        }

        // Case not desired
        else {
            throw new Exception("Tree has wrong form.");
        }

    }

    // apply string operator on integer operands
    private int evaluate(String op, int left, int right) {
        switch(op) {
            case "+":
                return (left + right);
            case "*":
                return (left * right);
            case "-":
                return (left - right);
            default:
                return 0;
        }
    }
}

//672115019 Thinnapop srisomboon
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class assign3 {
    public static void main(String[] args) {
        try {
            Scanner filePath = new Scanner(System.in);
            System.out.println("Enter your file path: ");
            String path = filePath.nextLine();
            File f = new File(path);

            if (!f.exists()) {
                System.out.println("File not found: " + path);
                return;
            }

            Scanner readFile = new Scanner(f);
            while (readFile.hasNext()) {
                String line = readFile.nextLine();
                if (isValidInfix(line)) {
                    System.out.println("Valid\n" + infixToPostfix(line).trim());
                } else {
                    System.out.println("Not-Valid");
                }
            }
            readFile.close();
        } catch (IOException e) {
            System.out.println("Error! " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isValidInfix(String expression) {
        expression = expression.replaceAll("\\s+", ""); // Remove spaces
        int openParenthesis = 0;
        boolean wasOperator = true;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                wasOperator = false;
            } else if (c == '(') {
                openParenthesis++;
                wasOperator = true;
            } else if (c == ')') {
                if (openParenthesis == 0) return false; // Unmatched closing parenthesis
                openParenthesis--;
                wasOperator = false;
            } else if (isOperator(c)) {
                if (wasOperator) return false; // Consecutive operators
                wasOperator = true;
            } else {
                return false; // Invalid character
            }
        }
        return openParenthesis == 0 && !wasOperator;
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public static String infixToPostfix(String expression) {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                output.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop());
                }
                if (!stack.isEmpty()) stack.pop(); // Remove '('
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek()) && isLeftAssociative(c)) {
                    output.append(stack.pop());
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop());
        }
        return output.toString();
    }

    public static int precedence(char c) {
        switch (c) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            case '^': return 3;
            default: return -1;
        }
    }

    public static boolean isLeftAssociative(char c) {
        return c != '^'; // '^' is right-associative, others are left-associative
    }
}

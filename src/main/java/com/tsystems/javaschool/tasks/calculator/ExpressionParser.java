package com.tsystems.javaschool.tasks.calculator;

import java.util.*;

class ExpressionParser {
    private static String operators = "+-*/";
    private static String delimiters = "() " + operators;
    public static boolean flag = true;
    private static Map<String, Integer> priorityMap;
    private static Set<String> delimiterSet;
    private static Set<String> operatorSet;
    static {
        priorityMap = new HashMap<>();
        delimiterSet = new HashSet<>();
        operatorSet = new HashSet<>();
        priorityMap.put("(", 1);
        priorityMap.put("-", 2);
        priorityMap.put("+", 2);
        priorityMap.put("*", 3);
        priorityMap.put("/", 3);
        for (int i = 0; i < delimiters.length(); i++) {
            delimiterSet.add("" + delimiters.charAt(i));
        }
        for (int i = 0; i < operators.length(); i++) {
            operatorSet.add("" + operators.charAt(i));
        }
    }

    private static boolean isDelimiter(String token) {
        return delimiterSet.contains(token);
    }

    private static boolean isOperator(String token) {
        return operatorSet.contains(token);
    }

    private static int priority(String token) throws CalculatorParseException{
        int priority = priorityMap.get(token);
        if (token == null)
            throw new CalculatorParseException("wrong priority item");
        return priority;
    }

    public static List<String> parse(String infix) throws CalculatorParseException {
        List<String> strOut = new ArrayList<String>();
        Deque<String> stack = new ArrayDeque<String>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (curr.equals(" ")) continue;
            else if (isDelimiter(curr)) {
                if (curr.equals("(")) stack.push(curr);
                else if (curr.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        strOut.add(stack.pop());
                        if (stack.isEmpty()) {
                            throw new CalculatorParseException("Parentheses unpaired");
                        }
                    }
                    stack.pop();
                }
                else if(isOperator(curr)) {
                    if (!tokenizer.hasMoreTokens()) {
                        throw new CalculatorParseException("Incorrect syntax");
                    }
                    if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev)  && !prev.equals(")")))) {
                        curr = "u-";
                    }
                    else {
                        while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                            strOut.add(stack.pop());
                        }

                    }
                    stack.push(curr);
                }

            }

            else {
                strOut.add(curr);
            }
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) strOut.add(stack.pop());
            else {
                throw new CalculatorParseException("Parentheses unpaired");
            }
        }
        return strOut;
    }

    public static void main(String[] args) {
        try {
            System.out.println(parse("( 8 + 2 * 5 ) / ( 1 + 3 * 2 — 4 )"));
        } catch (CalculatorParseException cpe) {
            System.out.println(cpe.getMessage());
        }
    }
}
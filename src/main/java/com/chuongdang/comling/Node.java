package com.chuongdang.comling;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.DecimalFormat;

/**
 * @author Chuong Dang on 4/29/2016.
 */
class Node   {
    Node left, right;

    public Rule getRule() {
        return rule;
    }

    Rule rule;

    public double getProb() {
        return prob;
    }

    double prob;

    public Node() {
    }

    public Node(Rule rule, double prob) {
        this.rule = rule;
        this.prob = prob;

    }
    public void printTree(/*OutputStreamWriter out*/)  {
        if (right != null) {
            right.printTree(true, "");
        }
        printNodeValue();
        if (left != null) {
            left.printTree(false, "");
        }
    }

    private void printNodeValue(/*OutputStreamWriter out*/)   {
        if (rule.isTerminal()) {
            System.out.print(rule.toString());
        } else {
            DecimalFormat df = new DecimalFormat("#.#############");

            System.out.print(rule.getHead()+"("+df.format(prob)+")");
        }
        System.out.print('\n');
    }
    // use string and not stringbuffer on purpose as we need to change the indent at each recursion
    private void printTree(boolean isRight, String indent)   {
        if (right != null) {
            right.printTree( true, indent + (isRight ? "        " : " |      "));
        }
        System.out.print(indent);
        if (isRight) {
            System.out.print(" /");
        } else {
            System.out.print(" \\");
        }
        System.out.print("-------- ");
        printNodeValue();
        if (left != null) {
            left.printTree(false, indent + (isRight ? " |      " : "        "));
        }
    }
    public void printTree2(PrintStream out) throws IOException {
        if (right != null) {
            right.printTree2(out, true, "");
        }
        printNodeValue2(out);
        if (left != null) {
            left.printTree2(out, false, "");
        }
    }
    private void printNodeValue2(PrintStream out) throws IOException {
        if (rule.isTerminal()) {
            out.print(rule.toString());
        } else {
            DecimalFormat df = new DecimalFormat("#.#############");

            out.print(rule.getHead()+"("+df.format(prob)+")");
        }
        out.println();
    }
    // use string and not stringbuffer on purpose as we need to change the indent at each recursion
    private void printTree2(PrintStream out, boolean isRight, String indent) throws IOException {
        if (right != null) {
            right.printTree2(out, true, indent + (isRight ? "        " : " |      "));
        }
        out.print(indent);
        if (isRight) {
            out.print(" /");
        } else {
            out.print(" \\");
        }
        out.print("----- ");
        printNodeValue2(out);
        if (left != null) {
            left.printTree2(out, false, indent + (isRight ? " |      " : "        "));
        }
    }

    public static int  countLeaves(Node node){
        if( node == null )
            return 0;
        if( node.left == null && node.right == null ) {
            return 1;
        } else {
            return countLeaves(node.left) + countLeaves(node.right);
        }
    }
    public static String  concenate(Node node){
        if( node == null )
            return "";
        if( node.left == null && node.right == null ) {
            return node.getRule().getBody().get(0);
        } else {
            return concenate(node.left) + " "+ concenate(node.right);
        }
    }
}
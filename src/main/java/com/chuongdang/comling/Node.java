package com.chuongdang.comling;

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


}
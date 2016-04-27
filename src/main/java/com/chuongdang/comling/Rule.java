package com.chuongdang.comling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Chuong Dang on 4/24/2016.
 */
public class Rule {

    private String head;
    private List<String> body = new ArrayList<String>(2);
    private double probability = 0.0;

    public Rule(String head, List<String> body) {
        this.head = head;
        this.body = body;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public boolean isTerminal() {
        return body.size() == 1;
    }

    public boolean isSameHead(Rule ruleB) {
        return this.head.equals(ruleB.getHead());
    }
    public boolean isSameBody(String b1, String b2) {
        if(this.isTerminal()) return false;
        return body.get(0).equals(b1) && body.get(1).equals(b2);
    }
    @Override
    public String toString() {
        return head + "->" + Arrays.toString(body.toArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (!head.equals(rule.head)) return false;
        for (String s: this.body) {
            if(!rule.getBody().contains(s)) {
                return false;
            }
        }
        return true;

    }

    @Override
    public int hashCode() {
        int result = head.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }
}

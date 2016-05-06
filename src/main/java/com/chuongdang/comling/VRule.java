package com.chuongdang.comling;

/**
 * @author Chuong Dang on 5/6/2016.
 */
public class VRule {
    private String ruleStr;
    private String prob;

    public VRule(String ruleStr, String prob) {
        this.ruleStr = ruleStr;
        this.prob = prob;
    }

    public String getRuleStr() {
        return ruleStr;
    }

    public void setRuleStr(String ruleStr) {
        this.ruleStr = ruleStr;
    }

    public String getProb() {
        return prob;
    }

    public void setProb(String prob) {
        this.prob = prob;
    }
}

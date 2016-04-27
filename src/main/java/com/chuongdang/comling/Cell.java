package com.chuongdang.comling;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuong Dang on 4/24/2016.
 */
public class Cell {
    private boolean ruleNotFound = false;

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    private List<Rule> rules = new ArrayList<>();
    private List<AssocCell> assocCells = new ArrayList<>();
    private List<Double> totalProbabilities = new ArrayList<>();

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    private String word = "";

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    private int xPos = 0;
    private int yPos = 0;

    public Cell(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public boolean isRuleNotFound() {
        return ruleNotFound;
    }

    public void setRuleNotFound(boolean ruleNotFound) {
        this.ruleNotFound = ruleNotFound;
    }

    public List<Rule> getRules() {
        return rules;
    }


    public List<AssocCell> getAssocCells() {
        return assocCells;
    }

    public void setAssocCells(List<AssocCell> assocCells) {
        this.assocCells = assocCells;
    }
    public void addAssocCell(AssocCell cell) {
        this.assocCells.add(cell);
    }
    public boolean containAssocCells(Cell c1, Cell c2) {
//        boolean contained = true;

        for (AssocCell assocCell: assocCells) {
            if(assocCell.getC1().getxPos() != c1.getxPos()) return false;
            if(assocCell.getC1().getyPos() != c1.getyPos()) return false;
            if(assocCell.getC2().getxPos() != c2.getxPos()) return false;
            if(assocCell.getC2().getyPos() != c2.getyPos()) return false;


        }
        return true;
    }

    public List<Double> getTotalProbabilities() {
        return totalProbabilities;
    }

    public void addTotalProbability(Double totalProbability) {
        this.totalProbabilities.add(totalProbability);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int k=0; k< this.getRules().size();k++) {
            final Rule rule = getRules().get(k);
            if(this.getAssocCells().isEmpty()) {
                DecimalFormat df = new DecimalFormat("#.#####");
                sb.append("[" + rule.getHead() + "](P="+df.format(rule.getProbability())+")");
            }
            else {
                DecimalFormat df = new DecimalFormat("#.##########");
                AssocCell assocCell = this.getAssocCells().get(k);
                sb.append("[" + rule.getHead()
                        + "((" + assocCell.getC1().getxPos() + ","+assocCell.getC1().getyPos()
                        + ")+(" + assocCell.getC2().getxPos() + ","+assocCell.getC2().getyPos() +"))]"
                        + "(P="+df.format(this.getTotalProbabilities().get(k))+")");
                sb.append(";");
            }
        }
        return sb.toString();
    }
}

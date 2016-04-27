package com.chuongdang.comling;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Chuong Dang on 4/25/2016.
 */
public class PCKYParser {

    public Cell[][] parseCKY(String[] words, Set<Rule> ruleSet) {
        List<String> wordList = Arrays.asList(words);
        Cell[][] table = new Cell[words.length][words.length];
        for (int i = 0; i < words.length; i++) {
            Cell wordCell = new Cell(i, i);
            wordCell.setWord(words[i]);

            Set<Rule> availRules = ruleSet.stream().filter(rule -> rule.isTerminal()).filter(rule -> rule.getBody().iterator().next().equals(wordCell.getWord())).collect(Collectors.toSet());
            Rule terminalRule = availRules.iterator().next();
            wordCell.addRule(terminalRule);
            wordCell.addTotalProbability(terminalRule.getProbability());
            table[i][i] = wordCell;

        }

        for (int j = 0; j < words.length; j++) {
            for (int i = j; i >= 0; i--) {
//                System.out.print("["+i+"]["+j+"]\t");
                for (int k = j; k >= i; k--) {
                    for (int l = i; l < j; l++) {
                        if (table[i][l] != null && table[k][j] != null) {
                            Cell c1 = table[i][l];
                            Cell c2 = table[k][j];
                            for (Rule rule : ruleSet) {
                                for (int u=0; u < c1.getRules().size(); u++) {
                                    for (int v=0; v < c2.getRules().size(); v++) {
                                        if (rule.isSameBody(
                                                c1.getRules().get(u).getHead(),
                                                c2.getRules().get(v).getHead())) {

                                            if (table[i][j] == null) {
                                                Cell newCell = new Cell(i, j);
                                                newCell.addRule(rule);
                                                newCell.addAssocCell(new AssocCell(c1, c2));
                                                newCell.addTotalProbability(
                                                                rule.getProbability()
                                                                * c1.getTotalProbabilities().get(u)
                                                                * c2.getTotalProbabilities().get(v));
                                                table[i][j] = newCell;
                                            } else {
                                                if(!table[i][j].containAssocCells(c1,c2)) {
                                                    table[i][j].addRule(rule);
                                                    table[i][j].addAssocCell(new AssocCell(c1, c2));
                                                    table[i][j].addTotalProbability(
                                                            rule.getProbability()
                                                                    * c1.getTotalProbabilities().get(u)
                                                                    * c2.getTotalProbabilities().get(v));
                                                }

                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return table;
    }
}

package com.chuongdang.comling;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chuong Dang on 4/25/2016.
 */
public class PCKYParser {
    public Cell[][] getTable() {
        return table;
    }
    private int length;
    private Cell[][] table;

    public void parseCKY(String[] words, Set<Rule> ruleSet) {
        List<String> wordList = Arrays.asList(words);
        table = new Cell[words.length][words.length];
        length = words.length;
        for (int i = 0; i < words.length; i++) {
            Cell wordCell = new Cell(i, i);
            wordCell.setWord(words[i]);

            Set<Rule> availRules = ruleSet.stream()
                    .filter(Rule::isTerminal)
                    .filter(rule -> rule.getBody().iterator().next().equals(wordCell.getWord()))
                    .collect(Collectors.toSet());
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
//                                Set<Rule> r1 = c1.getRules().stream().filter(r -> rule.getBody().get(0).equals(r.getHead())).collect(Collectors.toSet());
//                                Set<Rule> r2 = c2.getRules().stream().filter(r -> rule.getBody().get(1).equals(r.getHead())).collect(Collectors.toSet());

                                for (int u=0; u < c1.getRules().size(); u++) {
                                    for (int v=0; v < c2.getRules().size(); v++) {
                                        for (Rule rule : ruleSet.stream().filter(r-> !r.isTerminal()).collect(Collectors.toSet())) {

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
                                                if(!table[i][j].containAssocCells(c1,c2,c1.getTotalProbabilities().get(u),c2.getTotalProbabilities().get(v))) {
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

    }

    public List<Node> getTreeList() {
        return treeList;
    }

    List<Node> treeList = new ArrayList<>();

    public void buildTree() {
        for(int j = length - 1; j >= 0; j--) {
            for(int i = 0; i < j; i++) {
                if(j == (length -1) && i == 0) {
                    Cell cell = table[i][j];
                    Node node = createRoot(cell);
                    if (node != null) {
                        treeList.add(node);
                    }
                }
            }
        }
    }

    private Node createRoot(Cell cell) {
        Node root = null;
        for(int k = 0; k < cell.getRules().size(); k++) {
            if(Objects.equals(cell.getRules().get(k).getHead(), "s") && cell.getTotalProbabilities().get(k) >= 1E-20) {
                root = new Node(cell.getRules().get(k), cell.getTotalProbabilities().get(k));

                genTreeFromRoot(root, cell.getAssocCells().get(k));
                if(Node.countLeaves(root) == length) {
                    System.out.println("Leaf nodes: "+Node.countLeaves(root));
                    root.printTree();

                }
//                cell.getAssocCells().get(k);
            }
        }
        return root;
    }

    private void genTreeFromRoot(Node root, AssocCell assocCell) {
        Set<Rule> term1 = assocCell.getC1().getRules().stream().filter(Rule::isTerminal).collect(Collectors.toSet());
        Set<Rule> term2 = assocCell.getC2().getRules().stream().filter(Rule::isTerminal).collect(Collectors.toSet());
        if(term1.size() ==1 && term2.size() == 1) {
            Rule termRule1 = term1.iterator().next();
            root.left = new Node(termRule1, termRule1.getProbability());
            Rule termRule2 = term2.iterator().next();
            root.right = new Node(termRule2, termRule2.getProbability());
        } else if(term1.size() != 1 && term2.size() == 1) {
            Rule termRule2 = term2.iterator().next();
            root.right = new Node(termRule2, termRule2.getProbability());
            for(int u = 0; u<assocCell.getC1().getRules().size(); u++) {
                if (Math.abs(root.getProb() - root.getRule().getProbability() * assocCell.getC1().getTotalProbabilities().get(u) * termRule2.getProbability()) < 1E-11) {
                    root.left = new Node(assocCell.getC1().getRules().get(u), assocCell.getC1().getTotalProbabilities().get(u));
                    genTreeFromRoot(root.left, assocCell.getC1().getAssocCells().get(u));
                    break;
                }

            }

        } else if(term1.size() == 1 && term2.size() != 1) {
            Rule termRule1 = term1.iterator().next();
            root.left = new Node(termRule1, termRule1.getProbability());
            for(int v = 0; v<assocCell.getC2().getRules().size(); v++) {
                if (Math.abs(root.getProb() - (Double)root.getRule().getProbability() * assocCell.getC2().getTotalProbabilities().get(v) * termRule1.getProbability()) < 1E-11) {
                    root.right = new Node(assocCell.getC2().getRules().get(v), assocCell.getC2().getTotalProbabilities().get(v));
                    genTreeFromRoot(root.right, assocCell.getC2().getAssocCells().get(v));
                    break;
                }
            }

        } else {
            for(int u = 0; u<assocCell.getC1().getRules().size(); u++) {
                for (int v = 0; v < assocCell.getC2().getRules().size(); v++) {
                    if (Math.abs(root.getProb() - root.getRule().getProbability() * assocCell.getC1().getTotalProbabilities().get(u) * assocCell.getC2().getTotalProbabilities().get(v)) < 1E-11) {
                        root.left = new Node(assocCell.getC1().getRules().get(u), assocCell.getC1().getTotalProbabilities().get(u));
                        genTreeFromRoot(root.left, assocCell.getC1().getAssocCells().get(u));
                        root.right = new Node(assocCell.getC2().getRules().get(v), assocCell.getC2().getTotalProbabilities().get(v));
                        genTreeFromRoot(root.right, assocCell.getC2().getAssocCells().get(v));
                        break;
                    }

                }
            }

        }


    }

    public void printTree() {
        treeList.forEach(root -> {
//            BTreePrinter.printNode(root);
                root.printTree();
        });
    }

}

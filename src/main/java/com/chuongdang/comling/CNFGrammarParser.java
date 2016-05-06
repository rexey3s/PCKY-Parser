package com.chuongdang.comling;

import dnl.utils.text.table.TextTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chuong Dang on 4/24/2016.
 */
public class CNFGrammarParser {
    private static Logger logger = LoggerFactory.getLogger(CNFGrammarParser.class);

    public List<Rule> getGrammar() {
        return grammar;
    }

    private List<Rule> grammar = new ArrayList<Rule>();

    public String getSentence() {
        return sentence;
    }

    private String sentence = "";
    private List<String> totalWords = new ArrayList<>();
    public  void readFromFile(String pathToFile) throws FileNotFoundException {
        FileReader fr = new FileReader(pathToFile);

        try (BufferedReader br = new BufferedReader(fr))
        {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                grammar.add(buildRuleFromString(sCurrentLine));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  void readSentenceFile(String pathToSentenceFile) throws FileNotFoundException {
        FileReader fr = new FileReader(pathToSentenceFile);

        try (BufferedReader br = new BufferedReader(fr))
        {
            sentence = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void readGrammarDir(final File dir) throws FileNotFoundException {
        for (File grammarFile : dir.listFiles()) {
            if (grammarFile.isFile()) {
                readFromFile(grammarFile.toPath().toString());
            }
        }
    }
    public Set<Rule> getRulesWithProbabilities() {
        Map<String, List<Rule>> ruleByHead= getGrammar().stream().collect(Collectors.groupingBy(Rule::getHead, Collectors.toList()));
        ruleByHead.forEach((head, rules) -> {
//            logger.info("Group by rule Head: {}", head);
            List<Rule> ruleStream = new ArrayList<>(rules);
            rules.forEach(r -> {
                if(r.getProbability() == 0) {
                    r.setProbability((double) ruleStream.stream().filter(r2 -> r2.equals(r)).count()/rules.size());
//                    logger.info("Rule head: {}; rule body: {}; Prob: {}", r.getHead(), r.getBody().toArray(), r.getProbability());
                }
            });
        });
        Set<Rule> ruleSet = new HashSet<>();
        ruleByHead.values().stream().forEach(ruleSet::addAll);

        return ruleSet;
    }
    public Rule buildRuleFromString(String ruleString) {
        String[] args = ruleString.split("->");
        String[] body = args[1].split(",");
        return new Rule(args[0], Arrays.asList(body));
    }
    public int countDistinctWords(File pathToSentenceDir) {
        for (File sentenceFile : pathToSentenceDir.listFiles()) {
            if (sentenceFile.isFile()) {
                countWordsInSentence(sentenceFile.toPath().toString());
            }
        }
        final Set<String> distinctWords = new HashSet<>();
        distinctWords.addAll(totalWords);
        return distinctWords.size();
    }

    public void countWordsInSentence(String pathToSentence) {

        try
        {
            FileReader fr = new FileReader(pathToSentence);

            BufferedReader br = new BufferedReader(fr);
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                totalWords.addAll(Arrays.asList(sCurrentLine.split(" ")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Rule buildTestRule(String head, List<String> body) {
        return new Rule(head, body);
    }

//    public static void main(String[] args) throws Exception {
//        CNFGrammarParser ruleParser = new CNFGrammarParser();
//        PCKYParser pckyParser = new PCKYParser();
//        if(args.length != 2) throw new Exception("Invalid argument");
//        if(Objects.equals(args[0], "-f")) {
//            ruleParser.readSentenceFile(args[1]);
//        }
//        ruleParser.readGrammarDir(new File("./CNFgrammar"));
//        Set<Rule> ruleSet = ruleParser.getRulesWithProbabilities();
//        Object[] ruleArr =  ruleSet.toArray();
//        String[][] CNFgrammar = new String[ruleSet.size()][2];
//
////        for(Rule rule : ruleSet) {
////            logger.info("Rule head: {}; rule body: {}; Prob: {}", rule.getHead(), rule.getBody().toArray(), rule.getProbability());
////
////        };
//
//        for(int i = 0 ; i < ruleSet.size(); i++) {
//            DecimalFormat df = new DecimalFormat("#.#####");
//            Rule rule = (Rule) ruleArr[i];
//            CNFgrammar[i][0] = rule.toString();
//            CNFgrammar[i][1] = String.valueOf(df.format(rule.getProbability()));
//        }
//
////        String sentence = "Nam quen Lan ở thư_viện";
//        pckyParser.parseCKY(ruleParser.getSentence(), ruleSet);
//        System.out.println("Số từ khác nhau: " + ruleParser.countDistinctWords(new File("./sentence")));
//        TextTable grammarTable = new TextTable(new String[]{"CNF grammar", "P"}, CNFgrammar);
//        grammarTable.printTable();
//        TextTable tt = new TextTable( ruleParser.getSentence().split(" "), pckyParser.getTable());
//        tt.printTable();
//        pckyParser.buildTree();
//    }

    public void readSentence(String text) {

        sentence = text;
    }
}

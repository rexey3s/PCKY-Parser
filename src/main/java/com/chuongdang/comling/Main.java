package com.chuongdang.comling;

import dnl.utils.text.table.TextTable;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * @author Chuong Dang on 5/6/2016.
 */
public class Main extends Application {
    private TableView table = new TableView();
    private TableView<String[]> cellTable = new TableView<>();
    final ObservableList<VRule> ruleData = FXCollections.observableArrayList();
    ObservableList<String[]> cellData = FXCollections.observableArrayList();

    CNFGrammarParser ruleParser = new CNFGrammarParser();
    PCKYParser pckyParser = new PCKYParser();
    Set<Rule> ruleSet;


    public static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.setProperty("file.encoding", "utf-8");
        Scene scene = new Scene(new Group());
        stage.setTitle("View");
        stage.setWidth(1280);
        stage.setHeight(720);
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(10);

        final TextArea treeDisplay = new TextArea();
        treeDisplay.setPrefColumnCount(150);
        treeDisplay.setWrapText(true);
        treeDisplay.setPrefWidth(800);

        Console console= new Console(treeDisplay);
        PrintStream ps = new PrintStream(console, true);
        System.setOut(ps);
        System.setErr(ps);
//Defining the Name text field
        final TextField name = new TextField();
        name.setPromptText("Enter your sentence.");
        name.setPrefColumnCount(30);
        name.getText();
        GridPane.setConstraints(name, 0, 0);
        grid.getChildren().add(name);

//Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

        final Label label = new Label("Rule table");
        label.setFont(new Font("Arial", 20));
        TableColumn firstNameCol = new TableColumn("CNF grammar");
        TableColumn lastNameCol = new TableColumn("Xác suất");
        table.getColumns().addAll(firstNameCol, lastNameCol);
        try {
//            ruleParser.readSentence(name.getText());
            ruleParser.readGrammarDir(new File("./CNFgrammar"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        ruleSet = ruleParser.getRulesWithProbabilities();
        Object[] ruleArr =  ruleSet.toArray();
        for(int i = 0 ; i < ruleSet.size(); i++) {
            DecimalFormat df = new DecimalFormat("#.#####");
            Rule rule = (Rule) ruleArr[i];
            ruleData.add(new VRule(rule.toString(),String.valueOf(df.format(rule.getProbability()))));

        }

//        String sentence = "Nam quen Lan ở thư_viện";
//            pckyParser.parseCKY(ruleParser.getSentence(), ruleSet);
//            System.out.println("Số từ khác nhau: " + ruleParser.countDistinctWords(new File("./sentence")));
//            TextTable grammarTable = new TextTable(new String[]{"CNF grammar", "P"}, CNFgrammar);
//            grammarTable.printTable();
//            TextTable tt = new TextTable( ruleParser.getSentence().split(" "), pckyParser.getTable());
//            tt.printTable();
//            pckyParser.buildTree();


        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<VRule, String>("ruleStr"));
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<VRule, String>("prob"));
        table.setItems(ruleData);
        table.setMinWidth(1000);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(grid, label, table);
        final HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.getChildren().addAll(vbox, treeDisplay);

//        ((Group) scene.getRoot()).getChildren().add(grid);
        ((Group) scene.getRoot()).getChildren().add(hBox);

        stage.setScene(scene);
        stage.show();

        submit.setOnAction(e -> {
            ruleParser.readSentence(name.getText());
            pckyParser.parseCKY(ruleParser.getSentence(), ruleSet);
            cellData.clear();
            treeDisplay.clear();
            vbox.getChildren().remove(cellTable);
            cellTable.getColumns().clear();
            cellData.addAll(Arrays.asList(pckyParser.getStringTable()));
            String[] words = ruleParser.getSentence().split(" ");
            for (int i = 0; i < words.length; i++) {
                TableColumn tc = new TableColumn(words[i]);
                final int colNo = i;
                tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                        return new SimpleStringProperty((p.getValue()[colNo]));
                    }
                });
                tc.setPrefWidth(90);
                cellTable.getColumns().add(tc);

            }

            cellTable.setItems(cellData);
            cellTable.setMinWidth(1000);
            vbox.getChildren().add(cellTable);
            try {
                pckyParser.buildTree2(ps);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });




    }
}

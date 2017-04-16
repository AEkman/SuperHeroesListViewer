package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

public class Controller {

    // Instance variables
    private DatabaseConnection databaseConnection;

    @FXML
    private MenuItem menuSave;

    @FXML
    private MenuItem menuLoad;

    @FXML
    private MenuItem menuExit;

    @FXML
    private MenuItem menuAbout;

        @FXML
        private Button buttonFindTallest;

    @FXML
    private Button buttonFindOldest;

    @FXML
    private Button buttonFindHeaviest;

    @FXML
    private Button buttonSortByFirstName;

    @FXML
    private Button buttonAddHero;

    @FXML
    private TableView<Hero> mainTable;
    public ObservableList<Hero> mainTableData = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Hero, String> firstNameColumn;

    @FXML
    private TableColumn<Hero, String> lastNameColumn;

    @FXML
    private TableColumn<Hero, String> heroNameColumn;

    @FXML
    private TableColumn<Hero, Integer> heightColumn;

    @FXML
    private TableColumn<Hero, Integer> weightColumn;

    @FXML
    private TableColumn<Hero, Integer> ageColumn;

    @FXML
    private TableColumn<Hero, String> uncommonPowersColumn;

    @FXML
    private TextArea searchTextArea;

    @FXML
    private TextArea outputTextArea;

    // Contructor to load sampledata
    public Controller() {
        mainTableData.add(new Hero("Andreas", "Ekman", "Ironman", 192, 82, 36, "Hacker, Awesome chef"));
        mainTableData.add(new Hero("Peter", "Parker", "Spiderman", 178, 74, 28, "Web Creation, Substance Secretion"));
        mainTableData.add(new Hero("Clark", "Kent", "Superman", 191, 101, 34, "Super Breath, Hypnokinesis"));
    }

    // Initiate controller class. Runs after fxml file is loaded.
    @FXML
    private void initialize() {

        //Setting data to right column "cellvalue"
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Hero, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Hero, String>("lastName"));
        heroNameColumn.setCellValueFactory(new PropertyValueFactory<Hero, String>("heroName"));
        heightColumn.setCellValueFactory(new PropertyValueFactory<Hero, Integer>("height"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<Hero, Integer>("weight"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<Hero, Integer>("age"));
        uncommonPowersColumn.setCellValueFactory(new PropertyValueFactory<Hero, String>("uncommonPowers"));

        // Reset table if old data exists
        mainTable.setItems(null);

        // Get herodata to TableView "MainTable"
        mainTable.setItems(mainTableData);

        // Menu Save function
        menuSave.setOnAction((event) -> {

            // Write to .CSV file
            Writer writer = null;
            try {
                File file = new File("herolistexported.csv");
                writer = new BufferedWriter(new FileWriter(file));
                for (Hero hero : mainTableData) {
                    String text = hero.getFirstName() + ";" +
                            hero.getLastName() + ";" +
                            hero.getHeroName() + ";" +
                            hero.getHeight() + ";" +
                            hero.getWeight() + ";" +
                            hero.getAge() + ";" +
                            hero.getUncommonPowers() + "\n";

                    writer.write(text);
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                } finally {
                try {
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            outputTextArea.appendText("- Jarvis: Herolist created and exported as file with the name 'herolistexported.csv' \n");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Saved file");
            alert.setHeaderText("File has been saved!");

            alert.showAndWait();
        });

        // Connect to database @ localhost:3006
        databaseConnection = new DatabaseConnection();

        // Menu Load function
        menuLoad.setOnAction((event) -> {

            // Connect to database and getting all data from hero table
            Connection conn;
            try {
                conn = databaseConnection.Connect();
                System.out.println("- Hal: Fetching heroes data, please wait...");
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM hero");

                while(rs.next()) {
                    mainTableData.add(new Hero(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getInt(5),
                            rs.getInt(6),
                            rs.getInt(7),
                            rs.getString(8)
                    ));
                }

                outputTextArea.appendText("- Jarvis: All Heroes from database loaded successfully!\n");
                rs.close();
                conn.close();
                System.out.println("- Hal: Connection to database closed successfully...");
            } catch (SQLException e) {
                System.err.println("- Jarvis: Loading Heroes from database failed");
            }

            mainTable.setItems(mainTableData);
        });

        // Menu Exit function
        menuExit.setOnAction((event) -> {
            Platform.exit();
        });

        // Menu About function
        menuAbout.setOnAction((event) -> {
            Image image = new Image(getClass().getResource("../pics/about_slap.jpg").toExternalForm());
            ImageView aboutImageSlap = new ImageView(image);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("Thank you for using Super Hero List Viewer");
            alert.setGraphic(aboutImageSlap);
            alert.setContentText("This application was created by:\nAndreas Ekman 2017");

            alert.showAndWait();
        });

        // Button FindTallest function
        buttonFindTallest.setOnAction((event) -> {
            SortedList<Hero> sortedByHeight = new SortedList<Hero>(mainTableData, (Hero height1, Hero height2) -> {
                if (height1.getHeight() < height2.getHeight()) {
                    return 1;
                } else if (height1.getHeight() > height2.getHeight()){
                    return -1;
                } else {
                    return 0;
                }
            });

            mainTable.setItems(sortedByHeight);

            String tallestHeroFirstName = firstNameColumn.getCellData(0);
            String tallestHeroLastName = lastNameColumn.getCellData(0);
            String tallestHeroNickname = heroNameColumn.getCellData(0);
            int tallestHero = heightColumn.getCellData(0);
            outputTextArea.appendText("- Jarvis: The tallest Hero seems to be: " + tallestHeroFirstName + " " + tallestHeroLastName + " AKA " + tallestHeroNickname + " at height of " + tallestHero + " centimeters.\n");
        });

        // Button FindOldest function
        buttonFindOldest.setOnAction((event) -> {
            SortedList<Hero> sortedByAge = new SortedList<Hero>(mainTableData, (Hero age1, Hero age2) -> {
                if (age1.getAge() < age2.getAge()) {
                    return 1;
                } else if (age1.getAge() > age2.getAge()){
                    return -1;
                } else {
                    return 0;
                }
            });

            mainTable.setItems(sortedByAge);

            String oldestHeroFirstName = firstNameColumn.getCellData(0);
            String oldestHeroLastName = lastNameColumn.getCellData(0);
            String oldestHeroNickname = heroNameColumn.getCellData(0);
            int oldestHeroAge = ageColumn.getCellData(0);
            outputTextArea.appendText("- Jarvis: The oldest Hero I could find for you is: " + oldestHeroFirstName + " " + oldestHeroLastName + " AKA " + oldestHeroNickname + " at the age of " + oldestHeroAge + ".\n");
        });

        // Button FindHeaviest function
        buttonFindHeaviest.setOnAction((event) -> {
            SortedList<Hero> sortedByWeight = new SortedList<Hero>(mainTableData, (Hero weight1, Hero weight2) -> {
                if (weight1.getWeight() < weight2.getWeight()) {
                    return 1;
                } else if (weight1.getWeight() > weight2.getWeight()){
                    return -1;
                } else {
                    return 0;
                }
            });

            mainTable.setItems(sortedByWeight);

            String heaviestHeroFirstName = firstNameColumn.getCellData(0);
            String heaviestHeroLastName = lastNameColumn.getCellData(0);
            String heaviestHeroNickname = heroNameColumn.getCellData(0);
            int heaviestHeroWeight = weightColumn.getCellData(0);
            outputTextArea.appendText("- Jarvis: The heavies Hero I found is: " + heaviestHeroFirstName + " " + heaviestHeroLastName + " AKA " + heaviestHeroNickname + " at " + heaviestHeroWeight + " kg.\n");
        });

        // Button SortByName function
        buttonSortByFirstName.setOnAction((event) -> {
            Collections.sort(mainTableData, new Comparator<Hero>() {
                public int compare(Hero firstName1, Hero firstName2) {
                    return firstName1.getFirstName().compareTo(firstName2.getFirstName());
                }
            });

            mainTable.setItems(mainTableData);
            outputTextArea.appendText("- Jarvis: I sorted the list by first name as you asked.\n");
        });

        // Button AddHero function
        buttonAddHero.setOnAction(((ActionEvent event) -> {
            Alert addHeroDialog = new Alert(Alert.AlertType.CONFIRMATION);
            addHeroDialog.setTitle("Add new Hero");
            addHeroDialog.setHeaderText("Enter information for new Hero:");

            Label firstNameLabel = new Label("Enter first name:");
            Label lastNameLabel = new Label("Enter last name:");
            Label heroNameLabel = new Label("Enter Hero name:");
            Label heightLabel = new Label("Enter height:");
            Label weightLabel = new Label("Enter weight:");
            Label ageLabel = new Label("Enter age:");
            Label uncommonPowersLabel = new Label("Enter uncommon powers:");

            TextField textFieldFirstName = new TextField();
            TextField textFieldLastName = new TextField();
            TextField textFieldHeroName = new TextField();
            TextField textFieldHeight = new TextField();
            TextField textFieldWeight = new TextField();
            TextField textFieldAge = new TextField();
            TextField textFieldUncommonPowers = new TextField();

            ButtonType buttonAddHero = new ButtonType("Add Hero");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            GridPane grid = new GridPane();
            grid.add(firstNameLabel, 1,1);
            grid.add(textFieldFirstName, 2,1);

            grid.add(lastNameLabel, 1,2);
            grid.add(textFieldLastName, 2,2);

            grid.add(heroNameLabel, 1,3);
            grid.add(textFieldHeroName, 2,3);

            grid.add(heightLabel, 1,4);
            grid.add(textFieldHeight, 2,4);

            grid.add(weightLabel, 1,5);
            grid.add(textFieldWeight, 2,5);

            grid.add(ageLabel, 1,6);
            grid.add(textFieldAge, 2,6);

            grid.add(uncommonPowersLabel, 1,7);
            grid.add(textFieldUncommonPowers, 2,7);

            addHeroDialog.getDialogPane().setContent(grid);
            addHeroDialog.getButtonTypes().setAll(buttonAddHero, buttonTypeCancel);

            // Add hero dialog button functions
            Optional<ButtonType> result = addHeroDialog.showAndWait();
            if (result.get() == buttonAddHero){
                if (textFieldFirstName.getText() == null || textFieldLastName.getText().trim().isEmpty() ||
                        textFieldLastName.getText().trim().isEmpty() ||
                        textFieldLastName.getText().trim().isEmpty() ||
                        textFieldHeroName.getText().trim().isEmpty() ||
                        textFieldHeight.getText().trim().isEmpty() ||
                        textFieldWeight.getText().trim().isEmpty() ||
                        textFieldAge.getText().trim().isEmpty() ||
                        textFieldUncommonPowers.getText().trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("All fields must be filled");
                    alert.setContentText("Try again!");

                    alert.showAndWait();
                } else {
                    mainTableData.add(new Hero(textFieldFirstName.getText(),
                            textFieldLastName.getText(),
                            textFieldHeroName.getText(),
                            Integer.parseInt(textFieldHeight.getText()),
                            Integer.parseInt(textFieldWeight.getText()),
                            Integer.parseInt(textFieldAge.getText()),
                            textFieldUncommonPowers.getText()));
                }
            } else if (result.get() == buttonTypeCancel) {
                outputTextArea.appendText("- Jarvis: I cancelled the Hero creation tool for you master...\n");
            }
        }));

        // TableView "mainTable" selection changes
        mainTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // outputTextArea.appendText("ListView Selection Changed (selected: " + newValue.toString() + ")\n");
        });

        // Search function - SearchTextField "filter" by search string
        searchTextArea.textProperty().addListener((observable, oldValue, newValue) -> {

            // Wrapping observable list in a FilteredList
            FilteredList<Hero> filteredData = new FilteredList<>(mainTableData, p-> true);
                filteredData.setPredicate(hero -> {

                    if(newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if(hero.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (hero.getLastName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if (hero.getHeroName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if (String.valueOf(hero.getHeight()).contains(lowerCaseFilter)){
                        return true;
                    } else if (String.valueOf(hero.getWeight()).contains(lowerCaseFilter)){
                        return true;
                    } else if (String.valueOf(hero.getAge()).contains(lowerCaseFilter)){
                        return true;
                    } else if (hero.getUncommonPowers().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                    return false;
            });

            // Wrapping filtered data to SortedList
            SortedList<Hero> sortedData = new SortedList<>(filteredData);

            // Binding sorted list to TableView comparator
            sortedData.comparatorProperty().bind(mainTable.comparatorProperty());

            // Adding sorted and filtered data to table
            mainTable.setItems(sortedData);
        });
    }
}
package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class is for creating new heroes
 *
 * Created by Andreas on 2017-03-17.
 */

public class Hero {

    // Instance variables
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty heroName;
    private final IntegerProperty height;
    private final IntegerProperty weight;
    private final IntegerProperty age;
    private final StringProperty uncommonPowers;

    // Constructor
    public Hero(String firstName, String lastName, String heroName, int height, int weight, int age, String uncommonPowers) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.heroName = new SimpleStringProperty(heroName);
        this.height = new SimpleIntegerProperty(height);
        this.weight = new SimpleIntegerProperty(weight);
        this.age = new SimpleIntegerProperty(age);
        this.uncommonPowers = new SimpleStringProperty(uncommonPowers);
    }

    // Getters
    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getHeroName() {
        return heroName.get();
    }

    public int getHeight() {
        return height.get();
    }

    public int getWeight() {
        return weight.get();
    }

    public int getAge() {
        return age.get();
    }

    public String getUncommonPowers() {
        return uncommonPowers.get();
    }

    // Setters
    public void setFirstName(String value) {
        firstName.set(value);
    }

    public void setLastName(String value) {
        lastName.set(value);
    }

    public void setHeroName(String value) {
        heroName.set(value);
    }

    public void setWeight(int value) {
        weight.set(value);
    }

    public void setHeight(int value) {
        height.set(value);
    }

    public void setAge(int value) {
        age.set(value);
    }

    public void setUncommonPowers(String value) {
        uncommonPowers.set(value);
    }

    // Property Values
//    public StringProperty firstNameProperty() {
//        return firstName;
//    }
//
//    public StringProperty lastNameProperty() {
//        return lastName;
//    }
//
//    public StringProperty heroNameProperty() {
//        return heroName;
//    }
//
//    public IntegerProperty heightProperty() {
//        return height;
//    }
//
//    public IntegerProperty weightProperty() {
//        return weight;
//    }
//
//    public IntegerProperty ageProperty() {
//        return age;
//    }
//
//    public StringProperty uncommonPowersProperty() {
//        return uncommonPowers;
//    }
}
package models;

public class Player {
    private String name;

    private int age;

    private Address address;

    private int score;

    public Player(String name, int age, Address address, int score) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.score = score;
    }

    public Player(String name) {
        this.name = name;
    }

    public Player(Address address) {
        this.address = address;
    }

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Address getAddress() {
        return address;
    }

    public int getScore() {
        return score;
    }

}

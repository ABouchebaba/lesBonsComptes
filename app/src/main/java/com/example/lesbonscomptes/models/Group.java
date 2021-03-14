package com.example.lesbonscomptes.models;

public class Group {

    private final static String tableName = "groups";

    private Long id;
    private String name;

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String createQuery(){
        return
                "CREATE TABLE "+ tableName + " ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        "name TEXT NOT NULL)";
    }

    public static String dropQuery(){
        return "DROP TABLE IF EXISTS " + tableName;
    }
}

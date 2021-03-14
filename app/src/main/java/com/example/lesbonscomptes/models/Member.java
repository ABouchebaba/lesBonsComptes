package com.example.lesbonscomptes.models;

public class Member {

    private final static String tableName = "members";

    private Long id;
    private String name;
    private String phone;

    public Member(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Member() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static String createQuery(){
        return
                "CREATE TABLE "+ tableName + " ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        "name TEXT NOT NULL," +
                        "phone TEXT NOT NULL," +
                        "group_id INTEGER NOT NULL," +
                        "FOREIGN KEY(group_id) references groups(id));";
    }

    public static String dropQuery(){
        return "DROP TABLE IF EXISTS " + tableName;
    }
}

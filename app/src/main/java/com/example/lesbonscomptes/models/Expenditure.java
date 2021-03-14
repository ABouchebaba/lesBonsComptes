package com.example.lesbonscomptes.models;

import java.util.Date;

public class Expenditure {

    private final static String tableName = "expenditures";

    private Long id;
    private float cost;
    private Date date;
    private Long payerId;

    public Expenditure(Long id, float cost, Date date, Long payerId) {
        this.id = id;
        this.cost = cost;
        this.date = date;
        this.payerId = payerId;
    }

    public Expenditure() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public static String createQuery(){
        return
                "CREATE TABLE "+ tableName + " ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "cost REAL NOT NULL," +
                    "payer_id" + " INTEGER NOT NULL," +
                    "date TEXT NOT NULL," +
                    "FOREIGN KEY (payer_id) references members(id))";
    }

    public static String dropQuery(){
        return "DROP TABLE IF EXISTS " + tableName;
    }
}

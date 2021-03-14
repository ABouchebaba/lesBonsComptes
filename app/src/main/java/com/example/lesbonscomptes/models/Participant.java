package com.example.lesbonscomptes.models;

public class Participant {

    private final static String tableName = "participants";

    private Long id;
    private Long expenditureId;
    private Long memberId;

    public Participant() {
    }

    public Participant(Long id, Long expenditureId, Long memberId) {
        this.id = id;
        this.expenditureId = expenditureId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExpenditureId() {
        return expenditureId;
    }

    public void setExpenditureId(Long expenditureId) {
        this.expenditureId = expenditureId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public static String createQuery(){
        return
                "CREATE TABLE "+ tableName +" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "expenditure_id" + " INTEGER NOT NULL," +
                "member_id INTEGER NOT NULL)" +
                "FOREIGN KEY (expenditure_id) references expenditures(id)," +
                "FOREIGN KEY (member_id) references members(id)";
    }

    public static String dropQuery(){
        return "DROP TABLE IF EXISTS " + tableName;
    }
}

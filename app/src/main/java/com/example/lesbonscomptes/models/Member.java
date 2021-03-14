package com.example.lesbonscomptes.models;

public class Member {

    private Long id;
    private Long expenditureId;
    private Long contactId;

    public Member() {
    }

    public Member(Long id, Long expenditureId, Long contactId) {
        this.id = id;
        this.expenditureId = expenditureId;
        this.contactId = contactId;
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

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
}

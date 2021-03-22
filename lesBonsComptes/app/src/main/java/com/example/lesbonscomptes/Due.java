package com.example.lesbonscomptes;

import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Member;

public class Due {

    private Member member;
    private float totalDue;

    public Due(DbHelper db, long idMember, float dueAmount){
        this.member  = Member.find(db,idMember);
        this.totalDue = dueAmount;
    }

    public Member getMember(){return member;}
    public float getTotalDue(){return totalDue;}

    public void increaseDue(float increaseAmount){
        this.totalDue = this.totalDue + increaseAmount;
    }
    public void decreaseDue(float decreaseAmount){
        this.totalDue = this.totalDue - decreaseAmount;
    }
}
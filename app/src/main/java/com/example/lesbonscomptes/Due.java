package com.example.lesbonscomptes;

import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Member;

public class Due {

    private static DbHelper db;
    private Member member;
    private int totalDue;

    public Due(long idMember){
        member = Member.find(db,idMember);
        totalDue = 0;
    }

    public Member getMember(){return member;}
    public int getTotalDue(){return totalDue;}
    public void addToDue(int aDue){ this.totalDue = this.totalDue + aDue;}
}
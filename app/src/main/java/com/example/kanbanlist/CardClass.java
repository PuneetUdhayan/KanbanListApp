package com.example.kanbanlist;

import androidx.annotation.NonNull;

public class CardClass {

    private String cardTitle;
    private String desciption;
    private String memberList;
    private String memberBoard;

    public CardClass() {
    }

    public CardClass(String cardTitle) {
        this.cardTitle = cardTitle;
        this.desciption="";
    }

    public CardClass(String cardTitle, String desciption, String memberList, String memberBoard) {
        this.cardTitle = cardTitle;
        this.desciption = desciption;
        this.memberList = memberList;
        this.memberBoard = memberBoard;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getMemberList() {
        return memberList;
    }

    public void setMemberList(String memberList) {
        this.memberList = memberList;
    }

    public String getMemberBoard() {
        return memberBoard;
    }

    public void setMemberBoard(String memberBoard) {
        this.memberBoard = memberBoard;
    }

    @NonNull
    @Override
    public String toString() {
        return cardTitle;
    }
}

package com.example.kanbanlist;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ListClass {

    private String listName;
    private ArrayList<CardClass> cardClassList;

    public ListClass() {
    }

    public ListClass(String listName) {
        this.listName = listName;
        cardClassList = new ArrayList<CardClass>();
    }

    public ListClass(String listName, ArrayList<CardClass> cardClassList) {
        this.listName = listName;
        this.cardClassList = cardClassList;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public ArrayList<CardClass> getCardClassList() {
        return cardClassList;
    }

    public void setCardClassList(ArrayList<CardClass> cardClassList) {
        this.cardClassList = cardClassList;
    }

    @NonNull
    @Override
    public String toString() {
        return listName;
    }
}

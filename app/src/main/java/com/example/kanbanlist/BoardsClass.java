package com.example.kanbanlist;

import java.util.ArrayList;

public class BoardsClass {

    private String boardName;
    private ArrayList<ListClass> listClasses;

    public BoardsClass() {
    }

    public BoardsClass(String boardName, ArrayList<ListClass> listClasses) {
        this.boardName = boardName;
        this.listClasses = listClasses;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public ArrayList<ListClass> getListClasses() {
        return listClasses;
    }

    public void setListClasses(ArrayList<ListClass> listClasses) {
        this.listClasses = listClasses;
    }

    @Override
    public String toString() {
        return boardName;
    }

}

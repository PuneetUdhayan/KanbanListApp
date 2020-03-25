package com.example.kanbanlist;

import java.util.ArrayList;

public class UserClass {
    private String username;
    private ArrayList<String> userBoards;

    public UserClass() {
    }

    public UserClass(String username, ArrayList<String> userBoards) {
        this.username = username;
        this.userBoards = userBoards;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getUserBoards() {
        return userBoards;
    }

    public void setUserBoards(ArrayList<String> userBoards) {
        this.userBoards = userBoards;
    }

    public void addBoard(String key){
        userBoards.add(key);
    }

}

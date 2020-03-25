package com.example.kanbanlist;

import android.content.Context;
import android.view.View;

import com.allyants.boardview.SimpleBoardAdapter;

import java.util.ArrayList;

public class CustomBoardAdapter extends SimpleBoardAdapter {

    public CustomBoardAdapter(Context context, ArrayList<SimpleColumn> data) {
        super(context, data);
    }

    @Override
    public Object createFooterObject(int column_position) {
        return "Add Card";
    }
}

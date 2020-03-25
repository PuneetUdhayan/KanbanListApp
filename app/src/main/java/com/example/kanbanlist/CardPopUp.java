package com.example.kanbanlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CardPopUp extends AppCompatActivity {

    private TextView cardNameDisplay;
    private TextView boardNameDisplay;
    private FirebaseDatabase database;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_pop_up);

        database=FirebaseDatabase.getInstance();
        mRef=database.getReference();

        cardNameDisplay=findViewById(R.id.card_name);
        cardNameDisplay.setText(ConstantsClass.card.getCardTitle());
        boardNameDisplay=findViewById(R.id.board_name);
        boardNameDisplay.setText(ConstantsClass.board.getBoardName());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,BoardActivity.class));
    }

    public void cardBack(View view){
        startActivity(new Intent(this,BoardActivity.class));
    }

    public void deleteCard(View view){
        ConstantsClass.board.getListClasses().
                get(ConstantsClass.cardColumnIndex).getCardClassList().remove(ConstantsClass.card);
        mRef.child("Boards").child(ConstantsClass.boardKey).setValue(ConstantsClass.board);
        startActivity(new Intent(this,BoardActivity.class));
    }
}

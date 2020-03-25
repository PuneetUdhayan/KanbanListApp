package com.example.kanbanlist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.boardview.BoardAdapter;
import com.allyants.boardview.BoardView;
import com.allyants.boardview.Item;
import com.allyants.boardview.SimpleBoardAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {

    private TextView boardName;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private BoardView boardView;
    private ArrayList<SimpleBoardAdapter.SimpleColumn> data;
    private CustomBoardAdapter boardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        database = FirebaseDatabase.getInstance();
        mRef=database.getReference();

        boardName=findViewById(R.id.main_board_name);
        boardName.setText(ConstantsClass.board.getBoardName());

        if(ConstantsClass.board.getListClasses()==null){
            ArrayList<ListClass> temp=new ArrayList<>();
            ConstantsClass.board.setListClasses(temp);
        }


        boardView =findViewById(R.id.board_view);
        boardView.SetColumnSnap(true);
        data= new ArrayList<>();

        ArrayList<Item> empty = new ArrayList<>();

        for(int i=0;i<ConstantsClass.board.getListClasses().size();i++){
            ArrayList<ListClass> ListOfLists=ConstantsClass.board.getListClasses();
            if(ListOfLists.get(i).getCardClassList()==null){
                data.add(new SimpleBoardAdapter.SimpleColumn(ListOfLists.get(i).getListName(),(ArrayList)empty));
            }
            else{
                data.add(new SimpleBoardAdapter.SimpleColumn(ListOfLists.get(i).getListName(),(ArrayList)ListOfLists.get(i).getCardClassList()));
            }
        }

        boardAdapter= new CustomBoardAdapter(this,data);
        boardView.setAdapter(boardAdapter);
        boardView.setOnDoneListener(new BoardView.DoneListener() {
            @Override
            public void onDone() {
                Log.e("scroll","done");
            }
        });

        boardView.setOnItemClickListener(new BoardView.ItemClickListener() {
            @Override
            public void onClick(View view, int i, int i1) {
                Toast.makeText(getApplication(),"Column pos: "+i+" Item pos: "+i1,Toast.LENGTH_SHORT).show();
                ConstantsClass.card=ConstantsClass.board.getListClasses().get(i).getCardClassList().get(i1);
                ConstantsClass.cardColumnIndex=i;
                startActivity(new Intent(BoardActivity.this,CardPopUp.class));
            }
        });

        boardView.setOnFooterClickListener(new BoardView.FooterClickListener() {
            @Override
            public void onClick(View view, int i) {
                //illie indha
                final int index=i;
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(BoardActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_new_board,null);
                final EditText newBoard=mView.findViewById(R.id.new_board_name);
                Button newBoardButton=mView.findViewById(R.id.new_board_button);

                mBuilder.setView(mView);
                final AlertDialog dialog=mBuilder.create();

                newBoardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newBoard.getText().toString().isEmpty()){
                            Toast.makeText(BoardActivity.this,"Please enter a name",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            ArrayList<CardClass> newList;
                            String cardTitle=newBoard.getText().toString();
                            if(ConstantsClass.board.getListClasses().get(index).getCardClassList()==null){
                                newList=new ArrayList<>();
                                newList.add(new CardClass(cardTitle));
                            }
                            else {
                                newList = ConstantsClass.board.getListClasses().get(index).getCardClassList();
                                newList.add(new CardClass(cardTitle));
                            }
                            ConstantsClass.board.getListClasses().get(index).setCardClassList(newList);
                            data.set(index,new SimpleBoardAdapter.SimpleColumn(ConstantsClass.board.getListClasses().get(index).getListName(),(ArrayList)newList));

                            mRef.child("Boards").child(ConstantsClass.boardKey).setValue(ConstantsClass.board);

                            boardAdapter= new CustomBoardAdapter(BoardActivity.this,data);
                            boardView.setAdapter(boardAdapter);
                            Toast.makeText(getApplication(), "Card added", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
                //illie thanka

            }
        });

        boardView.setOnHeaderClickListener(new BoardView.HeaderClickListener() {
            @Override
            public void onClick(View view, int i) {
                final int index=i;
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);

                // Set a title for alert dialog
                builder.setTitle("Delete board");

                // Ask the final question
                builder.setMessage("Do you want to delete this board?");

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ConstantsClass.board.getListClasses().remove(index);
                        mRef.child("Boards").child(ConstantsClass.boardKey).setValue(ConstantsClass.board);

                        data.remove(index);

                        boardAdapter= new CustomBoardAdapter(BoardActivity.this,data);
                        boardView.setAdapter(boardAdapter);

                        Toast.makeText(BoardActivity.this,"Column deleted",Toast.LENGTH_SHORT).show();
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked

                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
                Toast.makeText(BoardActivity.this,i+"index",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void returnToHomePB(View view){
        startActivity(new Intent(this,HomeActivity.class));
    }

    public void addColumn(View view){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_new_column,null);
        final EditText newColumn=mView.findViewById(R.id.new_column_name);
        Button newColumnButton=mView.findViewById(R.id.new_column_button);

        mBuilder.setView(mView);
        final AlertDialog dialog=mBuilder.create();

        newColumnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newColumn.getText().toString().isEmpty()){
                    Toast.makeText(BoardActivity.this,"Please enter a name",Toast.LENGTH_SHORT).show();
                }
                else{
                    data.add(new SimpleBoardAdapter.SimpleColumn(newColumn.getText().toString(),(ArrayList)new ArrayList<>()));
                    ConstantsClass.board.getListClasses().add(new ListClass(newColumn.getText().toString()));

                    mRef.child("Boards").child(ConstantsClass.boardKey).setValue(ConstantsClass.board);

                    boardAdapter= new CustomBoardAdapter(BoardActivity.this,data);
                    boardView.setAdapter(boardAdapter);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

}

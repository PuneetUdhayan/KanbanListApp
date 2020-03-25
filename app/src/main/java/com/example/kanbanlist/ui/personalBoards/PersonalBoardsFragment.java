package com.example.kanbanlist.ui.personalBoards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.kanbanlist.BoardActivity;
import com.example.kanbanlist.BoardsClass;
import com.example.kanbanlist.ConstantsClass;
import com.example.kanbanlist.ListClass;
import com.example.kanbanlist.R;
import com.example.kanbanlist.UserClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PersonalBoardsFragment extends Fragment {

    private Button addBoardButton;
    private ArrayList<BoardsClass> boardArray;
    private ArrayList<String> boardKeyArray;
    private UserClass userData;
    private ArrayAdapter<BoardsClass> adapter;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private ListView listView;
    //private DatabaseReference myRef;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_personal_boards, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        boardArray = new ArrayList<>();
        boardKeyArray=new ArrayList<>();
        addBoardButton=root.findViewById(R.id.addCardButton);
        addBoardButton.setVisibility(View.GONE);
        listView = root.findViewById(R.id.personal_list_view);
        progressBar=root.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference myRef = database.getReference().child("Users").child(mAuth.getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData=dataSnapshot.getValue(UserClass.class);
                setBoards();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //ArrayList<String> temp=new ArrayList<>();
        //temp.add("-M3BGyLihdoRS-8Z7jY4");
        //String key=myRef.push().getKey();
        //myRef.child("Users").child(mAuth.getUid()).setValue(new UserClass("Puneet",temp));

        return root;
    }

    private void setBoards() {
        //Reading boards data from the database
        DatabaseReference myRefBoards = database.getReference().child("Boards");
        myRefBoards.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boardArray.clear();
                if(userData.getUserBoards()==null){
                    progressBar.setVisibility(View.GONE);
                    addBoardButton.setVisibility(View.VISIBLE);
                    userData.setUserBoards(new ArrayList<String>());
                    Toast.makeText(getActivity(),"No boards",Toast.LENGTH_SHORT).show();
                }
                else {
                    for (DataSnapshot boards : dataSnapshot.getChildren()) {
                        if (userData.getUserBoards().contains(boards.getKey())) {
                            boardArray.add(boards.getValue(BoardsClass.class));
                            boardKeyArray.add(boards.getKey());
                        }
                    }
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setAdapter() {
        adapter = new ArrayAdapter<BoardsClass>(getActivity(), android.R.layout.simple_list_item_1, boardArray);
        listView.setLongClickable(true);
        progressBar.setVisibility(View.GONE);
        addBoardButton.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConstantsClass.board = boardArray.get(position);
                ConstantsClass.boardKey=boardKeyArray.get(position);
                startActivity(new Intent(getActivity(), BoardActivity.class));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // Set a title for alert dialog
                builder.setTitle("Delete board");

                // Ask the final question
                builder.setMessage("Do you want to delete this board?");

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when user clicked the Yes button
                        // Set the TextView visibility GONE
                        boardArray.remove(position);
                        DatabaseReference temp=database.getReference();
                        temp.child("Boards").child(boardKeyArray.get(position)).removeValue();
                        boardKeyArray.remove(position);
                        Toast.makeText(getActivity(),"Board deleted",Toast.LENGTH_SHORT).show();
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getActivity(),
                                "No Button Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
                return true;
            }
        });

        addBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_new_board,null);
                final EditText newBoard=mView.findViewById(R.id.new_board_name);
                Button newBoardButton=mView.findViewById(R.id.new_board_button);

                mBuilder.setView(mView);
                final AlertDialog dialog=mBuilder.create();

                newBoardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newBoard.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(),"Please enter a name",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            boardArray.add(new BoardsClass(newBoard.getText().toString(),new ArrayList<ListClass>()));
                            DatabaseReference usertemp=database.getReference();
                            String key=usertemp.push().getKey();
                            boardKeyArray.add(key);
                            usertemp.child("Boards").child(key).setValue(new BoardsClass(newBoard.getText().toString(),new ArrayList<ListClass>()));
                            userData.addBoard(key);
                            usertemp.child("Users").child(mAuth.getUid()).setValue(userData);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(),"Board created",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

    }
}
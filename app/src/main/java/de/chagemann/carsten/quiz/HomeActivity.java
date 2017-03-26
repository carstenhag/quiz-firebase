package de.chagemann.carsten.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import de.chagemann.carsten.quiz.model.Player;
import de.chagemann.carsten.quiz.model.Question;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference dbref;
    DatabaseReference pendingQuestionsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        pendingQuestionsRef = FirebaseDatabase.getInstance().getReference();
        final String UID = mAuth.getCurrentUser().getUid();

        final Button buttonQuiz = (Button) findViewById(R.id.buttonQuiz);
        final Button buttonSubmitQuestions = (Button) findViewById(R.id.buttonSubmitQuestions);
        final Button buttonApproveQuestions = (Button) findViewById(R.id.buttonApproveQuestions);
        final Button buttonReinitialize = (Button) findViewById(R.id.buttonReinitialize);

        pendingQuestionsRef = pendingQuestionsRef.child("questions-pending-approval");

        // Listens only once
        ValueEventListener singleValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    buttonApproveQuestions.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                SnackbarHelper.showSnackbar(HomeActivity.this, databaseError.toString());
            }
        };
        // Listens on all child changes changes
        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("MY TAG", "Child Added");
                if(dataSnapshot.getChildrenCount() > 0) {
                    buttonApproveQuestions.setEnabled(true);
                } else {
                    buttonApproveQuestions.setEnabled(false);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // Enable/Disable toggle maybe also has to get added here,
                // depending how the children of "questions-pending-approval" get deleted
                // or if the entire node gets deleted
                Log.i("MY TAG", "Child Changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("MY TAG", "Child Removed");
                if(dataSnapshot.getChildrenCount()-1 > 0) {
                    buttonApproveQuestions.setEnabled(true);
                } else {
                    buttonApproveQuestions.setEnabled(false);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.i("MY TAG", "Child moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("MY TAG", "Cancelled");
            }
        };

        pendingQuestionsRef.addListenerForSingleValueEvent(singleValueEventListener);
        pendingQuestionsRef.addChildEventListener(childEventListener);


        buttonQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        buttonSubmitQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewQuestionActivity.class);
                startActivity(intent);
            }
        });

        buttonApproveQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ApproveQuestionsActivity.class);
                startActivity(intent);
            }
        });

        buttonReinitialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillDBwithValues(UID);
            }
        });

    }

    private void fillDBwithValues(String UID) {
        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.setValue(null);
        dbref.child("players").child(UID).setValue(new Player(UID));
        dbref.child("players").child("BSI3ws7CRWO4K5vrpvM5caspPtC3").setValue(new Player("BSI3ws7CRWO4K5vrpvM5caspPtC3"));

        Question questionDE1 = new Question("Question 1 DE", Arrays.asList("A 1", "A 2", "A 3", "A 4"), 10, "BSI3ws7CRWO4K5vrpvM5caspPtC3");
        Question questionDE2 = new Question("Question 2 DE", Arrays.asList("A 1", "A 2", "A 3", "A 4"), 10, UID);
        dbref.child("questions").child("DE").push().setValue(questionDE1);
        dbref.child("questions").child("DE").push().setValue(questionDE2);

        Question questionEN1 = new Question("Question 1 EN", Arrays.asList("A 1", "A 2", "A 3", "A 4"), 10, UID);
        Question questionEN2 = new Question("Question 2 EN", Arrays.asList("A 1", "A 2", "A 3", "A 4"), 10, "BSI3ws7CRWO4K5vrpvM5caspPtC3");
        dbref.child("questions").child("EN").push().setValue(questionEN1);
        dbref.child("questions").child("EN").push().setValue(questionEN2);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}

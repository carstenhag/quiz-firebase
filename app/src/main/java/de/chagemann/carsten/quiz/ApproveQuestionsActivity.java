package de.chagemann.carsten.quiz;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.chagemann.carsten.quiz.model.Question;

public class ApproveQuestionsActivity extends AppCompatActivity {

    DatabaseReference pendingApprovalRef;
    DataSnapshot childSnapshot;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_questions);

        final TextView textViewQuestion = (TextView) findViewById(R.id.textViewQuestionApproval);
        final TextView textViewRightAnswer = (TextView) findViewById(R.id.textViewRightAnswer);
        final TextView textViewWrongAnswer0 = (TextView) findViewById(R.id.textViewWrongAnswer0);
        final TextView textViewWrongAnswer1 = (TextView) findViewById(R.id.textViewWrongAnswer1);
        final TextView textViewWrongAnswer2 = (TextView) findViewById(R.id.textViewWrongAnswer2);

        final Button buttonRejectQuestion = (Button) findViewById(R.id.buttonRejectQuestion);
        final Button buttonApproveQuestion = (Button) findViewById(R.id.buttonApproveQuestion);

        pendingApprovalRef = FirebaseDatabase.getInstance().getReference().child("questions-pending-approval").child("DE");

        pendingApprovalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childSnapshot = dataSnapshot.getChildren().iterator().next();
                question = childSnapshot.getValue(Question.class);

                textViewQuestion.setText("Question: " + question.getQuestion());
                textViewRightAnswer.setText("Right Answer: " + question.getAnswers().get(0));
                textViewWrongAnswer0.setText("Wrong Answer: " + question.getAnswers().get(1));
                textViewWrongAnswer1.setText("Wrong Answer: " + question.getAnswers().get(2));
                textViewWrongAnswer2.setText("Wrong Answer: " + question.getAnswers().get(3));

                buttonApproveQuestion.setEnabled(true);
                buttonRejectQuestion.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonRejectQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SnackbarHelper.showSnackbar(ApproveQuestionsActivity.this, "Question has been rejected.");
                    }
                });
                buttonApproveQuestion.setEnabled(false);
                buttonRejectQuestion.setEnabled(false);
            }
        });


        buttonApproveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childSnapshot.getRef().removeValue();

                pendingApprovalRef.getRoot().child("questions").child("DE").push().setValue(question).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SnackbarHelper.showSnackbar(ApproveQuestionsActivity.this, "Question has been approved.");
                    }
                });
                buttonApproveQuestion.setEnabled(false);
                buttonRejectQuestion.setEnabled(false);
            }
        });


    }
}

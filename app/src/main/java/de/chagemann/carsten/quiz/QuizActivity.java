package de.chagemann.carsten.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Random;

import de.chagemann.carsten.quiz.model.Question;

public class QuizActivity extends AppCompatActivity {

    DatabaseReference questionsRef;
    DataSnapshot childSnapshot;
    Question question;

    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final TextView textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);

        final Button buttonAnswer0 = (Button) findViewById(R.id.buttonAnswer0);
        final Button buttonAnswer1 = (Button) findViewById(R.id.buttonAnswer1);
        final Button buttonAnswer2 = (Button) findViewById(R.id.buttonAnswer2);
        final Button buttonAnswer3 = (Button) findViewById(R.id.buttonAnswer3);

        random = new Random();
        questionsRef = FirebaseDatabase.getInstance().getReference().child("questions").child("DE");

        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int questionCount = (int) dataSnapshot.getChildrenCount();
                int rand = random.nextInt(questionCount);
                Iterator itr = dataSnapshot.getChildren().iterator();

                for(int i = 0; i < rand; i++) {
                    itr.next();
                }
                childSnapshot = (DataSnapshot) itr.next();
                question = childSnapshot.getValue(Question.class);

                textViewQuestion.setText("Question: " + question.getQuestion());
                buttonAnswer0.setText(question.getAnswers().get(0));
                buttonAnswer1.setText(question.getAnswers().get(1));
                buttonAnswer2.setText(question.getAnswers().get(2));
                buttonAnswer3.setText(question.getAnswers().get(3));
                buttonAnswer0.setEnabled(true);
                buttonAnswer1.setEnabled(true);
                buttonAnswer2.setEnabled(true);
                buttonAnswer3.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if(button.getText().toString().equals(question.getAnswers().get(0))) {
                    SnackbarHelper.showSnackbar(QuizActivity.this, "Yay, correct Answer!");
                } else {
                    SnackbarHelper.showSnackbar(QuizActivity.this, "Aww, try again!");
                }

            }
        };

        buttonAnswer0.setOnClickListener(onClickListener);
        buttonAnswer1.setOnClickListener(onClickListener);
        buttonAnswer2.setOnClickListener(onClickListener);
        buttonAnswer3.setOnClickListener(onClickListener);


    }
}

package de.chagemann.carsten.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Arrays;

import de.chagemann.carsten.quiz.model.Player;
import de.chagemann.carsten.quiz.model.Question;

public class NewQuestionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        mAuth = FirebaseAuth.getInstance();
        final String UID = mAuth.getCurrentUser().getUid();
        dbref = FirebaseDatabase.getInstance().getReference();

        if(!dbref.child("players").child(UID).getKey().equals(UID)) {
            dbref.child("players").child(UID).setValue(new Player(UID));
        }

        final EditText editTextQuestion = (EditText) findViewById(R.id.editTextQuestion);
        final EditText editTextRightAnswer = (EditText) findViewById(R.id.editTextRightAnswer);
        final EditText editTextWrongAnswer0 = (EditText) findViewById(R.id.editTextWrongAnswer0);
        final EditText editTextWrongAnswer1 = (EditText) findViewById(R.id.editTextWrongAnswer1);
        final EditText editTextWrongAnswer2 = (EditText) findViewById(R.id.editTextWrongAnswer2);

        final DiscreteSeekBar discreteSeekBar = (DiscreteSeekBar) findViewById(R.id.seekBarPoints);

        final Button buttonSubmitQuestion = (Button) findViewById(R.id.buttonSubmitQuestion);

        buttonSubmitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linesFilled(editTextQuestion, editTextRightAnswer, editTextWrongAnswer0, editTextWrongAnswer1, editTextWrongAnswer2)) {

                    Question question = new Question(getEditTextString(editTextQuestion),
                            Arrays
                            .asList(getEditTextString(editTextRightAnswer),
                                    getEditTextString(editTextWrongAnswer0),
                                    getEditTextString(editTextWrongAnswer1),
                                    getEditTextString(editTextWrongAnswer2)),
                            discreteSeekBar.getProgress(),
                            UID
                    );

                    dbref.child("questions-pending-approval").child("DE").push().setValue(question);

                    cleanLines(editTextQuestion, editTextRightAnswer, editTextWrongAnswer0, editTextWrongAnswer1, editTextWrongAnswer2);
                    SnackbarHelper.showSnackbar(NewQuestionActivity.this, "Submitted Question, thanks!");
                } else {
                    SnackbarHelper.showSnackbar(NewQuestionActivity.this, "Fill out every Line, please.");
                }
            }
        });

    }

    private boolean linesFilled(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if((getEditTextString(editText).equals("") || getEditTextString(editText).length() < 4)) {
                return false;
            }
        }
        return true;
    }

    private void cleanLines(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setText("");
        }
    }

    private String getEditTextString(EditText editText) {
        return editText.getText().toString().trim();
    }
}

























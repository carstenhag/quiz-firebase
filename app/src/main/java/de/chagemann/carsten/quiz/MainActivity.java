package de.chagemann.carsten.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "QuizApp";
    private static final int RC_SIGN_IN = 433;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    MainActivity.this.startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                    ))
                                    .setLogo(R.drawable.kl_alt)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    SnackbarHelper.showSnackbar(MainActivity.this, "Cancelled");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    SnackbarHelper.showSnackbar(MainActivity.this, "No internet connection");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    SnackbarHelper.showSnackbar(MainActivity.this, "Error, please try again");
                    return;
                }
            }
            SnackbarHelper.showSnackbar(MainActivity.this, "Error, please try again");
        }
    }

}

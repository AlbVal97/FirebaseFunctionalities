package it.unipd.sistemiembedded1819.firebaseauthentication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SIGN_IN_INTENT_CODE = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createSignInIntent() {

        //List of chosen authentication providers
        List<AuthUI.IdpConfig> signInProviders = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        //Create and launch the sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(signInProviders)
                        .setLogo(R.drawable.running)
                        .setTheme(R.style.AppTheme)
                        .build(),
                SIGN_IN_INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_INTENT_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                //Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, user + " successfully signed in!", Toast.LENGTH_SHORT).show();

                //Launch database activity to let the user access its data
                Intent intent = new Intent(this, UserData.class);
                intent.putExtra("userNameSurname", user.getDisplayName());
                intent.putExtra("userEmail", user.getEmail());
                startActivity(intent);
            }
            else {
                //If response is null, the user canceled the sign-in flow pressing the back button
                if (response == null) {
                    Toast.makeText(this, "Sign-in process calceled!", Toast.LENGTH_SHORT).show();
                }
                else {
                    int error = response.getError().getErrorCode();
                    Toast.makeText(this, "The following sign-in error occurred: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void launchDatabaseActivity(View view) {
        createSignInIntent();
    }
}

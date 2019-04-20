package it.unipd.sistemiembedded1819.firebaseauthentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase extends AppCompatActivity {

    private TextView mUserNameTextview;
    private TextView mUserEmailTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_database);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String userEmail = intent.getStringExtra("userEmail");

        mUserNameTextview = findViewById(R.id.user_name_textview);
        mUserEmailTextview = findViewById(R.id.user_email_textview);

        mUserNameTextview.setText(username);
        mUserEmailTextview.setText(userEmail);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, String> user = new HashMap<>();
        user.put("name", username);
        user.put("email", userEmail);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(
                                UserDatabase.this,
                                "User added to the database with the ID: " + documentReference.getId(),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                UserDatabase.this,
                                "An error occurred adding the user to the database!",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}

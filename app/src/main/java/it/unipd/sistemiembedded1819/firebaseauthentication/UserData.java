package it.unipd.sistemiembedded1819.firebaseauthentication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserData extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    private TextView mUserNameTextview;
    private TextView mUserEmailTextview;
    private EditText mNicknameEdittext;
    private EditText mBirthdateEdittext;
    private EditText mHeightEdittext;
    private EditText mWeightEdittext;
    private Button mConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        mBirthdateEdittext = findViewById(R.id.birthdate_edittext);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

                mBirthdateEdittext.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mBirthdateEdittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(UserData.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Retrieve information from login process and store into constants
        Intent intent = getIntent();
        final String userNameSurname = intent.getStringExtra("userNameSurname");
        final String userEmail = intent.getStringExtra("userEmail");

        //Compile user information (name + surname and email)
        mUserNameTextview = findViewById(R.id.user_name_textview);
        mUserEmailTextview = findViewById(R.id.user_email_textview);

        mUserNameTextview.setText(userNameSurname);
        mUserEmailTextview.setText(userEmail);

        mNicknameEdittext = findViewById(R.id.nickname_edittext);
        mHeightEdittext =  findViewById(R.id.height_edittext);
        mWeightEdittext = findViewById(R.id.weight_edittext);

        mConfirmButton = findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Create an instance of the database in order to load all user data (previous + new)
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // A user is a series of <key , value> couples
                Map<String, Object> user = new HashMap<>();
                user.put("nome", userNameSurname);
                user.put("email", userEmail);
                //user.put("nickname", mNicknameEdittext.getText().toString());
                //user.put("altezza", mHeightEdittext.getText());
                //user.put("peso", mWeightEdittext.getText());
                //user.put("data_di_nascita", mBirthdateEdittext.getText());

                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(
                                        UserData.this,
                                        "User added to the database with the ID: " + documentReference.getId(),
                                        Toast.LENGTH_SHORT)
                                        .show();

                                //Launch database activity that summarizes tha user data
                                Intent intent = new Intent(UserData.this, UserDataSummary.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(
                                        UserData.this,
                                        "An error occurred adding the user to the database!",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        });


    }
}

package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.user.User;
import com.example.assignment1.user.UserStore;
public class SignupActivity extends AppCompatActivity {

    EditText edtusername;
    EditText edtpassword;
    TextView error;
    Button signup;

    private SharedPreferences prefs;
    private static final String PREF_NAME = "user_prefs";
    private static final String USERS_KEY = "registered_users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtusername = findViewById(R.id.sedtUsername);
        edtpassword = findViewById(R.id.sedtpassword);
        signup = findViewById(R.id.btnSignupSubmit);
        error = findViewById(R.id.signuperror);

        prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void signupAction(View view) {
        String usernametext = edtusername.getText().toString().trim();
        String passwordtext = edtpassword.getText().toString().trim();

        if (usernametext.isEmpty() || passwordtext.isEmpty()) {
            error.setText("Username and password cannot be empty.");
            return;
        }

        // Load existing users
        String existingUsers = prefs.getString(USERS_KEY, "");
        String[] userArray = existingUsers.split(";");

        for (String userData : userArray) {
            if (userData.isEmpty()) continue;
            String[] parts = userData.split(":");
            if (parts.length == 2 && parts[0].equals(usernametext)) {
                error.setText(R.string.the_username_already_exist);
                edtusername.setText(null);
                edtpassword.setText(null);
                return;
            }
        }

        // Add new user to shared preferences
        String newUserEntry = usernametext + ":" + passwordtext;
        String updatedUsers = existingUsers + newUserEntry + ";";

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERS_KEY, updatedUsers);
        editor.apply();

        // Add user to in-memory store for immediate session use
        User newUser = new User(usernametext, passwordtext);
        UserStore.users.add(newUser);

        Toast.makeText(this, "Sign-up successful! Please log in.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

package com.example.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.user.User;

public class LoginActivity extends AppCompatActivity {

    private EditText edtusername;
    private EditText edtpassword;
    private Button signup;
    private CheckBox box;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtusername = findViewById(R.id.ledtUsername);
        edtpassword = findViewById(R.id.ledtPassword);
        signup = findViewById(R.id.btnsignlog);
        box = findViewById(R.id.checkbox_auto_login);

        prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);

        autoLoginIfNeeded();

        signup.setOnClickListener(e -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });

        // Note: No need to set login button listener here, handled via XML (android:onClick)
    }

    private void autoLoginIfNeeded() {
        boolean autoLogin = prefs.getBoolean("auto_login", false);
        if (autoLogin) {
            String savedUser = prefs.getString("logged_user", "");
            if (!savedUser.isEmpty()) {
                String[] parts = savedUser.split(":");
                if (parts.length == 2) {
                    edtusername.setText(parts[0]);
                    edtpassword.setText(parts[1]);
                }
            }
        }
    }

    public void loginAction(View view) {
        String username = edtusername.getText().toString().trim();
        String password = edtpassword.getText().toString().trim();

        String registeredUsers = prefs.getString("registered_users", "");
        String[] userArray = registeredUsers.split(";");

        User loggedInUser = null;

        for (String userData : userArray) {
            if (userData.isEmpty()) continue;
            String[] parts = userData.split(":");
            if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                loggedInUser = new User(parts[0], parts[1]);
                break;
            }
        }

        if (loggedInUser == null) {
            Toast.makeText(this, "Wrong username or password.", Toast.LENGTH_SHORT).show();
        } else {
            if (box.isChecked()) {
                prefs.edit()
                        .putBoolean("auto_login", true)
                        .putString("logged_user", username + ":" + password)
                        .apply();
            }

            Intent intent = new Intent(this, DashBoardActivity.class);
            intent.putExtra("currentUser", loggedInUser);
            startActivity(intent);
            finish();
        }
    }
}

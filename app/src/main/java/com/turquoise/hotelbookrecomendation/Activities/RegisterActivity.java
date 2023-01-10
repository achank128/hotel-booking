package com.turquoise.hotelbookrecomendation.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.api.ApiService;
import com.turquoise.hotelbookrecomendation.model.LoginInfo;
import com.turquoise.hotelbookrecomendation.model.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button register;
    private TextInputLayout username, password, email, fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        fullName = findViewById(R.id.fullName);
        register = findViewById(R.id.reBtn);

        register.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.reBtn) {
            if (getUsername().equals("") || getPassword().equals("")) {
                setError(username, "Enter valid username with length greater than 5 char");
                setError(password, "Enter valid password with length greater than 6 char");
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences(getUsername(), MODE_PRIVATE);
                SharedPreferences.Editor editor = getSharedPreferences(getUsername(), MODE_PRIVATE).edit();

                if (sharedPreferences.getString("active", "in").equals("in")) {
                    SharedPreferences.Editor edit = getSharedPreferences("cur", MODE_PRIVATE).edit();
                    edit.putString("user", getUsername());
                }

                User user = new User(getUsername(), getPassword(), getFullName(), getEmail());

                callApiRegister(user);

            }
        }
    }

    private void setError(@NonNull TextInputLayout username, String s) {
        username.setError(s);
    }


    private String getUsername() {
        return username.getEditText().getText().toString();
    }

    private String getPassword() {
        return password.getEditText().getText().toString();
    }

    private String getEmail() {
        return email.getEditText().getText().toString();
    }

    private String getFullName() {
        return fullName.getEditText().getText().toString();
    }

    private void callApiRegister(User user) {
        ApiService.apiService.register(user).enqueue(new Callback<LoginInfo>() {
            @Override
            public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                Toast.makeText(RegisterActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                User user = response.body().getUser();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);

                startActivity(intent);
            }

            @Override
            public void onFailure(Call<LoginInfo> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Register Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
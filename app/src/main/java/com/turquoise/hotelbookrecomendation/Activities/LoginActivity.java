package com.turquoise.hotelbookrecomendation.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.api.ApiService;
import com.turquoise.hotelbookrecomendation.model.LoginInfo;
import com.turquoise.hotelbookrecomendation.model.User;

import static com.turquoise.hotelbookrecomendation.Utils.Utilities.newActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login, register;
    private TextInputLayout username;
    private TextInputLayout password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginBtn);
        register = findViewById(R.id.reBtn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newActivity(LoginActivity.this, RegisterActivity.class);
            }
        });
        login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginBtn) {
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
                callApiLogin(getUsername(), getPassword());
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

    private void callApiLogin(String username, String password) {

        User userLogin = new User(username, password);

        ApiService.apiService.login(userLogin).enqueue(new Callback<LoginInfo>() {
            @Override
            public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                User user = response.body().getUser();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);

                startActivity(intent);
            }

            @Override
            public void onFailure(Call<LoginInfo> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.akg.gp.fcmdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements NetworkResponseListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_SIGNUP = 0;
    private FetchData fetchData;
    private TextInputEditText emailEditText;
    private TextInputEditText passEditText;
    private Button loginButton;
    private TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailEditText = (TextInputEditText) findViewById(R.id.input_email);
        passEditText = (TextInputEditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        registerTextView = (TextView) findViewById(R.id.link_register);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

/*
//        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
//                R.style.AppTheme);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();
*/

        String email = emailEditText.getText().toString();
        String password = passEditText.getText().toString();

        // TODO: Implement your own authentication logic here.
        fetchData = new FetchData(MainActivity.this, MainActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);

            fetchData.setData(jsonObject);
            fetchData.setType_of_request(Config.POST);
            fetchData.setUrl("http://akgecnotifier.000webhostapp.com/api/login_student.php");
            fetchData.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        String password = passEditText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("enter a valid email address");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passEditText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passEditText.setError(null);
        }

        return valid;
    }

    @Override
    public void preRequest() {

    }

    @Override
    public Object postRequest(Object object) {
        Log.d("Result", "Result" + object);
        if (object.toString().equals("success\n")) {
            Toast.makeText(MainActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
            Log.d("postobject","chal raha h kuch toh "+object.toString() );
            loginButton.setEnabled(true);
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(MainActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
            loginButton.setEnabled(true);
        }
        return null;
    }
}

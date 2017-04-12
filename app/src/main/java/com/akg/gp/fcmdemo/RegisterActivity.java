package com.akg.gp.fcmdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class RegisterActivity extends AppCompatActivity implements NetworkResponseListener {
    private FetchData fetchRegisterData;
    public static final String ROOT_URL = "http://akgecnotifier.000webhostapp.com/api/";

    private Button registerButton;
    private EditText firstNameEditText;
    private EditText middleNameEditText;
    private EditText lastNameEditText;
    private EditText rollNoEditText;
    private EditText contactNoEditText;
    private EditText sessionBeginEditText;
    private EditText sessionEndEditText;
    private EditText semesterEditText;
    private EditText sectionEditText;

    private TextView loginTextView;

    private EditText emailEditText;
    private EditText passwordEditText;

    private String rollNo;
    private String firstName;
    private String middleName;
    private String lastName;
    private String contactNo;
    private String email;
    private String password;
    private String sessionBegin;
    private String sessionEnd;
    private String semester;
    private String section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = (Button) findViewById(R.id.btn_register);

        firstNameEditText = (EditText) findViewById(R.id.input_first_name);
        middleNameEditText = (EditText) findViewById(R.id.input_middle_name);
        lastNameEditText = (EditText) findViewById(R.id.input_last_name);
        rollNoEditText = (EditText) findViewById(R.id.input_roll_no);
        contactNoEditText = (EditText) findViewById(R.id.input_contact_no);
        emailEditText = (EditText) findViewById(R.id.input_email);
        passwordEditText = (EditText) findViewById(R.id.input_password);
        sessionBeginEditText = (EditText) findViewById(R.id.input_session_begin);
        sessionEndEditText = (EditText) findViewById(R.id.input_session_end);
        semesterEditText = (EditText) findViewById(R.id.input_semester);
        sectionEditText = (EditText) findViewById(R.id.input_section);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollNo = rollNoEditText.getText().toString();
                firstName = firstNameEditText.getText().toString();
                middleName = middleNameEditText.getText().toString();
                lastName = lastNameEditText.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                contactNo = contactNoEditText.getText().toString();
                sessionBegin = sessionBeginEditText.getText().toString();
                sessionEnd = sessionEndEditText.getText().toString();
                semester = semesterEditText.getText().toString();
                section = sectionEditText.getText().toString();

                insertUser();
            }
        });
        loginTextView = (TextView) findViewById(R.id.link_login);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void insertUser() {
        //RegisterAsync async = new RegisterAsync();
        //async.execute();

        //Here we will handle the http request to insert user to mysql db using RegisterAPI
//        Retrofit adapter = new Retrofit.Builder()
//                .baseUrl(ROOT_URL) //Setting the Root URL
//                .addConverterFactory(new ToStringConverterFactory())
//                .build(); //Finally building the adapter
//
//        //Creating object for our interface
//        RegisterAPI api = adapter.create(RegisterAPI.class);
//
//        api.insertStudent(
//                rollNo,
//                null,
//                firstName,
//                middleName,
//                lastName,
//                password,
//                email,
//                contactNo,
//                2013,
//                2017, new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        Toast.makeText(RegisterActivity.this, response.body().toString(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
        fetchRegisterData = new FetchData(RegisterActivity.this, RegisterActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            String token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();
            Log.d("token", token);
            jsonObject.put("rollno", rollNo);
            jsonObject.put("token", token);
            jsonObject.put("firstname", firstName);
            jsonObject.put("middlename", middleName);
            jsonObject.put("lastname", lastName);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
            jsonObject.put("contactno", contactNo);
            jsonObject.put("sessionstart", sessionBegin);
            jsonObject.put("sessionend", sessionEnd);
            jsonObject.put("semester", semester);
            jsonObject.put("section", section);
            fetchRegisterData.setData(jsonObject);
            fetchRegisterData.setType_of_request(Config.POST);
            fetchRegisterData.setUrl("http://akgecnotifier.000webhostapp.com/api/insert_student.php");
            fetchRegisterData.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void preRequest() {

    }

    @Override
    public Object postRequest(Object object) {
        if (object.toString().equals("successfully registered")) {
            Toast.makeText(RegisterActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
            Log.e("object", object.toString());
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(RegisterActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
            Log.e("object", object.toString());
        }
        return null;
    }

    /*public class RegisterAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("indoinback", "aagye");
            try {
                Log.e("try", "aagye");
                URL url = new URL(ROOT_URL);
                String urlParameters = "rollno=13258450&token=null&firstname=ab&middlename=gh&lastname=jj&password=12345&email=abc2@hh.com&contactno=9965432109&sessionstart=2013&sessionend=2017";
                byte[] postData = urlParameters.getBytes();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
                connection.setUseCaches(false);
                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.write(postData);
                }
                connection.connect();

                if (connection.getResponseCode()==200){
                    Log.e("200","gya"+connection.getResponseCode());
                }else{
                    Log.e("200","naa chlhe"+connection.getResponseCode());
                }
            } catch (Exception e) {

            }
            return null;
        }
    }*/

    /*
          new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, Response<Response> response) {
                        Toast.makeText(RegisterActivity.this, response.body().toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                    }
                    /*@Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String output = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                            Log.d("Buffered Reader", response.getBody().in().toString());
                            //Reading the output in the string
                            output = reader.readLine();
                            Log.d("Buffered out", response.getReason());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Displaying the output as a toast
                        Toast.makeText(RegisterActivity.this, output, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }*/

}

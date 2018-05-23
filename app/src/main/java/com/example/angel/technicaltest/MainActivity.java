package com.example.angel.technicaltest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private EditText userNameEditText;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        //views
        userNameEditText = findViewById(R.id.userNameEditText);
        final Button validate = findViewById(R.id.userNameValidateButton);


        //when "ok" is clicked
         validate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(final View view) {

                 Intent intent = new Intent(context, ReposListActivity.class);
                 String userName = userNameEditText.getText().toString();
                 intent.putExtra(EXTRA_MESSAGE, userName);
                 startActivity(intent);
                 userNameEditText.setText("");
             }

         });
    }
}
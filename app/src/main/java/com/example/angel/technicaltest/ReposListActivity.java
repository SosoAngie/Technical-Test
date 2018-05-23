package com.example.angel.technicaltest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class ReposListActivity extends Activity {


    private Context context;

    private ImageView userAvatar;
    private TextView userNameTextView;
    private ListView userReposList;

    static List<Repo> listRepositories;
    static UserReposListAdapter userReposListAdapter;


    public ReposListActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_repos);

        context = this;

        userAvatar = findViewById(R.id.userAvatar);
        userNameTextView = findViewById(R.id.userNameTextView);
        userReposList = findViewById(R.id.userReposListView);

        //list repos
        listRepositories = new ArrayList<>();
        userReposListAdapter = new UserReposListAdapter(context,listRepositories);

        //url image avatar
        final String[] avatarUrl = {""};

        Intent intent = getIntent();
        final String user = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        @SuppressLint("StaticFieldLeak") AsyncTask asyncTask = new AsyncTask() {
            @Nullable
            @Override
            protected Object doInBackground(Object[] objects) {
                try{
                    //Create URL
                    URL githubEndpoint = new URL("https://api.github.com/users/"+user+"/repos");

                    //Create connection
                    HttpsURLConnection githubConnection = (HttpsURLConnection) githubEndpoint.openConnection();

                    //Request Header
                    githubConnection.setRequestProperty("User-Agent", "technicaltest");
                    githubConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");


                    if (githubConnection.getResponseCode() == 200) {
                        // Success
                        InputStream inputStream = githubConnection.getInputStream();

                        //Parse Json
                        String result = null;
                        try {

                            // json is UTF-8 by default
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                            StringBuilder sb = new StringBuilder();

                            String line = null;
                            while ((line = reader.readLine()) != null)
                            {
                                sb.append(line + "\n");
                            }
                            result = sb.toString();

                        } catch (Exception e) {
                            // Oops
                            e.printStackTrace();
                        }
                        finally {
                            try{if(inputStream != null)inputStream.close();}catch(Exception squish){squish.printStackTrace();}
                        }

                        //Create Json object
                        JSONArray jArray = new JSONArray(result);

                        for (int i=0; i < jArray.length(); i++)
                        {
                            try {
                                JSONObject oneObject = jArray.getJSONObject(i);
                                // Pulling items from json
                                String nameRepo = oneObject.getString("name");
                                String descriptionRepo = oneObject.getString("description");
                                int watcherCountRepo = oneObject.getInt("watchers_count");

                                if(descriptionRepo == "null"){
                                    descriptionRepo = "No description";
                                }

                                //create object repo and add it to list
                                listRepositories.add(new Repo(nameRepo, descriptionRepo,watcherCountRepo));

                                //Getting avatar_url
                                JSONObject owner = oneObject.getJSONObject("owner");
                                if(avatarUrl[0].isEmpty()) {
                                    avatarUrl[0] =owner.getString("avatar_url");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        githubConnection.disconnect();

                    } else {
                        // Error handling code goes here
                    }

                }
                catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };


        try {
            asyncTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        String status = asyncTask.getStatus().toString();

        //while asynctask is not finished
        while(status=="PENDING")
        {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //put avatar into imageView
        Picasso.with(context)
                .load(avatarUrl[0])
                .resize(200, 200)
                .into(userAvatar);

        userNameTextView.setText(user);



        // listRepositories.sort();
        quickSort(0,listRepositories.size()-1,listRepositories);
        userReposListAdapter.notifyDataSetChanged();

        userReposList.setAdapter(userReposListAdapter);

        // Check if no view has focus:
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            //Hide keyboard
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(),0);//, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }


    }

    //sort the list by watcherCount
    private void quickSort(int lowerIndex, int higherIndex, List<Repo> array) {

        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        int pivot = array.get(lowerIndex+(higherIndex-lowerIndex)/2).getWatcherCount();
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (array.get(i).getWatcherCount() < pivot) {
                i++;
            }
            while (array.get(j).getWatcherCount() > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j, array);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j,array);
        if (i < higherIndex)
            quickSort(i, higherIndex,array);
    }

    private void exchangeNumbers(int i, int j, List<Repo> array) {
        Repo temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }


}




package com.gtappdevelopers.instagram;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGpt extends AppCompatActivity {

    private TextView responseTV;
    private TextView questionTV;
    private EditText queryEdt;
    private ImageView sendbtn;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    private String url = "https://api.openai.com/v1/completions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_gpt);

        responseTV = findViewById(R.id.chatgptres);
        questionTV = findViewById(R.id.qq);
        queryEdt = findViewById(R.id.msgtext);

        sendbtn=findViewById(R.id.sendbtn);


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = queryEdt.getText().toString();
                if (query.length() > 0) {
                    responseTV.setText("Please wait..");
                   callAPI(query);
                } else {
                    Toast.makeText(ChatGpt.this, "Please enter your query..", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    public void callAPI(String query)
    {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "gpt-3.5-turbo");
            jsonObject.put("prompt", query);
            jsonObject.put("temperature", 0);
            jsonObject.put("max_tokens", 400);

        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestBody body=RequestBody.create(jsonObject.toString(),JSON);
        okhttp3.Request request= new okhttp3.Request.Builder().url(url)
                .addHeader("Authorization","Bearer "+"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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("rrrr",e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e("rrrr","ff"+response);

                if(response.isSuccessful())
                {
                    try {
                        JSONObject obj=new JSONObject(response.body().string());
                        JSONArray jsonArray= obj.getJSONArray("choices");
                        String result=jsonArray.getJSONObject(0).getString("text");


                        responseTV.setText(result);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }



                }
                else{

                }
            }
        });

    }

    private void getResponse(String query) {
        questionTV.setText(query);
        queryEdt.setText("");

        RequestQueue queue = Volley.newRequestQueue(ChatGpt.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "gpt-3.5-turbo");
            jsonObject.put("prompt", query);
            jsonObject.put("temperature", 0);
            jsonObject.put("max_tokens", 100);
            jsonObject.put("top_p", 1);
            jsonObject.put("frequency_penalty", 0.0);
            jsonObject.put("presence_penalty", 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }



        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        String responseMsg = response.getJSONArray("choices").getJSONObject(0).getString("text");
                        responseTV.setText(responseMsg);


                        Log.d("api_chat","opkahy"+responseMsg);
                        Toast.makeText(this, "msg"+responseMsg, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error: 123" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.e("TAGAPI", "Error: " + error.getMessage(), error);
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer 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");
                return params;
            }
        };

        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });

        queue.add(postRequest);
    }


}

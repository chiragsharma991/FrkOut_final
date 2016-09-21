package com.codeginger.frkout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

// Created by Pratik Mehta

public class FacebookSignInActivity extends Activity
{
    CallbackManager callbackManager;
    Button Login;
    TextView textView;
    private Bitmap bitmap;
    private ProgressDialog pDialog;
    private Context context;
    private Bitmap image;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Activity Res", "" + requestCode);

        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_sign_in);
        FacebookSdk.sdkInitialize(this);


        callbackManager = CallbackManager.Factory.create();
        Login = (Button) findViewById(R.id.login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(FacebookSignInActivity.this, Arrays.asList("email", "user_photos", "public_profile","basic_info","user_birthday"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Log.e("process",""+"onsucess");
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object, GraphResponse response) {
                                                Log.e("process",""+"oncomplete");
                                                String name = null;
                                                String email = null;
                                                String gender=null;
                                                String birthday=null;
                                                String id= null;
                                                //object.toString();
                                                //String json=response.toString();

                                                //JSONObject profile=new JSONObject(json);

                                                try {
                                                    name = object.getString("name");
                                                    email =object.getString("email");
                                                    gender = object.getString("gender");
                                                    birthday = object.getString("birthday");
                                                    id=object.getString("id");
                                                    //ProfilePictureView profilePictureView=(ProfilePictureView)findViewById(R.id.profile_pic);
                                                    //profilePictureView.setProfileId(id);


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                                //Log.e("email",""+email);
                                                StringBuilder stringBuilder=new StringBuilder();
                                                stringBuilder.append("profile"+"\n"+name+"\n"+email+"\n"+gender+"\n"+birthday   );
                                                textView=(TextView)findViewById(R.id.txt);
                                                textView.setText(stringBuilder);
                                                  //ImageView imageView=(ImageView)findViewById(R.id.pic);
                                                context=FacebookSignInActivity.this;

//                                                Picasso.with(context)
//                                                        .load("https://graph.facebook.com/"+id+"/picture?type=large")
//                                                        .into(imageView);
                                                Thread thread = new Thread() {
                                                    @Override
                                                    public void run()
                                                    {
                                                        Log.e("on thread","thread");
                                                        URL url = null;
                                                        try {
                                                            url = new URL("https://graph.facebook.com/1121251407934785/picture?type=large");
                                                        } catch (MalformedURLException e) {
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                                            Log.e("bitmap",""+image);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }




                                                    }
                                                };

                                                thread.start();
                                                ImageView imageView=(ImageView)findViewById(R.id.pic);
                                                imageView.setImageBitmap(image);






                                                // new LoadImage().execute("http://graph.facebook.com/1079108998815693/picture");


                                            }
//                                            class LoadImage extends AsyncTask<String, String, Bitmap>
//                                            {
//
//
//
//                                                @Override
//                                                protected void onPostExecute(Bitmap image) {
//                                                    super.onPostExecute(bitmap);
//                                                    ImageView imageView=(ImageView)findViewById(R.id.pic);
//                                                    if(image != null){
//                                                        imageView.setImageBitmap(image);
//                                                        pDialog.dismiss();
//
//                                                    }else{
//
//                                                        pDialog.dismiss();
//                                                        Toast.makeText(FacebookSignInActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
//
//                                                    }
//                                                }
//
//                                                @Override
//                                                protected Bitmap doInBackground(String... args) {
//
//
//                                                    try {
//                                                        bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
//                                                        Log.e("doinbackround",""+"log");
//                                                        Log.e("bitmap",""+bitmap);
//                                                    } catch (IOException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    return bitmap;
//                                                }
//
//                                                @Override
//                                                protected void onPreExecute() {
//                                                    Log.e("process",""+"onpost");
//                                                    super.onPreExecute();
//                                                    pDialog = new ProgressDialog(FacebookSignInActivity.this);
//                                                    pDialog.setMessage("Loading Image ....");
//                                                    pDialog.show();
//                                                }
//                                            }
//









                                        });
                                Bundle parameters = new Bundle();

                                parameters.putString("fields", "id,name,email,gender, birthday");

                                request.setParameters(parameters);

                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {

                                Log.e("TAG", "" + "eroor");


                            }

                            @Override
                            public void onError(FacebookException error) {

                                Log.e("TAG", "" + "cancel");

                            }


                        });


            }


        });
    }




}
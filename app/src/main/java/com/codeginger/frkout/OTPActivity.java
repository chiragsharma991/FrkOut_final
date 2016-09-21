package com.codeginger.frkout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.IntentSender.SendIntentException;
import android.util.Log;
import android.view.View.OnClickListener;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import java.io.*;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.net.Uri;
import org.json.JSONException;
import org.json.JSONObject;
// Created by Pratik Mehta
public class OTPActivity extends Activity implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener
{
    int OTP_Code, otp_code_entered;
    String IMEINo, MobileNO, OTPSendUrl, OTPUrl, result = "";
    RelativeLayout rlRequest_OTP, rlSubmit_OTP;
    EditText etxtMobile_No, etxtOTP;
    Button btnRequest_OTP, btnSubmit_OTP,Login;
    TextView txtResendOTP,sign_user,textView;
    private static OTPActivity inst;
    Common c;
    CallbackManager callbackManager;
    UserSessionManager usm;
    // Facebook
    // Your Facebook APP ID
   //private Facebook facebook = new Facebook("1233454473345447");
    //private Facebook facebook = new Facebook("1191062100926565");
    //private Facebook facebook = new Facebook("766328193502503");
    //private static final int FB_SIGN_IN = 32665;
    private AsyncFacebookRunner mAsyncRunner;
    // Google
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    ImageView ivSign_in,ivLoginTwitter;

    // Twitter
    static String TWITTER_CONSUMER_KEY = "1iNBqutA8G1H5JDJC1n5NvUnA";
    static String TWITTER_CONSUMER_SECRET = "3svqW1Ir1IJS62YHX7LWGUEdfR7bThJGc2JrHEtgGnUtgPFpEO";
    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    private static Twitter twitter;
    private static RequestToken requestToken;
    private AccessToken accessToken;
    private String access;
    private TwitterAuthClient mTwitterAuthClient;


    public static OTPActivity instance()
    {
        return inst;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        FacebookSdk.sdkInitialize(this);
        Log.e("starter on create",""+"log");


        callbackManager = CallbackManager.Factory.create();
        Twitter_login twitter_login =new Twitter_login();
      /*  twitter_login.onmethod();*/

        sign_user=(TextView)findViewById(R.id.txtSignIn);
        etxtMobile_No = (EditText) findViewById(R.id.etxtMobile_No);
        sign_user.setOnClickListener(this);

        c = new Common();
        usm = new UserSessionManager(getApplicationContext());
        findViewById();
        getIMEINo();
        if(!usm.isFolderCreated())
        {
            createFolder();
        }
        // Mobile No. May or May Not be fetch due to unknown state.
        //TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        //String number = tm.getLine1Number();
    }

    public void fillOTP(final int OTP_Code)
    {
        etxtOTP.setText(OTP_Code);
    }

    private void getIMEINo()
    {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEINo = tm.getDeviceId();
    }

    private void createFolder()
    {
        File appFolder, imageFolder, videoFolder;
        appFolder = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name));
        imageFolder = new File(Environment.getExternalStoragePublicDirectory(getResources().getString(R.string.app_name)), getResources().getString(R.string.image_folder));
        videoFolder = new File(Environment.getExternalStoragePublicDirectory(getResources().getString(R.string.app_name)), getResources().getString(R.string.video_folder));

        if(!appFolder.exists() || !imageFolder.exists() || !videoFolder.exists())
        {
            if (!appFolder.exists())
            {
                appFolder.mkdirs();
            }
            if (!imageFolder.exists())
            {
                imageFolder.mkdirs();
            }
            if (!videoFolder.exists())
            {
                videoFolder.mkdirs();
            }
        }
        usm.editor.putBoolean(usm.KEY_IS_FOLDER_CREATED, true);
        usm.editor.commit();
    }

    // FindViewById
    public void findViewById()
    {
        Typeface roboto = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "font/Roboto-Light.ttf"); //use this.getAssets if you are calling from an Activity
        Typeface roboto_bold = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "font/Roboto-Bold.ttf");
        rlRequest_OTP = (RelativeLayout) findViewById(R.id.rlRequest_OTP);
        etxtMobile_No = (EditText) findViewById(R.id.etxtMobile_No);
        btnRequest_OTP = (Button) findViewById(R.id.btnRequest_OTP);

        rlSubmit_OTP = (RelativeLayout) findViewById(R.id.rlSubmit_OTP);
        etxtOTP = (EditText) findViewById(R.id.etxtOTP);
        etxtOTP.setTypeface(roboto);

        txtResendOTP = (TextView) findViewById(R.id.txtResendOTP);
        btnSubmit_OTP = (Button) findViewById(R.id.btnSubmit_OTP);
        btnSubmit_OTP.setTypeface(roboto_bold);


        ivSign_in = (ImageView) findViewById(R.id.ivSign_in);
        etxtMobile_No.setTypeface(roboto);
        btnRequest_OTP.setTypeface(roboto_bold);
        ivSign_in.setOnClickListener(this);
        //Facebook
       // mAsyncRunner = new AsyncFacebookRunner(facebook);

        // Google
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((ConnectionCallbacks) this)
                .addOnConnectionFailedListener((OnConnectionFailedListener) this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        // Twitter
    /*    if (!usm.isTwitterLoggedInAlready() && getIntent().getData() != null)
        {
            // Save Twitter User Profile Data
            new AsyncSaveTwitterData().execute();
        }*/

        txtResendOTP.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                txtResendOTP.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.slide_in_left));
                new AsyncSendOTP().execute();
            }
        });
    }

    /* START OF FACEBOOK SIGN IN PROCESS */
    // Function to login into facebook
    public void loginToFacebook(View View)
    {
        Log.e("login to facbook",""+"log");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile","basic_info","user_birthday"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("sucess",""+"log");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
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
                                         //   ProfilePictureView profilePictureView=(ProfilePictureView)findViewById(R.id.profile_pic);
                                           // profilePictureView.setProfileId(id);
                                            usm.editor.putString(usm.KEY_NAME, name);
                                            usm.editor.putString(usm.KEY_EMAIL, email);
                                            usm.editor.putString(usm.KEY_BIRTHDAY,birthday);
                                            usm.editor.putString(usm.KEY_GENDER,gender);
                                            usm.editor.putString(usm.KEY_ID,id);
                                            usm.editor.putBoolean(usm.KEY_FACEBOOK_LOGIN, true);
                                            usm.editor.commit();
                                            Conditionclass conditionclass=new Conditionclass(1);
                                            Log.e("facebook log","condition one");


                                            gotoProfileActivity();


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        // Log.e("name",""+name);

                                        //Log.e("email",""+email);
                                        /*StringBuilder stringBuilder=new StringBuilder();
                                        stringBuilder.append("profile"+"\n"+name+"\n"+email+"\n"+gender+"\n"+birthday   );
                                        textView=(TextView)findViewById(R.id.txt);
                                        textView.setText(stringBuilder);
                                        */

                                    }
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





        //     if (!facebook.isSessionValid())
  //      {
//            facebook.authorize(this,
//                    // new String[] { "email", "publish_actions" },
//                    new String[] { "email"},
//                    new Facebook.DialogListener()
//                    {
//                        @Override
//                        public void onCancel()
//                        { // Function to handle cancel event
//                        }
//                        @Override
//                        public void onComplete(Bundle values)
//                        {
//                            usm.editor.putString(usm.KEY_FACEBOOK_ACCESS_TOKEN, facebook.getAccessToken());
//                            usm.editor.putLong(usm.KEY_FACEBOOK_ACCESS_EXPIRES, facebook.getAccessExpires());
 //                          usm.editor.putBoolean(usm.KEY_FACEBOOK_LOGIN, true);
//                            usm.editor.putBoolean(usm.KEY_IS_USER_LOGIN, true);
//                            usm.editor.commit();
//                            getFacebookProfileInformation();
//                        }
//
//                        @Override
//                        public void onError(DialogError error)
//                        { // Function to handle error
//                        }
//
//                        @Override
//                        public void onFacebookError(FacebookError fberror)
//                        { // Function to handle Facebook errors
//                        }
//                    });
   //   }
    }

 /*   // Get Profile information by making request to Facebook Graph API
    public void getFacebookProfileInformation()
    {

         //mAsyncRunner.request("me?fields=name,email,birthday&access_token=" + access , new AsyncFacebookRunner.RequestListener() {
       mAsyncRunner.request("me", new AsyncFacebookRunner.RequestListener() {

            @Override
            public void onComplete(String response, Object state)
            {
                Log.d("Profile", response);
                String json = response;
                try
                {
                    // Facebook Profile JSON data
                    JSONObject profile = new JSONObject(json);
                    String name = profile.getString("name");
                    Log.e("name","name");
                   //final String email = profile.getString("email");
                    //Log.e("email",""+email);
                     //final String birthday = profile.getString("birthday");
                    usm.editor.putString(usm.KEY_NAME, name);
                    //usm.editor.putString(usm.KEY_EMAIL, email);
                    usm.editor.commit();
                    gotoProfileActivity();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onIOException(IOException e, Object state)
            {
                System.out.print(e);
            }

            @Override
            public void onFileNotFoundException(FileNotFoundException e,Object state)
            {
                System.out.print(e);
            }

            @Override
            public void onMalformedURLException(MalformedURLException e,Object state)
            {
                System.out.print(e);
            }

            @Override
            public void onFacebookError(FacebookError e, Object state)
            {
                System.out.print(e);
            }
        });
    }
*/

    /* END OF FACEBOOK SIGN IN PROCESS */
    /* START OF GOOGLE SIGN IN PROCESS */
    // Google resolveSignInError
    private void resolveSignInError()
    {
        if (mConnectionResult != null && mConnectionResult.hasResolution())
        {
            try
            {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            }
            catch (SendIntentException e)
            {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    // onConnectionFailed
    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        if (!result.hasResolution())
        {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,0).show();
            return;
        }

        if (!mIntentInProgress)
        {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;
            if (mSignInClicked)
            {
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,Intent intent)
    {
        super.onActivityResult(requestCode, responseCode, intent);

        callbackManager.onActivityResult(requestCode, responseCode, intent);
     //   mTwitterAuthClient.onActivityResult(requestCode, responseCode, intent);



        Log.e("OnActivity",""+"log");
        // Google SignIn
        if(requestCode == RC_SIGN_IN)
        {
            if(responseCode != RESULT_OK)
            {
                mSignInClicked = false;
            }

            mIntentInProgress = false;
            if(!mGoogleApiClient.isConnecting())
            {
                mGoogleApiClient.connect();
            }
        }

        // Facebook SignIn
//        else if(requestCode == FB_SIGN_IN)
//        {
//            Log.e("facbook on actovity",""+"log");
//            super.onActivityResult(requestCode, responseCode, intent);
//            facebook.authorizeCallback(requestCode, responseCode, intent);
//            callbackManager.onActivityResult(requestCode, requestCode, intent);
//        }
    }

    @Override
    public void onConnected(Bundle arg0)
    {
        Conditionclass conditionclass=new Conditionclass(2);
        Log.e("google log","condition two");
        mSignInClicked = false;
        getProfileInformation();
        gotoProfileActivity();
    }

    private void getProfileInformation()
    {
        try
        {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null)
            {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();

                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                String sex = String.valueOf(currentPerson.getGender());
                String personPhotoUrl = currentPerson.getImage().getUrl();
                Log.e("url in first",""+personPhotoUrl);
                String gender = null;
                if(sex=="0"){
                    gender="male";
                }else
                {
                    gender="female";
                }

                //String birthday = currentPerson.getBirthday();
                String birthday= null;



                usm.editor.putString(usm.KEY_NAME, personName);
                usm.editor.putString(usm.KEY_EMAIL, email);
                usm.editor.putString(usm.KEY_GENDER, gender);
                usm.editor.putString(usm.KEY_BIRTHDAY, birthday);
                usm.editor.putString(usm.KEY_URL,personPhotoUrl);
                usm.editor.putBoolean(usm.KEY_GOOGLE_LOGIN, true);

                usm.editor.putBoolean(usm.KEY_IS_USER_LOGIN, true);
                usm.editor.commit();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Person information is Empty", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0)
    {
        mGoogleApiClient.connect();
    }

    // Sign-in into google
    public void signInWithGplus()
    {
        if (!mGoogleApiClient.isConnecting())
        {
            mGoogleApiClient.connect();
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ivSign_in:
                signInWithGplus();
                break;
            //direct for user
            case R.id.txtSignIn:
                Intent intent=new Intent(getApplicationContext(),Sign_userActivity.class);
                startActivity(intent);
                break;

        }
    }

    /* END OF GOOGLE SIGN IN PROCESS */


   /* START OF TWITTER SIGN IN PROCESS */

    // Function to login twitter
   /* public void loginToTwitter(View view)
    {
        // Check if already logged in
        if (!usm.isTwitterLoggedInAlready())
        {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
                        OTPActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        else
        {
            // Already Logged into twitter
        }

        // Save Twitter User Profile Data
        new AsyncSaveTwitterData().execute();

    }

    public class AsyncSaveTwitterData extends AsyncTask<String, Void, String>
    {
        String username = "";

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                if (!usm.isTwitterLoggedInAlready())
                {
                    Uri uri = getIntent().getData();
                    if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL))
                    {
                        // oAuth verifier
                        final String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
                        try
                        {
                            // Get the access token
                            OTPActivity.this.accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        usm.editor.putString(usm.KEY_TWITTER_OAUTH_TOKEN, accessToken.getToken());
                        usm.editor.putString(usm.KEY_TWITTER_OAUTH_SECRET, accessToken.getTokenSecret());
                        usm.editor.putString(usm.KEY_NAME, twitter.showUser(accessToken.getUserId()).getName());
                        usm.editor.putBoolean(usm.KEY_TWITTER_LOGIN, true);
                        usm.editor.putBoolean(usm.KEY_IS_USER_LOGIN, true);
                        usm.editor.commit();
                        gotoProfileActivity();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                System.out.print("onPostExecute");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
*/
   /* END OF TWITTER SIGN IN PROCESS */
/* START OF OTP SIGN IN PROCESS */

    // OTP Request
    public void OTPRequest(View view)
    {
        if(etxtMobile_No.getText().toString().length() == 10)
        {
            MobileNO = etxtMobile_No.getText().toString();
            OTP_Code = c.randomNo(getApplicationContext());
            // Sending OTP to Mobile
            new AsyncSendOTP().execute();
            GotoOTPSubmit(MobileNO, OTP_Code);
        }
        else
        {
            // Toast.makeText(OTPActivity.this, getResources().getString(R.string.mobileno_validation), Toast.LENGTH_SHORT).show();
            etxtMobile_No.setError(getResources().getString(R.string.mobileno_validation));
        }
    }

    // Open OTPSubmit Screen
    public void GotoOTPSubmit(String MobileNO, int OTP_Code)
    {
        rlRequest_OTP.setVisibility(View.GONE);
        rlSubmit_OTP.setVisibility(View.VISIBLE);
        etxtOTP.requestFocus();

        //etxtOTP.setText("" + OTP_Code);
    }

/*
    // OTP Request
    public void OTPResend(View view)
    {
        String D = "44";
        // Sending OTP to Mobile
        //new AsyncSendOTP().execute("");
    }
*/
    // OTP Submit
    public void OTPSubmit(View view)
    {
        if(etxtOTP.getText().toString().length() > 0)
        {
            otp_code_entered = Integer.parseInt(etxtOTP.getText().toString());

            if(otp_code_entered == OTP_Code)
            {
                btnSubmit_OTP.setEnabled(false);
                new AsyncSubmitOTP().execute();
            }
            else
            {
                etxtOTP.setError(getResources().getString(R.string.OTP_invalid));
            }
        }
        else
        {
            etxtOTP.setError(getResources().getString(R.string.OTP_validation));
        }
    }

    private class AsyncSendOTP extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                // http://smshorizon.co.in/api/sendsms.php?user=FrkOut&apikey=26nTVueKbS1ZNajp5fBa&mobile=8097244854&message=FrkOutOTPIs123456&senderid=MYTEXT&type=txt
                //http://smshorizon.co.in/api/status.php?user=FrkOut&apikey=26nTVueKbS1ZNajp5fBa&msgid=15310426

                result = "";
                //OTPSendUrl = "http://smshorizon.co.in/api/sendsms.php?user=FrkOut&apikey=26nTVueKbS1ZNajp5fBa&mobile=" + MobileNO +"&message=Use%20" + OTP_Code + "%20as%20FrkOut%20OTP%20to%20verify%20your%20identity&senderid=MYTEXT&type=txt";

                OTPSendUrl = "http://103.16.101.52:8080/bulksms/bulksms?username=bhd-codeginger&password=santosh&type=0&dlr=1&destination="+MobileNO+"&source=PACEMS&message="+OTP_Code+"";
                Log.e("send otp",""+OTP_Code);

                result = c.GetData(OTPSendUrl);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            //Toast.makeText(OTPActivity.this, result.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private class AsyncSubmitOTP extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OTPActivity.this);
            progressDialog.setTitle(getResources().getString(R.string.OTP_sbm_title_val));
            progressDialog.setMessage(getResources().getString(R.string.OTP_sbm_desc_val));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                result = "";
                OTPUrl = "http://frkout.geecs.in/Master.svc/InsertOTP/" + MobileNO + "/" + OTP_Code;
                Log.e("submit otp",""+OTP_Code);
                result = c.GetData(OTPUrl);
             }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            // Toast.makeText(OTPActivity.this, result.toString(), Toast.LENGTH_LONG).show();
            if(result.length() > 0)
            {
                try
                {
                    progressDialog.dismiss();

                    JSONObject jsonObject = new JSONObject(result);
                    if((jsonObject.getString("InsertOTPResult").equals("")))
                    {
                        usm.editor.putString(usm.KEY_OTP_ID, jsonObject.getString("InsertOTPResult"));
                        usm.editor.putBoolean(usm.KEY_IS_USER_LOGIN, true);

                        usm.editor.commit();
                        gotoProfileActivity();
                    }
                    else
                    {
                        btnSubmit_OTP.setEnabled(true);
                       // Toast.makeText(OTPActivity.this, getResources().getString(R.string.OTP_sbm_err), Toast.LENGTH_SHORT).show();
                        Typeface myAwesomeTypeFace = null;
                        SnackbarManager.show(Snackbar.with(OTPActivity.this).text(R.string.OTP_sbm_err).actionLabel("dismiss").actionColor(Color.RED).actionLabelTypeface(myAwesomeTypeFace).actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                snackbar.dismiss();
                            }
                            }));
                        }
                }
                catch (Exception e)
                {
                    btnSubmit_OTP.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }
    }

    /* END OF OTP SIGN IN PROCESS */

    public void gotoProfileActivity()
    {
        Log.e("next",""+"log");
        startActivity(new Intent(OTPActivity.this, UserProfileActivity.class));
        finish();
    }



    private class Twitter_login
    {
        TextView screen_name,user_name,user_location,user_timezone,user_description;
        TwitterSession session;
        ImageView user_picture;
        String screenname,username,twitterImage,location,timeZone,description,birth;
        private static final String TWITTER_KEY = "zM77a92ty3whnLrhcOJ3sijK3";
        private static final String TWITTER_SECRET = "b3mdCvezGqbL1vzk6vAq9lredds9KbYaZn2V1OChL4xi8KfTfG";







        // Add your Consumer Key and Consumer Secret Key generated while creating app on Twitter.
        //See "Keys and access Tokens" in your app on twitter to find these keys.


        public void onmethod()
        {
            Log.e("onmethod","log");

            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
          /*  Fabric.with(getApplicationContext(), new com.twitter.sdk.android.Twitter(authConfig));*/
            mTwitterAuthClient= new TwitterAuthClient();

            setContentView(R.layout.activity_otp);
 //       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
            ivLoginTwitter=(ImageView) findViewById(R.id.ivLoginTwitter);
            ivLoginTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Log.e("onclick","log");

                    mTwitterAuthClient.authorize(OTPActivity.this, new Callback<TwitterSession>() {
                        @Override
                        public void success(Result<TwitterSession> result) {
                            Log.e("TwitterKit", "Login Sucess");

                            session = com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession();

                            com.twitter.sdk.android.Twitter.getApiClient(session).getAccountService()
                                    .verifyCredentials(true, false, new Callback<User>() {

                                        @Override
                                        public void success(Result<User> userResult) {
                                            Log.e("TwitterKit", "twitter on Sucess");

                                            User user = userResult.data;

                                            twitterImage = user.profileImageUrl;
                                            Log.e("TwitterKit",""+twitterImage);
                                            screenname = user.screenName;
                                            Log.e("TwitterKit",""+screenname);
                                            username = user.name;
                                            Log.e("TwitterKit",""+username);
                                            location = user.location;
                                            Log.e("TwitterKit",""+location);
                                            timeZone = user.timeZone;
                                            Log.e("TwitterKit",""+timeZone);
                                            description = user.description;
                                            Log.e("TwitterKit",""+description);
                                            usm.editor.putString(usm.KEY_NAME, username);
                                            usm.editor.putString(usm.KEY_EMAIL, screenname);
                                            usm.editor.putString(usm.KEY_URL,twitterImage);
                                            usm.editor.putBoolean(usm.KEY_TWITTER_LOGIN, true);
                                            Conditionclass conditionclass=new Conditionclass(3);

                                            usm.editor.commit();


                                            gotoProfileActivity();




                                        }

                                        @Override
                                        public void failure(TwitterException e) {

                                        }

                                    });

                            // loginButton.setVisibility(View.GONE);

                        }

                        @Override
                        public void failure(TwitterException exception)
                        {
                            Log.e("TwitterKit", "Login with Twitter failure", exception);

                        }
                    });




                }
            });



        }




    }


}
package com.codeginger.frkout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import java.io.*;
import java.net.URL;
import java.util.*;

import android.app.DatePickerDialog;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.support.v7.app.ActionBar;

// Created by Pratik Mehta

public class UserProfileActivity extends ActionBarActivity implements View.OnClickListener
{
    String UserProfileUrl, location_name = "NULL", Sub_location_name = "NULL", disrtict_name = "NULL", pincode = "NULL", result = "", Gender = "M", gender,name,birthday,id,url, emailID, OTP_ID = "", UsersId = "";
    int age;
    RelativeLayout  rlUser_Profile_2;
    LinearLayout rlUser_Profile;
    Switch swtUserProfileGender;
    EditText etxtUserProfileName, etxtUserProfileDOB, etxtUserProfileAge, etxtUserProfileEmailID, etxtUserProfileLocation;
    CheckBox chkUserProfileAgree;
    ImageView ivUserProfile;
    Button  btnUserProfileSaveNext;
    ImageView btnUserProfileFemale,btnUserProfileOther,btnUserProfileMale;


    RadioGroup rgUserProfileGender;
    RadioButton rdbUserProfileMale;

    Common c;
    UserSessionManager usm;
    GPSTracker gps;

    // UserProfile 2
    Button btnUserProfile2Done;
    int interestCount = 0;

    int n;
    int a=0;
    int[] InterestListID;
    ArrayList<Integer> SelectedInterestList = new ArrayList<Integer>();
    String SelectedInterestID = "";
    String[] InterestList;
    FlowLayout flUserProfileInterest;
    JSONArray jsonarray = null;


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "L2C";
    GoogleCloudMessaging gcm;
    String regid = "", GCM_REG_FROM, GCM_REG_ID;
    ActionBar mActionBar;

    String USER_ID, strImageName = "", CurrentPhotoPath, strImgByte = "";
    private Bitmap photo;
    private static final int CAMERA_REQUEST = 0, VIDEO_REQUEST = 1, SELECT_IMAGE = 2, SELECT_VIDEO = 3, SELECT_IMAGE_VIDEO = 4;
    File Imagefile;
    Uri selectedImage, imageUri;
    int imageRotate = 0;
    String FileName, FileByte;
    public int value;
    private Context context;
    private Bitmap bitmap;
    private int check_value;
   //  ArrayList<NewclassforInterest> list;
    public static ArrayList<NewclassforInterest> list = new ArrayList<NewclassforInterest>();
    CustomAdapter lisrviewadapter;
    private ListView listView;
    ArrayList<Integer> list_of_item=new ArrayList<>();
     String interest;
    private String Manual_gender="";
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        findViewById();
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));
        mActionBar.setTitle(getResources().getString(R.string.user_profile_1_header_title));
        Log.e("value",""+value);
        check_value=Conditionclass.value;

        c = new Common();
        usm = new UserSessionManager(getApplicationContext());
        HashMap<String, String> app =  usm.getUserDetails();
        name = app.get(usm.KEY_NAME);
        emailID = app.get(usm.KEY_EMAIL);
        birthday = app.get(usm.KEY_BIRTHDAY);
         url=app.get(usm.KEY_URL);
        Log.e("url",""+url);
       //birthday="02/09/1993";
        btnUserProfileFemale.setOnClickListener(this);
        btnUserProfileMale.setOnClickListener(this);
        btnUserProfileOther.setOnClickListener(this);
        gender = app.get(usm.KEY_GENDER);



        id=app.get(usm.KEY_ID);
        Log.e("id",""+id);
        if (gender==null)
        {
            GenderMale();
        }else
        {
            switch(gender)
            {
                case "male":
                    GenderMale();
                    Log.e("gender","male");
                    break;
                case "female":
                    GenderFemale();
                    Log.e("gender","Female");
                default:
                    GenderOther();
                    Log.e("gender","default");


            }

        }


        Calendar calendar = Calendar.getInstance();
        // this is current year of calender
        int current_year=Integer.parseInt(String.valueOf(calendar.get(Calendar.YEAR)))   ;

            //user birthyear
        if(birthday==null)
        {
            age=0;
        }else {

            String result=birthday.substring(birthday.lastIndexOf("/")+1);
            int Result=Integer.parseInt(result);
            age= (current_year)-(Result);

            Log.e("age",""+age);
        }
        if(url!=null||id!=null)
        {
            Log.e("condition check","log");
            if(check_value==1)//for fb
            {
                Log.e("condition value",""+check_value);
                new loadimage().execute("https://graph.facebook.com/"+id+"/picture?type=large");

            }else if (check_value==2)//for google
            {
                Log.e("condition value",""+check_value);
                new loadimage().execute(url);
            }
            else if (check_value==3)//twitter
            {
                Log.e("condition value",""+check_value);
                new loadimage().execute(url);
            }
        }





        //Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.pratik);
        //ivUserProfile.setImageBitmap(icon);
        //ivUserProfile.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.mipmap.pratik));


        if (!usm.isUserProfileInfoSetup())
        {
            location();
        }
        else
        {
            // User Profile Interest
            GotoUserProfileInterest();
        }


        ivUserProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CharSequence colors[] = new CharSequence[] {"Take Pictures", "Upload Pictures"};
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                builder.setItems(colors, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(which == 0)
                        {
                            try
                            {
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                strImageName = getResources().getString(R.string.img) + c.getDDMMYYCurrentDateTime() + ".Jpeg";
                                // Imagefile = new File(Environment.getExternalStoragePublicDirectory(getResources().getString(R.string.app_name)) + File.separator + getResources().getString(R.string.image_folder), strImageName);
                                // CurrentPhotoPath = Imagefile.getAbsolutePath();
                                // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Imagefile));
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }
                            catch (Exception e)
                            {
                                System.out.print(e);
                            }
                        }
                        else if(which == 1)
                        {
                            strImageName = getResources().getString(R.string.img) + c.getDDMMYYCurrentDateTime() + ".Jpeg";
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, getResources().getText(R.string.broad_creative_imageupload_validation)), SELECT_IMAGE);
                        }
                    }
                });
                builder.show();
            }
        });

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Capture Image and Choose Image
        if ((requestCode == CAMERA_REQUEST || requestCode == SELECT_IMAGE))
        {
            if(resultCode == RESULT_OK)
            {
                try
                {
                    photo = null;
                    // Image Capture
                    if (requestCode == CAMERA_REQUEST)
                    {
                        try
                        {
                            if(data != null)
                                photo = (Bitmap) data.getExtras().get("data");
                            Log.e("bitmap of capture",""+photo);
/*
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            bmOptions.inJustDecodeBounds = false;
                            bmOptions.inSampleSize = 1;
                            photo = BitmapFactory.decodeFile(CurrentPhotoPath, bmOptions);
                            selectedImage = Uri.fromFile(new File(CurrentPhotoPath));
*/
                        }
                        catch (Exception e)
                        {
                            System.out.println("Exception : " + e);
                        }
                    }

                    // Image Choose
                    else if (requestCode == SELECT_IMAGE && data != null)
                    {
                        try
                        {
                            selectedImage = data.getData();
                            CurrentPhotoPath = selectedImage.getPath();
                            photo = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            Log.e("bitmap of select",""+photo);




                            //photo = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(CurrentPhotoPath), 200, 200);
                            //photo = getThumbnail(getContentResolver(), CurrentPhotoPath);
                        }
                        catch (Exception e)
                        {
                            System.out.println("Exception : " + e);
                        }
                    }

/*
                    // Rotation Check
                    Imagefile = new File(CurrentPhotoPath);
                    ExifInterface exif = new ExifInterface(Imagefile.getAbsolutePath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation)
                    {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            imageRotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            imageRotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            imageRotate = 90;
                            break;
                    }

                    Matrix matrix = new Matrix();
                    matrix.postRotate(imageRotate);
                    imageRotate = 0;
                    photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
*/
                    if(photo != null)
                        ivUserProfile.setImageBitmap(photo);
                }
                catch (Exception e)
                {
                    System.out.println("Exception : " + e);
                }
            }
        }
    }







    // GenderMale
    public void GenderMale()
    {
        Log.e("gender method","male is on");
        Gender = "M";
        btnUserProfileMale = (ImageView) findViewById(R.id.btnUserProfileMale);
        btnUserProfileFemale = (ImageView) findViewById(R.id.btnUserProfileFemale);
        btnUserProfileOther=(ImageView) findViewById(R.id.btnUserProfileOther);
        btnUserProfileMale.setImageResource(R.mipmap.male);
        btnUserProfileFemale.setImageResource(R.mipmap.femalegrey);
        btnUserProfileOther.setImageResource(R.mipmap.othergrey);



    }

    // GenderFemale
    public void GenderFemale()
    {
        Log.e("gender method","female is on");
        Gender = "F";
        btnUserProfileMale = (ImageView) findViewById(R.id.btnUserProfileMale);
        btnUserProfileFemale = (ImageView) findViewById(R.id.btnUserProfileFemale);
        btnUserProfileOther=(ImageView) findViewById(R.id.btnUserProfileOther);
        btnUserProfileMale.setImageResource(R.mipmap.malegrey);
        btnUserProfileFemale.setImageResource(R.mipmap.female);
        btnUserProfileOther.setImageResource(R.mipmap.othergrey);
    }
    public void GenderOther()
    {
        Log.e("gender method","other is on");
        Gender = "O";
        btnUserProfileMale = (ImageView) findViewById(R.id.btnUserProfileMale);
        btnUserProfileFemale = (ImageView) findViewById(R.id.btnUserProfileFemale);
        btnUserProfileOther=(ImageView) findViewById(R.id.btnUserProfileOther);
        btnUserProfileMale.setImageResource(R.mipmap.malegrey);
        btnUserProfileFemale.setImageResource(R.mipmap.femalegrey);
        btnUserProfileOther.setImageResource(R.mipmap.other);
    }




    private void storeAnswer(int question, int answer)
    {
        Log.w("ANDROID DYNAMIC VIEWS:", "Question: " + String.valueOf(question) + " * " + "Answer: " + String.valueOf(answer));

        Toast toast = Toast.makeText(this, String.valueOf(question) + " * " + "Answer: " + String.valueOf(answer), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }

    // UserProfile Submit
    public void UserProfileSubmit(View view)
    {
        Log.e("user profile submit","log");
        if(etxtUserProfileName.getText().toString().length() > 0 && c.isValidEmail(etxtUserProfileEmailID.getText().toString()))
        {
            Log.e("in user submit","log");
            //Gender = swtUserProfileGender.isChecked() ? "F" : "M";
            //Gender = rdbUserProfileMale.isChecked() ? "M" : "F";

            // Image Encoding
            if(photo != null)
            {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.img_quality), outputStream);
                byte[] bytes_pic = outputStream.toByteArray();
                strImgByte = Base64.encodeToString(bytes_pic, Base64.NO_WRAP);
                FileName = strImageName;
                FileByte = strImgByte;
            }

            if(checkPlayServices())
            {
                btnUserProfileSaveNext.setEnabled(false);
                new Register().execute();
            }

            //new AsyncAddUserProfile().execute("");
            //usm.editor.putBoolean(usm.KEY_IS_USER_PROFILE_INFO_SETUP, true);
            //usm.editor.commit();
            //GotoUserProfileInterest();
        }
        else
        {
            if(!(etxtUserProfileName.getText().toString().length() > 0))
            {
                etxtUserProfileName.setError(getResources().getString(R.string.user_profile_1_name_validation));
            }
            if(!(c.isValidEmail(etxtUserProfileEmailID.getText().toString())))
            {
                Log.e("invalid mail",""+c.isValidEmail(etxtUserProfileEmailID.getText().toString()));
                etxtUserProfileEmailID.setError(getResources().getString(R.string.user_profile_1_email_id_validataion));
            }
        /*    if(!(chkUserProfileAgree.isChecked()))
            {
                chkUserProfileAgree.setError(getResources().getString(R.string.user_profile_1_iagree_validataion));
            }*/
/*
            if(!(etxtUserProfileDOB.getText().toString().length() > 0))
            {
                etxtUserProfileDOB.setError(getResources().getString(R.string.user_profile_1_dob_validataion));
            }
            if(!(etxtUserProfileLocation.getText().toString().length() > 0))
            {
                etxtUserProfileLocation.setError(getResources().getString(R.string.user_profile_1_location_validataion));
            }
*/
        }
    }

    // Open UserProfile Interest Screen
    public void GotoUserProfileInterest()
    {
        Log.e("goto user interest","log");
        new AsyncSelectUserInterest().execute();
    }

    public void setimageid(int id)
    {
        if(id==1)
        {
             value=1;

        }

    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.btnUserProfileMale:
                GenderMale();
                Log.e("on click ","male");
                break;
            case R.id.btnUserProfileFemale:
                GenderFemale();
                Log.e("on click ","female");
                break;
            case R.id.btnUserProfileOther:
                GenderOther();
                Log.e("on click ","other");
                break;


        }


    }

    // Async Process
    private class AsyncAddUserProfile extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            Log.e("user preuse preasyntask","log");
            super.onPreExecute();
            progressDialog = new ProgressDialog(UserProfileActivity.this);
            progressDialog.setTitle(getResources().getString(R.string.userprofile_sbm_title_val));
            progressDialog.setMessage(getResources().getString(R.string.userprofile_sbm_desc_val));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {

            try
            {

                int Age;
                String DOB, City, District;
                result = "";

                HashMap<String, String> app =  usm.getUserDetails();
                OTP_ID = app.get(usm.KEY_OTP_ID);
                if(OTP_ID == null || OTP_ID == "")
                    OTP_ID = "NULL";

                String LocationName = (etxtUserProfileLocation.getText().toString());
                if(LocationName.contains(" "))
                    LocationName = LocationName.toString().replace(" ","%20");


                if(!etxtUserProfileLocation.getText().toString().equals(Sub_location_name))
                {
                    location_name = "NULL";
                    disrtict_name = "NULL";
                }


                if(location_name != null) {
                    City = location_name;
                    if (City.contains(" "))
                        City = City.toString().replace(" ", "%20");
                }
                else
                {
                    City = "NULL";
                }


                if(disrtict_name != null) {
                    District =  disrtict_name;
                    if(District.contains(" "))
                        District = District.toString().replace(" ","%20");
                }
                else
                {
                    District = "NULL";
                }
                Log.e("new District  is = ",""+District );
                String Facebook = "NULL";
                String Google = "NULL";
                String Twitter = "NULL";

                if(usm.isFacebookLoggedInAlready())
                    Facebook = "FACEBOOK";
                if(usm.isGoogleLoggedInAlready())
                    Google = "GOOGLE";
                if(usm.isTwitterLoggedInAlready())
                    Twitter = "TWITTER";

                Log.e("Facebook  is = ",""+Facebook );
                Log.e("Google  is = ",""+Google );
                Log.e("Twitter  is = ",""+Twitter );

                if(etxtUserProfileAge.getText().toString().length() == 0)
                {
                    Age = 0;

                }
                else
                {
                    Age = Integer.parseInt(etxtUserProfileAge.getText().toString());

                }
                DOB = "NULL";


                /*if(etxtUserProfileDOB.getText().toString().length() == 0)
                {
                    DOB = "NULL";
                    Log.e("DOB  is null= ",""+DOB );
                }
                else
                {
                   DOB = etxtUserProfileDOB.getText().toString();
                    Log.e("DOB  is not null= ",""+DOB );
                }*/


                // InsertUserProfile/{Name}/{Gender}/{DOB}/{EmailID}/{OTPId}/{District}/{City}/{Location}/{FB}/{Google}/{Twitter}/{CompId}/{UserTypeId}/{CreatedBy}/{LoginType}/{Pincode}/{RegID}
                // http://localhost:56961/Master.svc/InsertUserProfile/Pratik%20Mehta/M/03-08-1992/pratikmehta01@gmail.com/1/Mumbai%20Suburban/Mumbai/Kandivali%20West/FB/Google/Twitter/1/1/1/GENERAL/400067/reg_id
/*
                UserProfileUrl = "http://frkout.geecs.in/Master.svc/InsertUserProfile/" + UsersName + "/" + Gender + "/" +
                DOB + "/"  + (etxtUserProfileEmailID.getText().toString()) + "/" + OTP_ID + "/" + District + "/" + City + "/" + LocationName  +
                 "/" + Facebook + "/" + Google + "/" + Twitter + "/NULL/NULL/NULL/GENERAL/" + pincode + "/" + regid + "/" + Age + "/" + FileName + "/" + FileByte ;
                result = c.GetData(UserProfileUrl);
*/


                JSONObject jsonObject = new JSONObject();
                Log.e("Json Created= ","json");
                jsonObject.put("Name", etxtUserProfileName.getText().toString());
                Log.e("Json Created= ", ""+etxtUserProfileName.getText().toString());
                jsonObject.put("Gender", Gender);
                jsonObject.put("DOB", DOB);
                jsonObject.put("EmailID", etxtUserProfileEmailID.getText().toString());
                jsonObject.put("OTPId", OTP_ID);
                jsonObject.put("District", District);
                jsonObject.put("City", City);
                jsonObject.put("Location", LocationName);
                jsonObject.put("FB", Facebook);
                jsonObject.put("Google", Google);
                jsonObject.put("Twitter", Twitter);
                jsonObject.put("CompId", "NULL");
                jsonObject.put("UserTypeId", "NULL");
                jsonObject.put("CreatedBy", "NULL");
                jsonObject.put("LoginType", "GENERAL");
                jsonObject.put("Pincode", pincode);
                jsonObject.put("RegID", regid);
                jsonObject.put("Age", Age);
                jsonObject.put("FileName", FileName);
                jsonObject.put("FileByte", FileByte);
                String json = jsonObject.toString();
                Log.e("json values= ",""+json );
                result = c.PostData(getResources().getString(R.string.InsertUserProfile), json);
                Log.e("json interest",""+result);
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
            Log.e("user profon onpost","log");
            progressDialog.dismiss();

            try
            {
                if(result != null && result != "")
                {
                    usm.editor.putString(usm.KEY_USER_ID, result.substring(1,result.length()-1));
                    usm.editor.putString(usm.KEY_USER_NAME, etxtUserProfileName.getText().toString());
                    usm.editor.putBoolean(usm.KEY_IS_USER_PROFILE_INFO_SETUP, true);
                    usm.editor.commit();
                    GotoUserProfileInterest();
                }
                else
                {
                    btnUserProfileSaveNext.setEnabled(true);
                    Toast.makeText(UserProfileActivity.this, getResources().getString(R.string.userprofile_sbm_err), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                btnUserProfileSaveNext.setEnabled(true);
                e.printStackTrace();
            }
        }
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private class Register extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... args)
        {
            Log.e("register asyn task","log");
            try
            {
                if (gcm == null)
                {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    regid = gcm.register(getResources().getString(R.string.gcm_sender_id));
                    usm.editor.putString(usm.KEY_GCM_REG_ID, regid);
                    usm.editor.commit();
                }
            }
            catch (IOException ex)
            {
                Log.e("Error", ex.getMessage());
                return "Fails";
            }
            catch (Exception ex)
            {
                Log.e("Error", ex.getMessage());
                return "Fails";
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String json)
        {
            new AsyncAddUserProfile().execute();
        }

    }

     class AsyncSelectUserInterest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            Log.e("select Int asn task ","log");
            try
            {
                result = "";
                // GetInterest
                // http://localhost:56961/Master.svc/GetInterest
                UserProfileUrl = "http://frkout.geecs.in/Master.svc/GetInterest";
                result = c.GetData(UserProfileUrl);
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
            Log.e("on post and layout","log");
            if(result != null)
            {
                try
                {
                    JSONObject jsonRootObject = new JSONObject(result);
                    jsonarray = jsonRootObject.optJSONArray("GetInterestResult");
                    n = jsonarray.length();
                    InterestList = new String[n];
                    InterestListID = new int[n];
                     listView=(ListView)findViewById(R.id.list_view);
                    //list=new ArrayList<NewclassforInterest>();

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        InterestListID[i] = jsonobject.getInt("Int_Id");
                        InterestList[i] = jsonobject.getString("Int_Name");
                        Log.e("Interest id", "" + InterestListID[i]);
                        Log.e("Interest list", "" + InterestList[i]);
                        NewclassforInterest newclassforInterest = new NewclassforInterest(InterestListID[i], InterestList[i]);
                        list.add(newclassforInterest);

                    }
                    Log.e("total list",list.toString());

                    lisrviewadapter=new CustomAdapter(getApplicationContext(),R.layout.raw_layout,list);
                    listView.setAdapter((ListAdapter) lisrviewadapter);
                    rlUser_Profile.setVisibility(View.GONE);
                    rlUser_Profile_2.setVisibility(View.VISIBLE);
                    mActionBar.setTitle(getResources().getString(R.string.user_profile_2_header_title));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {





                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long l)
                        {
                            if(CustomAdapter.chkBool[i]) {
                                a--;
                                CustomAdapter.chkBool[i] = false;
                                lisrviewadapter.notifyDataSetChanged();
                            }else{
                                a++;

                                CustomAdapter.chkBool[i] = true;
                                lisrviewadapter.notifyDataSetChanged();
                            }
                            mActionBar.setTitle("Selected Items...          "+a);
                            menu.getItem(0).setVisible(true);


                            Log.e("on item click","log");
                            interest = String.valueOf(list.get(i).getArea());
                            Toast.makeText(UserProfileActivity.this, interest, Toast.LENGTH_SHORT).show();


                        }
                    });




                    /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) LayoutParams.WRAP_CONTENT, (int) LayoutParams.WRAP_CONTENT);
                    for (int i = 0; i < InterestList.length; i++)
                    {
                        final Button btn = new Button(UserProfileActivity.this);
                        btn.setId(InterestListID[i]);
                        btn.setText(InterestList[i]);
                        btn.setTextColor(getResources().getColor(R.color.grey_light));
                        btn.setBackgroundResource(R.drawable.bg_button_semi_rounded_white);
                        params.setMargins(5, 5, 5, 5);
                        btn.setLayoutParams(params);
                        flUserProfileInterest.addView(btn);

                        btn.setOnClickListener(new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if(btn.getCurrentTextColor() == (getResources().getColor(R.color.grey_light)))
                                {
                                    btn.setTextColor(getResources().getColor(R.color.white));
                                    btn.setBackgroundResource(R.drawable.bg_button_semi_rounded_orange);
                                    SelectedInterestList.add(btn.getId());
                                }
                                else
                                {
                                    btn.setTextColor(getResources().getColor(R.color.grey_light));
                                    btn.setBackgroundResource(R.drawable.bg_button_semi_rounded_white);
                                    SelectedInterestList.remove(SelectedInterestList.indexOf(btn.getId()));
                                }
                            }
                        });
                    }

                    mActionBar.setTitle(getResources().getString(R.string.user_profile_2_header_title));*/
                    /*rlUser_Profile_1.setVisibility(View.GONE);
                    rlUser_Profile_2.setVisibility(View.VISIBLE);*/
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    // UserProfile Done
    public void UserProfileDone()
    {
        for(int i=0;i<list.size();i++)
        {
            if(CustomAdapter.chkBool[i]){
                list_of_item.add(list.get(i).getId());  //newclass=(Newclass)list.get(i)  see in dash board

            }



        }
  //      Log.e("list add1",String.valueOf(list_of_item.get(0).toString()));
   //     Log.e("list add2",String.valueOf(list_of_item.toArray()[1].toString()));

       // list_of_item.add(selection);

        //Log.e("list add",list_of_item.toArray()[I].toString());
        // Log.e("final result2",""+Arrays.toString(data));
        //Log.e("final result",""+lst);
     //   Log.e("final result2",list_of_item.toArray()[0].toString());
        //Toast.makeText(UserProfileActivity.this,"Thanks for using Frkout", Toast.LENGTH_LONG).show();






        StringBuilder sb = new StringBuilder();
        for(int i=0; i<list_of_item.size(); i++)
        {
            sb.append(String.valueOf(list_of_item.get(i)) + ",");
            Log.e("append",""+sb);

        }
        if(sb.length()>0)
            SelectedInterestID = sb.substring(0,sb.length()-1);
        else
            SelectedInterestID = "0";
        Log.e("selected ID",""+SelectedInterestID);
        btnUserProfile2Done.setEnabled(false);
        new AsyncAddUserInterest().execute();
    }


    private class AsyncAddUserInterest extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UserProfileActivity.this);
            progressDialog.setTitle(getResources().getString(R.string.userintrest_sbm_title_val));
            progressDialog.setMessage(getResources().getString(R.string.userintrest_sbm_desc_val));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                result = "";

                HashMap<String, String> app =  usm.getUserDetails();
                UsersId = app.get(usm.KEY_USER_ID);

                // InsertUserProfileInterest/{UsersId}/{IntId}/{CreatedBy}
                // http://localhost:56961/Master.svc/InsertUserProfileInterest/1/1,2,3,4,5/1
                UserProfileUrl = "http://frkout.geecs.in/Master.svc/InsertUserProfileInterest/" + UsersId + "/" + SelectedInterestID + "/" + UsersId;
                result = c.GetData(UserProfileUrl);
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
            try
            {
                progressDialog.dismiss();

                if(result != null && result != "")
                {
                    if (result.contains("Success"))
                    {
                        //Success
                        usm.editor.putBoolean(usm.KEY_IS_USER_PROFILE_INTEREST_SETUP, true);
                        usm.editor.commit();
                        startActivity(new Intent(UserProfileActivity.this, DashboardActivity.class));
                        finish();
                    }
                }
                else
                {
                    btnUserProfile2Done.setEnabled(true);
                    //edited botttom
                    //startActivity(new Intent(UserProfileActivity.this, DashboardActivity.class));
                    Toast.makeText(UserProfileActivity.this, getResources().getString(R.string.userintrest_sbm_err), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                btnUserProfile2Done.setEnabled(true);
                e.printStackTrace();
            }

        }
    }

    public void SetDOB(View V)
    {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(UserProfileActivity.this, new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                String sdayOfMonth, smonthOfYear;
                if(dayOfMonth < 10)
                {
                    sdayOfMonth = "0"+dayOfMonth;
                }
                else
                {
                    sdayOfMonth = ""+dayOfMonth;
                }
                monthOfYear = monthOfYear+1;
                if(monthOfYear < 10)
                {
                    smonthOfYear = "0"+monthOfYear;
                }
                else
                {
                    smonthOfYear = ""+monthOfYear;
                }
                etxtUserProfileDOB.setText((smonthOfYear) + "-" + sdayOfMonth + "-" + (year));
            }
        }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(new Date().getTime());
        dpd.show();
    }

    public void Setlocation(View V)
    {
        location();
    }

    public void location()
    {
        gps = new GPSTracker(UserProfileActivity.this, 10);
        if(gps != null)
        {
            // check if GPS enabled
            if (gps.canGetLocation())
            {
                location_name = gps.getLocation_name();
                Sub_location_name = gps.getSub_location_name();
                disrtict_name = gps.getDisrtict_name();
                pincode = gps.getPincode();

                etxtUserProfileLocation.setText(Sub_location_name);
            }
            else
            {
                // gps.showSettingsAlert();
                // Toast.makeText(UserProfileActivity.this, getResources().getString(R.string.location_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void tos(View view)
    {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.user_profile_1_tos))
                .setMessage(getResources().getString(R.string.user_profile_1_tos_description))
                .setPositiveButton("Done", null)
                .show();
        alertbox.setCanceledOnTouchOutside(true);
    }

    // FindViewById
    public void findViewById()
    {
        // UserProfile 1
        rlUser_Profile = (LinearLayout) findViewById(R.id.rlUser_Profile);

        //swtUserProfileGender = (Switch) findViewById(R.id.swtUserProfileGender);

        etxtUserProfileName = (EditText) findViewById(R.id.etxtUserProfileName);
        //etxtUserProfileDOB = (EditText) findViewById(R.id.etxtUserProfileDOB);
        etxtUserProfileAge = (EditText) findViewById(R.id.etxtUserProfileAge);
        etxtUserProfileEmailID = (EditText) findViewById(R.id.etxtUserProfileEmailID);
        etxtUserProfileLocation = (EditText) findViewById(R.id.etxtUserProfileLocation);
        //rgUserProfileGender = (RadioGroup) findViewById(R.id.rgUserProfileGender);
        //rdbUserProfileMale = (RadioButton) findViewById(R.id.rdbUserProfileMale);

        ivUserProfile = (ImageView) findViewById(R.id.ivUserProfile);

       // chkUserProfileAgree = (CheckBox) findViewById(R.id.chkUserProfileAgree);

        btnUserProfileMale = (ImageView) findViewById(R.id.btnUserProfileMale);
        btnUserProfileFemale = (ImageView) findViewById(R.id.btnUserProfileFemale);
        btnUserProfileOther = (ImageView) findViewById(R.id.btnUserProfileOther);
        btnUserProfileSaveNext = (Button) findViewById(R.id.btnUserProfileSaveNext);

        // UserProfile 2
        rlUser_Profile_2 = (RelativeLayout) findViewById(R.id.rlUser_Profile_2);

        btnUserProfile2Done = (Button) findViewById(R.id.btnUserProfile2Done);
        //flUserProfileInterest = (FlowLayout) findViewById(R.id.flUserProfileInterest);

    }

    @Override
    protected void onResume()
    {
        Log.e("onRessume","on");
        super.onResume();
        ivUserProfile = (ImageView) findViewById(R.id.ivUserProfile);

        if(!(TextUtils.isEmpty(name)))
            etxtUserProfileName.setText(name);
        if(!(TextUtils.isEmpty(emailID)))
        etxtUserProfileEmailID.setText(emailID);
        if(!(TextUtils.isEmpty(birthday)))
            etxtUserProfileAge.setText(""+age);
    }


    public static Bitmap getThumbnail(ContentResolver cr, String path)
    {
        try
        {
            Cursor ca = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns._ID},
                    MediaStore.MediaColumns.DATA + "=?", new String[]{path}, null);
            if (ca != null && ca.moveToFirst())
            {
                int id = ca.getInt(ca.getColumnIndex(MediaStore.MediaColumns._ID));
                ca.close();
                return MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
            }
            ca.close();
        }
        catch (Exception e)
        {
            System.out.println("Exception : " + e);
        }
        return null;
    }


    private class loadimage extends AsyncTask<String,String,Bitmap>
    {


        @Override
        protected Bitmap doInBackground(String... args) {
            Log.e("do in back","log");
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            Log.e("on pre excute","log");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.e("on post","log");
            ivUserProfile.setImageBitmap(bitmap);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.img_quality), outputStream);
            byte[] bytes_pic = outputStream.toByteArray();
            strImageName = getResources().getString(R.string.img) + c.getDDMMYYCurrentDateTime() + ".Jpeg";
            strImgByte = Base64.encodeToString(bytes_pic, Base64.NO_WRAP);
            FileName = strImageName;
            FileByte = strImgByte;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu=menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.mnMain_Submit)
        {
            UserProfileDone();
            //usm.Clear();
            //Toast.makeText(this,getResources().getString(R.string.clear),Toast.LENGTH_SHORT).show();
            //return true;
        }
        return super.onOptionsItemSelected(item);
    }



}

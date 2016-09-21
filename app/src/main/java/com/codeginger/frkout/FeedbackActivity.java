package com.codeginger.frkout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import android.view.View.OnClickListener;

import android.view.KeyEvent;
import android.view.KeyEvent;
import android.widget.ImageView.ScaleType;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

// Created by Pratik Mehta

public class FeedbackActivity extends ActionBarActivity implements View.OnClickListener
{
    ActionBar mActionBar;

    RelativeLayout rlFBMedia, rlFBAddMedia, rlFBAddCat, rlFBAddBrandSocial, rlFBSuccess,relativeLayoutBrands,relativeLayoutSocial,relativeLayoutForFeebBackSuceess;
    LinearLayout llFBAddMediaTray;
    EditText etxtFBDesc, etxtFBLocation,etxtFBDesc1,editExperienceLocation;
    AutoCompleteTextView actxtFBIndustry, actxtFBBrand, actxtFBSocial;
    Spinner spFBIndustry;
    ImageView imgFeedback;
    TextView txtFBTitle, txtFBAddMediaTitle, txtFBAddMoreMediaTitle, txtFBAddMediaSkip, txtFBAddMoreMediaDone,txtFBTime,txtCamera,textView2;
    ImageButton imgFBDescEdit, imgFBMediaNo, imgFBMediaYes, imgFBAddMediaTakeImg, imgFBAddMediaUploadImg,TakeMediaImage;
    Button btnFBBrands, btnFBSocial, btnFBSubmit, btnFBSuccessAddmore,btnFeedbackSubmit,btnSelectCompany,buttonSocial,buttongps,buttonsubmit,btnsubmitoksocialbrands,FbBtnAddmore;
    HorizontalScrollView hSVFBAddMediaTray;
    ImageButton objImgBtnClose,objImgBtn;
    String strCategory, strBrandSocialName,strBrandsCompanyName,strsocialName;
    LinearLayout linearFeedbackSubmit,liearFistFeedback;

    String USER_ID, strImageName = "", CurrentPhotoPath, strImgByte = "";
    String SelectedBrands="" ,SelectedSocialBrands="" ,SelectedLocation="";
    public final String FeedbackMed_Type ="";
    public final String FeedbackMed_FileName="";
    public final String FeedbackMed_FileByte="";
    private Bitmap photo;
    private static final int CAMERA_REQUEST = 0, VIDEO_REQUEST = 1, SELECT_IMAGE = 2,SELECT_VIDEO = 3, SELECT_IMAGE_VIDEO = 4;
    File Imagefile;
    Uri selectedImage;
    int imageRotate = 0;

    Common c;
    UserSessionManager usm;
    GPSTracker gps;
    String Sub_location_name, location_name,  district_name;
    private ArrayList<FeedbackMedia> alFBMed;
    JSONArray jAryFBMed;
    JSONObject jObjFBMed;
    String result;
    int intid = 0;
    //String[] lsIndustry, lsBrand, lsSocial;
    String[] lsIndustry, lsBrand, lsSocial;
    public static ArrayList<NewclassforInterest> list_feedback = new ArrayList<NewclassforInterest>();

    public CustomAdapter feedback_adapter;
    public ListView ListViewOnSelect;
    private String social;
    private RelativeLayout feedback_listview,relativeLayoutIn,relativeLayout4;
    public static ArrayList<NewclassforInterest> list_feedback2= new ArrayList<NewclassforInterest>();
    private int reffer_value;
    private Menu menu;

    //String[] lsIndustry = {"ENTERTAINMENT","F & B","AUTOMOBILES","POLITICS","HEALTHCARE","IT","EDUCATION","MEDIA","PHARMA","NATURAL RESOURCES","COMMODITIES","STOCK EXCHANGE","TELE COMMUNICATION"};
     //String[] lsBrand = {"Rupali","Maruti","PayTm","Maruti Suzuki","yahoo","maruthi","Monaco","Images","Rajat","CG","code g", "Santosh avduta","mahindra ","Testing Images"};
     //String[] lsSocial = {"DMC","Test","freedom india","gaurav","goregaon potholes ","Kandivali west ", "sports event","app status","t 20 cup","India", "cricket","cool pc","USA"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity_new);
        findViewById();
        gps_auto_connect();

        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));
        new AsyncGetFbIntBrdSoc().execute();



        Calendar calendar = Calendar.getInstance();
        DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
        String formattedTime = outputFormat.format(calendar.getTime());
        txtFBTime.setText(formattedTime);
        btnSelectCompany.setOnClickListener(this);
        buttonSocial.setOnClickListener(this);
        btnsubmitoksocialbrands.setOnClickListener(this);

        buttonsubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                feeback_submit();
                if (!(strCategory.length() >0))
                {
                    Toast.makeText(getApplicationContext(),"Please",Toast.LENGTH_SHORT).show();

                }else {


                    new FeedBackSubmitActivity().execute();

                }

            }
        });





        TakeMediaImage.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

                TakeMediaImage.startAnimation(animFadein);
                CharSequence choices[] = new CharSequence[]{"Take Pictures" ,"Upload Pictures"};
                AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);

                builder.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which ==0)
                        {
                            try {


                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                strImageName = getResources().getString(R.string.img) + c.getDDMMYYCurrentDateTime() + ".Jpeg";
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }catch(Exception e)
                            {
                                System.out.print(e);

                            }


                        }
                        else if(which == 1)
                        {
                            strImageName = getResources().getString(R.string.img) + c.getDDMMYYCurrentDateTime() + ".Jpeg";
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, getResources().getText(R.string.fb_takeImage_camera)), SELECT_IMAGE);
                        }


                    }
                });
                builder.show();


            }
        });

        FbBtnAddmore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedbackActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });



      /*  alFBMed = new ArrayList<FeedbackMedia>();
        location();
        // Get Feedback Interest, Brand, Social
        new AsyncGetFbIntBrdSoc().execute();
        //new GetLocation().execute();

        imgFBAddMediaTakeImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    strImageName = getResources().getString(R.string.img) + c.getDDMMYYCurrentDateTime() + "_" + USER_ID + ".Jpeg";
                    Imagefile = new File(Environment.getExternalStoragePublicDirectory(getResources().getString(R.string.app_name)) + File.separator + getResources().getString(R.string.image_folder), strImageName);
                    CurrentPhotoPath = Imagefile.getAbsolutePath();
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(Imagefile));
                    cameraIntent.putExtra("Cancel",true);
                    startActivityForResult(cameraIntent,CAMERA_REQUEST);
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
        });


        imgFBAddMediaUploadImg.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                strImageName = getResources().getString(R.string.img) + c.getDDMMYYCurrentDateTime() + "_"  + USER_ID + ".Jpeg";
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image*//*");
                startActivityForResult(Intent.createChooser(intent, getResources().getText(R.string.broad_creative_imageupload_validation)), SELECT_IMAGE);
            }
        });

        imgFBMediaNo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(isFBDescExist()) {
                    txtFBTitle.setVisibility(View.GONE);
                    rlFBMedia.setVisibility(View.GONE);
                    rlFBAddCat.setVisibility(View.VISIBLE);
                }
            }
        });

        imgFBMediaYes.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if(isFBDescExist()) {
                    txtFBTitle.setVisibility(View.GONE);
                    rlFBMedia.setVisibility(View.GONE);
                    rlFBAddMedia.setVisibility(View.VISIBLE);
                }
            }
        });

        txtFBAddMediaSkip.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                rlFBAddMedia.setVisibility(View.GONE);
                rlFBAddCat.setVisibility(View.VISIBLE);
            }
        });

        txtFBAddMoreMediaDone.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                rlFBAddMedia.setVisibility(View.GONE);
                rlFBAddCat.setVisibility(View.VISIBLE);
            }
        });

        btnFBBrands.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                strCategory = "BRAND";
                rlFBAddCat.setVisibility(View.GONE);
                rlFBAddBrandSocial.setVisibility(View.VISIBLE);
                actxtFBBrand.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adpBrand = new ArrayAdapter<String>(FeedbackActivity.this,android.R.layout.simple_list_item_1, lsBrand);
                actxtFBBrand.setThreshold(0);
                actxtFBBrand.setAdapter(adpBrand);
                ArrayAdapter<String> adpIndustry = new ArrayAdapter<String>(FeedbackActivity.this, android.R.layout.simple_spinner_item, lsIndustry);
                spFBIndustry.setAdapter(adpIndustry);
            }
        });

        btnFBSocial.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                strCategory = "SOCIAL";
                rlFBAddCat.setVisibility(View.GONE);
                rlFBAddBrandSocial.setVisibility(View.VISIBLE);
                actxtFBSocial.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adpSocial = new ArrayAdapter<String>(FeedbackActivity.this,android.R.layout.simple_list_item_1, lsSocial);
                actxtFBSocial.setThreshold(0);
                actxtFBSocial.setAdapter(adpSocial);
                ArrayAdapter<String> adpIndustry = new ArrayAdapter<String>(FeedbackActivity.this, android.R.layout.simple_spinner_item, lsIndustry);
                spFBIndustry.setAdapter(adpIndustry);

            }
        });

        btnFBSubmit.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                if (isFBCatSubmit())
                {
                    if(!etxtFBLocation.getText().toString().equals(Sub_location_name))
                    {
                        location_name = "";
                        district_name = "";
                    }
                    btnFBSubmit.setEnabled(false);

                    InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    new FeedbackSubmit().execute();
                }
            }
        });

        btnFBSuccessAddmore.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                Intent i = new Intent(FeedbackActivity.this, FeedbackActivity.class);
                startActivity(i);
                finish();
            }
        });*/



/*
        etxtFBDesc.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    txtFBTitle.setVisibility(View.GONE);
                    rlFBMedia.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

        btnFBDescDone.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                txtFBTitle.setVisibility(View.GONE);
                etxtFBDesc.setEnabled(false);
                imgFBDescEdit.setVisibility(View.VISIBLE);
                rlFBAddCat.setVisibility(View.VISIBLE);
            }
        });

        imgFBDescEdit.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                etxtFBDesc.setEnabled(true);
                imgFBDescEdit.setVisibility(View.GONE);
                btnFBDescDone.setVisibility(View.VISIBLE);
            }
        });
*/

    }

    private void gps_auto_connect()
    {
        gps = new GPSTracker(FeedbackActivity.this, 10);
        location_name = gps.getLocation_name();
        Sub_location_name = gps.getSub_location_name();
        district_name = gps.getDisrtict_name();
        editExperienceLocation.setText(Sub_location_name);
        SelectedLocation =editExperienceLocation.getText().toString();
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
            btnsubmitoksocialbrands();
            //usm.Clear();
            //Toast.makeText(this,getResources().getString(R.string.clear),Toast.LENGTH_SHORT).show();
            //return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void btnsubmitoksocialbrands()
    {
        feedback_listview=(RelativeLayout)findViewById(R.id.feedback_listview);
        linearFeedbackSubmit = (LinearLayout) findViewById(R.id.linearFeedbackSubmit);
        linearFeedbackSubmit.setVisibility(View.VISIBLE);
        feedback_listview.setVisibility(View.GONE);
        if(reffer_value==1)
        {
            strCategory="Brands";
            btnSelectCompany.setText(social);
            buttonSocial.setEnabled(false);
            SelectedSocialBrands =btnSelectCompany.getText().toString();
                  /*  relativeLayoutBrands=(RelativeLayout)findViewById(R.id.relativeLayoutBrands);
                    relativeLayoutSocial=(RelativeLayout)findViewById(R.id.relativeLayoutSocial);
                    relativeLayoutBrands.setVisibility(View.VISIBLE);
                    relativeLayoutSocial.setVisibility(View.GONE);*/


        }else
        {
            strCategory="Social";
            buttonSocial.setText(social);
            btnSelectCompany.setEnabled(false);
            SelectedSocialBrands =buttonSocial.getText().toString();
                   /* relativeLayoutBrands=(RelativeLayout)findViewById(R.id.relativeLayoutBrands);
                    relativeLayoutSocial=(RelativeLayout)findViewById(R.id.relativeLayoutSocial);
                    relativeLayoutBrands.setVisibility(View.GONE);
                    relativeLayoutSocial.setVisibility(View.VISIBLE);*/
        }

    }


    @Override
    public void onClick(View view)
    {
        Log.e(" click on","log");
        switch (view.getId())
        {
            case R.id.btnSelectCompany:
                reffer_value=1;
                feedback_0n_list();
                mActionBar.setTitle("Select company/Brand");
                menu.getItem(0).setVisible(true);
                Log.e("feedback 1 click on","log");
                feedback_listview=(RelativeLayout)findViewById(R.id.feedback_listview);
                linearFeedbackSubmit = (LinearLayout) findViewById(R.id.linearFeedbackSubmit);
                linearFeedbackSubmit.setVisibility(View.GONE);
                feedback_listview.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonSocial:
                reffer_value=2;
                feedback_0n_list();
                mActionBar.setTitle("Select social");
                menu.getItem(0).setVisible(true);
                Log.e("feedback 2 click on","log");
                feedback_listview=(RelativeLayout)findViewById(R.id.feedback_listview);
                linearFeedbackSubmit = (LinearLayout) findViewById(R.id.linearFeedbackSubmit);
                linearFeedbackSubmit.setVisibility(View.GONE);
                feedback_listview.setVisibility(View.VISIBLE);
                break;
          /*  case R.id.btnsubmitoksocialbrands:
                feedback_listview=(RelativeLayout)findViewById(R.id.feedback_listview);
                linearFeedbackSubmit = (LinearLayout) findViewById(R.id.linearFeedbackSubmit);
                linearFeedbackSubmit.setVisibility(View.VISIBLE);
                feedback_listview.setVisibility(View.GONE);
                if(reffer_value==1)
                {
                    strCategory="Brands";
                    btnSelectCompany.setText(social);
                    buttonSocial.setEnabled(false);
                    SelectedSocialBrands =btnSelectCompany.getText().toString();
                  *//*  relativeLayoutBrands=(RelativeLayout)findViewById(R.id.relativeLayoutBrands);
                    relativeLayoutSocial=(RelativeLayout)findViewById(R.id.relativeLayoutSocial);
                    relativeLayoutBrands.setVisibility(View.VISIBLE);
                    relativeLayoutSocial.setVisibility(View.GONE);*//*


                }else
                {
                    strCategory="Social";
                    buttonSocial.setText(social);
                    btnSelectCompany.setEnabled(false);
                    SelectedSocialBrands =buttonSocial.getText().toString();
                   *//* relativeLayoutBrands=(RelativeLayout)findViewById(R.id.relativeLayoutBrands);
                    relativeLayoutSocial=(RelativeLayout)findViewById(R.id.relativeLayoutSocial);
                    relativeLayoutBrands.setVisibility(View.GONE);
                    relativeLayoutSocial.setVisibility(View.VISIBLE);*//*
                }*/





        }

    }

    private  void feeback_submit()
    {
        if(strCategory=="Brands")
        {
            SelectedSocialBrands =btnSelectCompany.getText().toString();

        }else if(strCategory == "Social")
        {
            SelectedSocialBrands =buttonSocial.getText().toString();

        }
    }

    private void feedback_0n_list()
    {
        Log.e("feedback list on","log");
        if(reffer_value==1)
        {
            Log.e("value=1","log");
            feedback_adapter=new CustomAdapter(getApplicationContext(),R.layout.raw_layout,list_feedback2);
        }else
        {
            Log.e("value=2","log");
            feedback_adapter=new CustomAdapter(getApplicationContext(),R.layout.raw_layout,list_feedback);

        }


        ListViewOnSelect.setAdapter(feedback_adapter);
       // rlUser_Profile.setVisibility(View.GONE);
        //rlUser_Profile_2.setVisibility(View.VISIBLE);
        //mActionBar.setTitle(getResources().getString(R.string.user_profile_2_header_title));
        ListViewOnSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l)
            {

                if(CustomAdapter.chkBool[i]) {
                   // a--;
                    CustomAdapter.chkBool[i] = false;
                    runOnUiThread(new Runnable(){
                        public void run() {
                            Log.e("UI thread", "I am the UI thread");
                            feedback_adapter.notifyDataSetChanged();
                        }
                    });



                    //feedback_adapter.notifyDataSetChanged();
                }else{
                    //a++;
                   // CustomAdapter.chkBool=new boolean[UserProfileActivity.list.size()];
                    for (int n=0; n <CustomAdapter.list.size(); n++)
                    {
                        CustomAdapter.chkBool[n]=false;
                    }
                    CustomAdapter.chkBool[i] = true;
                    runOnUiThread(new Runnable(){
                        public void run() {
                            Log.e("UI thread", "I am the UI thread");
                            feedback_adapter.notifyDataSetChanged();
                        }
                    });
                    //feedback_adapter.notifyDataSetChanged();
                }
              //  mActionBar.setTitle("Selected Items...            "+a);


                Log.e("on item feedback click","log");
                if(reffer_value==1)
                {
                    social = String.valueOf(list_feedback2.get(i).getArea());
                }else
                {
                    social = String.valueOf(list_feedback.get(i).getArea());
                }

                Toast.makeText(getApplicationContext(), social, Toast.LENGTH_SHORT).show();


            }
        });


    }

    /*  @Override
      public void onResume() {
          super.onResume();

      }
  */
    class FeedbackMedia
    {
        public final String FeedbackMed_Type;
        public final String FeedbackMed_FileName;
        public final String FeedbackMed_FileByte;

        public FeedbackMedia( String FeedbackMed_FileName, String FeedbackMed_FileByte, String FeedbackMed_Type)
        {
            this.FeedbackMed_Type = FeedbackMed_Type;
            this.FeedbackMed_FileName = FeedbackMed_FileName;
            this.FeedbackMed_FileByte = FeedbackMed_FileByte;
        }
    }

    private class GetLocation extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... args)
        {
            location();
            return null;
        }
    }

    private class FeedBackSubmitActivity extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            Log.e("Json value","Value send Start");
            super.onPreExecute();
            progressDialog = new ProgressDialog(FeedbackActivity.this);
            progressDialog.setTitle(getResources().getString(R.string.fb_sbm_title_val));
            progressDialog.setMessage(getResources().getString(R.string.fb_sbm_desc_val));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            String result = "";
            try
            {
                jAryFBMed = new JSONArray();
                jObjFBMed = new JSONObject();
                /*jObjFBMed.put("FeedbackMed_Type", alFBMed.get(0).FeedbackMed_Type);
                jObjFBMed.put("FeedbackMed_FileName", alFBMed.get(0).FeedbackMed_FileName);
                jObjFBMed.put("FeedbackMed_FileByte", alFBMed.get(0).FeedbackMed_FileByte);*/
                jObjFBMed.put("FeedbackMed_Type", "IMAGE");
                jObjFBMed.put("FeedbackMed_FileName", strImageName);
                jObjFBMed.put("FeedbackMed_FileByte", strImgByte);
                Log.e("About Image",jObjFBMed.toString());
                jAryFBMed.put(jObjFBMed);




               /* if(alFBMed.size() > 0)
                {*/
                    // Feedback Image Issued due to WCF webconfig.
                   /* for (int i = 0; i < alFBMed.size(); i++)
                    //for (int i = 0; i < 1; i++)
                    {
                        *//*jObjFBMed = new JSONObject();
                        *//**//*jObjFBMed.put("FeedbackMed_Type", alFBMed.get(i).FeedbackMed_Type);*//**//*
                        jObjFBMed.put("FeedbackMed_FileName", alFBMed.get(i).FeedbackMed_FileName);
                        jObjFBMed.put("FeedbackMed_FileByte", alFBMed.get(i).FeedbackMed_FileByte);
                        jAryFBMed.put(jObjFBMed);*//*
                    }*/
               /* }*/


                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Feedback_Description", etxtFBDesc1.getText().toString());
                jsonObject.put("Feedback_Category", strCategory);
                jsonObject.put("Feedback_BrandSocialName", SelectedSocialBrands);
                jsonObject.put("Feedback_LocationName", SelectedLocation);
                jsonObject.put("Feedback_CityName","");
                jsonObject.put("Feedback_DistrictName", " ");
                jsonObject.put("Feedback_IntName", " ");
                jsonObject.put("Feedback_UsersID", USER_ID);
                jsonObject.put("FeedbackMedia", jAryFBMed);

                String json = jsonObject.toString();
                result = c.PostData(getResources().getString(R.string.InsertFeedback), json);
                Log.e("result of the Json",jsonObject.toString());
            }
            catch (Exception e)
            {
                System.out.print(e);
            }
            return result;
        }

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
                        liearFistFeedback.setVisibility(View.GONE);
                        linearFeedbackSubmit.setVisibility(View.GONE);
                        feedback_listview.setVisibility(View.GONE);
                        relativeLayoutForFeebBackSuceess.setVisibility(View.VISIBLE);

                        Log.e("Sucess is ","Working");
                        //Toast.makeText(FeedbackActivity.this, "Feedback Send Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                   /* btnFBSubmit.setEnabled(true);*/
                    /*Toast.makeText(FeedbackActivity.this,getResources().getString(R.string.fb_sbm_err), Toast.LENGTH_SHORT).show();*/
                    Toast.makeText(FeedbackActivity.this,"Not SuccesFul", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                /*btnFBSubmit.setEnabled(true);*/
                e.printStackTrace();
            }
        }
    }

    private class AsyncGetFbIntBrdSoc extends AsyncTask<String, Void, String>
    {
        String result = "";
        @Override
        protected String doInBackground(String... urls)
        {

            result = c.GetData("http://frkout.geecs.in/Master.svc/GetFbIntBrdSoc");
            if(result != null)
            {
                try
                {
                    JSONObject jsonRootObject = new JSONObject(result);
                    JSONArray jsonArrayFbInt = jsonRootObject.optJSONArray("GetFbIntBrdSocResult").getJSONObject(0).getJSONArray("FbInt");
                    JSONArray jsonArrayFbBrd = jsonRootObject.optJSONArray("GetFbIntBrdSocResult").getJSONObject(0).getJSONArray("FbBrd");
                    JSONArray jsonArrayFbSoc = jsonRootObject.optJSONArray("GetFbIntBrdSocResult").getJSONObject(0).getJSONArray("FbSoc");

                    int intFbIntCount = jsonArrayFbInt.length();
                    int intFbBrdCount = jsonArrayFbBrd.length();
                    int intFbSocCount = jsonArrayFbSoc.length();

                    if(intFbIntCount > 0)
                    {
                        lsIndustry = new String[intFbIntCount];
                        for (int i = 0; i < intFbIntCount; i++)
                        {
                            JSONObject jsonobject = jsonArrayFbInt.getJSONObject(i);
                            lsIndustry[i] = jsonobject.getString("Name");
                            Log.e("industry all json",""+ Arrays.toString(lsIndustry));


                        }
                    }

                    if(intFbBrdCount > 0)
                    {
                        lsBrand = new String[intFbBrdCount];
                        for (int i = 0; i < intFbBrdCount; i++)
                        {
                            JSONObject jsonobject = jsonArrayFbBrd.getJSONObject(i);
                            lsBrand[i] = jsonobject.getString("Name");
                            Log.e("social all json",""+ Arrays.toString(lsBrand));
                            NewclassforInterest newclassforInterest2 = new NewclassforInterest(0,lsBrand[i]);
                            list_feedback2.add(newclassforInterest2);




                        }
                    }

                    if(intFbSocCount > 0)
                    {
                        lsSocial = new String[intFbSocCount];
                        for (int i = 0; i < intFbSocCount; i++)
                        {
                            JSONObject jsonobject = jsonArrayFbSoc.getJSONObject(i);
                            lsSocial[i] = jsonobject.getString("Name");
                            Log.e("social all json",""+ Arrays.toString(lsSocial));
                            NewclassforInterest newclassforInterest = new NewclassforInterest(0,lsSocial[i]);
                            list_feedback.add(newclassforInterest);



                        }
                    }


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

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }

    public void fetchlocation(View V)
    {
        location();
    }

    public void location()
    {
        gps = new GPSTracker(FeedbackActivity.this, 10);
        if(gps != null)
        {
            // check if GPS enabled
            if (gps.canGetLocation())
            {
                location_name = gps.getLocation_name();
                Sub_location_name = gps.getSub_location_name();
                district_name = gps.getDisrtict_name();
                editExperienceLocation.setText(Sub_location_name);
                SelectedLocation =editExperienceLocation.getText().toString();
            }
            else
            {
                //gps.showSettingsAlert();
            }
        }
    }
/*
    private boolean isFBDescExist()
    {
        if(etxtFBDesc.getText().toString().length() > 0)
        {
            return true;
        }
        else
        {
            etxtFBDesc.setError(getResources().getString(R.string.fb_desc_validation));
            return false;
        }
    }

    private boolean isFBCatSubmit()
    {
        if(strCategory.equals("BRAND"))
        {
            if(actxtFBBrand.getText().toString().length() > 0 && etxtFBLocation.getText().toString().length() > 0)
            {
                strBrandSocialName = actxtFBBrand.getText().toString();
                return true;
            }
            else
            {
                if(!(actxtFBBrand.getText().toString().length() > 0))
                {
                    actxtFBBrand.setError(getResources().getString(R.string.select_brand_validation));
                }
                if(!(etxtFBLocation.getText().toString().length() > 0))
                {
                    etxtFBLocation.setError(getResources().getString(R.string.select_location_validation));
                }
                return false;
            }
        }
        else if(strCategory.equals("SOCIAL"))
        {
            if(actxtFBSocial.getText().toString().length() > 0 && etxtFBLocation.getText().toString().length() > 0)
            {
                strBrandSocialName = actxtFBSocial.getText().toString();
                return true;
            }
            else
            {
                if(!(actxtFBSocial.getText().toString().length() > 0))
                {
                    actxtFBSocial.setError(getResources().getString(R.string.select_social_validation));
                }
                if(!(etxtFBLocation.getText().toString().length() > 0))
                {
                    etxtFBLocation.setError(getResources().getString(R.string.select_location_validation));
                }
                return false;
            }
        }
        return false;
    }*/

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
                            selectedImage = Uri.fromFile(new File(CurrentPhotoPath));


                           /* BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            bmOptions.inJustDecodeBounds = false;
                            bmOptions.inSampleSize = 1;
                            photo = BitmapFactory.decodeFile(CurrentPhotoPath, bmOptions);
                            selectedImage = Uri.fromFile(new File(CurrentPhotoPath));*/

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
                            photo = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            CurrentPhotoPath = selectedImage.getPath();
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
                  //  ImageButton objImgBtn = new ImageButton (getApplicationContext());
                  //  objImgBtn.setBackgroundDrawable(new BitmapDrawable(getResources(), photo));
                  //  objImgBtn.setScaleType(ScaleType.FIT_XY);
                  //  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(82,82);
                  //  params.setMargins(5, 5, 5, 5);
                  //  objImgBtn.setLayoutParams(params);
                  //  llFBAddMediaTray.addView(objImgBtn);


                   // intid = intid + 1;
                   /* final RelativeLayout objRelativeLayout = new RelativeLayout (getApplicationContext());*/
                   // objRelativeLayout.setId(intid);
                   /* objRelativeLayout.setBackgroundResource(R.drawable.frame);*/
                    //RelativeLayout.LayoutParams paramsRel = new RelativeLayout.LayoutParams(24, 24);
                   /* RelativeLayout.LayoutParams paramsRel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    objRelativeLayout.setLayoutParams(paramsRel);

                    objImgBtn = new ImageButton (getApplicationContext());
                    objImgBtn.setBackgroundDrawable(new BitmapDrawable(getResources(), photo));
                    objImgBtn.setScaleType(ScaleType.FIT_XY);*/
                   // objImgBtn.setId(intid);
                    /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150,150);
                    params.setMargins(5, 5, 5, 5);
                    objImgBtn.setLayoutParams(params);
                    objRelativeLayout.addView(objImgBtn);

                    objImgBtnClose = new ImageButton (getApplicationContext());
                    objImgBtnClose.setBackgroundResource(R.mipmap.close_black);
                    objImgBtnClose.setScaleType(ScaleType.FIT_XY);*/
                   // objImgBtn.setId(intid);
                   /* RelativeLayout.LayoutParams paramsClose = new RelativeLayout.LayoutParams(40,40);
                    paramsClose.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    paramsClose.addRule(RelativeLayout.RIGHT_OF);
                    objImgBtnClose.setLayoutParams(paramsClose);
                    objRelativeLayout.addView(objImgBtnClose);
                    llFBAddMediaTray.addView(objRelativeLayout);

                    objImgBtnClose.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                objRelativeLayout.setVisibility(View.GONE);
                                //llFBAddMediaTray.removeView(objImgBtn);
                                llFBAddMediaTray.removeViewInLayout(objRelativeLayout);
                            }
                        });*/

                    // Image Encoding
                    if(photo != null)
                    {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.img_quality), outputStream);
                        byte[] bytes_pic = outputStream.toByteArray();
                        strImgByte = Base64.encodeToString(bytes_pic, Base64.NO_WRAP);
                    }
                    imgFeedback.setImageBitmap(photo);

                  /*  alFBMed.add(new FeedbackMedia("IMAGE", strImageName, strImgByte));*/

                   /* alFBMed.add(new FeedbackMedia("IMAGE", strImageName, strImgByte));*/
                   /* alFBMed.add(new FeedbackMedia(strImageName, strImgByte));*/

                    /*txtFBAddMediaTitle.setVisibility(View.GONE);
                    txtFBAddMediaSkip.setVisibility(View.GONE);
                    txtFBAddMoreMediaTitle.setVisibility(View.VISIBLE);
                    txtFBAddMoreMediaDone.setVisibility(View.VISIBLE);
*/



                }
                catch (Exception e)
                {
                    System.out.println("Exception : " + e);
                }
            }
            else
            {
                strImageName = "";
            }
        }
    }


    public  void SubmitFeedback(View view)
    {

        if(
                etxtFBDesc1.getText().toString().trim().length() <=0
                )
        {
            Toast.makeText(getApplicationContext(),"Please Enter Text",Toast.LENGTH_SHORT).show();
        }else if (!( photo != null) )
        {
            Toast.makeText(getApplicationContext(),"Please Enter Image",Toast.LENGTH_SHORT).show();

        }else {
            linearFeedbackSubmit = (LinearLayout) findViewById(R.id.linearFeedbackSubmit);
            liearFistFeedback = (LinearLayout) findViewById(R.id.liearFistFeedback);
            liearFistFeedback.setVisibility(View.GONE);
            linearFeedbackSubmit.setVisibility(View.VISIBLE);
        }

    }

   /* public void fetchlocation(View V)
    {
        location();

    }

    public void location()
    {
        gps = new GPSTracker(FeedbackActivity.this, 10);
        if(gps != null)
        {
            // check if GPS enabled
            if (gps.canGetLocation())
            {
                location_name = gps.getLocation_name();
                Sub_location_name = gps.getSub_location_name();
                district_name = gps.getDisrtict_name();
                etxtFBLocation.setText(Sub_location_name);
            }
            else
            {
                //gps.showSettingsAlert();
            }
        }
    }*/




/*
    private View.OnClickListener ClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.btn_sign_in:
                    break;
            }
        }
    };
*/

    // FindViewById
    public void findViewById()
    {
       /* rlFBMedia = (RelativeLayout) findViewById(R.id.rlFBMedia);
        rlFBAddMedia = (RelativeLayout) findViewById(R.id.rlFBAddMedia);
        rlFBAddCat = (RelativeLayout) findViewById(R.id.rlFBAddCat);
        rlFBAddBrandSocial = (RelativeLayout) findViewById(R.id.rlFBAddBrandSocial);
        rlFBSuccess = (RelativeLayout) findViewById(R.id.rlFBSuccess);

        llFBAddMediaTray = (LinearLayout) findViewById(R.id.llFBAddMediaTray);

        etxtFBDesc = (EditText) findViewById(R.id.etxtFBDesc);
        etxtFBLocation = (EditText) findViewById(R.id.etxtFBLocation);

        actxtFBIndustry = (AutoCompleteTextView) findViewById(R.id.actxtFBIndustry);
        actxtFBBrand = (AutoCompleteTextView) findViewById(R.id.actxtFBBrand);
        actxtFBSocial = (AutoCompleteTextView) findViewById(R.id.actxtFBSocial);

        spFBIndustry = (Spinner) findViewById(R.id.spFBIndustry);

        txtFBTitle = (TextView) findViewById(R.id.txtFBTitle);
        txtFBAddMediaTitle = (TextView) findViewById(R.id.txtFBAddMediaTitle);
        txtFBAddMoreMediaTitle = (TextView) findViewById(R.id.txtFBAddMoreMediaTitle);
        txtFBAddMediaSkip = (TextView) findViewById(R.id.txtFBAddMediaSkip);
        txtFBAddMoreMediaDone = (TextView) findViewById(R.id.txtFBAddMoreMediaDone);

        imgFBMediaNo = (ImageButton) findViewById(R.id.imgFBMediaNo);
        imgFBMediaYes = (ImageButton) findViewById(R.id.imgFBMediaYes);
        imgFBDescEdit = (ImageButton) findViewById(R.id.imgFBDescEdit);
        imgFBAddMediaTakeImg = (ImageButton) findViewById(R.id.imgFBAddMediaTakeImg);
        imgFBAddMediaUploadImg = (ImageButton) findViewById(R.id.imgFBAddMediaUploadImg);

        btnFBBrands = (Button) findViewById(R.id.btnFBBrands);
        btnFBSocial = (Button) findViewById(R.id.btnFBSocial);
        btnFBSubmit = (Button) findViewById(R.id.btnFBSubmit);
        btnFBSuccessAddmore = (Button) findViewById(R.id.btnFBSuccessAddmore);

        hSVFBAddMediaTray = (HorizontalScrollView) findViewById(R.id.hSVFBAddMediaTray);
*/


        imgFeedback=(ImageView)findViewById(R.id.imgFeedback);
        ListViewOnSelect=(ListView)findViewById(R.id.ListViewOnSelect);
        TakeMediaImage =(ImageButton)findViewById(R.id.TakeMediaImage);
        txtFBTime =(TextView)findViewById(R.id.txtFBTime);
        txtCamera=(TextView)findViewById(R.id.txtCamera);
        textView2=(TextView)findViewById(R.id.textView2);
        etxtFBDesc1=(EditText)findViewById(R.id.etxtFBDesc1);
        editExperienceLocation=(EditText)findViewById(R.id.editExperienceLocation);
        btnFeedbackSubmit=(Button)findViewById(R.id.btnFeedbackSubmit);
        btnSelectCompany=(Button) findViewById(R.id.btnSelectCompany);
        btnsubmitoksocialbrands=(Button)findViewById(R.id.btnsubmitoksocialbrands);
        FbBtnAddmore=(Button)findViewById(R.id.FbBtnAddmore);
        buttonSocial=(Button)findViewById(R.id.buttonSocial);
        buttongps=(Button)findViewById(R.id.buttongps);
        buttonsubmit=(Button)findViewById(R.id.buttonsubmit);
        relativeLayoutForFeebBackSuceess=(RelativeLayout)findViewById(R.id.relativeLayoutForFeebBackSuceess);


        c = new Common();
        usm = new UserSessionManager(getApplication());
        HashMap<String, String> app =  usm.getUserDetails();
        USER_ID = app.get(usm.KEY_USER_ID);
    }


}

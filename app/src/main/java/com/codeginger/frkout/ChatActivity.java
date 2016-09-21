package com.codeginger.frkout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;

import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.HashMap;
import java.util.List;
import android.support.v7.app.ActionBarActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import android.net.Uri;
import android.widget.VideoView;

import org.json.JSONObject;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;

public class ChatActivity extends ActionBarActivity
{
    List<NameValuePair> params;
    EditText chat_msg;
    ImageButton send_btn;
    Bundle bundle;

    String GCM_REG_ID, USER_ID, USER_NAME;
    Common c;
    UserSessionManager usm;

    String strImageName = "", CurrentPhotoPath, strImgByte = "";
    private Bitmap photo;
    private static final int CAMERA_REQUEST = 0, VIDEO_REQUEST = 1, SELECT_IMAGE = 2, SELECT_VIDEO = 3, SELECT_IMAGE_VIDEO = 4;
    File Imagefile, VideoFile;
    Uri selectedImage;
    int imageRotate = 0;
    private ImageView imgCmpLogo, imgDisCmpLogo;
    private VideoView videoCmpLogo, videoDisCmpLogo;
    RelativeLayout rlchatMsg;
    LinearLayout rlImgChat;
    int prevTextViewId = 0, curTextViewId = 0;
    android.support.v7.app.ActionBar actionBar;
    String strChatMsg = "", ChatUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        c = new Common();
        usm = new UserSessionManager(getApplication());
        HashMap<String, String> app =  usm.getUserDetails();
        GCM_REG_ID = app.get(usm.KEY_GCM_REG_ID);
        USER_ID = app.get(usm.KEY_USER_ID);
        USER_NAME = app.get(usm.KEY_USER_NAME);
        chat_msg = (EditText)findViewById(R.id.chat_msg);
        send_btn = (ImageButton)findViewById(R.id.sendbtn);
        rlchatMsg = (RelativeLayout)findViewById(R.id.rlchatMsg);
        rlImgChat = (LinearLayout)findViewById(R.id.rlImgChat);

        bundle = getIntent().getBundleExtra("INFO");
        ChatUserName = bundle.getString("ChatUserName");
        usm.editor.putString(usm.KEY_GCM_CURRENT_ACTIVE, bundle.getString("UserId"));
        usm.editor.commit();

        actionBar = getSupportActionBar();
        actionBar.setTitle(ChatUserName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        if(bundle.getString("name") != null)
        {
            actionBar.setTitle(bundle.getString("name"));
            ChatReceive(bundle.getString("msg"), bundle.getString("imgName"));

/*
            // Set ID
            curTextViewId = prevTextViewId + 1;

            TextView textview = new TextView(getApplicationContext());
            textview.setBackgroundResource(R.color.white);
            textview.setTextSize(20);
            textview.setPadding(5, 5, 5, 5);
            textview.setTextColor(getResources().getColor(R.color.chat_totext));
            textview.setText(bundle.getString("msg"));
            textview.setId(curTextViewId);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, prevTextViewId);
            params.setMargins(0, 0, 0, 5);
            textview.setLayoutParams(params);
            rlchatMsg.addView(textview);
            prevTextViewId = curTextViewId;
*/
        }

    }

    public void ChatSend(View v)
    {
        if(chat_msg.getText().toString().length() > 0)
        {
            // Set ID
            curTextViewId = prevTextViewId + 1;

            TextView textview = new TextView(getApplicationContext());
            textview.setBackgroundResource(R.color.chat_element_background);
            textview.setTextSize(20);
            textview.setPadding(5, 5, 5, 5);
            textview.setTextColor(getResources().getColor(R.color.black));
            textview.setText(chat_msg.getText().toString());
            textview.setId(curTextViewId);
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.BELOW, prevTextViewId);
            params.setMargins(0, 0, 0, 5);
            textview.setLayoutParams(params);
            rlchatMsg.addView(textview);
            prevTextViewId = curTextViewId;
            strChatMsg = chat_msg.getText().toString();
            chat_msg.setText("");
            new Send().execute();
        }
        else
        {
            if(!(chat_msg.getText().toString().length() > 0))
            {
                chat_msg.setError(getResources().getString(R.string.chat_msg_validation));
            }
        }
    }


    public void ChatReceive(String Msg, String imgName)
    {
        if(Msg != null && !Msg.isEmpty())
        {
            // Set ID
            curTextViewId = prevTextViewId + 1;

            TextView textview = new TextView(getApplicationContext());
            textview.setBackgroundResource(R.color.white);
            textview.setTextSize(20);
            textview.setPadding(5, 5, 5, 5);
            textview.setTextColor(getResources().getColor(R.color.chat_totext));
            textview.setText(Msg);
            textview.setId(curTextViewId);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, prevTextViewId);
            params.setMargins(0, 0, 0, 5);
            textview.setLayoutParams(params);
            rlchatMsg.addView(textview);
            prevTextViewId = curTextViewId;
        }

        if(imgName != null && !imgName.isEmpty())
        {
            String downloadImageUrl = getResources().getString(R.string.image_url) + File.separator + imgName;
            String[] toArr = {downloadImageUrl, imgName};
            new ImageDownloader().execute(toArr);
        }
    }


    private BroadcastReceiver onNotice= new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            HashMap<String, String> app =  usm.getUserDetails();
            String str2 = intent.getStringExtra("fromu");
            String str = intent.getStringExtra("msg");
            String fromname = intent.getStringExtra("fromname");
            String imgName = intent.getStringExtra("imgName");
            if(str2.equals(app.get(usm.KEY_GCM_CURRENT_ACTIVE)))
            {
                ChatReceive(str, imgName);
/*
                if(str != null && !str.isEmpty())
                {
                    // Set ID
                    curTextViewId = prevTextViewId + 1;
                    TextView textview = new TextView(getApplicationContext());
                    textview.setBackgroundResource(R.color.white);
                    textview.setTextSize(20);
                    textview.setPadding(5, 5, 5, 5);
                    textview.setTextColor(getResources().getColor(R.color.chat_totext));
                    textview.setText(str);
                    textview.setId(curTextViewId);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, prevTextViewId);
                    params.setMargins(0, 0, 0, 5);
                    textview.setLayoutParams(params);
                    rlchatMsg.addView(textview);
                    prevTextViewId = curTextViewId;
                }
                if(imgName != null && !imgName.isEmpty())
                {
                    String downloadImageUrl = getResources().getString(R.string.image_url) + File.separator + imgName;
                    String[] toArr = {downloadImageUrl, fromname, imgName};
                    new ImageDownloader().execute(toArr);
                }
*/
            }
        }
    };

    private class ImageDownloader extends AsyncTask<String, String, Bitmap>
    {
        //String[] toArr = {downloadImageUrl, imgName};
        String downloadImageUrl, imgName;

        @Override
        protected Bitmap doInBackground(String... params)
        {
            downloadImageUrl = params[0];
            imgName = params[1];
            return downloadBitmap(downloadImageUrl);
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(Bitmap photo)
        {
           // Set ID
            curTextViewId = prevTextViewId + 1;
            strChatMsg = "";
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageBitmap(photo);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(R.color.white);
            imageView.setPadding(5, 5, 5, 5);
            imageView.setId(curTextViewId);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(240, 180);
            params.addRule(RelativeLayout.BELOW, prevTextViewId);
            params.setMargins(0, 0, 0, 5);
            imageView.setLayoutParams(params);
            rlchatMsg.addView(imageView);
            // Set ID
            prevTextViewId = curTextViewId;
            // Download Image
            Imagefile = new File(Environment.getExternalStoragePublicDirectory(getResources().getString(R.string.app_name)) + File.separator + getResources().getString(R.string.image_folder), imgName);
            FileOutputStream out = null;
            try
            {
                out = new FileOutputStream(Imagefile);
                photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.img_quality), out);
            }
            catch (Exception e)
            {
                System.out.print(e);
            }
            finally
            {
                try
                {
                    if (out != null)
                    {
                        out.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        private Bitmap downloadBitmap(String url)
        {
            final DefaultHttpClient client = new DefaultHttpClient();
            final HttpGet getRequest = new HttpGet(url);
            try
            {
                HttpResponse response = client.execute(getRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK)
                {
                    return null;
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null)
                {
                    InputStream inputStream = null;
                    try
                    {
                        inputStream = entity.getContent();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        return bitmap;
                    }
                    finally
                    {
                        if (inputStream != null)
                        {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            }
            catch (Exception e)
            {
                getRequest.abort();
            }
            return null;
        }
    }

    private class Send extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... args)
        {
            String result = "";

            // Image Encoding
            if(photo != null)
            {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.img_quality), outputStream);
                byte[] bytes_pic = outputStream.toByteArray();
                strImgByte = Base64.encodeToString(bytes_pic, Base64.NO_WRAP);
            }
            try
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ChatImageName", strImageName);
                jsonObject.put("Chatmessages", strChatMsg);
                jsonObject.put("Users_reg_id", bundle.getString("RegId"));
                jsonObject.put("ToUser_ID", bundle.getString("UserId"));
                jsonObject.put("Users_ID", USER_ID);
                jsonObject.put("Users_Name", USER_NAME);
                jsonObject.put("ChatImageByte", strImgByte);
                String json = jsonObject.toString();
                result = c.PostData(getResources().getString(R.string.send), json);
                //Clear Data
                strImageName = "";
                strImgByte = "";
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
            /*
            try
            {
                if(result != null && result != "")
                {
                    if (result.contains("Success"))
                    {
                        //Success
                    }
                }
                else
                {
                    Toast.makeText(getApplication(), "Chat Is offline" , Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            */
        }
    }

    public void takeImage()
    {
        try
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            strImageName = getResources().getString(R.string.img) + c.getDDMMYYCurrentDateTime() + "_"  + USER_ID + ".Jpeg";
            Imagefile = new File(Environment.getExternalStoragePublicDirectory(getResources().getString(R.string.app_name)) + File.separator + getResources().getString(R.string.image_folder), strImageName);
            CurrentPhotoPath = Imagefile.getAbsolutePath();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Imagefile));
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
        catch (Exception e)
        {
            System.out.print(e);
        }
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
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            bmOptions.inJustDecodeBounds = false;
                            bmOptions.inSampleSize = 1;
                            photo = BitmapFactory.decodeFile(CurrentPhotoPath, bmOptions);
                            selectedImage = Uri.fromFile(new File(CurrentPhotoPath));
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
                        {System.out.println("Exception : " + e);}
                    }
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
                    // Set ID
                    curTextViewId = prevTextViewId + 1;
                    strChatMsg = "";

                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setImageBitmap(photo);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setBackgroundResource(R.color.chat_element_background);
                    imageView.setPadding(5, 5, 5, 5);
                    imageView.setId(curTextViewId);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(240, 180);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.BELOW, prevTextViewId);
                    params.setMargins(0, 0, 0, 5);
                    imageView.setLayoutParams(params);
                    rlchatMsg.addView(imageView);

                    // Set ID
                    prevTextViewId = curTextViewId;
                    chat_msg.setText("");
                    new Send().execute();
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

    @Override
    public void onBackPressed()
    {
        usm.editor.putString(usm.KEY_GCM_CURRENT_ACTIVE, "0");
        usm.editor.commit();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_takeImage)
        {
            takeImage();
        }
        return super.onOptionsItemSelected(item);
    }

}
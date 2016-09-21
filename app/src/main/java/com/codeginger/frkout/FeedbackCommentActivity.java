package com.codeginger.frkout;

// Created by Pratik Mehta on 30/03/2016.

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.codeginger.frkout.TextDrawable;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.widget.Toast;

public class FeedbackCommentActivity extends ActionBarActivity
{
    android.support.v7.app.ActionBar mActionBar;
    TextView txtFbCmtName, txtFbCmtDate, txtFbCmtCmt;
    ImageView ivFbCmt;
    ListView lsFbCmt;
    EditText etxtFbCmtMsg;
    ImageButton btnFbCmtSend;


    int Feedback_ID, Feedback_NoOfComment;
    String Users_Name, Users_FileName, Users_FilePath, Feedback_Title, Feedback_Date, FeedbackMed_Type, FeedbackMed_FileName, FeedbackMed_FilePath;

    String result;
    int n, intFbTtlCmt;
    String[] lsUsers_Name, lsUsers_FileName, lsUsers_FilePath, lsFeedbackCmt_Comment, lsFeedbackCmt_CreatedDate;
    int[] lsFeedbackCmt_ID, lsFbTtlCmt;

    Common c;
    UserSessionManager usm;
    String USER_ID;
    private List<FbCmtListViewItem> FbCmtList = new ArrayList<FbCmtListViewItem>();
    private FbCmtListViewAdapter adapter;
    String strUsrComment;
    int intLstFbCmtId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_comment);

        c = new Common();
        usm = new UserSessionManager(getApplication());
        HashMap<String, String> app =  usm.getUserDetails();
        USER_ID = app.get(usm.KEY_USER_ID);

        mActionBar = getSupportActionBar();
        findViewById();
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));
        mActionBar.setTitle(Users_Name);

        //if(Feedback_NoOfComment > 0 )
        new AsyncGetFbCmtList().execute();
    }

    public void FbCmtSend(View v)
    {
        strUsrComment = etxtFbCmtMsg.getText().toString();
        etxtFbCmtMsg.setText("");

        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        new AsyncSendFbCmt().execute();
    }

    private class AsyncGetFbCmtList extends AsyncTask<String, Void, String>
    {
        JSONArray jsonarray = null;

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                result = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("FeedbackCmt_FBID", Feedback_ID);
                jsonObject.put("FeedbackCmt_ID", intLstFbCmtId);
                String json = jsonObject.toString();
                result = c.PostData(getResources().getString(R.string.GetFbCmtList), json);
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
            if(result != null)
            {
                try
                {
                    jsonarray = new JSONArray(result);
                    n = jsonarray.length();

                    lsFeedbackCmt_ID = new int[n];
                    lsFbTtlCmt = new int[n];
                    lsFeedbackCmt_Comment = new String[n];
                    lsFeedbackCmt_CreatedDate = new String[n];
                    lsUsers_Name = new String[n];
                    lsUsers_FileName = new String[n];
                    lsUsers_FilePath = new String[n];

                    if(n > 0)
                    {
                        for (int i = 0; i < jsonarray.length(); i++)
                        {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            lsFeedbackCmt_ID[i] = jsonobject.getInt("FeedbackCmt_ID");
                            lsFbTtlCmt[i] = jsonobject.getInt("TotalComment");
                            lsFeedbackCmt_Comment[i] = jsonobject.getString("FeedbackCmt_Comment");
                            lsFeedbackCmt_CreatedDate[i] = jsonobject.getString("FeedbackCmt_CreatedDate");
                            lsUsers_Name[i] = jsonobject.getString("Users_Name");
                            lsUsers_FileName[i] = jsonobject.getString("Users_FileName");
                            lsUsers_FilePath[i] = jsonobject.getString("Users_FilePath");
                            FbCmtList.add(new FbCmtListViewItem(lsUsers_Name[i], lsUsers_FileName[i], lsUsers_FilePath[i], lsFeedbackCmt_Comment[i], lsFeedbackCmt_CreatedDate[i]));
                        }
                        intLstFbCmtId = lsFeedbackCmt_ID[0];
                        intFbTtlCmt =   lsFbTtlCmt[0];
                        txtFbCmtCmt.setText(intFbTtlCmt + " Comments");
                    }
                    adapter = new FbCmtListViewAdapter(FeedbackCommentActivity.this, FbCmtList);
                    lsFbCmt.setAdapter(adapter);
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

    private class AsyncUpdateFbCmtList extends AsyncTask<String, Void, String>
    {
        JSONArray jsonarray = null;

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                result = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("FeedbackCmt_FBID", Feedback_ID);
                jsonObject.put("FeedbackCmt_ID", intLstFbCmtId);
                String json = jsonObject.toString();
                result = c.PostData(getResources().getString(R.string.GetFbCmtList), json);
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
            if(result != null)
            {
                try
                {
                    jsonarray = new JSONArray(result);
                    n = jsonarray.length();

                    lsFeedbackCmt_ID = new int[n];
                    lsFbTtlCmt = new int[n];
                    lsFeedbackCmt_Comment = new String[n];
                    lsFeedbackCmt_CreatedDate = new String[n];
                    lsUsers_Name = new String[n];
                    lsUsers_FileName = new String[n];
                    lsUsers_FilePath = new String[n];

                    if(n > 0)
                    {
                        for (int i = 0; i < jsonarray.length(); i++)
                        {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            lsFeedbackCmt_ID[i] = jsonobject.getInt("FeedbackCmt_ID");
                            lsFbTtlCmt[i] = jsonobject.getInt("TotalComment");
                            lsFeedbackCmt_Comment[i] = jsonobject.getString("FeedbackCmt_Comment");
                            lsFeedbackCmt_CreatedDate[i] = jsonobject.getString("FeedbackCmt_CreatedDate");
                            lsUsers_Name[i] = jsonobject.getString("Users_Name");
                            lsUsers_FileName[i] = jsonobject.getString("Users_FileName");
                            lsUsers_FilePath[i] = jsonobject.getString("Users_FilePath");
                            FbCmtList.add(0,new FbCmtListViewItem(lsUsers_Name[i], lsUsers_FileName[i], lsUsers_FilePath[i], lsFeedbackCmt_Comment[i], lsFeedbackCmt_CreatedDate[i]));
                        }
                        intLstFbCmtId = lsFeedbackCmt_ID[0];
                        intFbTtlCmt =   lsFbTtlCmt[0];
                        txtFbCmtCmt.setText(intFbTtlCmt + " Comments");
                    }
                    adapter = new FbCmtListViewAdapter(FeedbackCommentActivity.this, FbCmtList);
                    lsFbCmt.setAdapter(adapter);
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


    private class AsyncSendFbCmt extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... args)
        {
            try
            {
                result = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("FeedbackCmt_FBID", Feedback_ID);
                jsonObject.put("FeedbackCmt_Comment", strUsrComment);
                jsonObject.put("Users_ID", Integer.parseInt(USER_ID));
                String json = jsonObject.toString();
                result = c.PostData(getResources().getString(R.string.InsertFbCmt), json);
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
                if(result != null && result != "")
                {
                    if (result.contains("Success"))
                    {
                        //Success
                        new AsyncUpdateFbCmtList().execute();
                    }
                }
                else
                {
                    Toast.makeText(getApplication(), "Response is Empty", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    // FindViewById
    private void findViewById()
    {
        txtFbCmtName = (TextView) findViewById(R.id.txtFbCmtName);
        txtFbCmtDate = (TextView) findViewById(R.id.txtFbCmtDate);
        txtFbCmtCmt = (TextView) findViewById(R.id.txtFbCmtCmt);

        ivFbCmt = (ImageView) findViewById(R.id.ivFbCmt);

        lsFbCmt = (ListView) findViewById(R.id.lsFbCmt);

        etxtFbCmtMsg = (EditText) findViewById(R.id.etxtFbCmtMsg);
        btnFbCmtSend = (ImageButton) findViewById(R.id.btnFbCmtSend);

        // Get Data From Dashboard
        Intent intent = getIntent();
        if(intent != null)
        {
            Feedback_ID = intent.getIntExtra("Feedback_ID", 0);
            Users_Name = intent.getStringExtra("Users_Name");
            Users_FileName = intent.getStringExtra("Users_FileName");
            Users_FilePath = intent.getStringExtra("Users_FilePath");
            Feedback_Title = intent.getStringExtra("Feedback_Title");
            Feedback_Date = intent.getStringExtra("Feedback_Date");
            Feedback_NoOfComment = intent.getIntExtra("Feedback_NoOfComment",0);
            Users_Name = intent.getStringExtra("Users_Name");

            FeedbackMed_Type = intent.getStringExtra("FeedbackMed_Type");
            FeedbackMed_FileName = intent.getStringExtra("FeedbackMed_FileName");
            FeedbackMed_FilePath = intent.getStringExtra("FeedbackMed_FilePath");

            // Display Data
            txtFbCmtName.setText(Feedback_Title);
            txtFbCmtDate.setText(Feedback_Date);
            txtFbCmtCmt.setText(Feedback_NoOfComment + " Comments");

            // Feedback Media
            if (FeedbackMed_Type != null && FeedbackMed_Type.equals("IMAGE") && FeedbackMed_FileName != null)
            {
                if(ivFbCmt.getDrawable() == null)
                {
                    String downloadImageUrl = getResources().getString(R.string.image_url) + "/" + FeedbackMed_FileName;
                    new ImageDownloaderTask(ivFbCmt).execute(downloadImageUrl);
                }
            }

            /*
            if(Users_FileName != null && Users_FileName.length() > 0)
            {
                if(ivFbCmt.getDrawable() == null)
                {
                    String downloadImageUrl = getResources().getString(R.string.image_url) + Users_FileName;
                    ImageDownloaderTask task = new ImageDownloaderTask(ivFbCmt)
                    {
                        @Override
                        public void onPostExecute(Bitmap bitmap)
                        {
                            mActionBar.setIcon(new BitmapDrawable(getResources(),bitmap));
                        }
                    };
                    task.execute(downloadImageUrl);
                }
            }
            else
            {
                if(Users_Name != null && Users_Name.length() > 0)
                {
                    String letter = String.valueOf(Users_Name.toUpperCase().charAt(0));
                    TextDrawable drawable = TextDrawable.builder().buildRound(letter, R.color.green_dark);
                    mActionBar.setIcon(drawable);
                }
            }
            */
        }

    }

}


class FbCmtListViewItem
{
    public final String txtFbCmtUserName, txtFbCmtUserCmt, txtFbCmtUserCmtDate;
    public final String FbCmtUserFileName, FbCmtUserFilePath;

    public FbCmtListViewItem(String txtFbCmtUserName, String FbCmtUserFileName, String FbCmtUserFilePath,String txtFbCmtUserCmt, String txtFbCmtUserCmtDate)
    {
        this.txtFbCmtUserName = txtFbCmtUserName;
        this.FbCmtUserFileName = FbCmtUserFileName;
        this.FbCmtUserFilePath = FbCmtUserFilePath;
        this.txtFbCmtUserCmt = txtFbCmtUserCmt;
        this.txtFbCmtUserCmtDate = txtFbCmtUserCmtDate;
    }

}

class FbCmtListViewAdapter extends ArrayAdapter<FbCmtListViewItem>
{
    private List<FbCmtListViewItem> items;
    private LayoutInflater layoutInflater;

    public FbCmtListViewAdapter(Context context, List<FbCmtListViewItem> items)
    {
        super(context, R.layout.activity_feedback_comment_listview, items);
        this.items = items;
        this.layoutInflater = LayoutInflater.from(getContext());
    }

    private static class ViewHolder
    {
        ImageView ivFbCmtUser;
        TextView txtFbCmtUser, txtFbCmtUserName, txtFbCmtUserCmt, txtFbCmtUserCmtDate;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public int getViewTypeCount()
    {
        return 500;
    }


    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public FbCmtListViewItem getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final int pos = position;
        ViewHolder viewHolder;

        if(convertView == null)
        {
            // inflate the GridView item layout
            // LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.activity_feedback_comment_listview, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivFbCmtUser = (ImageView) convertView.findViewById(R.id.ivFbCmtUser);

            viewHolder.txtFbCmtUser = (TextView) convertView.findViewById(R.id.txtFbCmtUser);
            viewHolder.txtFbCmtUserName = (TextView) convertView.findViewById(R.id.txtFbCmtUserName);
            viewHolder.txtFbCmtUserCmt = (TextView) convertView.findViewById(R.id.txtFbCmtUserCmt);
            viewHolder.txtFbCmtUserCmtDate = (TextView) convertView.findViewById(R.id.txtFbCmtUserCmtDate);
            convertView.setTag(viewHolder);
        }
        else
        {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        FbCmtListViewItem item = getItem(position);
        viewHolder.txtFbCmtUserName.setText(item.txtFbCmtUserName);
        viewHolder.txtFbCmtUserCmt.setText(item.txtFbCmtUserCmt);
        viewHolder.txtFbCmtUserCmtDate.setText(item.txtFbCmtUserCmtDate);

        // User Profile
        if(item.FbCmtUserFileName != null && item.FbCmtUserFileName.length() > 0)
        {
            if(viewHolder.ivFbCmtUser.getDrawable() == null)
            {
                String downloadImageUrl = "http://frkout.geecs.in/Image/" + item.FbCmtUserFileName;
                new ImageDownloaderTask(viewHolder.ivFbCmtUser).execute(downloadImageUrl);
            }
        }
        else
        {
            if(viewHolder.ivFbCmtUser.getDrawable() == null)
            {
                if(item.txtFbCmtUserName != null && item.txtFbCmtUserName.length() > 0)
                    viewHolder.txtFbCmtUser.setText(String.valueOf(item.txtFbCmtUserName.toUpperCase().charAt(0)));
                //viewHolder.ivDashUser.setBackgroundResource(R.drawable.bg_button_circle_green_theme);
                viewHolder.ivFbCmtUser.setImageResource(R.mipmap.green_theme);
            }
        }
        return convertView;
    }

}

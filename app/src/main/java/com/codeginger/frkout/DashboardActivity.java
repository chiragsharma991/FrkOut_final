package com.codeginger.frkout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Picasso;

import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import android.widget.AbsListView.OnScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Created by Pratik Mehta

public class DashboardActivity extends ActionBarActivity
{
    FloatingActionsMenu famDashboard;
    FloatingActionButton fabDashChat, fabDashFeedback;

    public Common c;
    UserSessionManager usm;
    private List<DashboardListViewItem> DashList = new ArrayList<DashboardListViewItem>();
    private ListView listView;
    private DashBoardListViewAdapter adapter;

    String result;
    int n;
    int[] lsFeedback_ID, lsBrand_Id, lsFeedback_CreatedBy, lsTotalComment, lsTotalBrandReview, lsYes, lsNo;
    String[] lsFeedback_Description, lsBrand_Name, lsBrand_LogoName, lsBrand_LogoPath, lsCat_Name, lsFeedback_CreatedDate, lsFeedbackMed_Type, lsFeedbackMed_FileName,
            lsFeedbackMed_FilePath, lsUsers_Name, lsUsers_FileName,lsFeedback_Time,lsFeedback_Date, lsUsers_FilePath;
    ActionBar mActionBar;

    String USER_ID;
    int intLstFeedbackID = 0, lstLstRecord = 0, intFBYNYes, intFBYNNo;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));


        c = new Common();
        usm = new UserSessionManager(getApplicationContext());
        HashMap<String, String> app =  usm.getUserDetails();
        USER_ID = app.get(usm.KEY_USER_ID);

        findViewById();
        new AsyncGetDashboardList().execute();

        listView.setOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                int threshold = 1;
                int count = listView.getCount();
                if (scrollState == SCROLL_STATE_IDLE)
                {
                    if (listView.getLastVisiblePosition() >= count - threshold)
                    {
                        new AsyncGetDashboardList().execute();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }

        });


        fabDashFeedback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, FeedbackActivity.class);
                startActivity(i);
            }
        });

        fabDashChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ChatListActivity.class);
                startActivity(i);
            }
        });



    }

    private class AsyncGetDashboardList extends AsyncTask<String, Void, String>
    {
        JSONArray jsonarray = null;

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                result = "";
                JSONObject jsonObject = new JSONObject();  //it is creating json object you cant hit directly
                jsonObject.put("Feedback_ID", intLstFeedbackID);
                String json = jsonObject.toString();
                Log.e("json",""+json.toString());  //{"Feedback_ID",0}
                result = c.PostData(getResources().getString(R.string.GetDashboardList), json);  //pass json object as a string type  just post not hit
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

                    if(n > 0)
                    {
                        lsFeedback_ID = new int[n];
                        lsBrand_Id = new int[n];
                        lsFeedback_CreatedBy = new int[n];
                        lsTotalComment = new int[n];
                        lsTotalBrandReview = new int[n];
                        lsYes = new int[n];
                        lsNo = new int[n];
                        lsFeedback_Description = new String[n];
                        lsBrand_Name = new String[n];
                        lsBrand_LogoName = new String[n];
                        lsBrand_LogoPath = new String[n];
                        lsFeedback_CreatedDate = new String[n];
                        lsFeedback_Time = new String[n];
                        lsFeedback_Date = new String[n];
                        lsFeedbackMed_Type = new String[n];
                        lsFeedbackMed_FileName = new String[n];
                        lsFeedbackMed_FilePath = new String[n];
                        lsUsers_Name = new String[n];
                        lsUsers_FileName = new String[n];
                        lsUsers_FilePath = new String[n];
                        lsCat_Name = new String[n];

                        for (int i = 0; i < jsonarray.length(); i++)
                        {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            lsFeedback_ID[i] = jsonobject.getInt("Feedback_ID");
                            lsBrand_Id[i] = jsonobject.getInt("Brand_Id");
                            lsFeedback_CreatedBy[i] = jsonobject.getInt("Feedback_CreatedBy");
                            lsTotalComment[i] = jsonobject.getInt("TotalComment");
                            lsTotalBrandReview[i] = jsonobject.getInt("TotalBrandReview");
                            lsYes[i] = jsonobject.getInt("Yes");
                            lsNo[i] = jsonobject.getInt("No");
                            lsFeedback_Description[i] = jsonobject.getString("Feedback_Description");
                            lsBrand_Name[i] = jsonobject.getString("Brand_Name");
                            lsBrand_LogoName[i] = jsonobject.getString("Brand_LogoName");
                            lsBrand_LogoPath[i] = jsonobject.getString("Brand_LogoPath");
                            lsFeedback_CreatedDate[i] = jsonobject.getString("Feedback_CreatedDate");
                            lsFeedback_Time[i] = jsonobject.getString("FeedbackTime");
                            Log.e("feedback time",""+String.valueOf(lsFeedback_Time.toString()));
                            lsFeedback_Date[i] = jsonobject.getString("FeedbackDate");
                            Log.e("feedback date",""+String.valueOf(lsFeedback_Date.toString()));
                            lsFeedbackMed_Type[i] = jsonobject.getString("FeedbackMed_Type");
                            lsFeedbackMed_FileName[i] = jsonobject.getString("FeedbackMed_FileName");
                            lsFeedbackMed_FilePath[i] = jsonobject.getString("FeedbackMed_FilePath");
                            lsUsers_Name[i] = jsonobject.getString("Users_Name");
                            lsUsers_FileName[i] = jsonobject.getString("Users_FileName");
                            lsUsers_FilePath[i] = jsonobject.getString("Users_FilePath");
                            lsCat_Name[i] = jsonobject.getString("Cat_Name");
                            DashList.add(new DashboardListViewItem(null, null, null, lsFeedback_Description[i], lsFeedback_Time[i], lsTotalComment[i], lsBrand_Name[i], lsBrand_LogoName[i], lsBrand_LogoPath[i], lsCat_Name[i] + " - " ,lsTotalBrandReview[i] + " (Reviews)", lsFeedbackMed_Type[i], lsFeedbackMed_FileName[i], lsFeedbackMed_FilePath[i], lsUsers_Name[i], lsUsers_FileName[i], lsUsers_FilePath[i], lsFeedback_ID[i], lsYes[i], lsNo[i]));
                        }
                        intLstFeedbackID = lsFeedback_ID[n-1];
                    }
                    //DashList.add(new DashboardListViewItem(null,null,null,"Homi has narrated (the script) to Kangana and she is keen to work with him, but she is already committed to Hansal Mehtas film after Rangoon and the dates are clashing.","01 Feb 16, 12 : 45 PM","160 Comments","Domino's Pizza","Brand - 120 Reviews"));
                    lstLstRecord = listView.getCount();
                    adapter = new DashBoardListViewAdapter(DashboardActivity.this, DashList, Integer.parseInt(USER_ID), listView);
                    if(listView.getCount() == 0)
                        listView.setAdapter(adapter);
                    adapter.setNotifyOnChange(true);
                    listView.setSelectionFromTop(lstLstRecord, 0);

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


    // FindViewById
    public void findViewById()
    {
        famDashboard = (FloatingActionsMenu) findViewById(R.id.famDashboard);
        fabDashFeedback = (FloatingActionButton) findViewById(R.id.fabDashFeedback);
        fabDashChat = (FloatingActionButton) findViewById(R.id.fabDashChat);
        listView = (ListView) findViewById(R.id.list);
    }





/*
    // Menu Start
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_chatting)
        {
            Intent i = new Intent(this, ChatListActivity.class);
            startActivity(i);
        }

        if (id == R.id.action_refresh)
        {
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        }

        if (id == R.id.action_testing)
        {
            Intent i = new Intent(this, TestActivity.class);
            startActivity(i);
        }


        if (id == R.id.action_broadcasting)
        {
            Intent i = new Intent(this, BroadcastActivity.class);
            startActivity(i);
        }

        if (id == R.id.action_surveys)
        {
            Intent i = new Intent(this, SurveysActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
    // Menu End
*/





}



class DashboardListViewItem
{
    public final Drawable ivDashUser, ivDashBrand, ivDashCmt;
    public final String txtDashFb, txtDashFbDate, txtDashBrand, txtDashBrandReview,txtDashCat;
    public final String FeedbackMed_Type, FeedbackMed_FileName, FeedbackMed_FilePath, Users_Name, Users_FileName, Users_FilePath, Brand_LogoName, Brand_LogoPath;
    public final int Feedback_ID, txtDashFbComment, Yes, No;

    public DashboardListViewItem(Drawable ivDashUser, Drawable ivDashBrand, Drawable ivDashCmt, String txtDashFb, String txtDashFbDate, int txtDashFbComment,
                                 String txtDashBrand, String Brand_LogoName, String Brand_LogoPath,String txtDashCat, String txtDashBrandReview, String FeedbackMed_Type, String FeedbackMed_FileName, String FeedbackMed_FilePath, String Users_Name,
                                 String Users_FileName, String Users_FilePath, int Feedback_ID, int Yes, int No)
    {
        this.ivDashUser = ivDashUser;
        this.ivDashBrand = ivDashBrand;
        this.ivDashCmt = ivDashCmt;

        this.txtDashFb = txtDashFb;
        this.txtDashFbDate = txtDashFbDate;
        this.txtDashFbComment = txtDashFbComment;
        this.txtDashBrand = txtDashBrand;
        this.txtDashCat = txtDashCat;
        this.txtDashBrandReview = txtDashBrandReview;

        this.FeedbackMed_Type = FeedbackMed_Type;
        this.FeedbackMed_FileName = FeedbackMed_FileName;
        this.FeedbackMed_FilePath = FeedbackMed_FilePath;

        this.Users_Name = Users_Name;
        this.Users_FileName = Users_FileName;
        this.Users_FilePath = Users_FilePath;

        this.Brand_LogoName = Brand_LogoName;
        this.Brand_LogoPath = Brand_LogoPath;

        this.Feedback_ID = Feedback_ID;
        this.Yes = Yes;
        this.No = No;
    }

}

class DashBoardListViewAdapter extends ArrayAdapter<DashboardListViewItem>
{
    private List<DashboardListViewItem> items;
    private LayoutInflater layoutInflater;
    int USER_ID, intFBYNYes, intFBYNNo;
    ListView listView;


    private DashBoardListViewAdapter context;


    public DashBoardListViewAdapter(Context context, List<DashboardListViewItem> items, int USER_ID, ListView listView)
    {
        super(context, R.layout.activity_dashboard_listview, items);
        this.items = items;
        this.layoutInflater = LayoutInflater.from(getContext());
        this.USER_ID = USER_ID;
        this.listView = listView;

    }

    private static class ViewHolder
    {
        RelativeLayout rlDashFbBrand;
        TextView txtDashUser, txtDashFb, txtDashFbDate, txtDashFbComment, txtDashBrandImg, txtDashBrand, txtDashBrandReview, txtDashYesCount, txtDashNoCount,txtDashCat;
        ImageView ivDashUser, ivDashFb, ivDashBrand, ivDashCmt,ivDashBrandComment;
        SeekBar sbDashYesNo;
        Button btnLike,btnDislike;
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
    public DashboardListViewItem getItem(int position)
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
            convertView = layoutInflater.inflate(R.layout.activity_dashboard_listview, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.rlDashFbBrand = (RelativeLayout) convertView.findViewById(R.id.rlDashFbBrand);

            viewHolder.txtDashUser = (TextView) convertView.findViewById(R.id.txtDashUser);
            viewHolder.txtDashFb = (TextView) convertView.findViewById(R.id.txtDashFb);
            viewHolder.txtDashFbDate = (TextView) convertView.findViewById(R.id.txtDashFbDate);
            //viewHolder.txtDashFbComment = (TextView) convertView.findViewById(R.id.txtDashFbComment);
            viewHolder.txtDashBrandImg = (TextView) convertView.findViewById(R.id.txtDashBrandImg);
            viewHolder.txtDashBrand = (TextView) convertView.findViewById(R.id.txtDashBrand);
            viewHolder.txtDashBrandReview = (TextView) convertView.findViewById(R.id.txtDashBrandReview);
            viewHolder.txtDashYesCount = (TextView) convertView.findViewById(R.id.txtDashYesCount);
            viewHolder.txtDashNoCount = (TextView) convertView.findViewById(R.id.txtDashNoCount);
           // viewHolder.txtDashCat=(TextView) convertView.findViewById(R.id.txtDashCat);
            //viewHolder.ivDashUser = (ImageView) convertView.findViewById(R.id.ivDashUser);
            viewHolder.ivDashFb = (ImageView) convertView.findViewById(R.id.ivDashFb);
            viewHolder.ivDashBrand = (ImageView) convertView.findViewById(R.id.ivDashBrand);
            //viewHolder.ivDashCmt = (ImageView) convertView.findViewById(R.id.ivDashCmt);

            //viewHolder.sbDashYesNo = (SeekBar) convertView.findViewById(R.id.sbDashYesNo);
            viewHolder.btnLike = (Button) convertView.findViewById(R.id.btnLike);
            viewHolder.btnDislike = (Button) convertView.findViewById(R.id.btnDislike);
            convertView.setTag(viewHolder);
        }
        else
        {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        DashboardListViewItem item = getItem(position);

        //viewHolder.ivDashCmt.setBackgroundResource(R.mipmap.comments);
        viewHolder.txtDashFb.setText(item.txtDashFb);
        viewHolder.txtDashFbDate.setText(item.txtDashFbDate);
        //viewHolder.txtDashFbComment.setText(item.txtDashFbComment + "Comments");
        viewHolder.txtDashBrand.setText(item.txtDashBrand);
       // viewHolder.txtDashCat.setText(item.txtDashCat);
        viewHolder.txtDashBrandReview.setText(item.txtDashBrandReview);

        viewHolder.btnLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                intFBYNYes = 1; intFBYNNo = 0;
                String[] toArr = {items.get(pos).Feedback_ID + "", intFBYNYes + "", intFBYNNo + "", pos + ""};
                new AsyncSendFbYesNo().execute(toArr);
            }

        });
        viewHolder.btnDislike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                intFBYNYes = 0; intFBYNNo = 1;
                String[] toArr = {items.get(pos).Feedback_ID + "", intFBYNYes + "", intFBYNNo + "", pos + ""};
                new AsyncSendFbYesNo().execute(toArr);
            }

        });
        // Feedback Yes, No
        viewHolder.txtDashYesCount.setText(item.Yes + "");
        viewHolder.txtDashNoCount.setText(item.No + "");

        /*viewHolder.sbDashYesNo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            int intCurProg = 1;


            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                intCurProg = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar)
            {
                // TODO Auto-generated method stub
            }

           public void onStopTrackingTouch(SeekBar seekBar)
            {
                switch (intCurProg)
                {
                    //YES
                    case 2:
                        intFBYNYes = 1; intFBYNNo = 0;
                        break;
                    //NA
                    case 1:
                        intFBYNYes = 0; intFBYNNo = 0;
                        break;
                    //NO
                    case 0:
                        intFBYNYes = 0; intFBYNNo = 1;
                        break;
                }
                String[] toArr = {items.get(pos).Feedback_ID + "", intFBYNYes + "", intFBYNNo + "", pos + ""};
                new AsyncSendFbYesNo().execute(toArr);
            }
        });*/

        // User Profile
       /* if(item.Users_FileName != null && item.Users_FileName.length() > 0)
        {
            if(viewHolder.ivDashUser.getDrawable() == null)
            {
                String downloadImageUrl = "http://frkout.geecs.in/Image/" + item.Users_FileName;
                new ImageDownloaderTask(viewHolder.ivDashUser).execute(downloadImageUrl);
            }
        }*/
        //else
        //{
        //if(viewHolder.ivDashUser.getDrawable() == null)
        //{
        if(item.Users_Name != null && item.Users_Name.length() > 0)
            viewHolder.txtDashUser.setText(item.Users_Name);
        //viewHolder.txtDashUser.setText(String.valueOf(item.Users_Name.toUpperCase().charAt(0)));





//                id=app.get(usm.KEY_ID);


        //viewHolder.ivDashUser.setBackgroundResource(R.drawable.bg_button_circle_green_theme);
        //viewHolder.ivDashUser.setImageResource(R.mipmap.green_theme);
        //}
        //}

        // Brand
        if(item.Brand_LogoName != null && item.Brand_LogoName.length() > 0)
        {
            if(viewHolder.ivDashBrand.getDrawable() == null)
            {
                String downloadImageUrl = "http://frkout.geecs.in/Image/" + item.Brand_LogoName;
                new ImageDownloaderTask(viewHolder.ivDashBrand).execute(downloadImageUrl);
            }
        }
        else
        {
            if(viewHolder.ivDashBrand.getDrawable() == null)
            {
                if(item.txtDashBrand != null && item.txtDashBrand.length() > 0)
                    viewHolder.txtDashBrandImg.setText(String.valueOf(item.txtDashBrand.toUpperCase().charAt(0)));
                //viewHolder.ivDashUser.setBackgroundResource(R.drawable.bg_button_circle_green_theme);
                viewHolder.ivDashBrand.setImageResource(R.mipmap.green_theme);
            }
        }

        // Feedback Media
        if (item.FeedbackMed_Type != null && item.FeedbackMed_Type.equals("IMAGE") && item.FeedbackMed_FileName != null)
        {
            if(viewHolder.ivDashFb.getDrawable() == null)
            {
                //viewHolder.ivDashFb.setVisibility(View.VISIBLE);
                //viewHolder.ivDashFb.setImageResource(R.mipmap.ic_launcher);

                String downloadImageUrl = "http://frkout.geecs.in/Image/" + item.FeedbackMed_FileName;
                new ImageDownloaderTask(viewHolder.ivDashFb).execute(downloadImageUrl);
            }
        }
        else
        {
            viewHolder.txtDashFb.setMinLines(5);
        }


        viewHolder.rlDashFbBrand.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("Feedback_ID", items.get(pos).Feedback_ID);
                bundle.putString("Users_Name", items.get(pos).Users_Name);
                //bundle.putString("Users_FileName", items.get(pos).Users_FileName);
                //bundle.putString("Users_FilePath", items.get(pos).Users_FilePath);
                bundle.putString("Feedback_Title", items.get(pos).txtDashFb);
                bundle.putString("Feedback_Date", items.get(pos).txtDashFbDate);
                //bundle.putInt("Feedback_NoOfComment", items.get(pos).txtDashFbComment);
                bundle.putString("FeedbackMed_Type", items.get(pos).FeedbackMed_Type);
                bundle.putString("FeedbackMed_FileName", items.get(pos).FeedbackMed_FileName);
                bundle.putString("FeedbackMed_FilePath", items.get(pos).FeedbackMed_FilePath);
                getContext().startActivity(new Intent(getContext(), FeedbackCommentActivity.class).putExtras(bundle));
            }
        });

        return convertView;
    }


    public class AsyncSendFbYesNo extends AsyncTask<String, String, String>
    {
        int Feedback_ID, intFBYNYes, intFBYNNo, n, pos;
        String result = "";
        JSONArray jsonarray = null;

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                Feedback_ID = Integer.parseInt(params[0]);
                intFBYNYes = Integer.parseInt(params[1]);
                intFBYNNo = Integer.parseInt(params[2]);
                pos = Integer.parseInt(params[3]);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("FeedbackYN_FBID", Feedback_ID);
                jsonObject.put("FeedbackYN_Yes", intFBYNYes);
                jsonObject.put("FeedbackYN_No", intFBYNNo);
                jsonObject.put("FeedbackYN_CreatedBy", USER_ID);

                String json = jsonObject.toString();
                Common c = new Common();
                result = c.PostData(getContext().getResources().getString(R.string.InsertFbYN), json);
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
            if(result != null)
            {
                try
                {
                    // Success
                    jsonarray = new JSONArray(result);
                    n = jsonarray.length();
                    View v = listView.getChildAt(pos);
                    if(v == null)
                        return;
                    TextView txtDashYesCount = (TextView) v.findViewById(R.id.txtDashYesCount);
                    TextView txtDashNoCount = (TextView) v.findViewById(R.id.txtDashNoCount);

                    if(n > 0)
                    {
                        for (int i = 0; i < n; i++)
                        {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            Feedback_ID = jsonobject.getInt("FeedbackYN_FBID");
                            intFBYNYes = jsonobject.getInt("FeedbackYN_Yes");
                            intFBYNNo = jsonobject.getInt("FeedbackYN_No");
                            txtDashYesCount.setText(intFBYNYes + "");
                            txtDashNoCount.setText(intFBYNNo + "");
                        }
                    }
                    else
                    {
                        txtDashYesCount.setText("0");
                        txtDashNoCount.setText("0");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }




}




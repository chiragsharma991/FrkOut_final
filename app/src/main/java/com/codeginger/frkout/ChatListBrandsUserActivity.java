package com.codeginger.frkout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Created by Pratik Mehta

public class ChatListBrandsUserActivity extends ActionBarActivity
{
    ListView list;

    JSONArray jsonarray = null;
    int n, intLstUserID = 0, intUserListLstRecord = 0;
    String[] lsUsers_Name, lsUsers_FileName, lsUsers_reg_id;
    int[] lsUsers_ID;
    private List<BrandsUsersUsersListViewItem> BrandsUsersUsersList = new ArrayList<BrandsUsersUsersListViewItem>();
    private BrandsUsersUsersListViewAdapter adapter;

    String USER_ID;
    Common c;
    Bundle bundle;
    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chatlist);

        c = new Common();
        list = (ListView) findViewById(R.id.listView);

        bundle = getIntent().getBundleExtra("INFO");
        USER_ID = bundle.getString("UserId");
        actionBar = getSupportActionBar();
        actionBar.setTitle(bundle.getString("ChatUserName"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));

        new Load().execute();

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = list.getCount();
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (list.getLastVisiblePosition() >= count - threshold) {
                        new Load().execute();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }

        });

    }

    public class Load extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... args)
        {
            String result = "";
            try
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Users_ID", USER_ID);
                jsonObject.put("LastUsers_ID", intLstUserID);
                jsonObject.put("MODE", "BRANDUSERS");
                String json = jsonObject.toString();
                result = c.PostData(getResources().getString(R.string.userchatlist), json);
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
                    jsonarray = new JSONArray(result);
                    n = jsonarray.length();
                    if(n > 0)
                    {
                        lsUsers_ID = new int[n];
                        lsUsers_Name = new String[n];
                        lsUsers_FileName = new String[n];
                        lsUsers_reg_id = new String[n];
                        for (int i = 0; i < n; i++)
                        {
                            try
                            {
                                JSONObject c = jsonarray.getJSONObject(i);
                                lsUsers_ID[i] = c.getInt("Users_ID");
                                lsUsers_Name[i] = c.getString("Users_Name");
                                lsUsers_reg_id[i] = c.getString("Users_reg_id");
                                lsUsers_FileName[i] = c.getString("Users_FileName");
                                BrandsUsersUsersList.add(new BrandsUsersUsersListViewItem(lsUsers_Name[i], lsUsers_reg_id[i], lsUsers_FileName[i], lsUsers_ID[i]));
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
                        intLstUserID = lsUsers_ID[n-1];
                    }
                    intUserListLstRecord = list.getCount();
                    adapter = new BrandsUsersUsersListViewAdapter(ChatListBrandsUserActivity.this, BrandsUsersUsersList);
                    if(list.getCount() == 0)
                        list.setAdapter(adapter);
                    adapter.setNotifyOnChange(true);
                    list.setSelectionFromTop(intUserListLstRecord, 0);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

}

class BrandsUsersUsersListViewItem
{
    public final String txtChatListUserName;
    public final String ChatListUserRegId, ChatListUserFileName;
    public final int ChatListUsersID;

    public BrandsUsersUsersListViewItem(String txtChatListUserName, String ChatListUserRegId, String ChatListUserFileName, int ChatListUsersID)
    {
        this.txtChatListUserName = txtChatListUserName;
        this.ChatListUserRegId = ChatListUserRegId;
        this.ChatListUserFileName = ChatListUserFileName;
        this.ChatListUsersID = ChatListUsersID;
    }
}

class BrandsUsersUsersListViewAdapter extends ArrayAdapter<BrandsUsersUsersListViewItem>
{
    private List<BrandsUsersUsersListViewItem> items;
    private LayoutInflater layoutInflater;

    public BrandsUsersUsersListViewAdapter(Context context, List<BrandsUsersUsersListViewItem> items)
    {
        super(context, R.layout.fragment_chatlist_listview, items);
        this.items = items;
        this.layoutInflater = LayoutInflater.from(getContext());
    }

    private static class ViewHolder
    {
        RelativeLayout rlUserList;
        ImageView ivChatListUserImg;
        TextView txtChatListImgName, txtChatListUserName;
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
    public BrandsUsersUsersListViewItem getItem(int position)
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
            convertView = layoutInflater.inflate(R.layout.fragment_chatlist_listview, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.rlUserList = (RelativeLayout) convertView.findViewById(R.id.rlUserList);
            viewHolder.ivChatListUserImg = (ImageView) convertView.findViewById(R.id.ivChatListUserImg);
            viewHolder.txtChatListImgName = (TextView) convertView.findViewById(R.id.txtChatListImgName);
            viewHolder.txtChatListUserName = (TextView) convertView.findViewById(R.id.txtChatListUserName);
            convertView.setTag(viewHolder);
        }
        else
        {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        BrandsUsersUsersListViewItem item = getItem(position);
        viewHolder.txtChatListUserName.setText(item.txtChatListUserName);

        // User Profile
        if(item.ChatListUserFileName != null && item.ChatListUserFileName.length() > 0)
        {
            if(viewHolder.ivChatListUserImg.getDrawable() == null)
            {
                String downloadImageUrl = "http://frkout.geecs.in/Image/" + item.ChatListUserFileName;
                new ImageDownloaderTask(viewHolder.ivChatListUserImg).execute(downloadImageUrl);
            }
        }
        else
        {
            if(viewHolder.ivChatListUserImg.getDrawable() == null)
            {
                if(item.txtChatListUserName != null && item.txtChatListUserName.length() > 0)
                    viewHolder.txtChatListImgName.setText(String.valueOf(item.txtChatListUserName.toUpperCase().charAt(0)));
                //viewHolder.ivDashUser.setBackgroundResource(R.drawable.bg_button_circle_green_theme);
                viewHolder.ivChatListUserImg.setImageResource(R.mipmap.green_theme);
            }
        }

        viewHolder.rlUserList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent chat = new Intent(getContext(), ChatActivity.class);
                Bundle b = new Bundle();
                b.putString("ChatUserName", items.get(pos).txtChatListUserName);
                b.putString("RegId", items.get(pos).ChatListUserRegId);
                b.putString("UserId", items.get(pos).ChatListUsersID + "");
                chat.putExtra("INFO", b);
                getContext().startActivity(chat);
            }
        });

        return convertView;
    }

}


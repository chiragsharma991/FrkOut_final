package com.codeginger.frkout;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;

// Created by Pratik Mehta

public class ChatListBrandsFragment extends Fragment
{
    ListView list;

    JSONArray jsonarray = null;
    int n, intLstUserID = 0, intUserListLstRecord = 0;
    String[] lsUsers_Name, lsUsers_FileName, lsUsers_reg_id;
    int[] lsUsers_ID;
    private List<BrandsUsersListViewItem> BrandsUsersList = new ArrayList<BrandsUsersListViewItem>();
    private BrandsUsersListViewAdapter adapter;
    String USER_ID;
    Common c;
    UserSessionManager usm;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {
        View view =inflater.inflate(R.layout.fragment_chatlist, container, false);
        c = new Common();
        usm = new UserSessionManager(getActivity());
        HashMap<String, String> app =  usm.getUserDetails();
        USER_ID = app.get(usm.KEY_USER_ID);

        list = (ListView)view.findViewById(R.id.listView);

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
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {

            }
        });
        return view;
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
                jsonObject.put("MODE", "BRANDS");
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
                                BrandsUsersList.add(new BrandsUsersListViewItem(lsUsers_Name[i], lsUsers_reg_id[i], lsUsers_FileName[i], lsUsers_ID[i]));
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
                    adapter = new BrandsUsersListViewAdapter(getActivity(), BrandsUsersList);
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

class BrandsUsersListViewItem
{
    public final String txtChatListUserName;
    public final String ChatListUserRegId, ChatListUserFileName;
    public final int ChatListUsersID;

    public BrandsUsersListViewItem(String txtChatListUserName, String ChatListUserRegId, String ChatListUserFileName, int ChatListUsersID)
    {
        this.txtChatListUserName = txtChatListUserName;
        this.ChatListUserRegId = ChatListUserRegId;
        this.ChatListUserFileName = ChatListUserFileName;
        this.ChatListUsersID = ChatListUsersID;
    }
}

class BrandsUsersListViewAdapter extends ArrayAdapter<BrandsUsersListViewItem>
{
    private List<BrandsUsersListViewItem> items;
    private LayoutInflater layoutInflater;

    public BrandsUsersListViewAdapter(Context context, List<BrandsUsersListViewItem> items)
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
    public BrandsUsersListViewItem getItem(int position)
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
        BrandsUsersListViewItem item = getItem(position);
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

        viewHolder.rlUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chat = new Intent(getContext(), ChatListBrandsUserActivity.class);
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


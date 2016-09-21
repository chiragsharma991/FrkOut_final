package com.codeginger.frkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.widget.ArrayAdapter;
import java.util.List;
import android.support.v4.app.ListFragment;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import android.widget.ListView;

public class SurveysActiveFragment extends ListFragment
{
    private List<ListViewItem> mItems;        // ListView items list
    Common c;
    UserSessionManager usm;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // initialize the items list
        mItems = new ArrayList<ListViewItem>();
        Resources resources = getResources();

        //mItems.add(new ListViewItem(resources.getDrawable(R.drawable.ic_launcher), getString(R.string.bebo), getString(R.string.bebo_description)));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 1", "Descrption 1"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 2", "Descrption 2"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 3", "Descrption 3"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 4", "Descrption 4"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 5", "Descrption 5"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 6", "Descrption 6"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 7", "Descrption 7"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 8", "Descrption 8"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 9", "Descrption 9"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 10", "Descrption 10"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 11", "Descrption 11"));
        mItems.add(new ListViewItem(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher1), "Title 12", "Descrption 12"));
        // initialize and set the list adapter
        setListAdapter(new ListViewDemoAdapter(getActivity(), mItems));
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        // retrieve theListView item
        ListViewItem item = mItems.get(position);
        // do something
        Toast.makeText(getActivity(), item.title, Toast.LENGTH_SHORT).show();
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_surveys_active, container, false);

        dbHelper = new DbHelper(getActivity());
        c = new Common();
        usm = new UserSessionManager(getActivity());
        HashMap<String, String> app =  usm.getUserDetails();

        return rootView;
    }
*/

}

class ListViewItem
{
    public final Drawable icon;
    public final String title;
    public final String description;

    public ListViewItem(Drawable icon, String title, String description)
    {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }
}

class ListViewDemoAdapter extends ArrayAdapter<ListViewItem>
{
    public ListViewDemoAdapter(Context context, List<ListViewItem> items)
    {
        super(context, R.layout.fragment_surveys_active_listview, items);
    }

    // The view holder design pattern prevents using findViewById() repeatedly in the getView() method of the adapter.
    // developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
    private static class ViewHolder
    {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;

        if(convertView == null)
        {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fragment_surveys_active_listview, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(viewHolder);
        }
        else
        {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        ListViewItem item = getItem(position);
        viewHolder.ivIcon.setImageDrawable(item.icon);
        viewHolder.tvTitle.setText(item.title);
        viewHolder.tvDescription.setText(item.description);

        return convertView;
    }


}
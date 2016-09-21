package com.codeginger.frkout;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmpt on 6/20/2016.
 */
public class CustomAdapter extends ArrayAdapter<NewclassforInterest>
{
    public static ArrayList<NewclassforInterest> list;
    private SparseBooleanArray mSelectedItemsIds;
    private LayoutInflater inflater;
    private Context mContext;
    private ViewHolder holder;
    private int itemselected;
    private int select;
    public static boolean chkBool[];


    public CustomAdapter(Context context, int resourceId,ArrayList<NewclassforInterest> list) {
        super(context, resourceId, list);
        mSelectedItemsIds = new SparseBooleanArray();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.list = list;
        chkBool=new boolean[list.size()];
        for (int i=0;i<chkBool.length;i++)
        {
            chkBool[i]=false;
        }
    }






    private static class ViewHolder {
        TextView itemName,item_id;
    }

    public View getView(int position, View view, ViewGroup parent) {

        NewclassforInterest newclassforInterest=getItem(position);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.raw_layout, null);
            holder.itemName = (TextView) view.findViewById(R.id.interest_name);
            holder.item_id = (TextView) view.findViewById(R.id.interest_id);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.itemName.setText( newclassforInterest.getArea());
        holder.item_id.setText(String.valueOf(newclassforInterest.getId()) );
        if(chkBool[position]){
           // holder.itemName.setTextColor(Color.WHITE);
            holder.itemName.setBackgroundColor(Color.parseColor("#3990CA"));
        }else{
           // holder.itemName.setTextColor(Color.BLACK);
            holder.itemName.setBackgroundColor(Color.parseColor("#00000000"));
        }
        /*if(position==itemselected && select==100)
        {
            view.setBackgroundColor(Color.GREEN);
            holder.itemName.setTextColor(Color.RED);
        }*/
        return view;
    }


    public void remove(String string) {
        list.remove(string);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        Log.e("selected position",""+position);
        Log.e("boolean value",""+value);
        if (value){
            mSelectedItemsIds.put(position, value);
            Log.e("selected","log");
             select=100;
            itemselected=position;
            notifyDataSetChanged();


        }
        else{
            mSelectedItemsIds.delete(position);
            Log.e("deselected","log");
            notifyDataSetChanged();
           // holder.itemName.setBackgroundColor(Color.WHITE);

        }
                }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


}

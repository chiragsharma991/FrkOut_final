package com.codeginger.frkout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.List;

// Created by Pratik Mehta

public class LoginFragment extends Fragment
{
    EditText name, mobno;
    Button login;
    List<NameValuePair> params;
    ProgressDialog progress;

    String GCM_REG_ID;
    Common c;
    UserSessionManager usm;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        c = new Common();
        usm = new UserSessionManager(getActivity());
        HashMap<String, String> app =  usm.getUserDetails();
        GCM_REG_ID = app.get(usm.KEY_GCM_REG_ID);

        name = (EditText)view.findViewById(R.id.name);
        mobno = (EditText)view.findViewById(R.id.mobno);
        login = (Button)view.findViewById(R.id.log_btn);

        progress = new ProgressDialog(getActivity());
        progress.setMessage("Registering ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progress.show();
                new Login().execute();
            }
        });

        return view;
    }
    private class Login extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... args)
        {
            String result = "";
            try
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ChatUsers_name", name.getText().toString());
                jsonObject.put("ChatUsers_mobno", mobno.getText().toString());
                jsonObject.put("ChatUsers_reg_id", GCM_REG_ID);
                String json = jsonObject.toString();
                result = c.PostData(getResources().getString(R.string.login), json);
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
            progress.dismiss();
            try
            {
                if(result != null && result != "")
                {
                    if (result.contains("Sucessfully Registered") || result.contains("User already Registered"))

                    {
                        usm.editor.putString(usm.KEY_GCM_REG_FROM, mobno.getText().toString());
                        usm.editor.putString(usm.KEY_GCM_FROM_NAME, name.getText().toString());
                        usm.editor.commit();
/*
                        Fragment reg = new ChatListFollowersFragment();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, reg);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();
*/
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Error While Login", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Response is Empty" , Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

}
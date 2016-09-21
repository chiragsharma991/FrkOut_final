package com.codeginger.frkout;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.widget.TextView;
import android.widget.Toast;

public class BroadcastCampaignFragment extends Fragment
{
    EditText etxtBroadCampName, etxtBroadCampDesc;
    TextView txtBroadCampDate;
    Button btnBroadCampSave;
    long row = 0;

    DbHelper dbHelper;
    Common c;
    UserSessionManager usm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_broadcast_campaign, container, false);

        etxtBroadCampName = (EditText) rootView.findViewById(R.id.etxtBroadCampName);
        etxtBroadCampDesc = (EditText) rootView.findViewById(R.id.etxtBroadCampDesc);
        txtBroadCampDate = (TextView) rootView.findViewById(R.id.txtBroadCampDate);
        btnBroadCampSave = (Button) rootView.findViewById(R.id.btnBroadCampSave);
        dbHelper = new DbHelper(getActivity());

        c = new Common();
        usm = new UserSessionManager(getActivity());
        HashMap<String, String> app =  usm.getUserDetails();
        if(app.get(usm.KEY_BROADCAST_ID) != null)
        {
            row = Long.parseLong(app.get(usm.KEY_BROADCAST_ID));
        }

        txtBroadCampDate.setText(c.getDateTime());

        btnBroadCampSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (etxtBroadCampName.getText().toString().length() > 0 && etxtBroadCampDesc.getText().toString().length() > 0)
                {
                    if(row != 0)
                    {
                        row = dbHelper.updateBrodCamp(etxtBroadCampName.getText().toString(), etxtBroadCampDesc.getText().toString(), txtBroadCampDate.getText().toString(), row);
                    }
                    else
                    {
                        row = dbHelper.insertBrodCamp(etxtBroadCampName.getText().toString(), etxtBroadCampDesc.getText().toString(), txtBroadCampDate.getText().toString());
                    }
                    usm.editor.putString(usm.KEY_BROADCAST_ID, row + "");
                    usm.editor.commit();
                    Toast.makeText(getActivity(), getResources().getString(R.string.broad_camp_save), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (!(etxtBroadCampName.getText().toString().length() > 0))
                    {
                        etxtBroadCampName.setError(getResources().getString(R.string.broad_camp_name_validation));
                    }
                    if (!(etxtBroadCampDesc.getText().toString().length() > 0))
                    {
                        etxtBroadCampDesc.setError(getResources().getString(R.string.broad_camp_desc_validation));
                    }
                }
            }
        });

        txtBroadCampDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String sdayOfMonth, smonthOfYear;
                        if (dayOfMonth < 10) {
                            sdayOfMonth = "0" + dayOfMonth;
                        } else {
                            sdayOfMonth = "" + dayOfMonth;
                        }
                        monthOfYear = monthOfYear + 1;
                        if (monthOfYear < 10) {
                            smonthOfYear = "0" + monthOfYear;
                        } else {
                            smonthOfYear = "" + monthOfYear;
                        }
                        txtBroadCampDate.setText((sdayOfMonth) + "/" + smonthOfYear + "/" + (year));
                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(new Date().getTime());
                dpd.show();
            }
        });

        return rootView;
    }


}
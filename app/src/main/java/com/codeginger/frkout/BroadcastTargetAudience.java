package com.codeginger.frkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class BroadcastTargetAudience extends Fragment
{
    RelativeLayout rlBroadAud;
    LinearLayout llBroadAudAgeGrp;
    RadioGroup rgBroadAudGender, rgBroadAudAgeGrp;
    Button btnBroadCampSave;
    EditText etxtBroadAudAgeFrom, etxtBroadAudAgeTo;
    String gender, ageGrp, ageGrpFrom, ageGrpTo;
    long row = 0;

    DbHelper dbHelper;
    Common c;
    UserSessionManager usm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_broadcast_audience, container, false);

        dbHelper = new DbHelper(getActivity());
        c = new Common();
        usm = new UserSessionManager(getActivity());
        HashMap<String, String> app =  usm.getUserDetails();
        if(app.get(usm.KEY_BROADCAST_ID) != null)
        {
            row = Long.parseLong(app.get(usm.KEY_BROADCAST_ID));
        }

        rlBroadAud = (RelativeLayout) rootView.findViewById(R.id.rlBroadAud);

        llBroadAudAgeGrp = (LinearLayout) rootView.findViewById(R.id.llBroadAudAgeGrp);

        rgBroadAudGender = (RadioGroup) rootView.findViewById(R.id.rgBroadAudGender);
        rgBroadAudAgeGrp = (RadioGroup) rootView.findViewById(R.id.rgBroadAudAgeGrp);

        btnBroadCampSave = (Button) rootView.findViewById(R.id.btnBroadCampSave);

        etxtBroadAudAgeFrom = (EditText) rootView.findViewById(R.id.etxtBroadAudAgeFrom);
        etxtBroadAudAgeTo = (EditText) rootView.findViewById(R.id.etxtBroadAudAgeTo);

        btnBroadCampSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isAudienceValidate())
                {
                    RadioButton rbGender = (RadioButton) rgBroadAudGender.findViewById(rgBroadAudGender.getCheckedRadioButtonId());
                    gender = (String) rbGender.getText();
                    RadioButton rbAgeGrp = (RadioButton) rgBroadAudAgeGrp.findViewById(rgBroadAudAgeGrp.getCheckedRadioButtonId());
                    ageGrp = (String) rbAgeGrp.getText();
                    if(ageGrp.equals("Custom"))
                    {
                        ageGrpFrom = etxtBroadAudAgeFrom.getText().toString();
                        ageGrpTo = etxtBroadAudAgeTo.getText().toString();
                    }
                    else if(ageGrp.equals("All"))
                    {
                        ageGrpFrom = "";
                        ageGrpTo = "";
                    }

                    HashMap<String, String> app =  usm.getUserDetails();
                    if(app.get(usm.KEY_BROADCAST_ID) != null)
                    {
                        row = Long.parseLong(app.get(usm.KEY_BROADCAST_ID));
                    }

                    if(row != 0)
                    {
                        row = dbHelper.updateBrodAud(gender, ageGrp, ageGrpFrom, ageGrpTo, row);
                        Toast.makeText(getActivity(), getResources().getString(R.string.broad_camp_save), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), getResources().getString(R.string.campaign_validation), Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    if(rgBroadAudAgeGrp.getCheckedRadioButtonId() == R.id.rdbAgeGrpCustom)
                    {
                        if(!(etxtBroadAudAgeFrom.getText().toString().length() > 0))
                        {
                            etxtBroadAudAgeFrom.setError(getResources().getString(R.string.broad_aud_agefrom_validation));
                        }
                        if(!(etxtBroadAudAgeTo.getText().toString().length() > 0))
                        {
                            etxtBroadAudAgeTo.setError(getResources().getString(R.string.broad_aud_ageto_validation));
                        }
                    }
                }
            }
        });

        rgBroadAudAgeGrp.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if(checkedId == R.id.rdbAgeGrpCustom)
                {
                    llBroadAudAgeGrp.setVisibility(View.VISIBLE);
                }
                else if(checkedId == R.id.rdbAgeGrpAll)
                {
                    llBroadAudAgeGrp.setVisibility(View.GONE);
                }
            }
        });

        return rootView;
    }

    public boolean isAudienceValidate()
    {
        if(rgBroadAudAgeGrp.getCheckedRadioButtonId() == R.id.rdbAgeGrpCustom)
        {
            if(etxtBroadAudAgeFrom.getText().toString().length() > 0 && etxtBroadAudAgeTo.getText().toString().length() > 0)
            {
                  return  true;
            }
        }
        else if(rgBroadAudAgeGrp.getCheckedRadioButtonId() == R.id.rdbAgeGrpAll)
        {
            return  true;
        }
        return  false;
    }
}

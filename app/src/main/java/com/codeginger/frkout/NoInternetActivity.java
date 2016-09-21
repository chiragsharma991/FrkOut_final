package com.codeginger.frkout;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

// Created by Pratik Mehta

public class NoInternetActivity extends ActionBarActivity
{
    Common c;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernet);
        c = new Common();
    }

    public void Tryagain(View view)
    {
        if(c.isConnected(getApplicationContext()))
        {
            startActivity(new Intent(NoInternetActivity.this,SplashScreenActivity.class));
            finish();
        }
        else
        {
            Toast.makeText(NoInternetActivity.this, getResources().getString(R.string.nointernet_title), Toast.LENGTH_SHORT).show();
        }
    }


}

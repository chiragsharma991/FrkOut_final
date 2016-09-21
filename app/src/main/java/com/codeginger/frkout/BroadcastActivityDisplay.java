package com.codeginger.frkout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.InputStream;

public class BroadcastActivityDisplay extends ActionBarActivity
{
    TextView txtBroadDisplayTitle, txtBroadDisplayDesc;
    ImageView imgBroadDisplayImage;

    String title, description, type, filename;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_activity_display);
        findViewById();
        Bundle bundle = getIntent().getBundleExtra("BROADCAST");
        if(bundle != null)
        {
            title = bundle.getString("title");
            description = bundle.getString("description");
            type = bundle.getString("type");
            filename = bundle.getString("filename");
        }

        txtBroadDisplayTitle.setText(title);
        txtBroadDisplayDesc.setText(description);

        if(type.equals("IMAGE"))
        {
            if(filename != null && !filename.isEmpty())
            {
                String downloadImageUrl = getResources().getString(R.string.image_url) + File.separator + filename;
                String[] toArr = {downloadImageUrl};
                new ImageDownloader().execute(toArr);
            }
        }

    }

    // FindViewById
    public void findViewById()
    {
        txtBroadDisplayTitle = (TextView) findViewById(R.id.txtBroadDisplayTitle);
        txtBroadDisplayDesc = (TextView) findViewById(R.id.txtBroadDisplayDesc);
        imgBroadDisplayImage = (ImageView) findViewById(R.id.imgBroadDisplayImage);
    }


    private class ImageDownloader extends AsyncTask<String, String, Bitmap>
    {
        //String[] toArr = {downloadImageUrl};
        String downloadImageUrl;

        @Override
        protected Bitmap doInBackground(String... params)
        {
            downloadImageUrl = params[0];
            return downloadBitmap(downloadImageUrl);
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(Bitmap photo)
        {
            imgBroadDisplayImage.setVisibility(View.VISIBLE);
            imgBroadDisplayImage.setImageBitmap(photo);
        }

        private Bitmap downloadBitmap(String url)
        {
            final DefaultHttpClient client = new DefaultHttpClient();
            final HttpGet getRequest = new HttpGet(url);
            try
            {
                HttpResponse response = client.execute(getRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK)
                {
                    return null;
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null)
                {
                    InputStream inputStream = null;
                    try
                    {
                        inputStream = entity.getContent();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        return bitmap;
                    }
                    finally
                    {
                        if (inputStream != null)
                        {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            }
            catch (Exception e)
            {
                getRequest.abort();
            }
            return null;
        }
    }

}

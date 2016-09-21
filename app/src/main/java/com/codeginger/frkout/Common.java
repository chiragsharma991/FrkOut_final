package com.codeginger.frkout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;

// Created by Pratik Mehta

public class Common
{
    String result = "";

    URL url;
    HttpURLConnection connection = null;
    InputStream inputStream = null;

    // Check Internet Conncetion
    public boolean isConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }


    // Get Request
    public String GetData(String urlPath)
    {
        try
        {
            result = "";
            url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = connection.getResponseCode();

            if (responseCode == 200)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                in.close();

                result = response.toString();
            }

        }
        catch (Exception e)
        {
            result = e.toString();
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
        return result;
    }

    // Post Request
    public String PostDataOld(String urlPath, String urlParameters)
    {
        try
        {
            result = "";
            url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();

            //add reuqest header
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Language","en-US,en;q=0.5");
            // String urlParameters1 = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

            // Send post request
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                in.close();
                result = response.toString();
            }
        }
        catch (Exception e)
        {
            result = e.toString();
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
        return result;
    }

    public String PostData(String url, String json)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content� �
            httpPost.setHeader("Content-type", "application/json");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        }
        catch (Exception e)
        {
            System.out.print(e);
        }
        // 11. return result
        return result;
    }

    // Generate Random Number
    public int randomNo(Context context)
    {
        Random r = new Random();
        int otp_code = r.nextInt(9999 - 1001) + 1001;
        return otp_code;
    }

    // Get Current Time in DD/MM/YYYY Format
    public String getDateTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    // Get Current Time in DDMMYYYY_HHMMSS Format
    public String getDDMMYYCurrentDateTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
        return dateFormat.format(new Date());
    }

    // Check Is EmailId is Valid
    public boolean isValidEmail(CharSequence target)
    {
        if (target == null)
        {
            return false;
        }
        else
        {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        String result = "";
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
        }
        catch (Exception e)
        {
            System.out.print(e);
        }
        return result;
    }

}

class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap>
{
    private final WeakReference<ImageView> imageViewReference;

    public ImageDownloaderTask(ImageView imageView)
    {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params)
    {
        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if (isCancelled())
        {
            bitmap = null;
        }

        if (imageViewReference != null)
        {
            ImageView imageView = imageViewReference.get();
            if (imageView != null)
            {
                imageView.setVisibility(View.VISIBLE);
                if (bitmap != null)
                {
                    //imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    imageView.setImageBitmap(bitmap);
                }
                else
                {
                    //imageView.setBackgroundResource(R.mipmap.ic_launcher);
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.mipmap.ic_launcher1);
                    imageView.setImageDrawable(placeholder);
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url)
    {
        HttpURLConnection urlConnection = null;
        try
        {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK)
            {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null)
            {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        }
        catch (Exception e)
        {
            urlConnection.disconnect();
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}


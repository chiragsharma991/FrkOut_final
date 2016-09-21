package com.codeginger.frkout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

public class BroadcastCreativeFragment extends Fragment
{
    RelativeLayout rlBroadCreativeType, rlBroadCreativeText, rlBroadCreativeImage, rlBroadCreativeVideo;
    RadioGroup rgBroadCreativeType;

    Button btnBroadCreativeSave,btnBroadCreativeTextSave, btnBroadCreativeImageSave, btnBroadCreativeVideoSave;
    Button btnBroadCreativeTextBack, btnBroadCreativeImageBack, btnBroadCreativeVideoBack;
    Button btnBroadCreativeUploadImage, btnBroadCreativeTakeImage;
    EditText etxtBroadCreativeTextTitle, etxtBroadCreativeTextDesc, etxtBroadCreativeImageTitle, etxtBroadCreativeImageDesc;
    ImageView imgBroadCreativeImage;

    int intCheckedId;
    String title, description, type;

    String USER_ID, strImageName = "", CurrentPhotoPath, strImgByte = "";
    private static final int CAMERA_REQUEST = 0, VIDEO_REQUEST = 1, SELECT_IMAGE = 2, SELECT_VIDEO = 3, SELECT_IMAGE_VIDEO = 4;
    private Bitmap photo;
    Uri selectedImage;
    int imageRotate = 0;
    File Imagefile;

    Common c;
    UserSessionManager usm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_broadcast_creative, container, false);

        c = new Common();
        usm = new UserSessionManager(getActivity());
        HashMap<String, String> app =  usm.getUserDetails();
        USER_ID = app.get(usm.KEY_USER_ID);

        rlBroadCreativeType = (RelativeLayout) rootView.findViewById(R.id.rlBroadCreativeType);
        rlBroadCreativeText = (RelativeLayout) rootView.findViewById(R.id.rlBroadCreativeText);
        rlBroadCreativeImage = (RelativeLayout) rootView.findViewById(R.id.rlBroadCreativeImage);
        rlBroadCreativeVideo = (RelativeLayout) rootView.findViewById(R.id.rlBroadCreativeVideo);

        btnBroadCreativeSave = (Button) rootView.findViewById(R.id.btnBroadCreativeSave);
        btnBroadCreativeTextSave = (Button) rootView.findViewById(R.id.btnBroadCreativeTextSave);
        btnBroadCreativeImageSave = (Button) rootView.findViewById(R.id.btnBroadCreativeImageSave);
        btnBroadCreativeVideoSave = (Button) rootView.findViewById(R.id.btnBroadCreativeVideoSave);

        btnBroadCreativeTextBack = (Button) rootView.findViewById(R.id.btnBroadCreativeTextBack);
        btnBroadCreativeImageBack = (Button) rootView.findViewById(R.id.btnBroadCreativeImageBack);
        btnBroadCreativeVideoBack = (Button) rootView.findViewById(R.id.btnBroadCreativeVideoBack);

        btnBroadCreativeUploadImage = (Button) rootView.findViewById(R.id.btnBroadCreativeUploadImage);
        btnBroadCreativeTakeImage = (Button) rootView.findViewById(R.id.btnBroadCreativeTakeImage);

        etxtBroadCreativeTextTitle = (EditText) rootView.findViewById(R.id.etxtBroadCreativeTextTitle);
        etxtBroadCreativeTextDesc = (EditText) rootView.findViewById(R.id.etxtBroadCreativeTextDesc);
        etxtBroadCreativeImageTitle = (EditText) rootView.findViewById(R.id.etxtBroadCreativeImageTitle);
        etxtBroadCreativeImageDesc = (EditText) rootView.findViewById(R.id.etxtBroadCreativeImageDesc);

        rgBroadCreativeType = (RadioGroup) rootView.findViewById(R.id.rgBroadCreativeType);
        imgBroadCreativeImage = (ImageView) rootView.findViewById(R.id.imgBroadCreativeImage);

        btnBroadCreativeUploadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChooseImage();
            }
        });

        btnBroadCreativeTakeImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                takeImage();
            }
        });


        btnBroadCreativeImageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etxtBroadCreativeImageTitle.getText().toString().length() > 0 && etxtBroadCreativeImageDesc.getText().toString().length() > 0 && photo != null) {
                    title = etxtBroadCreativeImageTitle.getText().toString();
                    description = etxtBroadCreativeImageDesc.getText().toString();
                    type = "IMAGE";

                    // Convert Image to Byte
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.img_quality), outputStream);
                    byte[] bytes_pic = outputStream.toByteArray();
                    strImgByte = Base64.encodeToString(bytes_pic, Base64.NO_WRAP);

                    new Send().execute();
                } else {
                    if (!(etxtBroadCreativeImageTitle.getText().toString().length() > 0)) {
                        etxtBroadCreativeImageTitle.setError(getResources().getString(R.string.broad_creative_creativetitle_validation));
                    }
                    if (!(etxtBroadCreativeImageDesc.getText().toString().length() > 0)) {
                        etxtBroadCreativeImageDesc.setError(getResources().getString(R.string.broad_creative_creativedesc_validation));
                    }
                    if (!(photo != null)) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.broad_creative_imageupload_validation), Toast.LENGTH_LONG);
                    }
                }
            }
        });

        btnBroadCreativeTextSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(etxtBroadCreativeTextTitle.getText().toString().length() > 0 && etxtBroadCreativeTextDesc.getText().toString().length() > 0)
                {
                    title = etxtBroadCreativeTextTitle.getText().toString();
                    description = etxtBroadCreativeTextDesc.getText().toString();
                    type = "TEXT";
                    strImageName = "";
                    strImgByte = "";
                    new Send().execute();
                }
                else
                {
                    if(!(etxtBroadCreativeTextTitle.getText().toString().length() > 0))
                    {
                        etxtBroadCreativeTextTitle.setError(getResources().getString(R.string.broad_creative_creativetitle_validation));
                    }
                    if(!(etxtBroadCreativeTextDesc.getText().toString().length() > 0))
                    {
                        etxtBroadCreativeTextDesc.setError(getResources().getString(R.string.broad_creative_creativedesc_validation));
                    }
                }
            }
        });

        btnBroadCreativeSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intCheckedId = rgBroadCreativeType.getCheckedRadioButtonId();

                if(intCheckedId == R.id.rdbBroadCreativeText)
                {
                    rlBroadCreativeType.setVisibility(View.GONE);
                    rlBroadCreativeText.setVisibility(View.VISIBLE);
                }
                else if(intCheckedId == R.id.rdbBroadCreativeImage)
                {
                    rlBroadCreativeType.setVisibility(View.GONE);
                    rlBroadCreativeImage.setVisibility(View.VISIBLE);
                }
                else if(intCheckedId == R.id.rdbBroadCreativeVideo)
                {
                    rlBroadCreativeType.setVisibility(View.GONE);
                    rlBroadCreativeVideo.setVisibility(View.VISIBLE);
                }
            }
        });

        btnBroadCreativeTextBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rlBroadCreativeText.setVisibility(View.GONE);
                rlBroadCreativeType.setVisibility(View.VISIBLE);
            }
        });

        btnBroadCreativeImageBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rlBroadCreativeImage.setVisibility(View.GONE);
                rlBroadCreativeType.setVisibility(View.VISIBLE);
            }
        });

        btnBroadCreativeVideoBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rlBroadCreativeVideo.setVisibility(View.GONE);
                rlBroadCreativeType.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }

    private class Send extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... args)
        {
            String result = "";
            try
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("BroadcastMed_Title", title);
                jsonObject.put("BroadcastMed_Description", description);
                jsonObject.put("BroadcastMed_Type", type);
                jsonObject.put("BroadcastMed_FileName", strImageName);
                jsonObject.put("BroadcastMed_FileByte", strImgByte);
                String json = jsonObject.toString();
                result = c.PostData(getResources().getString(R.string.Broadcast), json);
                //Clear Data
                title = "";
                description = "";
                strImageName = "";
                strImgByte = "";
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
            try
            {
                if(result != null && result != "")
                {
                    if (result.contains("Success"))
                    {
                        //Success
                        Toast.makeText(getActivity(), "Broadcast Message Send Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Response is Empty", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public void ChooseImage()
    {
        strImageName = getResources().getString(R.string.img) + c.getDDMMYYCurrentDateTime() + "_"  + USER_ID + ".Jpeg";
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getResources().getText(R.string.broad_creative_imageupload_validation)),SELECT_IMAGE);
    }

    public void takeImage()
    {
        try
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            strImageName = getResources().getString(R.string.img) + c.getDDMMYYCurrentDateTime() + "_"  + USER_ID + ".Jpeg";
            Imagefile = new File(Environment.getExternalStoragePublicDirectory(getResources().getString(R.string.app_name)) + File.separator + getResources().getString(R.string.image_folder), strImageName);
            CurrentPhotoPath = Imagefile.getAbsolutePath();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Imagefile));
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
        catch (Exception e)
        {
            System.out.print(e);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Capture Image and Choose Image
        if ((requestCode == CAMERA_REQUEST || requestCode == SELECT_IMAGE))
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                try
                {
                    photo = null;
                    // Image Capture
                    if (requestCode == CAMERA_REQUEST)
                    {
                        try
                        {
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            bmOptions.inJustDecodeBounds = false;
                            bmOptions.inSampleSize = 1;
                            photo = BitmapFactory.decodeFile(CurrentPhotoPath, bmOptions);
                            selectedImage = Uri.fromFile(new File(CurrentPhotoPath));
                        }
                        catch (Exception e)
                        {
                            System.out.println("Exception : " + e);
                        }
                    }

                    // Image Choose
                    else if (requestCode == SELECT_IMAGE && data != null)
                    {
                        try
                        {
                            selectedImage = data.getData();
                            photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            CurrentPhotoPath = selectedImage.getPath();
                        }
                        catch (Exception e)
                        {
                            System.out.println("Exception : " + e);
                        }
                    }

                    // Rotation Check
                    Imagefile = new File(CurrentPhotoPath);
                    ExifInterface exif = new ExifInterface(Imagefile.getAbsolutePath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation)
                    {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            imageRotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            imageRotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            imageRotate = 90;
                            break;
                    }

                    Matrix matrix = new Matrix();
                    matrix.postRotate(imageRotate);
                    imageRotate = 0;
                    photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
                    imgBroadCreativeImage.setVisibility(View.VISIBLE);
                    imgBroadCreativeImage.setImageBitmap(photo);
                }
                catch (Exception e)
                {
                    System.out.println("Exception : " + e);
                }
            }
            else
            {
                strImageName = "";
            }
        }
    }

}
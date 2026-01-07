package com.sabapp.saba.events;



import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.sabapp.saba.R;
import com.sabapp.saba.adapters.SelectedCapabilityAdapter;
import com.sabapp.saba.adapters.finalbudgetRecyclerAdapter;
import com.sabapp.saba.adapters.vendormatchingRecyclerAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.SelectedCapabilityItem;
import com.sabapp.saba.data.model.SelectedVendorItem;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.sabaDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class finalizesetup extends AppCompatActivity {


    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    AppCompatSpinner setup_type_spinner;

    JSONArray dataobj;



    RecyclerView servicesselected;

    ImageView backbuttonImage;

    LinearLayout nextbuttonLayout;

    String EventId,eventBudgetvalue;

    ArrayList<String> capability_codeList;

    private String Document_img1="";

    ArrayList<String> base_pricelist;
    ArrayList<JSONObject> capability_detailslist;
    ArrayList<String> capability_idlist;
    ArrayList<String> service_image_locationlist;


    ArrayList<String> vendorserviceimage_idList;
    ArrayList<String> vendorserviceimagelocationList;
    ArrayList<String> vendoridList;

    ArrayList<String> vendornameList;
    ArrayList<String> vendorcapabilitynameList;
    ArrayList<String> vendorlocationList;

    ArrayList<sabaEventItem> eventwholearray;

    vendormatchingRecyclerAdapter sabaeventsadapter;

    ArrayList<SelectedCapabilityItem> capabilityList;

    ArrayList<SelectedVendorItem> vendorselectedList;

    ImageView addeventimage;

    RelativeLayout eventselectLayout;

    PermissionListener permissionlistener;

    TextView continuetonextbuttontext, budgetallocatedText;

    LinearLayout continuetonextbutton;

    private int GALLERY = 2, CAMERA = 1;

    public void showProgressBar()
    {

        progressindicator.smoothToShow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void hideProgressBar()
    {

        progressindicator.smoothToHide();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onBackPressed(){

        super.onBackPressed();
        Intent intent2 = new Intent(finalizesetup.this, sabaDrawerActivity.class);
        //intent2.putStringArrayListExtra("selected_services", selectedCapabilities);
        //intent2.putExtra("selected_services", selected);
        intent2.putExtra("event_id", EventId);
        startActivity(intent2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalizeclientsetup);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.progressindicator);
        app = (sabaapp) getApplicationContext();

        servicesselected = (RecyclerView) findViewById(R.id.selectableoptions);

        backbuttonImage = (ImageView) findViewById(R.id.backbuttonimage);

        addeventimage = (ImageView)findViewById(R.id.addeventimage);

        eventselectLayout = (RelativeLayout)findViewById(R.id.serviceselectedlayout);

        continuetonextbuttontext = (TextView)findViewById(R.id.nextbuttontext) ;
        continuetonextbutton = (LinearLayout)findViewById(R.id.nextbuttonselect);
        budgetallocatedText = (TextView)findViewById(R.id.budgetallocatedtext);


        Intent intent = getIntent();

        EventId = intent.getStringExtra("event_id");

        eventBudgetvalue = intent.getStringExtra("budget");

        if (eventBudgetvalue !=null && !eventBudgetvalue.isEmpty() && !eventBudgetvalue.equals("")){
            budgetallocatedText.setText("$"+eventBudgetvalue+"/ $0");
        }

        Serializable serializable = getIntent().getSerializableExtra("selected_services");
        if (serializable instanceof ArrayList<?>) {
            capabilityList =
                    (ArrayList<SelectedCapabilityItem>) serializable;

            // Set up RecyclerView
            servicesselected.setLayoutManager(new LinearLayoutManager(this));
            finalbudgetRecyclerAdapter adapter = new finalbudgetRecyclerAdapter(capabilityList, this);
            servicesselected.setAdapter(adapter);


            // Optional: Log to check
            for (SelectedCapabilityItem item : capabilityList) {
                Log.d("CAPABILITY_ITEM", item.getCode() + " - " + item.getName());
            }
        }


        Serializable serializable2 = getIntent().getSerializableExtra("selected_vendors");

        if (serializable2 instanceof ArrayList<?>) {
            vendorselectedList =
                    (ArrayList<SelectedVendorItem>) serializable2;

            for (SelectedVendorItem item : vendorselectedList) {
                Log.d("Vendor_ITEM", item.getCode() + " - " + item.getName());
            }
        }




        eventselectLayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(chooseservices.this, "Messages link clicked", Toast.LENGTH_SHORT).show();

            TedPermission.create()
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage(
                            "Camera permission is required to take photos.\n\nEnable it in Settings > Permissions"
                    )
                    .setPermissions(Manifest.permission.CAMERA)
                    .check();



        });

        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImage();


            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }



        };

        continuetonextbutton.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(chooseservices.this, "Messages link clicked", Toast.LENGTH_SHORT).show();

            postToFacebook();


        });





    }


    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(finalizesetup.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryLauncher.launch(intent);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        // 📸 CAMERA
        if (requestCode == CAMERA && data.getExtras() != null) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            if (thumbnail != null) {
                thumbnail = getResizedBitmap(thumbnail, 600);
                addeventimage.setImageBitmap(thumbnail);
                BitMapToString(thumbnail);
                addeventimage.setVisibility(View.VISIBLE);
            }

        }
        // 🖼️ GALLERY
        else if (requestCode == 2 && data.getData() != null) {

            Uri selectedImage = data.getData();

            try (InputStream inputStream =
                         getContentResolver().openInputStream(selectedImage)) {

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap = getResizedBitmap(bitmap, 600);

                addeventimage.setImageBitmap(bitmap);
                BitMapToString(bitmap);
                addeventimage.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Log.e("IMAGE_PICK", "Failed to load image", e);
            }
        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        app.setCreateAdImage(Document_img1);




        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try (InputStream inputStream = getContentResolver().openInputStream(selectedImage)) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmap = getResizedBitmap(bitmap, 600);
                        addeventimage.setImageBitmap(bitmap);
                        BitMapToString(bitmap);
                        addeventimage.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Log.e("IMAGE_PICK", "Failed to load image", e);
                    }
                }
            }
    );





    private void postToFacebook()
    {
        continuetonextbuttontext.setText("Setting up event...");
        showProgressBar();


        String phonenum;

        Log.d("API User Name", app.getApiusername() + "");
        Log.d("API Password", app.getApipassword() + "");


        JSONObject payload = new JSONObject();

        try {
            // =============================
            // ROOT PAYLOAD
            // =============================
            payload.put("image", app.getCreateAdImage());
            payload.put("eventid", EventId);
            payload.put("event_budget", eventBudgetvalue);

            // =============================
            // BUILD event_budgetitems ARRAY
            // =============================
            JSONArray capabilityCodesArray = new JSONArray();
            for (SelectedCapabilityItem item : capabilityList) {
                capabilityCodesArray.put(item.getCode());
            }


            JSONArray vendorlistArray = new JSONArray();
            for (SelectedVendorItem item : vendorselectedList) {
                vendorlistArray .put(item.getCode());
            }

            payload.put("capabilitieslist", capabilityCodesArray);

            payload.put("vendorlist", vendorlistArray);


        } catch (JSONException e) {
            hideProgressBar();
            e.printStackTrace();
            return;
        }


        String paymentsendpoint="https://api.sabaapp.co/v0/events/finalize";


        Log.e("response", "response ");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, paymentsendpoint, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "response "+response.toString());
                        hideProgressBar();
                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("STATUS");
                                String messagedetails =jsonObj.getString("MESSAGE");
                                if(message.toLowerCase().matches("success"))

                                {
                                    continuetonextbuttontext.setText("Request completed!");
                                    Log.e("Product Posted", "The product has been posted");

                                    Toast.makeText(getApplicationContext(), "Product added Successfully", Toast.LENGTH_SHORT).show();


                                    android.app.AlertDialog ad = new android.app.AlertDialog.Builder(finalizesetup.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Success");
                                    ad.setMessage("New event posted to your account");
                                    ad.setButton(getApplicationContext().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            startActivity(new Intent(getApplicationContext(), sabaDrawerActivity.class));

                                        }
                                    });
                                    ad.show();
                                }
                                else
                                {
                                    continuetonextbuttontext.setText("Finalise Setup");


                                    Toast.makeText(getApplicationContext(), "Failed :"+messagedetails, Toast.LENGTH_SHORT).show();

                                    android.app.AlertDialog ad = new android.app.AlertDialog.Builder(finalizesetup.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Failed");
                                    ad.setMessage(messagedetails);
                                    ad.setButton(getApplicationContext().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    ad.show();


                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("errorIs", "error"+e.getMessage());
                            }
                            hideProgressBar();

                        } else {
                            hideProgressBar();
                            Log.e("Your Array Response", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                continuetonextbuttontext.setText("Upload to your store");
                Log.e("error is ", "" + error);

                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //Log.e("error st_code ", "" + statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    //Log.e("encoding is ", "" + e.getMessage());
                    // exception
                }

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String apiusername = app.getApiusername();
                String apipassword = app.getApipassword();

                String creds = String.format("%s:%s",apiusername,apipassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization",auth);
                return params;
            }


        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


    }
}

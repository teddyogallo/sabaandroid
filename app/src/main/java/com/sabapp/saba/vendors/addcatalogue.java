package com.sabapp.saba.vendors;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.sabapp.saba.R;
import com.sabapp.saba.adapters.capabilityRecyclerAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.SelectedCapabilityItem;
import com.sabapp.saba.data.model.SelectedVendorItem;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.data.model.vendorservicesItem;
import com.sabapp.saba.events.chooseservices;
import com.sabapp.saba.events.finalizesetup;
import com.sabapp.saba.events.setupbudgets;
import com.sabapp.saba.sabaDrawerActivity;
import com.sabapp.saba.sabaVendorDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addcatalogue extends AppCompatActivity {

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    AppCompatSpinner setup_type_spinner;

    JSONArray dataobj;



    RecyclerView servicesselected;

    ImageView backbuttonImage;

    LinearLayout nextbuttonLayout;

    String EventId;


    ArrayList<String> capability_nameList;

    TextView continuetonextbuttontext;

    private String Document_img1="";

    EditText servicenameEdit, vendorlocationEdit, basepriceEdit;

    Spinner spinnerTypeservice;


    capabilityRecyclerAdapter sabaeventsadapter;

    ArrayList<vendorservicesItem> spinnerItems;

    String serviceId, serviceName;

    ImageView addeventimage;

    RelativeLayout eventselectLayout;

    PermissionListener permissionlistener;

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
        startActivity(new Intent(getApplicationContext(), sabaVendorDrawerActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcatalogue);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.progressindicator);
        app = (sabaapp) getApplicationContext();

        addeventimage = (ImageView)findViewById(R.id.addeventimage);

        eventselectLayout = (RelativeLayout)findViewById(R.id.serviceselectedlayout);

        spinnerItems = new ArrayList<>();


        spinnerTypeservice = findViewById(R.id.eventtypespinner);

        servicenameEdit = (EditText)findViewById(R.id.servicename);
        vendorlocationEdit = (EditText)findViewById(R.id.eventlocation);
        basepriceEdit = (EditText)findViewById(R.id.basepriceeditvalue);

        continuetonextbuttontext = (TextView)findViewById(R.id.nextbuttontext);

// Use ArrayAdapter
        ArrayAdapter<vendorservicesItem> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeservice.setAdapter(adapter);



        backbuttonImage = (ImageView) findViewById(R.id.backbuttonimage);


        nextbuttonLayout = (LinearLayout)findViewById(R.id.nextbuttonlayout);




        getCapabilitylist();



        backbuttonImage.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(chooseservices.this, "Messages link clicked", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(), sabaVendorDrawerActivity.class));


        });

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

        nextbuttonLayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            /*ArrayList<String> selectedCapabilities =
                    sabaeventsadapter.getSelectedCapabilityNames();*/


            if(servicenameEdit.getText().toString().matches("")){

                Toast.makeText(getApplicationContext(), "Service name cannot be empty", Toast.LENGTH_SHORT).show();


            }else if(vendorlocationEdit.getText().toString().matches("")){

                Toast.makeText(getApplicationContext(), "Location cannot be empty ", Toast.LENGTH_SHORT).show();

            }else if (basepriceEdit.getText().toString().matches("")){

                Toast.makeText(getApplicationContext(), "Base price cannot be empty", Toast.LENGTH_SHORT).show();


            }else if(serviceId.matches("")){

                Toast.makeText(getApplicationContext(), "Please select service type to continue", Toast.LENGTH_SHORT).show();


            }else{

                postToFacebook();
            }







        });

        spinnerTypeservice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vendorservicesItem selectedItem = (vendorservicesItem) parent.getItemAtPosition(position);
                serviceId = selectedItem.getId(); // The ID you want
                serviceName = selectedItem.getName(); // Optional: the name

                Log.d("SPINNER", "Selected ID: " + serviceId + ", Name: " + serviceName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: handle case where nothing is selected
            }
        });




        //end of oncreate
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(addcatalogue.this);
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
            //servicenameEdit, vendorlocationEdit, basepriceEdit
            payload.put("image", app.getCreateAdImage());
            payload.put("vendor_name", servicenameEdit.getText().toString());
            payload.put("vendor_location", vendorlocationEdit.getText().toString());
            payload.put("basepriceEdit", basepriceEdit.getText().toString());



        } catch (JSONException e) {
            hideProgressBar();
            e.printStackTrace();
            return;
        }


        String paymentsendpoint="https://api.sabaapp.co/v0/vendor/capability";


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


                                    android.app.AlertDialog ad = new android.app.AlertDialog.Builder(addcatalogue.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Success");
                                    ad.setMessage("New service posted to your account");
                                    ad.setButton(getApplicationContext().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            startActivity(new Intent(getApplicationContext(), sabaVendorDrawerActivity.class));

                                        }
                                    });
                                    ad.show();
                                }
                                else
                                {
                                    continuetonextbuttontext.setText("Finalise Setup");


                                    Toast.makeText(getApplicationContext(), "Failed :"+messagedetails, Toast.LENGTH_SHORT).show();

                                    android.app.AlertDialog ad = new android.app.AlertDialog.Builder(addcatalogue.this)
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


    private void getCapabilitylist()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();


        String request_username = app.getApiusername();


        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());


        String paymentsendpoint="https://api.sabaapp.co/v0/vendors/capabilities";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, paymentsendpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "ALL Products: "+response.toString());
                        hideProgressBar();
                        backbuttonImage.setEnabled(true);
                        nextbuttonLayout.setEnabled(true);
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
                                    dataobj = jsonObj.getJSONArray("DATA");

                                    if(dataobj.length()==0){
                                        // their are not values to add
                                        String messageerror="There was Zero products retrieved";
                                        Log.d("Msg:",messageerror);

                                        String id = null;
                                        String name = "No valid category";

                                        spinnerItems.add(new vendorservicesItem(id, name));


                                        //end of their are no values to add
                                    }else{



                                        for (int i = 0; i < dataobj.length(); i++) {
                                            jsonObj = dataobj.getJSONObject(i);
                                            String id = jsonObj.getString("capability_code");
                                            String name = jsonObj.getString("capability_name");
                                            String category= jsonObj.getString("category");
                                            String capability_imageid = jsonObj.getString("capability_imageid");

                                            String capability_image_location =jsonObj.getString("capability_image_location");
                                            spinnerItems.add(new vendorservicesItem(id, name));
                                        }



                                    }

                                }
                                else
                                {
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("errorIs", "error"+e.getMessage());
                            }
                            hideProgressBar();
                            backbuttonImage.setEnabled(true);
                            nextbuttonLayout.setEnabled(true);

                        } else {
                            hideProgressBar();
                            backbuttonImage.setEnabled(true);
                            nextbuttonLayout.setEnabled(true);
                            Log.e("Your Array Response", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                backbuttonImage.setEnabled(true);
                nextbuttonLayout.setEnabled(true);
                //continuetonextbutton.setText("Upload to your store");
                Log.e("Menu list error is ", "" + error);

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


                String creds = String.format("%s:%s",app.getApiusername(),app.getApipassword());
                Log.e("Login with API username", "" + app.getApiusername()+" , And API passwrod"+app.getApipassword());
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization",auth);
                return params;
            }


        };
        RequestQueue queue = Volley.newRequestQueue(context);
        request.setRetryPolicy(new DefaultRetryPolicy(
                80000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);


    }
}

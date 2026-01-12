package com.sabapp.saba.adapters;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sabapp.saba.R;
import com.sabapp.saba.messaging.conversationactivity;

public class conversationactivityRecycler extends RecyclerView.Adapter{
    int selected_position = 0;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private List<sabaEventItem> bitmapList;
    sabaapp app;
    private List<Boolean> selected;
    private Context context;
    private conversationactivity pulse;
    private conversationactivityRecycler adapter;
    private sabaEventItem sabaItem;


    public conversationactivityRecycler(Context context, List<sabaEventItem> bitmapList) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.pulse=pulse;
        this.adapter = this;
    }


    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {

        sabaEventItem message = (sabaEventItem) bitmapList.get(position);
        if(message.getChatusertype()==null){
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }else if (message.getChatusertype().equals("OUTGOING")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatoutgoing, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatincoming, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        sabaEventItem message = (sabaEventItem) bitmapList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }

    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView messageText, timeText, nameText,dateText,sendername;
        public ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = (ImageView) itemView.findViewById(R.id.image_gchat_profile_other);
            dateText =  (TextView) itemView.findViewById(R.id.text_gchat_date_other);
            sendername= (TextView) itemView.findViewById(R.id.text_gchat_user_other);
        }

        void bind(sabaEventItem message) {



            messageText.setText(message.getReceivedmessage());


            // Format the stored timestamp into a readable String using method.
            //timeText.setText(message.getDetails());


            nameText.setText(message.getReceivedmessagetitle());


            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
            /*if(message.getBytes()!=null)
            {
                Glide.with(context).load(message.getBytes()).into(profileImage);
            }
            else
            {
                Glide.with(context).load(R.drawable.profileresized).into(profileImage);
            }*/



            timeText.setText(message.getReceivedmessagetime());



            dateText.setText(message.getReceivedmessagedate());




        }


        @Override
        public void onClick(View v) {


            // Do your another stuff for your onClick
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText,dateText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
            dateText =  (TextView) itemView.findViewById(R.id.text_gchat_date_me);
        }

        void bind(sabaEventItem message) {

            if(message.getSentmessage()==null||message.getSentmessage().equals(""))
            {
                //timeText.setVisibility(View.GONE);
                messageText.setText("Failed to load sent message...");
            }
            else
            {
                messageText.setText(message.getSentmessage());
            }


            // Format the stored timestamp into a readable String using method.
            //timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
            if(message.getSentmessagetime()==null||message.getSentmessagetime().equals(""))
            {
                timeText.setVisibility(View.GONE);
            }
            else
            {
                timeText.setText(message.getSentmessagetime());
            }


            if(message.getReceivedmessagedate()==null || message.getReceivedmessagedate().equals(""))
            {
                dateText.setVisibility(View.GONE);
            }
            else
            {
                dateText.setText(message.getReceivedmessagedate());
            }



        }
    }



    private void getFevorites(String productname)
    {
        //continuetonextbutton.setText("Uploading to your store...");


        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());
        paramsotpu.put("item_name", productname);

        String loginuserendpoint="https://messaging.wayawaya.com/v0/janjaui/deleteproduct";


        Log.d("GET Favs API User Name", app.getApiusername() + "");
        Log.d("Get Favs API Password", app.getApipassword() + "");


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginuserendpoint, new JSONObject(paramsotpu),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response", "response "+response.toString());


                        if (response!=null) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("status");
                                String messagedetails =jsonObj.getString("message");
                                if(message.toLowerCase().matches("success"))
                                {

                                    String messageerror="Completed : "+messagedetails;
                                    Log.d("DELETE Success :",messageerror);

                                    /*Intent intent;

                                    intent =  new Intent(context, wayawayaDrawerActivity.class);
                                    context.startActivity(intent);*/
                                    //bitmapList.remove(sabaEventItem); //Actually change your list of items here
                                    //adapter.notifyDataSetChanged();



                                }
                                else
                                {



                                    String messageerror="There was an Error"+messagedetails;
                                    Log.d("Msg:",messageerror);

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("errorIs", "error"+e.getMessage());
                            }


                        } else {
                            //hideProgressBar();
                            Log.e("Your Array Response", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Log.e("error is ", "" + error);

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
        RequestQueue queue = Volley.newRequestQueue(context);
        request.setRetryPolicy(new DefaultRetryPolicy(
                80000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }
}

package com.sabapp.saba.onboarding;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.sabapp.saba.R;

public class onboardone extends AppCompatActivity {


    private static final int NUM_PAGES = 4;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    ImageView row1imageTop,row1imageBottom,row2imagetop,row2imageBottom,row3imageTop,row3imageBottom;

    TextView titleText, contentText;
    LinearLayout nextstepsbutton;

    int pagenumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboardone);

        row1imageTop = (ImageView)findViewById(R.id.row1topimage);
        row1imageBottom = (ImageView)findViewById(R.id.row1bottomimage);
        row2imagetop = (ImageView)findViewById(R.id.row2topimage);
        row2imageBottom = (ImageView)findViewById(R.id.row2bottomimage);
        row3imageTop = (ImageView)findViewById(R.id.row3topimage);
        row3imageBottom = (ImageView)findViewById(R.id.row3bottomimage);

        titleText = (TextView) findViewById(R.id.onboardtitle);
        contentText = (TextView) findViewById(R.id.onboardtitlecontent);
        nextstepsbutton = (LinearLayout) findViewById(R.id.nextbutton_layout);


        row1imageTop.setImageResource(R.drawable.tile1onboard1);
        row1imageBottom.setImageResource(R.drawable.tile2onboard1);
        row2imagetop.setImageResource(R.drawable.tile3onboard1);
        row2imageBottom.setImageResource(R.drawable.tile4onboard1);
        row3imageTop.setImageResource(R.drawable.tile5onboard1);
        row3imageBottom.setImageResource(R.drawable.tile6onboard1);

        titleText.setText(R.string.onboardtitle1);
        contentText.setText(R.string.onboardcontent1);




        nextstepsbutton.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(pagenumber < 1){

                            row1imageTop.setImageResource(R.drawable.tile1onboard1);
                            row1imageBottom.setImageResource(R.drawable.tile2onboard1);
                            row2imagetop.setImageResource(R.drawable.tile3onboard1);
                            row2imageBottom.setImageResource(R.drawable.tile4onboard1);
                            row3imageTop.setImageResource(R.drawable.tile5onboard1);
                            row3imageBottom.setImageResource(R.drawable.tile6onboard1);

                            titleText.setText(R.string.onboardtitle1);
                            contentText.setText(R.string.onboardcontent1);




                            pagenumber = 1;
                        }else if (pagenumber == 1){

                            pagenumber +=1;

                            row1imageTop.setImageResource(R.drawable.tile1onboard2);
                            row1imageBottom.setImageResource(R.drawable.tile2onboard2);
                            row2imagetop.setImageResource(R.drawable.tile3onboard2);
                            row2imageBottom.setImageResource(R.drawable.tile4onboard2);
                            row3imageTop.setImageResource(R.drawable.tile5onboard2);
                            row3imageBottom.setImageResource(R.drawable.tile6onboard2);

                            titleText.setText(R.string.onboardtitle2);
                            contentText.setText(R.string.onboardcontent2);

                        }else {


                            Intent intent =  new Intent(getApplicationContext(), login.class);
                            startActivity(intent);


                            pagenumber +=1;
                        }





                    }
                }

        );






    }





}

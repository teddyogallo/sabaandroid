package com.sabapp.saba.vendors;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.sabapp.saba.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class availabilitycalender extends AppCompatActivity {

    Set<CalendarDay> availableDays;
    Set<CalendarDay> busyDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.availabilitycalender);

        MaterialCalendarView calendarView = findViewById(R.id.calendarView);

        // ✅ Initialize sets INSIDE a method
        availableDays = new HashSet<>();
        busyDays = new HashSet<>();


        // 2️⃣ Define start and end dates
        int year = 2026;
        int month = 1; // January (1-indexed)
        int startDay = 20;
        int endDay = 27;

        // 3️⃣ Loop from start to end day and add each date
        for (int day = startDay; day <= endDay; day++) {
            availableDays.add(CalendarDay.from(year, month, day));
        }

        Map<CalendarDay, List<String>> availableTimes = new HashMap<>();

        for (CalendarDay day : availableDays) {
            List<String> times = new ArrayList<>();
            // Example: available every hour from 9 AM to 5 PM
            for (int hour = 9; hour <= 17; hour++) {
                times.add(hour + ":00");
                times.add(hour + ":30"); // half-hour slots
            }
            availableTimes.put(day, times);
        }


        busyDays.add(CalendarDay.from(2026, 1, 27));
        busyDays.add(CalendarDay.from(2026, 2, 14));

        calendarView.addDecorators(
                new AvailableDayDecorator(availableDays),
                new BusyDayDecorator(busyDays)
        );

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (availableDays.contains(date)) {
                List<String> times = availableTimes.get(date);
                // Show times in a RecyclerView, Dialog, or Spinner
                // Example: Toast
                Toast.makeText(this, "Available slots: " + times, Toast.LENGTH_LONG).show();
            } else if (busyDays.contains(date)) {
                Toast.makeText(this, "Day is fully booked", Toast.LENGTH_SHORT).show();
            }
        });

    }
}


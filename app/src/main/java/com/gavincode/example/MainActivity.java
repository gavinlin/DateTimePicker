package com.gavincode.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gavincode.datetimepicker.DateTimePicker;
import com.gavincode.datetimepicker.OnDateTimeCancelListener;
import com.gavincode.datetimepicker.OnDateTimeSetListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_main);
        DateTimePicker picker = new DateTimePicker.Builder(getSupportFragmentManager())
                .setInitialDate(new Date())
                .setSetListener(new OnDateTimeSetListener() {
                    @Override
                    public void onDateTimeSet(long seconds) {
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy  hh:mm a");
                        ZonedDateTime zonedDate = Instant.ofEpochSecond(seconds).atZone(ZoneId.systemDefault());

                        Log.i("result", zonedDate.format(format));
                    }
                })
                .setCancelListener(new OnDateTimeCancelListener() {
                    @Override
                    public void onDateTimeCancel() {

                    }
                })
                .build();
        picker.show();
    }
}

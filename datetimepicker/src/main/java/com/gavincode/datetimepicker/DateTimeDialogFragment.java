package com.gavincode.datetimepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by gavinlin on 21/6/17.
 */

public class DateTimeDialogFragment extends DialogFragment {
    public static final String TAG_DATE_TIME_DIALOG_FRAGMENT = "tagDateTimeDialogFragment";
    private static final int SECONDS_OF_DAY = 86400;

    private Button mOkButton;
    private Button mCancelButton;
    private Button mTodayButton;
    private Button mPlusOneWeekButton;
    private Button mPlusOneMonthButton;
    private Button mPlusOneYearButton;

    private Date mInitialDate;
    private Date mMinDate;
    private Date mMaxDate;

    private NumberPicker mDatePicker;
    private TimePicker mTimePicker;
    private LocalDate epoch = LocalDate.ofEpochDay(0);
    private DateTimeController mDateTimeController;

    public static DateTimeDialogFragment newInstance() {
        DateTimeDialogFragment dateTimeDialogFragment = new DateTimeDialogFragment();
        return dateTimeDialogFragment;
    }

    public void setDateTimeController(DateTimeController controller) {
        this.mDateTimeController = controller;
        mInitialDate = mDateTimeController.getInitialDate();
        mMaxDate = mDateTimeController.getMaxDate();
        mMinDate = mDateTimeController.getMinDate();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        AndroidThreeTen.init(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.date_time_picker, container);
        setup(root);

        ZonedDateTime min = Instant.ofEpochMilli(mMinDate.getTime()).atZone(ZoneId.systemDefault());

        int minDays = (int)ChronoUnit.DAYS.between(epoch, min);
        mDatePicker.setMinValue(minDays);

        ZonedDateTime max = Instant.ofEpochMilli(mMaxDate.getTime()).atZone(ZoneId.systemDefault());
        int maxDays = (int)ChronoUnit.DAYS.between(epoch, max);
        mDatePicker.setMaxValue(maxDays);

        ZonedDateTime init = Instant.ofEpochMilli(mInitialDate.getTime()).atZone(ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0);
        int initDays = (int)ChronoUnit.DAYS.between(epoch, init);
        mDatePicker.setValue(initDays);
        mDatePicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                Instant instant = Instant.ofEpochSecond((long)value * SECONDS_OF_DAY);
                LocalDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDateTime();
                return zonedDateTime.format(DateTimeFormatter.ofPattern("EEE-MM-dd"));
            }
        });

        //NumberPicker touch bug fixed
        try {
            Field f = NumberPicker.class.getDeclaredField("mInputText");
            f.setAccessible(true);
            EditText inputText = (EditText) f.get(mDatePicker);
            inputText.setFilters(new InputFilter[0]);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mPlusOneWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePicker.setValue(mDatePicker.getValue() + 7);
            }
        });
        mPlusOneMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int days = mDatePicker.getValue();
                LocalDate date = Instant.ofEpochSecond((long)days * SECONDS_OF_DAY).atZone(ZoneOffset.UTC).toLocalDate();
                mDatePicker.setValue((int)ChronoUnit.DAYS.between(epoch, date.plusMonths(1)));
            }
        });

        mPlusOneYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int days = mDatePicker.getValue();
                LocalDate date = Instant.ofEpochSecond((long)days * SECONDS_OF_DAY).atZone(ZoneId.systemDefault()).toLocalDate();
                mDatePicker.setValue((int)ChronoUnit.DAYS.between(epoch, date.plusYears(1)));
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateTimeController.onDateTimeCancel(DateTimeDialogFragment.this);
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long secondOfDate = (long)mDatePicker.getValue() * SECONDS_OF_DAY;
                ZonedDateTime zonedDate = Instant.ofEpochSecond(secondOfDate).atZone(ZoneId.systemDefault());
                mDateTimeController.onDateTimeSet(zonedDate.withHour(mTimePicker.getCurrentHour()).withMinute(mTimePicker.getCurrentMinute()).toEpochSecond(),
                        DateTimeDialogFragment.this);
            }
        });

        mTodayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int today = (int)ChronoUnit.DAYS.between(epoch, Instant.now().atZone(ZoneId.systemDefault()));
                mDatePicker.setValue(today);
            }
        });

        return root;
    }

    private void setup(View v) {
        mOkButton = (Button) v.findViewById(R.id.button_plus_ok);
        mCancelButton = (Button) v.findViewById(R.id.button_clear);
        mTodayButton = (Button) v.findViewById(R.id.button_today);
        mPlusOneWeekButton = (Button) v.findViewById(R.id.button_plus_one_week);
        mPlusOneMonthButton = (Button) v.findViewById(R.id.button_plus_one_month);
        mPlusOneYearButton = (Button) v.findViewById(R.id.button_plus_one_year);
        mDatePicker = (NumberPicker) v.findViewById(R.id.picker_date);
        mTimePicker = (TimePicker) v.findViewById(R.id.picker_time);
        mDatePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mTimePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public void show() {
        mDateTimeController.show();
    }
}

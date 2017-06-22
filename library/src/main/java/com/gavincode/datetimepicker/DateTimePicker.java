package com.gavincode.datetimepicker;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by gavinlin on 21/6/17.
 */

public class DateTimePicker {
    private Date mInitialDate;
    private Date mMinDate;
    private Date mMaxDate;
    private FragmentManager mFragmentManager;
    private DateTimePickerListener mListener;

    private DateTimePicker(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DateTimeDialogFragment.TAG_DATE_TIME_DIALOG_FRAGMENT) ;
        if (prev != null) {
            ft.remove(prev);
            ft.commit();
        }
        mFragmentManager = fm;
    }

    public void setListener(DateTimePickerListener listener) {
        this.mListener = listener;
    }

    public void setInitialDate(Date initialDate) {
        this.mInitialDate  = initialDate;
    }

    public void setMinDate(Date minDate) {
        this.mMinDate = minDate;
    }

    public void setMaxDate(Date maxDate) {
        this.mMaxDate = maxDate;
    }

    public void show() {
        if (mListener == null)
            throw new NullPointerException("Listener can not be null");

        if (mInitialDate == null) {
            setInitialDate(new Date());
        }

        if (mMinDate == null) {
            Date epoch = new Date();
            epoch.setTime(0);
            setMinDate(epoch);
        }

        if (mMaxDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2100, 11, 31);
            setMaxDate(calendar.getTime());
        }


        DateTimeDialogFragment dateTimeDialogFragment =
                DateTimeDialogFragment.newInstance(
                        mListener,
                        mInitialDate,
                        mMinDate,
                        mMaxDate
                );

        dateTimeDialogFragment.show(mFragmentManager,
                DateTimeDialogFragment.TAG_DATE_TIME_DIALOG_FRAGMENT);
    }

    public static class Builder {
        private FragmentManager fm;
        private DateTimePickerListener listener;

        private Date initialDate;
        private Date minDate;
        private Date maxDate;

        public Builder(FragmentManager fm) {
            this.fm = fm;
        }

        public Builder setListener(DateTimePickerListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setInitialDate(Date initialDate) {
            this.initialDate = initialDate;
            return this;
        }

        public Builder setMinDate(Date minDate) {
            this.minDate = minDate;
            return this;
        }

        public Builder setMaxDate(Date maxDate) {
            this.maxDate = maxDate;
            return this;
        }

        public DateTimePicker build() {
            DateTimePicker picker = new DateTimePicker(fm);
            picker.setListener(listener);
            picker.setInitialDate(initialDate);
            picker.setMinDate(minDate);
            picker.setMinDate(maxDate);
            return picker;
        }
    }
}

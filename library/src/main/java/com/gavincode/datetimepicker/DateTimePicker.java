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
    private OnDateTimeSetListener mSetListener;
    private OnDateTimeCancelListener mCancelListener;

    private DateTimePicker(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DateTimeDialogFragment.TAG_DATE_TIME_DIALOG_FRAGMENT) ;
        if (prev != null) {
            ft.remove(prev);
            ft.commit();
        }
        mFragmentManager = fm;
    }

    public void setSetListener(OnDateTimeSetListener listener) {
        this.mSetListener = listener;
    }

    public void setCancelListener(OnDateTimeCancelListener listener) {
        this.mCancelListener = listener;
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
        if (mSetListener == null)
            throw new NullPointerException("SetListener can not be null");
        if (mCancelListener == null)
            throw new NullPointerException("CancelListener can not be null");

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
                        mSetListener,
                        mCancelListener,
                        mInitialDate,
                        mMinDate,
                        mMaxDate
                );

        dateTimeDialogFragment.show(mFragmentManager,
                DateTimeDialogFragment.TAG_DATE_TIME_DIALOG_FRAGMENT);
    }

    public static class Builder {
        private FragmentManager fm;
        private OnDateTimeSetListener setListener;
        private OnDateTimeCancelListener cancelListener;

        private Date initialDate;
        private Date minDate;
        private Date maxDate;

        public Builder(FragmentManager fm) {
            this.fm = fm;
        }

        public Builder setSetListener(OnDateTimeSetListener listener) {
            this.setListener = listener;
            return this;
        }

        public Builder setCancelListener(OnDateTimeCancelListener listener) {
            this.cancelListener = listener;
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
            picker.setSetListener(setListener);
            picker.setCancelListener(cancelListener);
            picker.setInitialDate(initialDate);
            picker.setMinDate(minDate);
            picker.setMinDate(maxDate);
            return picker;
        }
    }
}

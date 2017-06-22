package com.gavincode.datetimepicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Date;

/**
 * Created by gavinlin on 22/6/17.
 */

public class DateTimeController {

    private Date mInitialDate;
    private Date mMinDate;
    private Date mMaxDate;
    private FragmentManager mFragmentManager;
    private OnDateTimeSetListener mSetListener;
    private OnDateTimeCancelListener mCancelListener;
    private DateTimeDialogFragment mDateTimeDialogFragment;

    public DateTimeController(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DateTimeDialogFragment.TAG_DATE_TIME_DIALOG_FRAGMENT) ;
        if (prev != null) {
            ft.remove(prev);
            ft.commit();
        }
        mFragmentManager = fm;
    }

    public void setDateTimeDialogFragment(DateTimeDialogFragment dateTimeDialogFragment) {
        mDateTimeDialogFragment = dateTimeDialogFragment;
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
        mDateTimeDialogFragment.show(mFragmentManager,
                DateTimeDialogFragment.TAG_DATE_TIME_DIALOG_FRAGMENT);
    }

    public void onDateTimeCancel(DateTimeDialogFragment fragment) {
        mCancelListener.onDateTimeCancel(fragment);
    }

    public void onDateTimeSet(long seconds, DateTimeDialogFragment fragment) {
        mSetListener.onDateTimeSet(seconds, fragment);
    }

    public Date getInitialDate() {
        return mInitialDate;
    }

    public Date getMinDate() {
        return mMinDate;
    }

    public Date getMaxDate() {
        return mMaxDate;
    }
}

package com.gavincode.datetimepicker;


import android.support.v4.app.FragmentManager;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by gavinlin on 21/6/17.
 */

public class DateTimePicker {

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

        public DateTimeDialogFragment build() {
            if (setListener == null)
                throw new NullPointerException("SetListener can not be null");
            if (cancelListener == null)
                throw new NullPointerException("CancelListener can not be null");
            DateTimeDialogFragment dateTimeDialogFragment = new DateTimeDialogFragment();
            DateTimeController controller = new DateTimeController(fm);
            controller.setDateTimeDialogFragment(dateTimeDialogFragment);
            controller.setSetListener(setListener);
            controller.setCancelListener(cancelListener);
            controller.setInitialDate(initialDate);
            if (minDate == null) {
                Date epoch = new Date();
                epoch.setTime(0);
                setMinDate(epoch);
            }
            controller.setMinDate(minDate);

            if (maxDate == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2100, 11, 31);
                setMaxDate(calendar.getTime());
            }
            controller.setMaxDate(maxDate);
            dateTimeDialogFragment.setDateTimeController(controller);
            return dateTimeDialogFragment;
        }
    }
}

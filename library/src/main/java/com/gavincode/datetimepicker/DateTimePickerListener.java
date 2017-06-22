package com.gavincode.datetimepicker;

/**
 * Created by gavinlin on 21/6/17.
 */

public interface DateTimePickerListener {
    void onDateTimeSet(long seconds);
    void onDateTimeCancel();
}

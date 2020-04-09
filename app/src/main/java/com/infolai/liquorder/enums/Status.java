package com.infolai.liquorder.enums;

import com.infolai.liquorder.R;

public enum Status {
    SAVE("SAVE"), SUBMIT("SUBMIT"), ACCEPT("ACCEPT"), REJECT("REJECT");

    private String value;

    private Status(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }

    public int getStyleResource() {
        int result = R.drawable.status_save;

        if (value.equals("SUBMIT")) {
            result = R.drawable.status_submit;
        } else if (value.equals("ACCEPT")) {
            result = R.drawable.status_accept;
        } else if (value.equals("REJECT")) {
            result = R.drawable.status_reject;
        }

        return result;
    }
}

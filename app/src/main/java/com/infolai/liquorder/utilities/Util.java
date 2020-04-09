package com.infolai.liquorder.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.infolai.liquorder.R;
import com.infolai.liquorder.enums.Status;
import com.infolai.liquorder.repositories.CartRepository;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public final class Util {
    public static final String UTC = "UTC";

    public static void blockUserInteraction(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void unblockUserInteraction(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void clearCartConfirm(Activity activity) {
        CartRepository repository = CartRepository.getInstance();
        if (!repository.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            DialogInterface.OnClickListener positiveOnClickListener = (dialog, which) -> {
                repository.clearCart();
                activity.onBackPressed();
            };
            DialogInterface.OnClickListener negativeOnClickListener = (dialog, which) -> dialog.dismiss();

            builder.setTitle(activity.getString(R.string.cart_is_not_empty));
            builder.setPositiveButton(R.string.commit, positiveOnClickListener);
            builder.setNegativeButton(R.string.cancel, negativeOnClickListener);
            builder.create().show();
        } else {
            activity.onBackPressed();
        }
    }

    public static void setStatusTextView(Context context, TextView textView, String strStatus) {
        Status status = Status.valueOf(strStatus);

        textView.setBackgroundResource(status.getStyleResource());
        textView.setTextColor(context.getColor(R.color.white));
//        if (status == Status.ACCEPT || status == Status.REJECT) {
//            textView.setTextColor(context.getColor(R.color.white));
//        } else if (status == Status.SUBMIT) {
//            textView.setTextColor(context.getColor(R.color.green));
//        } else {
//            textView.setTextColor(context.getColor(R.color.gray));
//        }
    }

    public static void IntList2IntArray(List<Integer> intList, int[] intArray) {
        if (intList.size() == intArray.length) {
            for (int i = 0; i < intList.size(); i++) {
                intArray[i] = intList.get(i);
            }
        }
    }

    public static String toUtcDate(String strLocal) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
        String result = null;

        try {
            Date date = dateFormat.parse(strLocal);

            result = dateFormat.format(date);
        } catch (Exception ex) {
            Log.e(Util.class.toString(), ex.getMessage());
        }

        return result;
    }

    public static int getCategoryPositionByItemPosition(int position, int[] categoryIndices) {
        int result = 0;

        for (int i = categoryIndices.length - 1; i >= 0; i--) {
            if (position >= categoryIndices[i]) {
                result = i;
                break;
            }
        }

        return result;
    }
}

package com.example.demolauncher.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;

    public static void showToast(Context context){
        if (toast == null) toast = new Toast(context);

        toast.setText("Leak");
        toast.show();
    }
}

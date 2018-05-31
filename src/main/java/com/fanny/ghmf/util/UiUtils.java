package com.fanny.ghmf.util;

import android.widget.Toast;

import com.fanny.ghmf.MyApp;

/**
 * Created by Fanny on 17/12/13.
 */

public class UiUtils {
    public static void showToast(String msg){
        Toast toast=Toast.makeText(MyApp.getContext(),msg,Toast.LENGTH_SHORT);
        toast.show();
    }
}

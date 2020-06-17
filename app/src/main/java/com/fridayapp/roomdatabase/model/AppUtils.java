package com.fridayapp.roomdatabase.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;

import com.fridayapp.roomdatabase.R;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class AppUtils {
    private Context mContext;
    public static void showMessage(RecyclerView recyclerView, String message) {
        Snackbar snackbar = Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT);
        TextView tv = snackbar.getView().findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void showMessageLinearLayout(LinearLayout linearLayout, String message) {
        Snackbar snackbar = Snackbar.make(linearLayout, message, Snackbar.LENGTH_SHORT);
        TextView tv = snackbar.getView().findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void openKeyboard(final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }, 500);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void backup(String outFileName) {

        //database path
        final String inFileName = mContext.getDatabasePath(Constants.PATH_TODOS).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, "Backup Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(String inFileName) {

        final String outFileName = mContext.getDatabasePath(Constants.PATH_TODOS).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, "Import Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to import database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}

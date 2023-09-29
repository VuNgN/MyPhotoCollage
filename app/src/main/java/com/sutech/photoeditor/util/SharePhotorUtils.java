package com.sutech.photoeditor.util;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import com.sutech.photoeditor.R;

import java.lang.reflect.Method;
import java.util.List;


public class SharePhotorUtils {
    private static SharePhotorUtils shareUtils;
    private final String BASE_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=";

    public SharePhotorUtils() {
    }

    public static SharePhotorUtils getInstance() {
        if (shareUtils == null) {
            shareUtils = new SharePhotorUtils();
        }
        return shareUtils;
    }

    public void sendShareMore(Context context, String filePath) {
        disableExposure();
        Intent share = new Intent(Intent.ACTION_SEND);
        // share.setName("text_new/plain");
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_TEXT, getTextShare(context));
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + filePath));
        context.startActivity(Intent.createChooser(share, context.getString(R.string.share)));
    }

    public void sendShareMore(Context context, Uri filePath) {
        disableExposure();
        Intent share = new Intent(Intent.ACTION_SEND);
        // share.setName("text_new/plain");
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_TEXT, getTextShare(context));
        share.putExtra(Intent.EXTRA_STREAM, filePath);
        context.startActivity(Intent.createChooser(share, context.getString(R.string.share)));
    }

    public void sendSharePackage(Context context, String filePath, String packages) {
        disableExposure();
        Intent share = new Intent(Intent.ACTION_SEND);
        // share.setName("text_new/plain");
        share.setType("image/*");
        share.setPackage(packages);
        share.putExtra(Intent.EXTRA_TEXT, getTextShare(context));
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + filePath));

        try {
            context.startActivity(Intent.createChooser(share, context.getString(R.string.share)));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "Please Install app ", Toast.LENGTH_LONG).show();
        }

    }

    public void shareVideoMore(Context context, String filePath) {
        disableExposure();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("video/mp4");
        share.putExtra(Intent.EXTRA_TEXT, getTextShare(context));
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + filePath));
        context.startActivity(Intent.createChooser(share, "share"));
    }

    public void shareVideoApplication(Context context, String packageName, String file) {
        disableExposure();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/mp4");
        intent.putExtra(Intent.EXTRA_TEXT, getTextShare(context));
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + file));
        boolean isAppFound = false;
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(packageName)) {
                intent.setPackage(info.activityInfo.packageName);
                isAppFound = true;
                break;
            }
        }
        if (isAppFound) {
            context.startActivity(intent);
        } else {
        }
    }

    public void sendShareApplication(Context context, String packageName, String file) {
        disableExposure();
        Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setName("text_new/plain");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, getTextShare(context));
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + file));
        // intent.putExtra(Intent.EXTRA_STREAM, getUriForFile(file));
        boolean isAppFound = false;
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(packageName)) {
                intent.setPackage(info.activityInfo.packageName);
                isAppFound = true;
                break;
            }
        }
        if (isAppFound) {
            context.startActivity(intent);
        }
    }

    @SuppressLint("StringFormatInvalid")
    private String getTextShare(Context context) {
        String strName = context.getString(R.string.app_name);
        String shareLink = BASE_GOOGLE_PLAY + context.getPackageName();
        return context.getString(R.string.share, strName, shareLink);
    }

    public static void disableExposure() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

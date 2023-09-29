package com.sutech.photoeditor.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.sutech.photoeditor.util.AppConstant.FOLDER_PHOTO_LAYOUT;


public class ViewToBitmap implements Parcelable {

    private static final String TAG = ViewToBitmap.class.getSimpleName();
    private static final String EXTENSION_PNG = ".png";
    private static final String EXTENSION_JPG = ".jpg";
    private static final int JPG_MAX_QUALITY = 100;
    private String fileName, fileExtension;
    private OnSaveResultListener onSaveResultListener;
    private int jpgQuality;
    private Handler handler;
    private View view;

    private ViewToBitmap(@NonNull View view) {
        this.view = view;
    }


    protected ViewToBitmap(Parcel in) {
        fileName = in.readString();
        fileExtension = in.readString();
        jpgQuality = in.readInt();

    }

    public static final Creator<ViewToBitmap> CREATOR = new Creator<ViewToBitmap>() {
        @Override
        public ViewToBitmap createFromParcel(Parcel in) {
            return new ViewToBitmap(in);
        }

        @Override
        public ViewToBitmap[] newArray(int size) {
            return new ViewToBitmap[size];
        }
    };

    public static ViewToBitmap of(@NonNull View view) {
        return new ViewToBitmap(view);
    }

    public ViewToBitmap toJPG() {
        jpgQuality = JPG_MAX_QUALITY;
        setFileExtension(EXTENSION_JPG);
        return this;
    }

    public ViewToBitmap toJPG(int jpgQuality) {
        this.jpgQuality = (jpgQuality == 0) ? JPG_MAX_QUALITY : jpgQuality;
        setFileExtension(EXTENSION_JPG);
        return this;
    }

    public ViewToBitmap toPNG() {
        setFileExtension(EXTENSION_PNG);
        return this;
    }

    public ViewToBitmap setOnSaveResultListener(OnSaveResultListener onSaveResultListener) {
        this.onSaveResultListener = onSaveResultListener;
        this.handler = new Handler(Looper.myLooper());
        return this;
    }


    public void save(Context context) {
        Bitmap bm = getBitmap();
        if (bm == null) return;
        AsyncSaveImage asyncSaveBitmap = new AsyncSaveImage(context, bm);
        asyncSaveBitmap.execute();
    }

    private Context getAppContext() {
        if (view == null) {
            throw new NullPointerException("Null cannot passed to ViewToBitmap.of()");
        } else {
            return view.getContext().getApplicationContext();
        }
    }

    private void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }


    private String getFileExtension() {
        return fileExtension;
    }


    private String getFilename() {
        if (fileName == null || fileName.isEmpty()) {
            return String.valueOf("picture_" + System.currentTimeMillis() + getFileExtension());
        } else {
            return fileName + getFileExtension();
        }
    }
    private String getFilename2() {
        if (fileName == null || fileName.isEmpty()) {
            return String.valueOf("picture_" + System.currentTimeMillis());
        } else {
            return fileName;
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = null;
        try {
            if(view!=null) {
                bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                canvas.scale(1f,1f);
                view.draw(canvas);
                canvas.setBitmap(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public Bitmap getBitmapPreview() {
        Bitmap bitmap = null;
        try {
            if(view!=null) {
                bitmap = Bitmap.createBitmap(view.getWidth()/2, view.getHeight()/2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.scale(0.5f,0.5f);
                view.draw(canvas);
                canvas.setBitmap(null);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private Bitmap getBitmapByCache() {
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // copy this bitmap otherwise distroying the cache will destroy
        // the bitmap for the referencing drawable and you'll not
        // get the captured view
        Bitmap b = b1.copy(Bitmap.Config.ARGB_8888, false);
        view.destroyDrawingCache();
        return b;
    }

    private void notifyListener(final boolean isSaved, final String path, final Uri uri) {
        if (onSaveResultListener != null) {
            handler.post(() -> onSaveResultListener.onSaveResult(isSaved, path,uri));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fileName);
        parcel.writeString(fileExtension);
        parcel.writeInt(jpgQuality);
    }

    public interface OnSaveResultListener {
        void onSaveResult(boolean isSaved, String path,Uri uri);
    }

    private class AsyncSaveImage extends AsyncTask<Void, Void, Boolean> implements MediaScannerConnection.OnScanCompletedListener {
        private Context context;
        private Bitmap bitmap;
        private String path = "";
        private Uri uri;

        Boolean  saveImage(Bitmap bitmap, String name) {
            boolean saved = false;
            OutputStream fos;

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + FOLDER_PHOTO_LAYOUT);
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    path = imageUri.getPath();
                    uri = imageUri;
                    fos =  resolver.openOutputStream(imageUri);
                } else {
                    String imagesDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DCIM).toString() + File.separator + FOLDER_PHOTO_LAYOUT;
                    File file = new File(imagesDir);
                    if (!file.exists()) {
                        file.mkdir();
                    }

                    File image = new File(imagesDir, name + ".png");
                    path = image.getPath();
                    fos = new FileOutputStream(image);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(image)));
                    uri = Uri.fromFile(image);

                }
                saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                fos.flush();
                fos.close();
            } catch (IOException e) {
                saved = false;
                e.printStackTrace();
            }
            return saved;
        }
        private AsyncSaveImage(Context context, Bitmap bitmap) {
            this.context = context;
            this.bitmap = bitmap;
        }

        protected void onPreExecute() {
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(Void... params) {
//            String root = Environment.getExternalStorageDirectory().toString();
//            File myDir = new File(AppConstant.FOLDER_TEXT_TO_PHOTO);
//            if (!myDir.exists()) {
//                myDir.mkdirs();
//            }
//            File imageFile = new File(AppConstant.FOLDER_TEXT_TO_PHOTO+ getFilename());
//            if (fileExtension == null) {
//                throw new IllegalStateException("A file format must be chosen to ViewToBitmap before calling save()");
//            } else {
//                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFile))) {
//                    switch (fileExtension) {
//                        case EXTENSION_JPG:
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, jpgQuality, out);
//                            break;
//                        case EXTENSION_PNG:
//                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                            break;
//                        default:
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, jpgQuality, out);
//                            break;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    bitmap = null;
//                    notifyListener(false, null);
//                    return false;
//                }
//            }
//
//            bitmap = null;
//            MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, this);
            return saveImage(bitmap,getFilename2());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            notifyListener(success, path,uri);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            if (uri != null && path != null) {
                notifyListener(true, path,uri);
            } else {
                notifyListener(false, null,null);
            }
        }
    }

}



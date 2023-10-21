package io.github.halleypay.presentation.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class FileInStorageManager {

    private final Context context;
    private String directoryName = "HallyPay";
    private String subDirectory = "data";
    private String fileName = null;
    private boolean external;

    public FileInStorageManager(Context context) {
        this.context = context;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public FileInStorageManager setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
        return this;
    }

    public FileInStorageManager setSubDirectory(String subDirectory) {
        this.subDirectory = subDirectory;
        return this;
    }

    public FileInStorageManager setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public FileInStorageManager setExternal(boolean external) {
        this.external = external;
        return this;
    }

    public void save(Bitmap bitmapImage) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + directoryName + "/" + subDirectory + "/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = null;

        if (fileName == null) {
            Random generator = new Random();
            int randomNumber = 10000;
            randomNumber = generator.nextInt(randomNumber);
            String fileName = "ima-" + randomNumber + ".jpg";
            file = new File(myDir, fileName);
        } else {
            file = new File(myDir, fileName);
        }

        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public File createFile() {
        File directory;
        if (external) {
            directory = getAlbumStorageDir(directoryName);
//            directory = Environment.getExternalStorageDirectory().getPath() + File.separator + "Ima";
        } else {
            directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        }
        if (!directory.exists() && !directory.mkdirs()) {
            Log.e("ImageSaver", "Error creating directory " + directory);
        }

        return new File(directory, fileName);
    }

    private File getAlbumStorageDir(String albumName) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
    }

    public Bitmap load() {
        FileInputStream inputStream = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/" + directoryName + "/" + subDirectory + "/" + fileName);
            inputStream = new FileInputStream(myDir);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

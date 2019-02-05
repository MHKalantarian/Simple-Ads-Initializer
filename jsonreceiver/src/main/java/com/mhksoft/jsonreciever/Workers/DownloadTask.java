package com.mhksoft.jsonreciever.Workers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static androidx.core.content.FileProvider.getUriForFile;

@SuppressLint("StaticFieldLeak")
public class DownloadTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private String packageName;
    private int retryTimes = 1;

    public DownloadTask(Context mContext, String packageName, int retryTimes) {
        this.mContext = mContext;
        this.packageName = packageName;
        this.retryTimes = retryTimes;
    }

    public DownloadTask(Context mContext, String packageName) {
        this.mContext = mContext;
        this.packageName = packageName;
    }

    @Override
    protected void onPreExecute() {
        ((PowerManager) mContext.getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName()).acquire();
    }

    @Override
    protected String doInBackground(String... urls) {
        if (appIsInstalled(packageName))
            return null;
        else {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                int fileLength = connection.getContentLength();

                File file = new File(getDownloadPath() + getFileNameByURL(urls[0]));
                if (file.exists() && file.length() == fileLength) {
                    return getDownloadPath() + getFileNameByURL(urls[0]);
                }

                input = connection.getInputStream();
                output = new FileOutputStream(getDownloadPath() + getFileNameByURL(urls[0]));

                byte data[] = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    output.write(data, 0, count);
                }

                return getDownloadPath() + getFileNameByURL(urls[0]);
            } catch (Exception e) {
                return null;
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String filepath) {
        if (filepath != null) {
            for (int i = 0; i < retryTimes; i++)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        installApk(filepath);
                    }
                }, TimeUnit.SECONDS.toMillis(i * 5));
        }
    }

    private String getFileNameByURL(String url) {
        String[] temp = url.split("/");
        return temp[temp.length - 1];
    }

    private String getDownloadPath() {
        String path = Environment.getExternalStorageDirectory().toString() + "/.apps/";
        File myDir = new File(path);
        myDir.mkdirs();
        return path;
    }

    private boolean appIsInstalled(String appPackageName) {
        List<ApplicationInfo> packages;
        PackageManager packageManager;
        packageManager = mContext.getPackageManager();
        packages = packageManager.getInstalledApplications(0);

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(appPackageName)) {
                return true;
            }
        }
        return false;
    }


    private void installApk(String url) {
        File toInstall;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            toInstall = new File(new File(Environment.getExternalStorageDirectory(), ".apps"), getFileNameByURL(url));
            Uri apkUri = getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", toInstall);

            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            toInstall = new File(url);
            Uri apkUri = Uri.fromFile(toInstall);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
}

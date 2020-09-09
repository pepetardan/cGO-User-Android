package id.dtech.cgo.Util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import id.dtech.cgo.BuildConfig;
import id.dtech.cgo.R;


public class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder build;
    private File fileurl;
    int id = 123;
    OutputStream output;
    private Context context;
    private String orderID;

    public DownloadFileFromURL(Context context, String orderID) {
        this.context = context;
        this.orderID = orderID;
    }

    protected void onPreExecute() {
        super.onPreExecute();

        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        build = new NotificationCompat.Builder(context);
        build.setContentTitle("Download")
                .setContentText("Download in progress")
                .setChannelId(id + "")
                .setAutoCancel(false)
                .setDefaults(0)
                .setSmallIcon(R.drawable.cgo_icon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id + "",
                    "cgo",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("no sound");
            channel.setSound(null, null);
            channel.enableLights(false);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(false);
            mNotifyManager.createNotificationChannel(channel);

        }

        build.setProgress(100, 0, false);
        mNotifyManager.notify(id, build.build());
    }

    @Override
    protected String doInBackground(String... f_url) {
        int count;

        try {
            URL url = new URL("https://cgostorage.blob.core.windows.net/cgo-storage/Ticket-Experience/4550773287376647431.pdf");
            URLConnection conection = url.openConnection();
            conection.connect();

            int lenghtOfFile = conection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            output = new FileOutputStream(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
                    + "/cGO/" + orderID + ".pdf");

            fileurl = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
                    + "/cGO/" + orderID + ".pdf");

            byte[] data = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                int cur = (int) ((total * 100) / lenghtOfFile);

                publishProgress(Math.min(cur, 100));

                if (Math.min(cur, 100) > 98) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Log.d("Failure", "sleeping failure");
                    }
                }

                Log.i("currentProgress", "currentProgress: " + Math.min(cur, 100) + "\n " + cur);

                output.write(data, 0, count);
            }

            output.flush();

            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        build.setProgress(100, progress[0], false);
        mNotifyManager.notify(id, build.build());
        super.onProgressUpdate(progress);
    }

    @Override
    protected void onPostExecute(String file_url) {
        build.setContentText("Download complete");

        mNotifyManager.notify(id, build.build());
        Intent intent = new Intent(context, DownloadBroadcastReceiver.class);

        Uri finalurl = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", fileurl);
        intent.putExtra("currenturl", finalurl.toString());
        intent.putExtra("selectedfilename", orderID);

        context.sendBroadcast(intent);

        build.setProgress(0, 0, false);
    }
}
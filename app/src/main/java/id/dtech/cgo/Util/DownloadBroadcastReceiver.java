package id.dtech.cgo.Util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;

import id.dtech.cgo.BuildConfig;
import id.dtech.cgo.R;

public class DownloadBroadcastReceiver extends BroadcastReceiver {
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder build;
    private int rId= 123;
    private String localuri="";
    private String selectedfilename="";

    @Override
    public void onReceive(Context context, Intent intent) {
        localuri=intent.getStringExtra("currenturl");
        selectedfilename=intent.getStringExtra("selectedfilename");
        startNotification(context,intent);
    }

    private void startNotification(Context context, Intent intent) {
        Log.e("fat", "startNotification: "+localuri );

        File fileurl = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
                + "/cGO/" + selectedfilename + ".pdf");
        Uri finalurl = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", fileurl);

        Intent downloadintent = new Intent(Intent.ACTION_VIEW);
        downloadintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        downloadintent.setDataAndType(finalurl, "application/pdf");

        grantAllUriPermissions(context, downloadintent, finalurl);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, downloadintent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        build = new NotificationCompat.Builder(context);
        build.setContentTitle("E-Ticket.pdf")
                .setContentText("Download completed")
                .setChannelId(rId+"")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setSmallIcon(R.drawable.cgo_icon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(rId+"" ,
                    "Call Reminder",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("With sound");
            channel.setSound(null,null);
            channel.enableLights(false);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            mNotifyManager.createNotificationChannel(channel);

        }

        mNotifyManager.notify(rId, build.build());
    }

    private void grantAllUriPermissions(Context context, Intent downloadintent, Uri finalurl) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(downloadintent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, finalurl, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }
}

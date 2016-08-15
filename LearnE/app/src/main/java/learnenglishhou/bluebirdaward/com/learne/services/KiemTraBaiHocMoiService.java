package learnenglishhou.bluebirdaward.com.learne.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import learnenglishhou.bluebirdaward.com.learne.R;
import learnenglishhou.bluebirdaward.com.learne.activities.DanhSachCauHoiActivity;
import learnenglishhou.bluebirdaward.com.learne.listclass.BaiHoc;

public class KiemTraBaiHocMoiService extends Service {

    private Firebase firebase;
    private NotificationCompat.Builder notificationBuilder;
    private int reqestCode=10101;
    private int notificationId=1019;

    private final String url="https://learne-51b47.firebaseio.com/TestDB/BaiHoc/";

    public KiemTraBaiHocMoiService() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(getApplicationContext());
        firebase=new Firebase("https://learne-51b47.firebaseio.com/");
        notificationBuilder=new NotificationCompat.Builder(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firebase.child("TestDB").child("BaiHoc").limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot==null)
                    return;
                Log.d("Bài mới datasnap", dataSnapshot.getKey().toString()+" ... "+dataSnapshot.getValue().toString());
                // Log.d("Bài mới 2", dataSnapshot.toString());


                BaiHoc baiMoi=new BaiHoc(dataSnapshot.getKey().toString());
                baiMoi.setGioiThieu(dataSnapshot.child("gioiThieu").getValue().toString());
                baiMoi.setTenBaihoc(dataSnapshot.child("tenBaiHoc").getValue().toString());
                Log.d(baiMoi.getTenBaihoc(), baiMoi.getGioiThieu());

                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
                notificationBuilder.setTicker("Learn E có bài mới....");

                notificationBuilder.setContentTitle(baiMoi.getTenBaihoc());
                notificationBuilder.setContentInfo(baiMoi.getGioiThieu());

                Intent intent=new Intent(getApplicationContext(), DanhSachCauHoiActivity.class);
                intent.putExtra("maBaiHoc", dataSnapshot.getKey().toString());

                PendingIntent pendingIntent=
                        PendingIntent.getActivity(getApplicationContext(), reqestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.setContentIntent(pendingIntent);

                NotificationManager notificationManager=
                        (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification= notificationBuilder.build();
                notificationManager.notify(notificationId, notification);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

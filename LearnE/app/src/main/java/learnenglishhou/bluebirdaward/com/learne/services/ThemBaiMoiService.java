package learnenglishhou.bluebirdaward.com.learne.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.Serializable;

import learnenglishhou.bluebirdaward.com.learne.listclass.BaiHoc;

import static android.R.attr.action;

public class ThemBaiMoiService extends IntentService {

    public static final String actionDownloadNewLesson="actionDownloadNewLesson";

    private Firebase firebase;



    public ThemBaiMoiService() {
        super("ThemBaiHocMoiService");
        firebase=new Firebase("https://learne-51b47.firebaseio.com/");
    }

    public static void startActionDownloadNewLesson(Context context) {
        Intent intent=new Intent(context, ThemBaiMoiService.class);
        intent.setAction(actionDownloadNewLesson);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if(actionDownloadNewLesson.equals(action)) {
                handleACtionDownloadNewLesson();
            }
        }
    }

    private void handleACtionDownloadNewLesson() {
        firebase.child("TestDB").child("BaiHoc").limitToLast(1).addChildEventListener(new ChildEventListener() {
            Intent broadcastIntent =new Intent();

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                broadcastIntent.setAction(actionDownloadNewLesson);
                BaiHoc baiHoc=new BaiHoc(dataSnapshot.getKey().toString());
                baiHoc.setTenBaihoc(dataSnapshot.child("tenBaiHoc").getValue().toString());
                baiHoc.setGioiThieu(dataSnapshot.child("gioiThieu").getValue().toString());

                broadcastIntent.putExtra("newLesson", (Serializable) baiHoc);
                sendBroadcast(broadcastIntent);
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
    }

    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}

package learnenglishhou.bluebirdaward.com.learne.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import learnenglishhou.bluebirdaward.com.learne.R;
import learnenglishhou.bluebirdaward.com.learne.adapter.DanhSachDiemAdapter;
import learnenglishhou.bluebirdaward.com.learne.listclass.UserFacebook;
import learnenglishhou.bluebirdaward.com.learne.listener.JsonParserTaskListener;
import learnenglishhou.bluebirdaward.com.learne.utils.DownloadTextTask;

public class DanhSachDiemActivity extends AppCompatActivity implements JsonParserTaskListener {

    public class ComparatorByScore implements Comparator<UserFacebook> {

        @Override
        public int compare(UserFacebook u1, UserFacebook u2) {
            if(u1.getScore()< u2.getScore())
                return 1;
            else if(u1.getScore()== u2.getScore())
                return 0;
            else
                return -1;
        }
    }

    private ListView lvDanhSachDiem;

    private ArrayList<UserFacebook> danhSachDiem;

    private final String url="https://learne-51b47.firebaseio.com/User.json";
    private final String urlFirebase ="https://learne-51b47.firebaseio.com/";
    private Firebase firebaseUser;

    private DanhSachDiemAdapter danhSachDiemAdapter;

    private int soDiemThemVao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_danh_sach_diem);

        if(!DanhSachBaiHocActivity.isLoggedIn())
            finish();
        soDiemThemVao= getIntent().getIntExtra("DiemThemVao", 0);
        firebaseUser= new Firebase(urlFirebase);
        downloadText();
     }


    @Override
    public void onDownloadTextSuccessed(String jsonText) {
        boolean daCoNguoiChoiHienTai=false;
        if(jsonText!=null) {
            try {
                JSONObject root = new JSONObject(jsonText);
                Iterator<String> keys= root.keys();
                while (keys.hasNext()) {
                    UserFacebook userFacebook=new UserFacebook();
                    userFacebook.setUserId(keys.next());

                    JSONObject infoUser= root.getJSONObject(userFacebook.getUserId());
                    userFacebook.setName(infoUser.getString("name"));
                    userFacebook.setScore(infoUser.getInt("score"));

                    if(userFacebook.getUserId().equals(DanhSachBaiHocActivity.currentUser.getUserId()) ) {
                        daCoNguoiChoiHienTai=true;
                        userFacebook.setCurrentUser(true);
                        userFacebook.setScore(userFacebook.getScore() + soDiemThemVao);
                        firebaseUser.child("User").child(DanhSachBaiHocActivity.currentUser.getUserId()).
                                child("score").setValue(userFacebook.getScore());
                    }
                    else {
                        userFacebook.setCurrentUser(false);
                    }
                    danhSachDiem.add(userFacebook);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!daCoNguoiChoiHienTai) {
            firebaseUser.child("User").child(DanhSachBaiHocActivity.currentUser.getUserId()).
                    child("name").setValue(DanhSachBaiHocActivity.currentUser.getName());

            firebaseUser.child("User").child(DanhSachBaiHocActivity.currentUser.getUserId()).
                    child("score").setValue(soDiemThemVao);
            DanhSachBaiHocActivity.currentUser.setCurrentUser(true);
            danhSachDiem.add(DanhSachBaiHocActivity.currentUser);
        }

        Collections.sort(danhSachDiem, new ComparatorByScore());
        danhSachDiemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onDownload() {

    }

    @Override
    public void downloadText() {
        DownloadTextTask downloadTextTask=new DownloadTextTask(this);
        downloadTextTask.execute(url);
    }

    @Override
    public void runLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_danh_sach_diem);
            }
        });

        lvDanhSachDiem=(ListView) findViewById(R.id.lvDanhSachDiem);
        danhSachDiem=new ArrayList<>();
        danhSachDiemAdapter=new DanhSachDiemAdapter(this, danhSachDiem);
        lvDanhSachDiem.setAdapter(danhSachDiemAdapter);
    }
}

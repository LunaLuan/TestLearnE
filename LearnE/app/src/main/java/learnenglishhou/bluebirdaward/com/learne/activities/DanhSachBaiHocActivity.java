package learnenglishhou.bluebirdaward.com.learne.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import learnenglishhou.bluebirdaward.com.learne.R;
import learnenglishhou.bluebirdaward.com.learne.adapter.DanhSachBaiHocAdapter;
import learnenglishhou.bluebirdaward.com.learne.listclass.BaiHoc;
import learnenglishhou.bluebirdaward.com.learne.listclass.UserFacebook;
import learnenglishhou.bluebirdaward.com.learne.listener.JsonParserTaskListener;
import learnenglishhou.bluebirdaward.com.learne.services.KiemTraBaiHocMoiService;
import learnenglishhou.bluebirdaward.com.learne.services.ThemBaiMoiService;
import learnenglishhou.bluebirdaward.com.learne.utils.DownloadTextTask;
import learnenglishhou.bluebirdaward.com.learne.utils.QuanLyService;

/*
TODO Xử lý danh sách các bài học được download từ url

 */

public class DanhSachBaiHocActivity extends AppCompatActivity implements JsonParserTaskListener {

    // private DownloadTextTask downloadTextTask;

    public class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ThemBaiMoiService.actionDownloadNewLesson)) {
                BaiHoc baiMoi= (BaiHoc) intent.getSerializableExtra("newLesson");
                danhSachBaiHoc.add(baiMoi);
                danhSachBaiHocAdapter.notifyDataSetChanged();
                Log.d("Đã đến đây", "A");
            }
        }
    }


    public class ComparatorByKey implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            int t1 = Integer.parseInt(s1.substring(3, s1.length()));
            int t2 = Integer.parseInt(s2.substring(3, s2.length()));
            if (t1 < t2)
                return -1;
            else if (t1 == t2)
                return 0;
            else
                return 1;
        }
    }

    public static UserFacebook currentUser;

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView tvName;

    private AccessTokenTracker accessTokenTracker;

    private ArrayList<BaiHoc> danhSachBaiHoc;

    private final String url = "https://learne-51b47.firebaseio.com/TestDB/BaiHoc.json";

    private ListView lvDanhSachBaiHoc;


    private DanhSachBaiHocAdapter danhSachBaiHocAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_danh_sach_bai_hoc);
        setTitle("Danh sách bài học....");

        downloadText();
    }

    @Override
    public void onDownloadTextSuccessed(String jsonText) {
        if (jsonText == null)
            return;
        List<String> listKeys = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonText);
            Iterator keys = root.keys();

            while (keys.hasNext()) {
                String maBaiHoc = (String) keys.next();
                Log.d("abc", maBaiHoc);
                listKeys.add(maBaiHoc);
            }

            // int i=0;
            Collections.sort(listKeys, new ComparatorByKey());
            for (int j = 0; j < listKeys.size(); j++) {
                String maBaiHoc = listKeys.get(j);
                BaiHoc baiHoc = new BaiHoc(maBaiHoc);
                JSONObject thongTinBaiHoc = root.getJSONObject(maBaiHoc);
                baiHoc.setTenBaihoc(thongTinBaiHoc.getString("tenBaiHoc"));
                baiHoc.setGioiThieu(thongTinBaiHoc.getString("gioiThieu"));

                danhSachBaiHoc.add(baiHoc);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Kích thước listKeys", String.valueOf(listKeys.size()));

        danhSachBaiHocAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDisconnect() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                setContentView(R.layout.layout_disconnect);
                Button btnThuLai = (Button) findViewById(R.id.btnThuLai);
                btnThuLai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadText();
                    }
                });
            }
        });

    }

    @Override
    public void onDownload() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.layout_connecting);
            }
        });
    }

    @Override
    public void downloadText() {
        DownloadTextTask downloadTextTask = new DownloadTextTask(this);
        downloadTextTask.execute(url);
    }


    public void taoDanhSachBaiHoc() {
        lvDanhSachBaiHoc = (ListView) findViewById(R.id.lvDanhSachBaiHoc);
        danhSachBaiHoc = new ArrayList<BaiHoc>();

        danhSachBaiHocAdapter = new DanhSachBaiHocAdapter(this, danhSachBaiHoc);
        lvDanhSachBaiHoc.setAdapter(danhSachBaiHocAdapter);
        // TODO khi người dùng click vào bài học tương ứng, các câu hỏi tương ứng của bài học đó sẽ xuất hiện
        lvDanhSachBaiHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DanhSachBaiHocActivity.this, DanhSachCauHoiActivity.class);
                intent.putExtra("maBaiHoc", danhSachBaiHoc.get(i).getMaBaiHoc());

                startActivity(intent);
            }
        });
    }

    public void dangNhapFacebook() {
        tvName = (TextView) findViewById(R.id.tvName);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvName.setText("Chờ chút...");
            }
        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        currentUser = new UserFacebook();
                        currentUser.setUserId(user.optString("id"));
                        currentUser.setEmail(user.optString("email"));
                        currentUser.setName(user.optString("name"));
                        // saveUser(userFacebook);

                        Toast.makeText(getApplicationContext(), "Xin chào " + currentUser.getName(), Toast.LENGTH_LONG).show();
                        tvName.setText("FB: " + currentUser.getName());
                        Log.d("user_id", currentUser.getUserId());
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                tvName.setText("");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null)
                    tvName.setText("");
            }
        };


    }

    @Override
    protected void onStop() {
        super.onStop();
        // TODO xử lý tiếp
        /*
        if(daGhi)
            unregisterReceiver(receiver);
        */
    }

    @Override
    public void runLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_danh_sach_bai_hoc);
            }
        });

        taoDanhSachBaiHoc();

        dangNhapFacebook();

        Intent service = new Intent(DanhSachBaiHocActivity.this, KiemTraBaiHocMoiService.class);
        if (!QuanLyService.isMyServiceRunning(this, KiemTraBaiHocMoiService.class))
            startService(service);
        /*
        receiver=new ResponseReceiver();
        registerReceiver(receiver, new IntentFilter(ThemBaiMoiService.actionDownloadNewLesson));
        daGhi=true;
        ThemBaiMoiService.startActionDownloadNewLesson(this);
        */
    }

    private boolean daGhi=false;
    ResponseReceiver receiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        LoginManager.getInstance().logOut();
    }

    public static boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    // TODO đang xử lý tiếp
    public void updateDanhSachBaiHoc() {
        Intent intent=new Intent(this, ThemBaiMoiService.AlarmReceiver.class);

        PendingIntent pi= PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager am=(AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pi);

    }

}

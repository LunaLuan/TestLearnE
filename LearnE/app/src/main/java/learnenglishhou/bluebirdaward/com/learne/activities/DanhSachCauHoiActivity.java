package learnenglishhou.bluebirdaward.com.learne.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import learnenglishhou.bluebirdaward.com.learne.R;
import learnenglishhou.bluebirdaward.com.learne.fragments.CauHoiFragment;
import learnenglishhou.bluebirdaward.com.learne.listclass.CauHoi;
import learnenglishhou.bluebirdaward.com.learne.listener.JsonParserTaskListener;
import learnenglishhou.bluebirdaward.com.learne.utils.DownloadTextTask;

public class DanhSachCauHoiActivity extends AppCompatActivity implements JsonParserTaskListener {

    private final String url = "https://learne-51b47.firebaseio.com/TestDB/CauHoi/";

    private String maBaiHoc;

    public static ArrayList<CauHoi> danhSachCauHoi;

    public int soCauConLai;
    public int soCauToiDa = 5;

    private Button btnCauTiepTheo;

    public int soDiem ;

    public static TextToSpeech textToSpeech;
    public static Intent intentRecognization;

    private int maNgonNgu;
    /*
    Mã ngôn ngữ: 1 là US, 2 là UK, 3 là Generic
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_danh_sach_cau_hoi);

        soCauConLai = soCauToiDa;
        soDiem=0;

        intentRecognization = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        setLanguage();

        Intent intent = getIntent();
        maBaiHoc = intent.getStringExtra("maBaiHoc");
        downloadText();
    }



    public void setLanguage() {
        String languages[] = getResources().getStringArray(R.array.listLanguage);

        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        maNgonNgu= Integer.parseInt(sp.getString("prefLanguage", "1"));
        // Toast.makeText(this, "Ngôn ngữ đang dùng: "+languages[maNgonNgu-1], Toast.LENGTH_SHORT).show();

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                    /*
                    if(maNgonNgu==2)
                        textToSpeech.setLanguage(Locale.UK);
                    else if(maNgonNgu==3)
                        textToSpeech.setLanguage(Locale.ENGLISH);
                    else
                        textToSpeech.setLanguage(Locale.US);
                    */
                }
            }
        });


        textToSpeech.setSpeechRate(0.9f);
        textToSpeech.setPitch(1f);

        intentRecognization.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, false);
        intentRecognization.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        intentRecognization.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        // intentRecognization.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói:" + cauHoi.getNdCauHoi());
        intentRecognization.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intentRecognization.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        intentRecognization.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5);
        // intentRecognization.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(60000));
        // intentRecognization.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, new Long(60000));
        intentRecognization.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        intentRecognization.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        /*
        if(maNgonNgu==2)
            intentRecognization.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-UK");
        else if(maNgonNgu==3)
            intentRecognization.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
        else
            intentRecognization.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        */

    }


    @Override
    public void onDownloadTextSuccessed(String jsonText) {
        danhSachCauHoi = new ArrayList<CauHoi>();
        try {
            JSONObject root = new JSONObject(jsonText);
            Iterator keys = root.keys();
            while (keys.hasNext()) {
                String maCauHoi = (String) keys.next();
                CauHoi cauHoi = new CauHoi(maCauHoi);

                JSONObject thongTinCauHoi = root.getJSONObject(maCauHoi);
                cauHoi.setNdCauHoi(thongTinCauHoi.getString("ndCauHoi"));
                cauHoi.setVietSub(thongTinCauHoi.getString("vietSub"));

                danhSachCauHoi.add(cauHoi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        taoCauHoi();
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


    private void taoCauHoi() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // speechRecognizer.setRecognitionListener(null);
        int k = (new Random()).nextInt(danhSachCauHoi.size());
        transaction.replace(R.id.fragmentCauHoi, new CauHoiFragment(danhSachCauHoi.get(k))).commit();
    }

    @Override
    public void downloadText() {
        DownloadTextTask task = new DownloadTextTask(this);
        task.execute(url + maBaiHoc + ".json");
    }

    @Override
    public void runLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_danh_sach_cau_hoi);
            }
        });
        btnCauTiepTheo = (Button) findViewById(R.id.btnCauTiepTheo);
        btnCauTiepTheo.setClickable(false);
        btnCauTiepTheo.setAlpha(0.5f);
    }


    public void daTraLoiXong() {
        btnCauTiepTheo.setClickable(true);
        btnCauTiepTheo.setAlpha(1);
        if (soCauConLai - 1 == 0)
            btnCauTiepTheo.setText("Kết thúc");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // setLanguage();
    }

    public void clickCauTiepTheo(View view) {
        soCauConLai--;
        if (soCauConLai == 0) {
            hienDialogKetThuc();
        } else {
            taoCauHoi();
        }
        btnCauTiepTheo.setClickable(false);
        btnCauTiepTheo.setAlpha(0.5f);
    }

    private void hienDialogKetThuc() {
        // Kết thúc
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Kết thúc");
        dialog.setContentView(R.layout.dialog_ket_thuc);
        dialog.setCanceledOnTouchOutside(true);

        TextView tvThanhTich = (TextView) dialog.findViewById(R.id.tvThanhTich);
        tvThanhTich.setText("Bạn dành được: " + soDiem + " điểm!!!!!!!");

        TextView tvNhanXet = (TextView) dialog.findViewById(R.id.tvNhanXet);
        float tyLe = (float) 1.0 * soDiem / soCauToiDa;
        if (tyLe <= 0.3f)
            tvNhanXet.setText("Bạn quá gà");
        else if (tyLe < 0.9f)
            tvNhanXet.setText("Bạn cần cố gắng thêm");
        else
            tvNhanXet.setText("Bạn quá giỏi");
        Log.d("Tỷ lệ", String.valueOf(tyLe));

        Button btnKetThuc = (Button) dialog.findViewById(R.id.btnKetThuc);
        btnKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DanhSachBaiHocActivity.isLoggedIn())
                    luuDiemMoi();
                finish();
            }
        });

        ShareButton btnChiaSe = (ShareButton) dialog.findViewById(R.id.btnChiaSe);

        // TODO cai dat layout
        dialog.show();
        Bitmap screenShot = takeScreenShot(dialog);
        ;

        SharePhoto photo = new SharePhoto.Builder().setBitmap(screenShot).build();
        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();

        btnChiaSe.setShareContent(content);
    }

    private void luuDiemMoi() {
        Intent intent=new Intent(DanhSachCauHoiActivity.this, DanhSachDiemActivity.class);
        intent.putExtra("DiemThemVao", soDiem);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    public Bitmap takeScreenShot(Dialog dialog) {
        View mainView = getWindow().getDecorView().getRootView();
        View dialogView = dialog.getWindow().getDecorView().getRootView();
        Log.d("abc", dialogView.toString());

        int location[] = new int[2];
        int location2[] = new int[2];

        mainView.getLocationOnScreen(location);
        dialogView.getLocationInWindow(location2);
        Log.d("Dialog view", location2[0] + ", " + location2[1]);
        Log.d("Main view", location[0] + ", " + location[1]);

        mainView.setDrawingCacheEnabled(true);
        dialogView.setDrawingCacheEnabled(true);

        dialogView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        dialogView.layout(0, 0, dialogView.getMeasuredWidth(), dialogView.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(mainView.getDrawingCache());
        Bitmap bitmap2 = Bitmap.createBitmap(dialogView.getDrawingCache());

        // Canvas canvas=new Canvas(bitmap);
        // canvas.drawBitmap(bitmap2, location2[0], location2[1],new Paint());

        return bitmap2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intent=new Intent(DanhSachCauHoiActivity.this, SettingsMenuActivity.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
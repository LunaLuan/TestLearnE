package learnenglishhou.bluebirdaward.com.learne.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import learnenglishhou.bluebirdaward.com.learne.R;
import learnenglishhou.bluebirdaward.com.learne.activities.DanhSachCauHoiActivity;
import learnenglishhou.bluebirdaward.com.learne.listclass.CauHoi;


public class CauHoiFragment extends Fragment {

    public ProgressBar progressBarLeft;
    public ProgressBar progressBarRight;

    public TextToSpeech textToSpeech;
    public SpeechRecognizer speechRecognizer;
    public Intent intentRecognization;

    private TextView tvTrangThaiNoi;
    private TextView tvDangNgheThay;

    private List<Button> danhSachButton;
    private Set<String> danhSachTu;

    private View view; // root view

    private final int request_code = 1010;
    private CauHoi cauHoi;

    private TextView tvNdCauHoi;
    private TextView tvVietSub;
    private TextView tvTraLoiDungHaySai;

    private Button btnNoi;
    private Button btnNgheThu;

    private LinearLayout layoutDanhSachTu;

    private int soLanNoiToiDa = 3;


    public CauHoiFragment(CauHoi cauHoi) {
        // Required empty public constructor
        this.cauHoi = cauHoi;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cau_hoi, container, false);

        // Tao intent nhan dang giong noi
        intentRecognization= DanhSachCauHoiActivity.intentRecognization;

        textToSpeech=DanhSachCauHoiActivity.textToSpeech;

        initLayout();

        return view;
    }

    public void initLayout() {
        progressBarLeft=(ProgressBar) view.findViewById(R.id.progressBarLeft);
        progressBarRight=(ProgressBar) view.findViewById(R.id.progressBarRight);

        tvTraLoiDungHaySai = (TextView) view.findViewById(R.id.tvTraLoiDungHaySai);
        tvDangNgheThay = (TextView) view.findViewById(R.id.tvDangNgheThay);
        tvTrangThaiNoi = (TextView) view.findViewById(R.id.tvTrangThaiNoi);
        // khởi tạo nội dung câu hỏi
        tvNdCauHoi = (TextView) view.findViewById(R.id.tvNdCauHoi);
        tvNdCauHoi.setText(cauHoi.getNdCauHoi());
        // khởi tạo vietSub
        tvVietSub = (TextView) view.findViewById(R.id.tvVietSub);
        tvVietSub.setText("(có nghĩa là: " + cauHoi.getVietSub() + ")");
        // khởi tạo danh sách từ, là phần sẽ chứa danh sách các từ đã nói đc và chưa nói đc
        layoutDanhSachTu = (LinearLayout) view.findViewById(R.id.layoutDanhSachTu);
        // khởi tạo btnNoi
        btnNoi = (Button) view.findViewById(R.id.btnNoi);
        btnNoi.setText("Nói (" + soLanNoiToiDa + ")");
        btnNoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtnNoi();
            }
        });
        // khởi tạo textToSpeech và btnNgheThu

        btnNgheThu = (Button) view.findViewById(R.id.btnNgheThu);
        btnNgheThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(cauHoi.getNdCauHoi(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        // khởi tạo tvTraLoiDungHaySai


        setDanhSachTu();
    }


    // Tạo danh sách từ và danh sách các nút để xử lý
    public void setDanhSachTu() {
        Long t1 = System.currentTimeMillis();
        Log.e("t1", t1 + "");
        danhSachTu = new TreeSet<>();
        danhSachButton = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(cauHoi.getNdCauHoi().trim());
        while (st.countTokens() > 0) {
            String s = st.nextToken();
            danhSachTu.add(s.trim().toLowerCase());

            Button btnTu = new Button(getContext());
            btnTu.setText(s);
            btnTu.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btnTu.setTextColor(Color.parseColor("#F44336"));
            btnTu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    textToSpeech.speak(b.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            });

            danhSachButton.add(btnTu);
        }
        // thêm danh sách nút vào layoutDanhSachTu
        int tro = 0;
        for (int i = 0; i < danhSachButton.size() / 3 + 1; i++) {
            if (tro == danhSachButton.size())
                break;
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rowLayout.setGravity(Gravity.CENTER);
            rowLayout.removeAllViews();

            for (int j = 0; j < 3; j++) {
                if (tro == danhSachButton.size())
                    break;
                rowLayout.addView(danhSachButton.get(tro));
                tro++;
            }
            layoutDanhSachTu.addView(rowLayout);
        }
        layoutDanhSachTu.setVisibility(View.INVISIBLE);
    }

    /*
       Nếu người dùng trả lời sai, sẽ có danh sách các từ họ đã trả lời đúng và sai, từ trả lời đúng có màu đỏ và có thể click để nghe xem
       nó đc phát âm như thế nào.
       - Với các từ trả lời sai: màu của text là màu đỏ, khi click sẽ phát âm.
       - Với các từ trả lời đúng: màu của text là màu xanh, ko cho click
    */
    public void setLayoutSauKhiTraLoi() {
        layoutDanhSachTu.setVisibility(View.VISIBLE);
        tvTrangThaiNoi.setText(" ");

        progressBarRight.setVisibility(View.INVISIBLE);
        progressBarLeft.setVisibility(View.INVISIBLE);

        // Xử lý danh sách button sau khi trả lời
        for (int i = 0; i < danhSachButton.size(); i++) {
            Button b = danhSachButton.get(i);
            if (!danhSachTu.contains(b.getText().toString().trim().toLowerCase())) {
                b.setTextColor(Color.parseColor("#4CAF50"));
                b.setClickable(false);
            } else {
                b.setTextColor(Color.parseColor("#F44336"));
            }
        }
        if (danhSachTu.size() == 0) {
            tvTraLoiDungHaySai.setText("Chuẩn rồi...");
            tvTraLoiDungHaySai.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            if (soLanNoiToiDa == 1)
                tvTraLoiDungHaySai.setText("Chuyển câu khác đi...");
            else
                tvTraLoiDungHaySai.setText("Cố lên...");
            tvTraLoiDungHaySai.setTextColor(Color.parseColor("#F44336"));
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public boolean daKetThuc() {
        soLanNoiToiDa--;
        speechRecognizer.destroy();

        tvTrangThaiNoi.setText(" ");
        tvDangNgheThay.setText(" ");

        btnNoi.setText("Nói (" + soLanNoiToiDa + ")");
        if (soLanNoiToiDa == 0 || danhSachTu.size() == 0) {

            btnNoi.setAlpha(0.5f);
            btnNoi.setClickable(false);

            DanhSachCauHoiActivity danhSachCauHoiActivity = (DanhSachCauHoiActivity) getContext();
            if (danhSachTu.size() == 0)
                danhSachCauHoiActivity.soDiem++;
            danhSachCauHoiActivity.daTraLoiXong();
            return true;
        }
        else {
            btnNoi.setClickable(true);
            btnNoi.setAlpha(1f);
        }
        return false;
    }

    /*
    Xu ly cac ket qua tra loi,
     */
    public void xuLyCauTraLoi(ArrayList<String> cauTraLoi) {

        if (cauTraLoi != null) {
            for (int i = 0; i < cauTraLoi.size(); i++) {
                Log.d("Câu trả lời", cauTraLoi.get(i));
                StringTokenizer st = new StringTokenizer(cauTraLoi.get(i));
                while (st.countTokens() > 0) {
                    String s = st.nextToken().trim().toLowerCase();
                    if (danhSachTu.contains(s))
                        danhSachTu.remove(s);
                }
            }
        }
    }


    // Xử lý sự kiện khi click btnNoi
    public void clickBtnNoi() {
        // dangNoi = true;
        tvTrangThaiNoi.setText("Chờ chút....");
        btnNoi.setClickable(false);
        btnNoi.setAlpha(0.5f);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            private CountDownTimer timer;
            private String currentAnswer;
            private int currentAnswerCountTokens;

            @Override
            public void onReadyForSpeech(Bundle bundle) {
                currentAnswer="";
                currentAnswerCountTokens=0;

                timer = new CountDownTimer(15000, 1000) {

                    @Override
                    public void onTick(long time) {
                        tvTrangThaiNoi.setText("Đang nghe đây. Nói đi ("+(time/1000)+")...");
                    }

                    @Override
                    public void onFinish() {
                        speechRecognizer.stopListening();
                        tvTrangThaiNoi.setText("Đang xử lý câu trả lời...");
                        onResults(null);
                    }
                }.start();

                progressBarLeft.setVisibility(View.VISIBLE);
                progressBarRight.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {
                Log.d("RMS", v+"");
                progressBarLeft.setProgress((int) v);
                progressBarRight.setProgress((int) v);

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                // Toast.makeText(getContext(), "Đã ngừng nói", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int errorCode) {
                String message;
                switch (errorCode) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        message = "Audio recording error";
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        message = "Client side error";
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        message = "Insufficient permissions";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        message = "Network error";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        message = "Network timeout";
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        message = "No match";
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        message = "RecognitionService busy";
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        message = "error from server";
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        message = "No speech input";
                        break;
                    default:
                        message = "Didn't understand, please try again.";
                        break;
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                speechRecognizer.stopListening();
                onResults(null);

            }

            @Override
            public void onResults(Bundle bundle) {
                // threadTvDangNgheThay.stop();
                if(timer!=null)
                    timer.cancel();
                setLayoutSauKhiTraLoi();
                daKetThuc();
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                if(bundle!=null && bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).size()>0) {
                    ArrayList<String> cacCauDangNgheThay = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    xuLyCauTraLoi(cacCauDangNgheThay);

                    tvDangNgheThay.setText(cacCauDangNgheThay.get(0));
                    Log.d("Các câu đang nghe thấy (0)", cacCauDangNgheThay.get(0).toString());
                }
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        speechRecognizer.startListening(intentRecognization);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

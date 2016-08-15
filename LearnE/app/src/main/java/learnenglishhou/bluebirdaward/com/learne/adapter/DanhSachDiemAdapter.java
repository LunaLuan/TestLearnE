package learnenglishhou.bluebirdaward.com.learne.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import learnenglishhou.bluebirdaward.com.learne.R;
import learnenglishhou.bluebirdaward.com.learne.listclass.UserFacebook;

/**
 * Created by asd on 01/08/2016.
 */

public class DanhSachDiemAdapter extends BaseAdapter {

    private ArrayList<UserFacebook> danhSachDiem;
    private Context context;

    private static LayoutInflater inflater;

    public DanhSachDiemAdapter(Context context, ArrayList<UserFacebook> danhSachDiem) {
        this.context= context;
        this.danhSachDiem= danhSachDiem;

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return danhSachDiem.size();
    }

    @Override
    public Object getItem(int i) {
        return danhSachDiem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view== null)
            view= inflater.inflate(R.layout.lv_diem, null);

        TextView tvName=(TextView) view.findViewById(R.id.tvName);
        TextView tvScore=(TextView) view.findViewById(R.id.tvScore);

        tvName.setText((i+1) +". "+danhSachDiem.get(i).getName());
        tvScore.setText("Điểm: "+danhSachDiem.get(i).getScore());

        if(i==0) {
            tvName.setTextColor(Color.parseColor("#FFFF00"));
        }

        if(danhSachDiem.get(i).isCurrentUser()) {
            tvName.setText((i+1) +". "+danhSachDiem.get(i).getName()+" (bạn đây)");

            tvName.setTextColor(Color.parseColor("#26C6DA"));
            tvScore.setTextColor(Color.parseColor("#26C6DA"));

        }
        // Log.d("DanhSachDiemAdapter", danhSachDiem.get(i).getName()+" "+danhSachDiem.get(i).isCurrentUser());

        return view;
    }
}
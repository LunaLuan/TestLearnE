package learnenglishhou.bluebirdaward.com.learne.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import learnenglishhou.bluebirdaward.com.learne.R;
import learnenglishhou.bluebirdaward.com.learne.listclass.BaiHoc;

/**
 * Created by SVTest on 21/07/2016.
 */

public class DanhSachBaiHocAdapter extends BaseAdapter {

    private ArrayList<BaiHoc> danhSachBaiHoc;
    private Context context;

    private static LayoutInflater inflater = null;

    public DanhSachBaiHocAdapter(Context context, ArrayList<BaiHoc> danhSachBaiHoc) {
        this.context = context;
        this.danhSachBaiHoc = danhSachBaiHoc;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return danhSachBaiHoc.size();
    }

    @Override
    public Object getItem(int i) {
        return danhSachBaiHoc.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = inflater.inflate(R.layout.lv_bai_hoc, null);
        TextView tvTenBaiHoc = (TextView) view.findViewById(R.id.tvTenBaiHoc);
        TextView tvGioiThieu = (TextView) view.findViewById(R.id.tvGioiThieu);

        tvTenBaiHoc.setText("Bài " + (i + 1) + ": " + danhSachBaiHoc.get(i).getTenBaihoc());
        tvGioiThieu.setText(danhSachBaiHoc.get(i).getGioiThieu());

        return view;
    }

}

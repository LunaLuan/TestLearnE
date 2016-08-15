package learnenglishhou.bluebirdaward.com.learne.listclass;

import java.util.List;

/**
 * Created by SVTest on 20/07/2016.
 */

public class BaiHoc {

    private String maBaiHoc;
    private String tenBaihoc;
    private String gioiThieu;

    private List<CauHoi> cauHoi;

    public BaiHoc(String maBaiHoc) {
        this.maBaiHoc = maBaiHoc;
    }

    public void setTenBaihoc(String tenBaihoc) {
        this.tenBaihoc = tenBaihoc;
    }

    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }

    public void addCauHoi(String maCauHoi, String ndCauHoi) {
        CauHoi cauHoiMoi = new CauHoi(maCauHoi);
        cauHoiMoi.setNdCauHoi(ndCauHoi);
        cauHoi.add(cauHoiMoi);
    }

    public String getTenBaihoc() {
        return tenBaihoc;
    }

    public String getGioiThieu() {
        return gioiThieu;
    }

    public String getMaBaiHoc() {
        return maBaiHoc;
    }
}

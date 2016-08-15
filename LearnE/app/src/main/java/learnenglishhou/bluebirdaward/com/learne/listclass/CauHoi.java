package learnenglishhou.bluebirdaward.com.learne.listclass;

/**
 * Created by SVTest on 20/07/2016.
 */

public class CauHoi {

    private String maBaiHoc;
    private String maCauHoi;
    private String ndCauHoi;
    private String vietSub;

    public CauHoi(String maCauHoi) {
        this.maCauHoi = maCauHoi;
    }

    public void setNdCauHoi(String ndCauHoi) {
        this.ndCauHoi = ndCauHoi;
    }

    public void setVietSub(String vietSub) {
        this.vietSub = vietSub;
    }

    public String getNdCauHoi() {
        return ndCauHoi;
    }

    public String getVietSub() {
        return vietSub;
    }
}

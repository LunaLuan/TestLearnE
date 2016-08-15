package learnenglishhou.bluebirdaward.com.learne.listener;

/**
 * Created by SVTest on 21/07/2016.
 */

public interface JsonParserTaskListener {


    // TODO Bắt sự kiện download thành công, xử lý jsonText trong class
    public void onDownloadTextSuccessed(String jsonText);

    // TODO Bắt các sự kiện khác
    public void onDisconnect();

    public void onDownload();

    // TODO Hàm download text, đối tượng có thể download text theo nhiều cách
    public void downloadText();

    // TODO Sau tất cả các xử lý mới tiến hành cài đặt layout
    public void runLayout();

}

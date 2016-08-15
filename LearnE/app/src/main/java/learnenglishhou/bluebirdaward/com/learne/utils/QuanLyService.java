package learnenglishhou.bluebirdaward.com.learne.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by asd on 01/08/2016.
 */

public class QuanLyService {

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

package mg.studio.weatherappdesign;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/3/21.
 */

public class GetNetWorkState {
    private GetNetWorkState() {
    }

    /**
     * Get the current state of the network
     *
     * @param context
     * @return
     */
    public static boolean getNetworkState(Context context) {
        // Get all the connection management objects of the mobile phone (including the management of Wi-Fi, net, etc.)
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // get NetworkInfo
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        // Traversing each object
        for (NetworkInfo networkInfo : networkInfos) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                // debug info
                System.out.println("current network type:" + networkInfo.getTypeName());
                // network available
                return true;
            }
        }
        // network unavailable
        return false;
    }
}

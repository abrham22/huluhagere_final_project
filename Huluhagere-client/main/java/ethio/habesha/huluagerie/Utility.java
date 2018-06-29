package ethio.habesha.huluagerie;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utility {

    private static boolean TOAST_DEBUG = true;

    static void toastMessage(Context c, String TAG, DataController.GeneralResponse resp) {
        if (resp.success) {
            Log.d(TAG, resp.message);
            Toast.makeText(c,
                    resp.message,
                    Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, resp.error);
            Toast.makeText(c,
                    resp.error,
                    Toast.LENGTH_LONG).show();
        }
    }

    static void toastText(Context c, String TAG, String text) {
        Log.d(TAG, text);
        if(TOAST_DEBUG) {
            Toast.makeText(c,
                    text,
                    Toast.LENGTH_LONG).show();
        }

    }

}

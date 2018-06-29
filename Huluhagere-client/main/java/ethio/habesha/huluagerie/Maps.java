package ethio.habesha.huluagerie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Maps {

    public static void showMap(Context context, String location) {
        Uri intentUri = Uri.parse("geo:0,0?q=" + location);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }

    public static void showDirection(Context context, String location1, String location2) {
        Uri intentUri = Uri.parse("http://maps.google.com/maps?saddr=" + location1 + "&daddr=" + location2);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }

}

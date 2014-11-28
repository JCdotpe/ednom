package ordanel.ednom.Library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by OrdNael on 28/11/2014.
 */
public class NetworkUtils {

    public static boolean haveNetworkConnection ( final Context paramContext ) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        final ConnectivityManager connectivityManager = (ConnectivityManager) paramContext.getSystemService( Context.CONNECTIVITY_SERVICE );

        if ( connectivityManager != null )
        {
            final NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

            for ( final NetworkInfo networkInfoCheck : networkInfos )
            {
                if ( networkInfoCheck.getTypeName().equalsIgnoreCase( "WIFI" ) )
                {
                    if ( networkInfoCheck.isConnected() )
                    {
                        haveConnectedWifi = true;
                    }
                }

                if ( networkInfoCheck.getTypeName().equalsIgnoreCase( "MOBILE" ) )
                {
                    if ( networkInfoCheck.isConnected() )
                    {
                        haveConnectedMobile = true;
                    }
                }
            }
        }

        return haveConnectedWifi || haveConnectedMobile;
    }

}
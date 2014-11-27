package ordanel.ednom;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * Created by OrdNael on 26/11/2014.
 */
public class EdnomApplication extends Application {

    private static final String TAG = EdnomApplication.class.getSimpleName();

    private static EdnomApplication ednomApplication;
    private boolean serviceRunningFlag;

    public static EdnomApplication getInstance() {
        return ednomApplication;
    }

    public boolean isServiceRunning(){
        return serviceRunningFlag;
    }

    public void setServiceRunningFlag( boolean serviceRunningFlag ){
        this.serviceRunningFlag = serviceRunningFlag;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e( TAG, "start EdnomApplication" );
        ednomApplication = this;
        startService( new Intent( EdnomApplication.this, UpdaterService.class ) );

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e( TAG, "end EdnomApplication" );
        stopService( new Intent( EdnomApplication.this, UpdaterService.class ) );
    }
}
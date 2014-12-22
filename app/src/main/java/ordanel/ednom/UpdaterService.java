package ordanel.ednom;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import ordanel.ednom.Business.PadronBL;

/**
 * Created by OrdNael on 26/11/2014.
 */
public class UpdaterService extends Service {

    private static final String TAG = UpdaterService.class.getSimpleName();

    static final int DELAY = 120000; // 300000
    private boolean runFlag = false;

    private Updater updater;
    private EdnomApplication ednomApplication;
    private PadronBL padronBL;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e( TAG, "onCreate UpdaterService" );
        ednomApplication = (EdnomApplication) getApplication();
        updater = new Updater();
        padronBL = new PadronBL(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e( TAG, "onDestroy UpdaterService" );

        runFlag = false;
        ednomApplication.setServiceRunningFlag( false );
        updater.interrupt();
        updater = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e( TAG, "onStartCommand UpdaterService" );

        if (!runFlag)
        {
            runFlag = true;
            ednomApplication.setServiceRunningFlag( true );
            updater.start();
        }

        return START_STICKY;
    }

    private class Updater extends Thread {

        public Updater() {
            super("UpdaterService-UpdaterThread");
        }

        @Override
        public void run() {

            UpdaterService updaterService = UpdaterService.this;

            while ( updaterService.runFlag )
            {
                Log.e( TAG, "UpdaterThread running" );

                try
                {
                    padronBL.getAllforSync();
                    Thread.sleep( DELAY );
                }
                catch (InterruptedException e)
                {
                    updaterService.runFlag = false;
                    ednomApplication.setServiceRunningFlag( false );
                }
            }

        }
    }

}

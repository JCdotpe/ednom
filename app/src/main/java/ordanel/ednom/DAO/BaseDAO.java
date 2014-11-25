package ordanel.ednom.DAO;

import android.content.Context;
import android.util.Log;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Library.HttpPostAux;

/**
 * Created by OrdNael on 25/11/2014.
 */
public class BaseDAO {

    private static final String TAG = BaseDAO.class.getSimpleName();

    public DBHelper dbHelper;
    public HttpPostAux httpPostAux;

    public void initHttPostAux() {

        httpPostAux = HttpPostAux.getInstance();

    }

    public void initDBHelper(Context paramContext) {

        dbHelper = DBHelper.getUtilDb( paramContext );

    }

    public void openDBHelper() {

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "openDBHelper : " + e.toString() );
        }

    }

    public void closeDBHelper() {

        try
        {
            dbHelper.endTransaction();
            dbHelper.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "closeDBHelper : " + e.toString() );
        }

    }


}

package ordanel.ednom.DAO;

import android.content.Context;
import android.util.Log;

public class ReporteGeneralDAO extends BaseDAO {

    private static ReporteGeneralDAO reporteGeneralDAO;

    public static ReporteGeneralDAO getInstance( Context paramContext ) {

        if ( reporteGeneralDAO == null )
        {
            reporteGeneralDAO = new ReporteGeneralDAO( paramContext );
        }
        return reporteGeneralDAO;

    }

    private ReporteGeneralDAO ( Context paramContext ) {
        initDBHelper( paramContext );
    }

    public String getPass (){
        String pass = "";
        try
        {
            openDBHelper();

            SQL = "SELECT clave FROM usuario_local LIMIT 1";
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                pass = cursor.getString( cursor.getColumnIndex("clave") );
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        return pass;

    }

}

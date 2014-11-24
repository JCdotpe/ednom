package ordanel.ednom.Business;

import android.content.Context;

import java.util.ArrayList;

import ordanel.ednom.DAO.AulaLocalDAO;
import ordanel.ednom.Entity.AulaLocalE;

/**
 * Created by OrdNael on 24/11/2014.
 */
public class AulaLocalBL {

    private static AulaLocalDAO aulaLocalDAO;
    private static ArrayList<AulaLocalE> aulaLocalEArrayList;
    private static ArrayList<String> stringArrayList;

    public AulaLocalBL( Context context ){
        aulaLocalDAO = new AulaLocalDAO( context );
    }

    public static ArrayList<String> getAllNroAula() {

        stringArrayList = new ArrayList<String>();
        aulaLocalEArrayList = aulaLocalDAO.getAllNroAula();

        for ( AulaLocalE aulaLocalE : aulaLocalEArrayList )
        {
            stringArrayList.add( aulaLocalE.getNro_aula().toString() );
        }

        return stringArrayList;

    }

}

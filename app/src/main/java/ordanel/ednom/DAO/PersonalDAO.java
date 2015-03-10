package ordanel.ednom.DAO;

import android.content.Context;

import java.util.ArrayList;

import ordanel.ednom.Entity.PersonalE;

/**
 * Created by Cr-Diego on 09/03/2015.
 */
public class PersonalDAO extends BaseDAO {
    private static final String TAG = PersonalDAO.class.getSimpleName();
    private static PersonalDAO personalDAO;
    PersonalE personalE;

    ArrayList<PersonalE> personalEArrayList;

    public synchronized static PersonalDAO getInstance(Context paramContext){
        if (personalDAO == null){
            personalDAO = new PersonalDAO(paramContext);
        }
        return personalDAO;
    }

    public PersonalDAO(Context paramContext) {
        initDBHelper(paramContext);
        initHttPostAux();
    }


}

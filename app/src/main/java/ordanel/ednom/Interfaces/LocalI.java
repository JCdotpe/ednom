package ordanel.ednom.Interfaces;

import android.view.View;

import java.util.ArrayList;

import ordanel.ednom.Entity.PadronE;

/**
 * Created by OrdNael on 05/11/2014.
 */
public interface LocalI {

    public void onSectionAttached( int number );

    public void searchPerson( View view );

    public void showPerson( ArrayList<PadronE> arrayList );

}
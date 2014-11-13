package ordanel.ednom.Interfaces;

import java.util.ArrayList;

import ordanel.ednom.Entity.PadronE;

/**
 * Created by OrdNael on 05/11/2014.
 */
public interface MainI {

    public void onSectionAttached( int number );

    public void searchPerson( );

    public void showPerson( ArrayList<PadronE> arrayList );

}
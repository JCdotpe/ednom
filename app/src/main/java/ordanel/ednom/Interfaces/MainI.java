package ordanel.ednom.Interfaces;

import ordanel.ednom.Entity.DocentesE;

/**
 * Created by OrdNael on 05/11/2014.
 */
public interface MainI {

    public void onSectionAttached( int number );

    public void searchPerson( String conditional );

    public void showPerson( DocentesE docentesE );

}
package ordanel.ednom.Interfaces;

/**
 * Created by OrdNael on 05/11/2014.
 */
public interface MainI {

    public void onSectionAttached( int number );

    public void asistenciaLocal( String paramDNI );

    public void asistenciaAula( String paramDNI, Integer paramNroAula );

    public void inventarioFicha( String paramCodFicha, Integer paramNroAula );

}
package ordanel.ednom.Interfaces;


public interface MainI {

    public void onSectionAttached( int number );

    public void asistenciaLocal( String paramDNI );

    public void asistenciaAula( String paramDNI, Integer paramNroAula );

    public void inventarioFicha( String paramCodFicha, Integer paramNroAula );

    public void inventarioCuadernillo( String paramCodCuadernillo, Integer paramNroAula );

    public void asistenciaPersonal(String nroDni);
}
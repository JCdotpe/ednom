package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cr-Diego on 09/03/2015.
 */
public class PersonalE implements Parcelable {

    public static final String DNI = "dni";
    public static final String APE_PAT = "ape_paterno";
    public static final String APE_MAT = "ape_materno";
    public static final String NOMBRES = "nombres";
    public static final String NOMBRE_COMPLETO = "nombre_completo";
    public static final String ID_CARGO = "id_cargo";
    public static final String COD_SEDE_OPERATIVA = "cod_sede_operativa";
    public static final String COD_LOCAL_SEDE = "cod_local_sede";
    public static final String ASISTENCIA = "asistencia";
    public static final String HORA_INGRESO = "hora_ingreso";
    public static final String HORA_SALIDA = "hora_salida";
    public static final String OBSERVACIONES = "observaciones";
    public static final String ESTADOCAMBIO = "estado_cambio";
    public static final String ESTADOREEMPLAZO = "estado_reemp";
    public static final String R_DNI = "r_dni";
    public static final String R_NOMBRE_COMPLETO = "r_nombre_completo";
    public static final String ID_CARGO_CAMBIO = "id_cargo_cambio";
    public static final String NRO_AULA = "nro_aula";
    public static final String NIVEL = "nivel";
    public static final String RESERVA = "reserva";

    private String dni;
    private String ape_pat ;
    private String ape_mat;
    private String nombres;
    private String nombre_completo;
    private int id_cargo;
    private int cod_sede_operativa;
    private int cod_local_sede;
    private String asistencia;
    private String hora_ingreso;
    private String hora_salida;
    private String observaciones;
    private String estadoCambio;
    private String estadoReemplazo;
    private String r_dni;
    private String r_nombre_completo;
    private int id_cargo_cambio;
    private int status;
    private LocalE localE;
    private CargoE cargoE;
    private String cargo;
    private int nro_aula;
    private String nivel;
    private String reserva;

    public PersonalE() { super();
    }

    public PersonalE(String dni, String nombre_completo, String asistencia, String cargo, String r_dni, String r_nombre_completo) {
        this.dni = dni;
        this.nombre_completo = nombre_completo;
        this.r_dni = r_dni;
        this.r_nombre_completo = r_nombre_completo;
        this.asistencia = asistencia;
        this.cargo = cargo;
    }

    public PersonalE(Parcel parcel) {
        dni = parcel.readString();
        ape_pat  = parcel.readString();
        ape_mat = parcel.readString();
        nombres = parcel.readString();
        nombre_completo = parcel.readString();
        id_cargo = parcel.readInt();
        cod_sede_operativa = parcel.readInt();
        cod_local_sede = parcel.readInt();
        asistencia = parcel.readString();
        hora_ingreso = parcel.readString();
        hora_salida = parcel.readString();
        observaciones = parcel.readString();
        estadoCambio = parcel.readString();
        estadoReemplazo = parcel.readString();
        r_dni = parcel.readString();
        r_nombre_completo = parcel.readString();
        id_cargo_cambio = parcel.readInt();
        status = parcel.readInt();
        localE = parcel.readParcelable(LocalE.class.getClassLoader());
        cargoE = parcel.readParcelable(CargoE.class.getClassLoader());
        cargo = parcel.readString();
        nro_aula = parcel.readInt();
        nivel = parcel.readString();
        reserva = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(dni);
        dest.writeString(ape_pat);
        dest.writeString(ape_mat);
        dest.writeString(nombres);
        dest.writeString(nombre_completo);
        dest.writeInt(id_cargo);
        dest.writeInt(cod_sede_operativa);
        dest.writeInt(cod_local_sede);
        dest.writeString(asistencia);
        dest.writeString(hora_ingreso);
        dest.writeString(hora_salida);
        dest.writeString(observaciones);
        dest.writeString(estadoCambio);
        dest.writeString(estadoReemplazo);
        dest.writeString(r_dni);
        dest.writeString(r_nombre_completo);
        dest.writeInt(id_cargo_cambio);
        dest.writeInt(status);
        dest.writeParcelable(localE,flags);
        dest.writeParcelable(cargoE, flags);
        dest.writeString(cargo);
        dest.writeInt(nro_aula);
        dest.writeString(nivel);
        dest.writeString(reserva);
    }

    public LocalE getLocalE() {
        return localE;
    }

    public void setLocalE(LocalE localE) {
        this.localE = localE;
    }

    public CargoE getCargoE() {
        return cargoE;
    }

    public void setCargoE(CargoE cargoE) {
        this.cargoE = cargoE;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static final Creator<PersonalE> CREATOR = new Creator<PersonalE>() {
        @Override
        public PersonalE createFromParcel(Parcel source) {
            return new PersonalE(source);
        }

        @Override
        public PersonalE[] newArray(int size) {
            return new PersonalE[size];
        }
    };

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApe_pat() {
        return ape_pat;
    }

    public void setApe_pat(String ape_pat) {
        this.ape_pat = ape_pat;
    }

    public String getApe_mat() {
        return ape_mat;
    }

    public void setApe_mat(String ape_mat) {
        this.ape_mat = ape_mat;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public int getId_cargo() {
        return id_cargo;
    }

    public void setId_cargo(int id_cargo) {
        this.id_cargo = id_cargo;
    }

    public int getCod_sede_operativa() {
        return cod_sede_operativa;
    }

    public void setCod_sede_operativa(int cod_sede_operativa) {
        this.cod_sede_operativa = cod_sede_operativa;
    }

    public int getCod_local_sede() {
        return cod_local_sede;
    }

    public void setCod_local_sede(int cod_local_sede) {
        this.cod_local_sede = cod_local_sede;
    }

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }

    public String getHora_ingreso() {
        return hora_ingreso;
    }

    public void setHora_ingreso(String hora_ingreso) {
        this.hora_ingreso = hora_ingreso;
    }

    public String getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public static String getApePat() {
        return APE_PAT;
    }

    public String getEstadoCambio() {
        return estadoCambio;
    }

    public void setEstadoCambio(String estadoCambio) {
        this.estadoCambio = estadoCambio;
    }

    public String getEstadoReemplazo() {
        return estadoReemplazo;
    }

    public void setEstadoReemplazo(String estadoReemplazo) {
        this.estadoReemplazo = estadoReemplazo;
    }

    public String getR_dni() {
        return r_dni;
    }

    public void setR_dni(String r_dni) {
        this.r_dni = r_dni;
    }

    public String getR_nombre_completo() {
        return r_nombre_completo;
    }

    public void setR_nombre_completo(String r_nombre_completo) {
        this.r_nombre_completo = r_nombre_completo;
    }

    public int getId_cargo_cambio() {
        return id_cargo_cambio;
    }

    public void setId_cargo_cambio(int id_cargo_cambio) {
        this.id_cargo_cambio = id_cargo_cambio;
    }

    public int getNro_aula() {
        return nro_aula;
    }

    public void setNro_aula(int nro_aula) {
        this.nro_aula = nro_aula;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getReserva() {
        return reserva;
    }

    public void setReserva(String reserva) {
        this.reserva = reserva;
    }
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

}

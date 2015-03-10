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
    public static final String ID_CARGO = "ID_CARGO";
    public static final String COD_SEDE_OPERATIVA = "cod_sede_operativa";
    public static final String COD_LOCAL_SEDE = "cod_local_sede";
    public static final String ASISTENCIA = "asistencia";
    public static final String HORA_INGRESO = "hora_ingreso";
    public static final String HORA_SALIDA = "hora_salida";
    public static final String OBSERVACIONES = "observaciones";
    public static final String CAMBIO = "cambio_tipo";
    public static final String R_DNI = "r_dni";
    public static final String R_NOMBRE_COMPLETO = "r_nombre_completo";
    public static final String ID_CARGO_CAMBIO = "id_cargo_cambio";

    private String dni;
    private String ape_pat ;
    private String ape_mat;
    private String nombres;
    private String nombre_completo;
    private int id_cargo;
    private int cod_sede_operativa;
    private int cod_local_sede;
    private int asistencia;
    private String hora_ingreso;
    private String hora_salida;
    private String observaciones;
    private String cambio;
    private String r_dni;
    private String r_nombre_completo;
    private int id_cargo_cambio;

    public PersonalE() { super();
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
        asistencia = parcel.readInt();
        hora_ingreso = parcel.readString();
        hora_salida = parcel.readString();
        observaciones = parcel.readString();
        cambio = parcel.readString();
        r_dni = parcel.readString();
        r_nombre_completo = parcel.readString();
        id_cargo_cambio = parcel.readInt();

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
        dest.writeInt(asistencia);
        dest.writeString(hora_ingreso);
        dest.writeString(hora_salida);
        dest.writeString(observaciones);
        dest.writeString(cambio);
        dest.writeString(r_dni);
        dest.writeString(r_nombre_completo);
        dest.writeInt(id_cargo_cambio);

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

    public int getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(int asistencia) {
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

    public String getCambio() {
        return cambio;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
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
}
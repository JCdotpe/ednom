package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by OrdNael on 18/11/2014.
 */
public class DocentesE implements Parcelable {

    private DiscapacidadE discapacidadE;
    private ModalidadE modalidadE;
    private String dre_des;
    private String ugel_des;
    private String tipo_doc;
    private String nro_doc;
    private String ape_pat;
    private String ape_mat;
    private String nombres;
    private String sexo;
    private String fecha_nac;
    private Integer edad;
    private String cod_ficha;
    private String cod_cartilla;
    private Integer estado;
    private String f_registro;
    private Integer estado_aula;
    private String f_aula;
    private Integer estado_ficha;
    private String f_ficha;
    private Integer estado_cartilla;
    private String f_caritlla;
    private Integer nro_aula_cambio;

    public DocentesE() {
        super();
    }

    public DocentesE( Parcel parcel ) {

        setDiscapacidadE( (DiscapacidadE) parcel.readParcelable( DiscapacidadE.class.getClassLoader() ) );
        setModalidadE( (ModalidadE) parcel.readParcelable( ModalidadE.class.getClassLoader() ) );

        setDre_des( parcel.readString() );
        setUgel_des( parcel.readString() );
        setNro_doc( parcel.readString() );
        setApe_pat( parcel.readString() );
        setApe_mat( parcel.readString() );
        setNombres( parcel.readString() );
        setSexo( parcel.readString() );
        setFecha_nac( parcel.readString() );
        setEdad( parcel.readInt() );
        setCod_ficha( parcel.readString() );
        setCod_cartilla( parcel.readString() );
        setEstado( parcel.readInt() );
        setF_registro( parcel.readString() );
        setEstado_aula( parcel.readInt() );
        setF_aula( parcel.readString() );
        setEstado_ficha( parcel.readInt() );
        setF_ficha( parcel.readString() );
        setEstado_cartilla( parcel.readInt() );
        setF_caritlla( parcel.readString() );
        setNro_aula_cambio( parcel.readInt() );

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DocentesE> CREATOR = new Creator<DocentesE>() {
        @Override
        public DocentesE createFromParcel(Parcel source) {
            return new DocentesE( source );
        }

        @Override
        public DocentesE[] newArray(int size) {
            return new DocentesE[size];
        }
    };


    public DiscapacidadE getDiscapacidadE() {
        return discapacidadE;
    }

    public void setDiscapacidadE(DiscapacidadE discapacidadE) {
        this.discapacidadE = discapacidadE;
    }

    public ModalidadE getModalidadE() {
        return modalidadE;
    }

    public void setModalidadE(ModalidadE modalidadE) {
        this.modalidadE = modalidadE;
    }

    public String getDre_des() {
        return dre_des;
    }

    public void setDre_des(String dre_des) {
        this.dre_des = dre_des;
    }

    public String getUgel_des() {
        return ugel_des;
    }

    public void setUgel_des(String ugel_des) {
        this.ugel_des = ugel_des;
    }

    public String getTipo_doc() {
        return tipo_doc;
    }

    public void setTipo_doc(String tipo_doc) {
        this.tipo_doc = tipo_doc;
    }

    public String getNro_doc() {
        return nro_doc;
    }

    public void setNro_doc(String nro_doc) {
        this.nro_doc = nro_doc;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getCod_ficha() {
        return cod_ficha;
    }

    public void setCod_ficha(String cod_ficha) {
        this.cod_ficha = cod_ficha;
    }

    public String getCod_cartilla() {
        return cod_cartilla;
    }

    public void setCod_cartilla(String cod_cartilla) {
        this.cod_cartilla = cod_cartilla;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getF_registro() {
        return f_registro;
    }

    public void setF_registro(String f_registro) {
        this.f_registro = f_registro;
    }

    public Integer getEstado_aula() {
        return estado_aula;
    }

    public void setEstado_aula(Integer estado_aula) {
        this.estado_aula = estado_aula;
    }

    public String getF_aula() {
        return f_aula;
    }

    public void setF_aula(String f_aula) {
        this.f_aula = f_aula;
    }

    public Integer getEstado_ficha() {
        return estado_ficha;
    }

    public void setEstado_ficha(Integer estado_ficha) {
        this.estado_ficha = estado_ficha;
    }

    public String getF_ficha() {
        return f_ficha;
    }

    public void setF_ficha(String f_ficha) {
        this.f_ficha = f_ficha;
    }

    public Integer getEstado_cartilla() {
        return estado_cartilla;
    }

    public void setEstado_cartilla(Integer estado_cartilla) {
        this.estado_cartilla = estado_cartilla;
    }

    public String getF_caritlla() {
        return f_caritlla;
    }

    public void setF_caritlla(String f_caritlla) {
        this.f_caritlla = f_caritlla;
    }

    public Integer getNro_aula_cambio() {
        return nro_aula_cambio;
    }

    public void setNro_aula_cambio(Integer nro_aula_cambio) {
        this.nro_aula_cambio = nro_aula_cambio;
    }
}
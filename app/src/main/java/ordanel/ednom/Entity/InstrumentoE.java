package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Leandro on 21/11/2014.
 */
public class InstrumentoE implements Parcelable {

    // table instrumento
    public static final String ID_INST = "id_inst";
    public static final String COD_FICHA = "cod_ficha";
    public static final String COD_CARTILLA = "cod_cartilla";
    public static final String NRO_AULA = "nro_aula";
    public static final String ESTADO_FICHA = "estado_ficha";
    public static final String F_FICHA = "f_ficha";
    public static final String ESTADO_CARTILLA = "estado_cartilla";
    public static final String F_CARTILLA = "f_cartilla";
    // .table instrumento

    private Integer id_inst;
    private LocalE localE;
    private String cod_ficha;
    private String cod_cartilla;
    private Integer nro_aula;
    private Integer estado_ficha;
    private String f_ficha;
    private Integer estado_cartilla;
    private String f_cartilla;
    private Integer status;

    public InstrumentoE() {
        super();
    }

    public InstrumentoE( Parcel parcel ) {

        id_inst = parcel.readInt();
        localE = parcel.readParcelable( LocalE.class.getClassLoader() );
        cod_ficha = parcel.readString();
        cod_cartilla = parcel.readString();
        nro_aula = parcel.readInt();
        estado_ficha = parcel.readInt();
        f_ficha = parcel.readString();
        estado_cartilla = parcel.readInt();
        f_cartilla = parcel.readString();
        status = parcel.readInt();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( id_inst );
        dest.writeParcelable( localE, flags );
        dest.writeString( cod_ficha );
        dest.writeString( cod_cartilla );
        dest.writeInt( nro_aula );
        dest.writeInt( estado_ficha );
        dest.writeString( f_ficha );
        dest.writeInt( estado_cartilla );
        dest.writeString( f_cartilla );
        dest.writeInt( status );

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InstrumentoE> CREATOR = new Creator<InstrumentoE>() {
        @Override
        public InstrumentoE createFromParcel(Parcel source) {
            return new InstrumentoE( source );
        }

        @Override
        public InstrumentoE[] newArray(int size) {
            return new InstrumentoE[size];
        }
    };

    public Integer getId_inst() {
        return id_inst;
    }

    public void setId_inst(Integer id_inst) {
        this.id_inst = id_inst;
    }

    public LocalE getLocalE() {
        return localE;
    }

    public void setLocalE(LocalE localE) {
        this.localE = localE;
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

    public Integer getNro_aula() {
        return nro_aula;
    }

    public void setNro_aula(Integer nro_aula) {
        this.nro_aula = nro_aula;
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

    public String getF_cartilla() {
        return f_cartilla;
    }

    public void setF_cartilla(String f_cartilla) {
        this.f_cartilla = f_cartilla;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

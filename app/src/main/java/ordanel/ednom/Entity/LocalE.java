package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by OrdNael on 17/11/2014.
 */
public class LocalE implements Parcelable {

    private SedeOperativaE sedeOperativaE;
    private Integer cod_local_sede;
    private String nombreLocal;
    private String direccion;
    private Integer naula_t;
    private Integer naula_n;
    private Integer naula_discapacidad;
    private Integer naula_contingencia;
    private Integer nficha;
    private Integer ncartilla;

    public LocalE() {
        super();
    }

    public LocalE( Parcel parcel ) {

        setSedeOperativaE( (SedeOperativaE) parcel.readParcelable( SedeOperativaE.class.getClassLoader() ) );
        setCod_local_sede( parcel.readInt() );
        setNombreLocal( parcel.readString() );
        setDireccion( parcel.readString() );
        setNaula_t( parcel.readInt() );
        setNaula_n( parcel.readInt() );
        setNaula_discapacidad( parcel.readInt() );
        setNaula_contingencia( parcel.readInt() );
        setNficha( parcel.readInt() );
        setNcartilla( parcel.readInt() );

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable( getSedeOperativaE(), flags );
        dest.writeInt( getCod_local_sede() );
        dest.writeString( getNombreLocal() );
        dest.writeString( getDireccion() );
        dest.writeInt( getNaula_t() );
        dest.writeInt( getNaula_n() );
        dest.writeInt( getNaula_discapacidad() );
        dest.writeInt( getNaula_contingencia() );
        dest.writeInt( getNficha() );
        dest.writeInt( getNcartilla() );

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocalE> CREATOR = new Creator<LocalE>() {
        @Override
        public LocalE createFromParcel(Parcel source) {
            return new LocalE( source );
        }

        @Override
        public LocalE[] newArray(int size) {
            return new LocalE[size];
        }
    };

    public SedeOperativaE getSedeOperativaE() {
        return sedeOperativaE;
    }

    public void setSedeOperativaE(SedeOperativaE sedeOperativaE) {
        this.sedeOperativaE = sedeOperativaE;
    }

    public Integer getCod_local_sede() {
        return cod_local_sede;
    }

    public void setCod_local_sede(Integer cod_local_sede) {
        this.cod_local_sede = cod_local_sede;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getNaula_t() {
        return naula_t;
    }

    public void setNaula_t(Integer naula_t) {
        this.naula_t = naula_t;
    }

    public Integer getNaula_n() {
        return naula_n;
    }

    public void setNaula_n(Integer naula_n) {
        this.naula_n = naula_n;
    }

    public Integer getNaula_discapacidad() {
        return naula_discapacidad;
    }

    public void setNaula_discapacidad(Integer naula_discapacidad) {
        this.naula_discapacidad = naula_discapacidad;
    }

    public Integer getNaula_contingencia() {
        return naula_contingencia;
    }

    public void setNaula_contingencia(Integer naula_contingencia) {
        this.naula_contingencia = naula_contingencia;
    }

    public Integer getNficha() {
        return nficha;
    }

    public void setNficha(Integer nficha) {
        this.nficha = nficha;
    }

    public Integer getNcartilla() {
        return ncartilla;
    }

    public void setNcartilla(Integer ncartilla) {
        this.ncartilla = ncartilla;
    }

}
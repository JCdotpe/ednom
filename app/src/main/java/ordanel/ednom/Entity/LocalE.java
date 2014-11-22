package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

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
    private List<UsuarioLocalE> usuarioLocalEList;
    private Integer status;

    public LocalE() {
        super();
    }

    public LocalE( Parcel parcel ) {

        sedeOperativaE = parcel.readParcelable( SedeOperativaE.class.getClassLoader() );
        nombreLocal = parcel.readString();
        direccion = parcel.readString();
        naula_t = parcel.readInt();
        naula_n = parcel.readInt();
        naula_discapacidad = parcel.readInt();
        naula_contingencia = parcel.readInt();
        nficha = parcel.readInt();
        ncartilla = parcel.readInt();
        usuarioLocalEList = new ArrayList<UsuarioLocalE>();
        parcel.readTypedList( usuarioLocalEList, UsuarioLocalE.CREATOR );
        status = parcel.readInt();


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable( sedeOperativaE, flags );
        dest.writeString( nombreLocal );
        dest.writeString( direccion );
        dest.writeInt( naula_t );
        dest.writeInt( naula_n );
        dest.writeInt( naula_discapacidad );
        dest.writeInt( naula_contingencia );
        dest.writeInt( nficha );
        dest.writeInt( ncartilla );
        dest.writeTypedList( usuarioLocalEList );
        dest.writeInt(status);

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

    public List<UsuarioLocalE> getUsuarioLocalEList() {
        return usuarioLocalEList;
    }

    public void setUsuarioLocalEList(List<UsuarioLocalE> usuarioLocalEList) {
        this.usuarioLocalEList = usuarioLocalEList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
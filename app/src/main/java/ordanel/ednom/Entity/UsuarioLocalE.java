package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by OrdNael on 06/11/2014.
 */
public class UsuarioLocalE implements Parcelable {

    private Integer idUsuario;
    private String usuario;
    private String clave;
    private Integer rol;
    private Integer nro_local;
    private String nombreLocal;
    private String naulas;
    private Integer ncontingencia;
    private String sede;

    public UsuarioLocalE() {
        super();
    }

    public UsuarioLocalE( Parcel parcel ) {

        idUsuario = parcel.readInt();
        usuario = parcel.readString();
        clave = parcel.readString();
        rol = parcel.readInt();
        nro_local = parcel.readInt();
        nombreLocal = parcel.readString();
        naulas = parcel.readString();
        ncontingencia = parcel.readInt();
        sede = parcel.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( idUsuario );
        dest.writeString( usuario );
        dest.writeString( clave );
        dest.writeInt( rol );
        dest.writeInt( nro_local );
        dest.writeString( nombreLocal );
        dest.writeString( naulas );
        dest.writeInt( ncontingencia );
        dest.writeString( sede );

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UsuarioLocalE> CREATOR = new Creator<UsuarioLocalE>() {
        @Override
        public UsuarioLocalE createFromParcel(Parcel source) {
            return new UsuarioLocalE( source );
        }

        @Override
        public UsuarioLocalE[] newArray(int size) {
            return new UsuarioLocalE[size];
        }
    };


    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Integer getRol() {
        return rol;
    }

    public void setRol(Integer rol) {
        this.rol = rol;
    }

    public Integer getNro_local() {
        return nro_local;
    }

    public void setNro_local(Integer nro_local) {
        this.nro_local = nro_local;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getNaulas() {
        return naulas;
    }

    public void setNaulas(String naulas) {
        this.naulas = naulas;
    }

    public Integer getNcontingencia() {
        return ncontingencia;
    }

    public void setNcontingencia(Integer ncontingencia) {
        this.ncontingencia = ncontingencia;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }
}

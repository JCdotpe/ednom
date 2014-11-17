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
    private LocalE localE = new LocalE();

    public UsuarioLocalE() {
        super();
    }

    public UsuarioLocalE( Parcel parcel ) {

        setIdUsuario( parcel.readInt() );
        setUsuario( parcel.readString() );
        setClave( parcel.readString() );
        setRol( parcel.readInt() );
        setLocalE( (LocalE) parcel.readParcelable( LocalE.class.getClassLoader() )  );

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( getIdUsuario() );
        dest.writeString( getUsuario() );
        dest.writeString( getClave() );
        dest.writeInt( getRol() );
        dest.writeParcelable( getLocalE(), flags );

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

    public LocalE getLocalE() {
        return localE;
    }

    public void setLocalE(LocalE localE) {
        this.localE = localE;
    }
}
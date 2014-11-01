package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronE implements Parcelable {

    private Integer Codigo;
    private String Sede;
    private Integer NroLocal;
    private String LocalAplicacion;
    private String Aula;
    private String NumDoc;
    private String ApePaterno;
    private String ApeMaterno;
    private String Nombres;
    private Integer Status;


    public PadronE() {
        super();
    }

    public PadronE( Parcel parcel) {

        setCodigo( parcel.readInt() );
        setSede( parcel.readString() );
        setNroLocal( parcel.readInt() );
        setLocalAplicacion( parcel.readString() );
        setAula( parcel.readString() );
        setNumDoc( parcel.readString() );
        setApePaterno( parcel.readString() );
        setApeMaterno( parcel.readString() );
        setNombres( parcel.readString() );
        setStatus( parcel.readInt() );

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( getCodigo() );
        dest.writeString( getSede() );
        dest.writeInt( getNroLocal() );
        dest.writeString( getLocalAplicacion() );
        dest.writeString( getAula() );
        dest.writeString( getNumDoc() );
        dest.writeString( getApePaterno() );
        dest.writeString( getApeMaterno() );
        dest.writeString( getNombres() );
        dest.writeInt( getStatus() );

    }

    public static final Creator<PadronE> CREATOR = new Creator<PadronE>() {
        @Override
        public PadronE createFromParcel(Parcel source) {
            return new PadronE( source );
        }

        @Override
        public PadronE[] newArray(int size) {
            return new PadronE[size];
        }
    };


    public Integer getCodigo() {
        return Codigo;
    }

    public void setCodigo(Integer codigo) {
        Codigo = codigo;
    }

    public String getSede() {
        return Sede;
    }

    public void setSede(String sede) {
        Sede = sede;
    }

    public Integer getNroLocal() {
        return NroLocal;
    }

    public void setNroLocal(Integer nroLocal) {
        NroLocal = nroLocal;
    }

    public String getLocalAplicacion() {
        return LocalAplicacion;
    }

    public void setLocalAplicacion(String localAplicacion) {
        LocalAplicacion = localAplicacion;
    }

    public String getAula() {
        return Aula;
    }

    public void setAula(String aula) {
        Aula = aula;
    }

    public String getNumDoc() {
        return NumDoc;
    }

    public void setNumDoc(String numDoc) {
        NumDoc = numDoc;
    }

    public String getApePaterno() {
        return ApePaterno;
    }

    public void setApePaterno(String apePaterno) {
        ApePaterno = apePaterno;
    }

    public String getApeMaterno() {
        return ApeMaterno;
    }

    public void setApeMaterno(String apeMaterno) {
        ApeMaterno = apeMaterno;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }
}

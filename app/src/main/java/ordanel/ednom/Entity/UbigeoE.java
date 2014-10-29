package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Leandro on 27/10/2014.
 */
public class UbigeoE implements Parcelable {

    private String Departamento;
    private String Provincia;
    private String Distrito;
    private String Local;


    public UbigeoE() {
        super();
    }

    public UbigeoE(Parcel parcel) {
        Departamento = parcel.readString();
        Provincia = parcel.readString();
        Distrito = parcel.readString();
        Local = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString( Departamento );
        dest.writeString( Provincia );
        dest.writeString( Distrito );
        dest.writeString( Local );

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UbigeoE> CREATOR = new Creator<UbigeoE>() {
        @Override
        public UbigeoE createFromParcel( Parcel source ) {
            return new UbigeoE( source );
        }

        @Override
        public UbigeoE[] newArray( int size ) {
            return new UbigeoE[size];
        }
    };



    public String getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(String departamento) {
        Departamento = departamento;
    }


    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String provincia) {
        Provincia = provincia;
    }


    public String getDistrito() {
        return Distrito;
    }

    public void setDistrito(String distrito) {
        Distrito = distrito;
    }


    public String getLocal() {
        return Local;
    }

    public void setLocal(String local) {
        Local = local;
    }

}
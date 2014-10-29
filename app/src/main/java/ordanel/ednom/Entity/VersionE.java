package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by OrdNael on 29/10/2014.
 */
public class VersionE implements Parcelable {

    private Integer idVersion;
    private String Descripcion;

    public VersionE() {
        super();
    }

    public VersionE(Parcel parcel) {

        setIdVersion(parcel.readInt());
        setDescripcion(parcel.readString());

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(getIdVersion());
        dest.writeString(getDescripcion());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VersionE> CREATOR = new Creator<VersionE>() {
        @Override
        public VersionE createFromParcel(Parcel source) {
            return new VersionE( source );
        }

        @Override
        public VersionE[] newArray(int size) {
            return new VersionE[size];
        }
    };


    public Integer getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(Integer idVersion) {
        this.idVersion = idVersion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
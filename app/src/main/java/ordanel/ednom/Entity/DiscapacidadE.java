package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by OrdNael on 18/11/2014.
 */
public class DiscapacidadE implements Parcelable {

    private Integer cod_discap;
    private String discapacidad;

    public DiscapacidadE() {
        super();
    }

    public DiscapacidadE( Parcel parcel ) {

        setCod_discap( parcel.readInt() );
        setDiscapacidad( parcel.readString() );

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( getCod_discap() );
        dest.writeString( getDiscapacidad() );

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public final static Creator<DiscapacidadE> CREATOR = new Creator<DiscapacidadE>() {
        @Override
        public DiscapacidadE createFromParcel(Parcel source) {
            return new DiscapacidadE( source );
        }

        @Override
        public DiscapacidadE[] newArray(int size) {
            return new DiscapacidadE[size];
        }
    };

    public Integer getCod_discap() {
        return cod_discap;
    }

    public void setCod_discap(Integer cod_discap) {
        this.cod_discap = cod_discap;
    }

    public String getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(String discapacidad) {
        this.discapacidad = discapacidad;
    }
}
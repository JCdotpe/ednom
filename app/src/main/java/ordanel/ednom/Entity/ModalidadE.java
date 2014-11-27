package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by OrdNael on 18/11/2014.
 */
public class ModalidadE implements Parcelable {

    // table modalidad
    public static final String COD_MODAL = "cod_modal";
    public static final String MODALIDAD = "modalidad";
    // .table modalidad

    private Integer cod_modal;
    private String modalidad;

    public ModalidadE() {
        super();
    }

    public ModalidadE( Parcel parcel ) {

        setCod_modal( parcel.readInt() );
        setModalidad( parcel.readString() );

    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( getCod_modal() );
        dest.writeString( getModalidad() );

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModalidadE> CREATOR = new Creator<ModalidadE>() {
        @Override
        public ModalidadE createFromParcel(Parcel source) {
            return new ModalidadE( source );
        }

        @Override
        public ModalidadE[] newArray(int size) {
            return new ModalidadE[size];
        }
    };

    public Integer getCod_modal() {
        return cod_modal;
    }

    public void setCod_modal(Integer cod_modal) {
        this.cod_modal = cod_modal;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }
}
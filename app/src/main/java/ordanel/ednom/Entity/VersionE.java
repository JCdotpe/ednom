package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by OrdNael on 29/10/2014.
 */
public class VersionE implements Parcelable {

    // table version
    public static final String VERCOD = "vercod";
    public static final String V_PADRON = "v_padron";
    public static final String V_SISTEM = "v_sistem";
    public static final String FECHA = "fecha";
    public static final String OBSERVA = "observa";
    // .table version

    private Integer vercod;
    private Integer v_padron;
    private Integer v_sistem;
    private String fecha;
    private String observa;
    private Integer status;

    public VersionE() {
        super();
    }

    public VersionE(Parcel parcel) {

        vercod = parcel.readInt();
        v_padron = parcel.readInt();
        v_sistem = parcel.readInt();
        fecha = parcel.readString();
        observa = parcel.readString();
        status = parcel.readInt();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( vercod );
        dest.writeInt( v_padron );
        dest.writeInt( v_sistem );
        dest.writeString( fecha );
        dest.writeString( observa );
        dest.writeInt( status );

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


    public Integer getVercod() {
        return vercod;
    }

    public void setVercod(Integer vercod) {
        this.vercod = vercod;
    }

    public Integer getV_padron() {
        return v_padron;
    }

    public void setV_padron(Integer v_padron) {
        this.v_padron = v_padron;
    }

    public Integer getV_sistem() {
        return v_sistem;
    }

    public void setV_sistem(Integer v_sistem) {
        this.v_sistem = v_sistem;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getObserva() {
        return observa;
    }

    public void setObserva(String observa) {
        this.observa = observa;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cr-Diego on 12/03/2015.
 */
public class CargoE implements Parcelable {

    public static final String IDCARGO = "id_cargo";
    public static final String CARGO = "cargo";
    public static final String CARGORES = "cargo_res";

    private int idCargo;
    private String cargo;
    private String cargoRes;

    public CargoE() { super();
    }

    public CargoE(Parcel parcel) {
        idCargo = parcel.readInt();
        cargo = parcel.readString();
        cargoRes = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCargo);
        dest.writeString(cargo);
        dest.writeString(cargoRes);
    }

    public static final Creator<CargoE> CREATOR = new Creator<CargoE>() {
        @Override
        public CargoE createFromParcel(Parcel source) {
            return new CargoE(source);
        }

        @Override
        public CargoE[] newArray(int size) {
            return new CargoE[size];
        }
    };

    public String getCargoRes() {
        return cargoRes;
    }

    public void setCargoRes(String cargoRes) {
        this.cargoRes = cargoRes;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }
}

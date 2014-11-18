package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OrdNael on 17/11/2014.
 */
public class SedeOperativaE implements Parcelable {

    private Integer cod_sede_operativa;
    private String sede_operativa;
    private List<LocalE> localEList;


    public SedeOperativaE() {
        super();
    }

    public SedeOperativaE( Parcel parcel )
    {
        setCod_sede_operativa( parcel.readInt() );
        setSede_operativa( parcel.readString() );
        setLocalEList( new ArrayList<LocalE>() );
        parcel.readTypedList( getLocalEList(), LocalE.CREATOR );
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( getCod_sede_operativa() );
        dest.writeString( getSede_operativa() );
        dest.writeTypedList( getLocalEList() );

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SedeOperativaE> CREATOR = new Creator<SedeOperativaE>() {
        @Override
        public SedeOperativaE createFromParcel(Parcel source) {
            return new SedeOperativaE( source );
        }

        @Override
        public SedeOperativaE[] newArray(int size) {
            return new SedeOperativaE[size];
        }
    };

    public Integer getCod_sede_operativa() {
        return cod_sede_operativa;
    }

    public void setCod_sede_operativa(Integer cod_sede_operativa) {
        this.cod_sede_operativa = cod_sede_operativa;
    }

    public String getSede_operativa() {
        return sede_operativa;
    }

    public void setSede_operativa(String sede_operativa) {
        this.sede_operativa = sede_operativa;
    }

    public List<LocalE> getLocalEList() {
        return localEList;
    }

    public void setLocalEList(List<LocalE> localEList) {
        this.localEList = localEList;
    }
}

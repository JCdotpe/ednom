package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronE implements Parcelable {

    private List<AulaLocalE> aulaLocalEList = new ArrayList<AulaLocalE>();
    private List<DocentesE> docentesEList = new ArrayList<DocentesE>();
    private List<DiscapacidadE> discapacidadEList = new ArrayList<DiscapacidadE>();
    private List<ModalidadE> modalidadEList = new ArrayList<ModalidadE>();
    private List<InstrumentoE> instrumentoEList = new ArrayList<InstrumentoE>();
    private Integer status;

    public PadronE() {
        super();
    }

    public PadronE( Parcel parcel) {

        parcel.readTypedList( aulaLocalEList, AulaLocalE.CREATOR );
        parcel.readTypedList( docentesEList, DocentesE.CREATOR );
        parcel.readTypedList( discapacidadEList, DiscapacidadE.CREATOR );
        parcel.readTypedList( modalidadEList, ModalidadE.CREATOR );
        parcel.readTypedList( instrumentoEList, InstrumentoE.CREATOR );
        status = parcel.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeTypedList(aulaLocalEList);
        dest.writeTypedList( docentesEList );
        dest.writeTypedList( discapacidadEList );
        dest.writeTypedList( modalidadEList );
        dest.writeTypedList(instrumentoEList);
        dest.writeInt( status );

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

    public List<AulaLocalE> getAulaLocalEList() {
        return aulaLocalEList;
    }

    public void setAulaLocalEList(List<AulaLocalE> aulaLocalEList) {
        this.aulaLocalEList = aulaLocalEList;
    }

    public List<DocentesE> getDocentesEList() {
        return docentesEList;
    }

    public void setDocentesEList(List<DocentesE> docentesEList) {
        this.docentesEList = docentesEList;
    }

    public List<DiscapacidadE> getDiscapacidadEList() {
        return discapacidadEList;
    }

    public void setDiscapacidadEList(List<DiscapacidadE> discapacidadEList) {
        this.discapacidadEList = discapacidadEList;
    }

    public List<ModalidadE> getModalidadEList() {
        return modalidadEList;
    }

    public void setModalidadEList(List<ModalidadE> modalidadEList) {
        this.modalidadEList = modalidadEList;
    }

    public List<InstrumentoE> getInstrumentoEList() {
        return instrumentoEList;
    }

    public void setInstrumentoEList(List<InstrumentoE> instrumentoEList) {
        this.instrumentoEList = instrumentoEList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
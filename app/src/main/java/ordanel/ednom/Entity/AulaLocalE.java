package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OrdNael on 18/11/2014.
 */
public class AulaLocalE implements Parcelable {

    // table aula_local
    public static final String NRO_AULA = "nro_aula";
    public static final String TIPO = "tipo";
    public static final String CANT_DOCENTE = "cant_docente";
    // .table aula_local

    private LocalE localE;
    private Integer nro_aula;
    private String tipo;
    private Integer cant_docente;
    private List<DocentesE> docentesEList;


    public AulaLocalE() {
        super();
    }

    public AulaLocalE( Parcel parcel ) {

        localE = parcel.readParcelable( LocalE.class.getClassLoader() );
        nro_aula = parcel.readInt();
        tipo = parcel.readString();
        cant_docente = parcel.readInt();
        docentesEList = new ArrayList<DocentesE>();
        parcel.readTypedList( docentesEList, DocentesE.CREATOR );

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable( localE, flags );
        dest.writeInt( nro_aula );
        dest.writeString( tipo );
        dest.writeInt( cant_docente );
        dest.writeTypedList( docentesEList );

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AulaLocalE> CREATOR = new Creator<AulaLocalE>() {
        @Override
        public AulaLocalE createFromParcel(Parcel source) {
            return new AulaLocalE( source );
        }

        @Override
        public AulaLocalE[] newArray(int size) {
            return new AulaLocalE[size];
        }
    };

    public LocalE getLocalE() {
        return localE;
    }

    public void setLocalE(LocalE localE) {
        this.localE = localE;
    }

    public Integer getNro_aula() {
        return nro_aula;
    }

    public void setNro_aula(Integer nro_aula) {
        this.nro_aula = nro_aula;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getCant_docente() {
        return cant_docente;
    }

    public void setCant_docente(Integer cant_docente) {
        this.cant_docente = cant_docente;
    }

    public List<DocentesE> getDocentesEList() {
        return docentesEList;
    }

    public void setDocentesEList(List<DocentesE> docentesEList) {
        this.docentesEList = docentesEList;
    }
}

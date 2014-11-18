package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by OrdNael on 18/11/2014.
 */
public class AulaLocalE implements Parcelable {

    private LocalE localE;
    private Integer nro_aula;
    private String tipo;
    private Integer cant_docente;


    public AulaLocalE() {
        super();
    }

    public AulaLocalE( Parcel parcel ) {

        setLocalE( (LocalE) parcel.readParcelable( LocalE.class.getClassLoader() ) );
        setNro_aula( parcel.readInt() );
        setTipo( parcel.readString() );
        setCant_docente( parcel.readInt() );

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable( getLocalE(), flags );
        dest.writeInt( getNro_aula() );
        dest.writeString( getTipo() );
        dest.writeInt( getCant_docente() );

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
}

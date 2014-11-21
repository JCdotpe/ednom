package ordanel.ednom.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronE implements Parcelable {

    private List<AulaLocalE> aulaLocalEList = new ArrayList<AulaLocalE>();
    private List<DocentesE> docentesEList = new ArrayList<DocentesE>();
    private List<DiscapacidadE> discapacidadEList = new ArrayList<DiscapacidadE>();
    private List<ModalidadE> modalidadEList = new ArrayList<ModalidadE>();
    private Integer status;

    /*private Integer codigo;
    private String sede;
    private Integer nro_local;
    private String local_aplicacion;
    private String aula;
    private String ins_numdoc;
    private String apepat;
    private String apemat;
    private String nombres;
    private Integer estatus;
    private Date fecha_registro;
    private Integer s_aula;
    private Date f_aula;
    private Integer s_ficha;
    private Date f_ficha;
    private Integer s_cartilla;
    private Date f_cartilla;
    private Integer id_local;
    private Integer id_aula;
    private String direccion;
    private String codFicha;
    private String codCartilla;
    private String aula_ficha;
    private String aula_cartilla;
    private String sf_cartilla;
    private String sf_aula;
    private String sf_ficha;
    private String sfecha_registro;
    private String new_aula;
    private String new_local;
    private Integer cant_ficha;
    private String tipo;*/

    public PadronE() {
        super();
    }

    public PadronE( Parcel parcel) {

        parcel.readTypedList( aulaLocalEList, AulaLocalE.CREATOR );
        parcel.readTypedList( docentesEList, DocentesE.CREATOR );
        parcel.readTypedList( discapacidadEList, DiscapacidadE.CREATOR );
        parcel.readTypedList( modalidadEList, ModalidadE.CREATOR );
        status = parcel.readInt();

        /*codigo = parcel.readInt();
        sede = parcel.readString();
        nro_local = parcel.readInt();
        local_aplicacion = parcel.readString();
        aula = parcel.readString();
        ins_numdoc = parcel.readString();
        apepat = parcel.readString();
        apemat = parcel.readString();
        nombres = parcel.readString();
        estatus = parcel.readInt();
        fecha_registro = (Date) parcel.readSerializable();
        s_aula = parcel.readInt();
        f_aula = (Date) parcel.readSerializable();
        s_ficha = parcel.readInt();
        f_ficha = (Date) parcel.readSerializable();
        s_cartilla = parcel.readInt();
        f_cartilla = (Date) parcel.readSerializable();
        id_local = parcel.readInt();
        id_aula = parcel.readInt();
        direccion = parcel.readString();
        codFicha = parcel.readString();
        codCartilla = parcel.readString();
        aula_ficha = parcel.readString();
        aula_cartilla = parcel.readString();
        sf_cartilla = parcel.readString();
        sf_aula = parcel.readString();
        sf_ficha = parcel.readString();
        new_aula = parcel.readString();
        new_local = parcel.readString();
        cant_ficha = parcel.readInt();
        tipo = parcel.readString();*/

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
        dest.writeInt( status );

        /*dest.writeInt( codigo );
        dest.writeString( sede );
        dest.writeInt( nro_local );
        dest.writeString( local_aplicacion );
        dest.writeString( aula );
        dest.writeString( ins_numdoc );
        dest.writeString( apepat );
        dest.writeString( apemat );
        dest.writeString( nombres );
        dest.writeInt( estatus );
        dest.writeSerializable( fecha_registro );
        dest.writeInt( s_aula );
        dest.writeSerializable( f_aula );
        dest.writeInt( s_ficha );
        dest.writeSerializable( f_ficha );
        dest.writeInt( s_cartilla );
        dest.writeSerializable( f_cartilla );
        dest.writeInt( id_local );
        dest.writeInt( id_aula );
        dest.writeString( direccion );
        dest.writeString( codFicha );
        dest.writeString( codCartilla );
        dest.writeString( aula_ficha );
        dest.writeString( aula_cartilla );
        dest.writeString( sf_cartilla );
        dest.writeString( sf_aula );
        dest.writeString( sf_ficha );
        dest.writeString( new_aula );
        dest.writeString( new_local );
        dest.writeInt( cant_ficha );
        dest.writeString( tipo );*/

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /*public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public Integer getNro_local() {
        return nro_local;
    }

    public void setNro_local(Integer nro_local) {
        this.nro_local = nro_local;
    }

    public String getLocal_aplicacion() {
        return local_aplicacion;
    }

    public void setLocal_aplicacion(String local_aplicacion) {
        this.local_aplicacion = local_aplicacion;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getIns_numdoc() {
        return ins_numdoc;
    }

    public void setIns_numdoc(String ins_numdoc) {
        this.ins_numdoc = ins_numdoc;
    }

    public String getApepat() {
        return apepat;
    }

    public void setApepat(String apepat) {
        this.apepat = apepat;
    }

    public String getApemat() {
        return apemat;
    }

    public void setApemat(String apemat) {
        this.apemat = apemat;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public Integer getS_aula() {
        return s_aula;
    }

    public void setS_aula(Integer s_aula) {
        this.s_aula = s_aula;
    }

    public Date getF_aula() {
        return f_aula;
    }

    public void setF_aula(Date f_aula) {
        this.f_aula = f_aula;
    }

    public Integer getS_ficha() {
        return s_ficha;
    }

    public void setS_ficha(Integer s_ficha) {
        this.s_ficha = s_ficha;
    }

    public Date getF_ficha() {
        return f_ficha;
    }

    public void setF_ficha(Date f_ficha) {
        this.f_ficha = f_ficha;
    }

    public Integer getS_cartilla() {
        return s_cartilla;
    }

    public void setS_cartilla(Integer s_cartilla) {
        this.s_cartilla = s_cartilla;
    }

    public Date getF_cartilla() {
        return f_cartilla;
    }

    public void setF_cartilla(Date f_cartilla) {
        this.f_cartilla = f_cartilla;
    }

    public Integer getId_local() {
        return id_local;
    }

    public void setId_local(Integer id_local) {
        this.id_local = id_local;
    }

    public Integer getId_aula() {
        return id_aula;
    }

    public void setId_aula(Integer id_aula) {
        this.id_aula = id_aula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodFicha() {
        return codFicha;
    }

    public void setCodFicha(String codFicha) {
        this.codFicha = codFicha;
    }

    public String getCodCartilla() {
        return codCartilla;
    }

    public void setCodCartilla(String codCartilla) {
        this.codCartilla = codCartilla;
    }

    public String getAula_ficha() {
        return aula_ficha;
    }

    public void setAula_ficha(String aula_ficha) {
        this.aula_ficha = aula_ficha;
    }

    public String getAula_cartilla() {
        return aula_cartilla;
    }

    public void setAula_cartilla(String aula_cartilla) {
        this.aula_cartilla = aula_cartilla;
    }

    public String getSf_cartilla() {
        return sf_cartilla;
    }

    public void setSf_cartilla(String sf_cartilla) {
        this.sf_cartilla = sf_cartilla;
    }

    public String getSf_aula() {
        return sf_aula;
    }

    public void setSf_aula(String sf_aula) {
        this.sf_aula = sf_aula;
    }

    public String getSf_ficha() {
        return sf_ficha;
    }

    public void setSf_ficha(String sf_ficha) {
        this.sf_ficha = sf_ficha;
    }

    public String getSfecha_registro() {
        return sfecha_registro;
    }

    public void setSfecha_registro(String sfecha_registro) {
        this.sfecha_registro = sfecha_registro;
    }

    public String getNew_aula() {
        return new_aula;
    }

    public void setNew_aula(String new_aula) {
        this.new_aula = new_aula;
    }

    public String getNew_local() {
        return new_local;
    }

    public void setNew_local(String new_local) {
        this.new_local = new_local;
    }

    public Integer getCant_ficha() {
        return cant_ficha;
    }

    public void setCant_ficha(Integer cant_ficha) {
        this.cant_ficha = cant_ficha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }*/

}
package ordanel.ednom.Library;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OrdNael on 18/11/2014.
 */
public class ConstantsUtils {

    public static final String IP_SERVER = "webinei.inei.gob.pe"; //"172.16.100.61" "jc.pe" "webinei.inei.gob.pe"
    public static final String BASE_URL = "http://" + IP_SERVER + "/oted/monitoreo/tablet-";// "/portafolio/ednom/" "/portafolio/isi/" "/oted/monitoreo/tablet-", "/ednom2015i/tablet-"
    public static final String URL_PADRON = BASE_URL + "padron";// "padron.php" "padron"
    public static final String URL_ACCESS = BASE_URL + "logeo"; // "acces.php" "logeo"
    public static final String URL_VERSION = BASE_URL + "version";// "version.php" "version"
    public static final String URL_SYNC = BASE_URL + "recibirpadron"; // "sync.php" "recibirPadron"
    public static final String PARAM_LOGIN = "sendPass"; // "password" "sendPass"
    public static final String URL_REPORTE = "http://webinei.inei.gob.pe/oted/monitoreo";
    public static final String URL_REPORTE_ACCESS = URL_REPORTE + "login";
    public static final String URL_REPORTE_SEARCH_DOCENTE = URL_REPORTE + "/search-docente";
    public static final String URL_SEARCH = URL_REPORTE + "/login";
    public static final String URL_REPORTE_SEARCH_PERSONAL = URL_REPORTE + "/search-personal";
    public static String getPass = "pass";
    public static int getRol = 0;

    public static String fecha_registro() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);

    }

}
package ordanel.ednom.Library;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OrdNael on 18/11/2014.
 */
public class ConstantsUtils {

    public static final String IP_SERVER = "jc.pe";
    public static final String BASE_URL = "http://" + IP_SERVER + "/portafolio/isi/";// "/portafolio/ednom/" "/portafolio/isi/"
    public static final String URL_PADRON = BASE_URL + "padron";// "padron.php" "padron"
    public static final String URL_ACCESS = BASE_URL + "logeo"; // "acces.php" "logeo"
    public static final String URL_VERSION = BASE_URL + "version";// "version.php" "version"
    public static final String URL_SYNC = BASE_URL + "recibirPadron"; // "sync.php" "recibirPadron"

    public static String fecha_registro() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);

    }

}
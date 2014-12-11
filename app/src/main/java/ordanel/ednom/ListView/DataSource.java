package ordanel.ednom.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OrdNael on 11/12/2014.
 */
public class DataSource {

    // singleton pattern
    private static DataSource dataSource = null;

    private List<String> data = null;
    private static final int SIZE = 74;

    public static DataSource getInstance() {

        if ( dataSource == null )
        {
            dataSource = new DataSource();
        }

        return dataSource;
    }

    private DataSource() {

        data = new ArrayList<>(SIZE);

        for ( int i = 1; i <= SIZE; i++ )
        {
            data.add( "row" + i );
        }

    }

    public int getSize() {
        return SIZE;
    }

    public List<String> getData( int offset, int limit ) {

        List<String> newList = new ArrayList<>(limit);
        int end = offset + limit;

        if ( end > data.size() )
        {
            end = data.size();
        }

        newList.addAll( data.subList( offset, end ) );

        return newList;

    }

}
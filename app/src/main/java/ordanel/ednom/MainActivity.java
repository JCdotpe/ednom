package ordanel.ednom;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ordanel.ednom.Asyncs.LocalAsync;
import ordanel.ednom.Entity.PadronE;
import ordanel.ednom.Fragments.AsistenciaAula;
import ordanel.ednom.Fragments.IngresoLocal;
import ordanel.ednom.Interfaces.LocalI;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, LocalI {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;

        switch ( position )
        {
            case 0:
                fragment = new IngresoLocal().newInstance( position + 1 );
                break;

            case 1:
                fragment = new AsistenciaAula().newInstance( position + 1 );
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        /*fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();*/
    }

    @Override
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void searchPerson(View view) {

        EditText edtDNI_Local = (EditText) findViewById( R.id.edtDNI_Local );

        LocalAsync localAsync = new LocalAsync( MainActivity.this );
        localAsync.setLocalI( MainActivity.this );
        localAsync.execute( edtDNI_Local.getText().toString() );

        edtDNI_Local.setText("");

    }

    @Override
    public void showPerson(ArrayList<PadronE> arrayList) {

        if ( arrayList != null )
        {
            TextView txtApePaterno = (TextView) findViewById( R.id.txtApePaterno );
            TextView txtApeMaterno = (TextView) findViewById( R.id.txtApeMaterno );
            TextView txtNombres = (TextView) findViewById( R.id.txtNombres );

            for ( int i = 0; i < arrayList.size(); i++ )
            {
                txtApePaterno.setText( arrayList.get(i).getApePaterno() );
                txtApeMaterno.setText( arrayList.get(i).getApeMaterno() );
                txtNombres.setText( arrayList.get(i).getNombres() );
            }
        }
        else
        {
            Toast toast = Toast.makeText( MainActivity.this, "Error en busqueda", Toast.LENGTH_SHORT );
            toast.show();
        }

    }

}
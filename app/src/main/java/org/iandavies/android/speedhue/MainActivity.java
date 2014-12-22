package org.iandavies.android.speedhue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity implements LocationListener {

    LocationManager locationManager = null;

    final float minHue = 115;
    final float maxHue = 0;

    final float minSpeed = 62;
    final float maxSpeed = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        this.findViewById(R.id.target).setBackgroundColor(mphToColor(66));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    private int mphToColor(float mph) {
        float t = (Math.round(mph) - minSpeed) / (maxSpeed - minSpeed);

        t = Math.min(t, 1);
        t = Math.max(t, 0);

        float hue = minHue + t*(maxHue-minHue);

        return Color.HSVToColor(new float[] { hue, 1, 1 });
    }

    public void onLocationChanged(Location location) {

        float mps = location.getSpeed(); // Metres per second

        float mph = mps * 2.23693629f;

        View layout = this.findViewById(R.id.layout);



        layout.setBackgroundColor(mphToColor(mph));

        TextView txtSpeed = (TextView)this.findViewById(R.id.txtSpeed);
        txtSpeed.setText(Math.round(mph));
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}

    @Override
    protected void onPause() {
        super.onPause();

        android.os.Process.killProcess(android.os.Process.myPid());
    }
}

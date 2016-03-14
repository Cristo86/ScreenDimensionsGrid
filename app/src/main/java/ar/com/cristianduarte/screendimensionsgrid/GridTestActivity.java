package ar.com.cristianduarte.screendimensionsgrid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GridTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_test);

        Intent intent = new Intent(this, GridService.class);
        //if(!stopService(intent)) {
            startService(intent);
        //}

    }

}

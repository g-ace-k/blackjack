package com.mikegacek.blackjack;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Michael on 3/21/2017.
 */

public class Rules extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rules);

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

}

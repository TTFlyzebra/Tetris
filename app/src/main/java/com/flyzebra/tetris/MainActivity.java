package com.flyzebra.tetris;

import android.app.Activity;
import android.os.Bundle;
public class MainActivity extends Activity {
	private TetrisView myview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myview = new TetrisView(this);
        setContentView(myview);
    }    
}

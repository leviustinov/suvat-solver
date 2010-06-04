package com.suvatSolver;

import android.app.Activity;
import android.os.Bundle;

public class activityAbout extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//load the about layout
		setContentView(R.layout.about);
	}
}

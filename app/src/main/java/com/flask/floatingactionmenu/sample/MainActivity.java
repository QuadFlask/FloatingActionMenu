package com.flask.floatingactionmenu.sample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.flask.floatingactionmenu.FadingBackgroundView;
import com.flask.floatingactionmenu.FloatingActionMenu;
import com.flask.floatingactionmenu.FloatingActionToggleButton;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOnClickEvent(R.id.fab1, "fab1");
		setOnClickEvent(R.id.fab2, "fab2");
		setOnClickEvent(R.id.fab3, "fab3");
		setOnClickEvent(R.id.faba, "faba");
		setOnClickEvent(R.id.fabb, "fabb");
		setOnClickEvent(R.id.fabc, "fabc");
		setOnClickEvent(R.id.fab_toggle, "toggle");

		FadingBackgroundView fadingBackgroundView = (FadingBackgroundView) findViewById(R.id.fading);
		FloatingActionMenu floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fam);
		floatingActionMenu.setFadingBackgroundView(fadingBackgroundView);
	}

	private void setOnClickEvent(int id, final String msg) {
		findViewById(id).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
}

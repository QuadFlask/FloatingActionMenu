package com.flask.floatingactionmenu.sample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.flask.floatingactionmenu.FloatingActionButton;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab);
		fab1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "fab1 clicked!", Toast.LENGTH_SHORT).show();
			}
		});
		FloatingActionButton faba = (FloatingActionButton) findViewById(R.id.faba);
		faba.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "faba clicked!", Toast.LENGTH_SHORT).show();
			}
		});
		FloatingActionButton fabb = (FloatingActionButton) findViewById(R.id.fabb);
		fabb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "fabb clicked!", Toast.LENGTH_SHORT).show();
			}
		});
	}
}

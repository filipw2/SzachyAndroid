package com.example.szachy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

public class MainActivity extends Activity {

	private Chessboard chessboard;
	private ChessMain chess;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		chess = new ChessMain();
		chessboard = new Chessboard(this, chess, null);
		chess.giveReference(chessboard);
		setContentView(chessboard);

		// chessboardGridView.setAdapter(new SquareAdapter(this));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_ng) {
			chess.init();
			chessboard.invalidate();
		}
		if (id == R.id.action_exit){
			System.exit(0);
		}
		return super.onOptionsItemSelected(item);
	}

}

package com.example.szachy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public final class Chessboard extends View {
	private static final String TAG = Chessboard.class.getSimpleName();

	private static final int COLS = 8;
	private static final int ROWS = 8;

	private final Tile[][] mTiles;
	private final ChessMain chess;
	private Bitmap[] b = new Bitmap[12];
	private Bitmap select;
	private int x0 = 0;
	private int y0 = 0;
	private int squareSize;
	private Boolean start;
	private Context context;
	private char piece;
	/** 'true' if black is facing player. */
	private boolean flipped = false;

	public Chessboard(final Context context, final ChessMain ches, final AttributeSet attrs) {
		super(context, attrs);
		this.mTiles = new Tile[COLS][ROWS];
		chess = ches;
		start = true;
		this.context = context;

		setFocusable(true);

		buildTiles();
		loadImages();
	}

	private void loadImages() {
		b[0] = BitmapFactory.decodeResource(getResources(), R.drawable.wpawn);
		b[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wknight);
		b[2] = BitmapFactory.decodeResource(getResources(), R.drawable.wbishop);
		b[3] = BitmapFactory.decodeResource(getResources(), R.drawable.wrook);
		b[4] = BitmapFactory.decodeResource(getResources(), R.drawable.wqueen);
		b[5] = BitmapFactory.decodeResource(getResources(), R.drawable.wking);
		b[6] = BitmapFactory.decodeResource(getResources(), R.drawable.bpawn);
		b[7] = BitmapFactory.decodeResource(getResources(), R.drawable.bknight);
		b[8] = BitmapFactory.decodeResource(getResources(), R.drawable.bbishop);
		b[9] = BitmapFactory.decodeResource(getResources(), R.drawable.brook);
		b[10] = BitmapFactory.decodeResource(getResources(), R.drawable.bqueen);
		b[11] = BitmapFactory.decodeResource(getResources(), R.drawable.bking);
		select = BitmapFactory.decodeResource(getResources(), R.drawable.b);
	}

	//choose figure dialog (pawn promotions)
	public void dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setTitle("Title");
		builder.setItems(new CharSequence[] { "Queen", "Rook", "Knight", "Bishop" },
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index position
						// of the selected item
						switch (which) {
						
						case 0:
							piece = 'Q';
							break;
						case 1:
							piece = 'R';
							break;
						case 2:
							piece = 'N';
							break;
						case 3:
							piece = 'B';
							break;
						
						}
						chess.promote(piece);
						invalidate();
					}
				});

		builder.create().show();
		
		
	}

	private void buildTiles() {
		for (int c = 0; c < COLS; c++) {
			for (int r = 0; r < ROWS; r++) {
				mTiles[c][r] = new Tile(c, r);
			}
		}
	}

	@Override
	protected void onDraw(final Canvas canvas) {

		final int width = getWidth();
		final int height = getHeight();

		this.squareSize = Math.min(getSquareSizeWidth(width), getSquareSizeHeight(height));

		if (start) {
			scale();
			start = false;
		}

		computeOrigins(width, height);

		for (int c = 0; c < COLS; c++) {
			for (int r = 0; r < ROWS; r++) {
				final int xCoord = getXCoord(c);
				final int yCoord = getYCoord(r);

				final Rect tileRect = new Rect(xCoord, // left
						yCoord, // top
						xCoord + squareSize, // right
						yCoord + squareSize // bottom
				);
				mTiles[c][r].setSelected(chess.isSelected(c, r));
				mTiles[c][r].setTileRect(tileRect);
				mTiles[c][r].draw(canvas);

				drawPiece(c, r, xCoord, yCoord, canvas);
				
				drawText(canvas,2);
				if (chess.isInCheck())
					drawText(canvas,1);
				
			}
		}

	}

	private void drawPiece(int c, int r, int xCoord, int yCoord, Canvas canvas) {
		int number = 0;
		if (chess.getFigure(c, r) == 0)
			return;

		switch (chess.getFigure(c, r)) {
		case 'P':
			number = 0;
			break;
		case 'N':
			number = 1;
			break;
		case 'B':
			number = 2;
			break;
		case 'R':
			number = 3;
			break;
		case 'Q':
			number = 4;
			break;
		case 'K':
			number = 5;
			break;

		default:
			break;
		}
		if (chess.getPlayer(c, r) == 'B')
			number += 6;
		canvas.drawBitmap(b[number], xCoord, yCoord, null);

	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Tile tile;
			for (int c = 0; c < COLS; c++) {
				for (int r = 0; r < ROWS; r++) {
					tile = mTiles[c][r];
					if (tile.isTouched(x, y)) {
						
						tile.handleTouch();
						chess.handleTouch(c, r);

						invalidate();

					}
				}
			}
		}
		return true;
	}

	private int getSquareSizeWidth(final int width) {
		return width / 8;
	}

	private int getSquareSizeHeight(final int height) {
		return height / 8;
	}

	private int getXCoord(final int x) {
		return x0 + squareSize * (flipped ? 7 - x : x);
	}

	private int getYCoord(final int y) {
		return y0 + squareSize * (flipped ? y : 7 - y);
	}

	private void computeOrigins(final int width, final int height) {
		this.x0 = (width - squareSize * 8) / 2;
		this.y0 = (height - squareSize * 8) / 2;
	}

	private void scale() {
		for (int i = 0; i < 12; i++) {
			b[i] = Bitmap.createScaledBitmap(b[i], squareSize, squareSize, false);
		}
		select = Bitmap.createScaledBitmap(select, squareSize, squareSize, false);
	}

	public void drawText(Canvas canvas,int x) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(40);
		paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		if(x==1)canvas.drawText("check", 20, 120, paint);
		if(x==2)canvas.drawText("Player: "+chess.getTurn(), 20, 70, paint);
	}
}

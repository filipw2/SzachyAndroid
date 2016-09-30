package com.example.szachy;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public final class Tile {
    private static final String TAG = Tile.class.getSimpleName();

    private final int col;
    private final int row;
    private Boolean isSelected;
    
    private final Paint squareColor;
    private Rect tileRect;

    public Tile(final int col, final int row) {
        this.col = col;
        this.row = row;
        this.isSelected=false;
        this.squareColor = new Paint();
        
    }

    public void draw(final Canvas canvas) {
    	if(isSelected)squareColor.setColor(Color.YELLOW);
    	else squareColor.setColor(isDark() ? Color.GRAY : Color.WHITE);
    		
        canvas.drawRect(tileRect, squareColor);
        
    }

    public String getColumnString() {
        switch (col) {
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            case 4: return "E";
            case 5: return "F";
            case 6: return "G";
            case 7: return "H";
            default: return null;
        }
    }

    public String getRowString() {
        // To get the actual row, add 1 since 'row' is 0 indexed.
        return String.valueOf(row + 1);
    }

    public void handleTouch() {
        Log.d(TAG, "handleTouch(): col: " + col);
        Log.d(TAG, "handleTouch(): row: " + row);
        System.out.println(col+" "+row);
    }

    public boolean isDark() {
        return (col + row) % 2 == 0;
    }

    public boolean isTouched(final int x, final int y) {
        return tileRect.contains(x, y);
    }

    public void setTileRect(final Rect tileRect) {
        this.tileRect = tileRect;
    }

    public String toString() {
        final String column = getColumnString();
        final String row    = getRowString();
        return "<Tile " + column + row + ">";
    }

	public void setSelected(Boolean b) {
		isSelected=b;
		
	}

}
package org.helyx.app.j2me.lib.ui.util;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public final class WordWrapUtil {

	private WordWrapUtil() {
		super();
	}
	
	public static void paint(Graphics g, String text, int x, int y, int fullWidth, int style) {
		StringBuffer positions = wrapText(g, text, fullWidth);
		paint(g, text, x, y, fullWidth, style, positions);
	}
	
	public static void paint(Graphics g, String text, int x, int y, int fullWidth, int style, StringBuffer positions) {

		Font font = g.getFont();
		int positionLength = positions.length();

		if ((style & Graphics.HCENTER) != 0) {
			x += fullWidth / 2; 
		}
		else if ((style & Graphics.RIGHT) != 0) {
			x += fullWidth;
		}
		
		if ((style & Graphics.VCENTER) != 0) {
			x += getHeight(g, positions) / 2; 
		}
		else if ((style & Graphics.BOTTOM) != 0) {
			x += getHeight(g, positions);
		}
		
		int pos = 0;
		
		for( int i = 0 ; i < positionLength ; i++ ) {

			final int cut = positions.charAt(i);

			int posX = x;

			g.drawSubstring(
				text, pos, cut > 0 && text.charAt(cut-1) <= ' ' ? cut - 1 - pos : cut - pos,
				posX, y, Graphics.LEFT | Graphics.TOP);
            pos = cut;
            y += font.getHeight();
        }
	}
	

    public static StringBuffer wrapText(Graphics g, String text, int fullWidth) {
    	 StringBuffer positions = new StringBuffer();
    	
    	 int curPos = 0;
         int strLen = text.length();
         Font font = g.getFont();

         //
         // Process all character of given string.
         while(curPos < strLen) {

         	final int start = curPos;
         	int i = curPos;

         	while (true) {
         		// Search for the next non alpha numeric character
         		while( i < strLen && text.charAt(i) > ' ' ) {
         			i++;
         		}

                // Because of the cut away support we need to calculate the available
                // width per line.
                int availableWidth = fullWidth;


         		// Calculate how much space we would need from the current position
         		// to the found non alphanumeric character or text length.
         		final int w = font.stringWidth(text.substring(start, i));

         		// If we would need more space for a single word then we have we need
         		// to find a maximal count of character we display in one row and split
         		// the word at this position.
         		if (curPos == start && w > availableWidth ) {

     				// Find the position where we have to split the word.
     				while (i > curPos && font.stringWidth(text.substring(start, --i)) > availableWidth) {
     					// Empty block
     				}
     				curPos = i;
     				break;
     			}

         		// If we could display more then this just store that position so we can
         		// use this position if we did not found a better one.
         		if( w <= availableWidth ) {
         			curPos = i;
         		}
         		
         		// If we need more space then we have or we found a newline character
         		// the use the last valid position we found
         		if( w > availableWidth || i >= strLen || text.charAt(i) == '\n' || text.charAt(i) == '\r' ) {
         			break;
         		}

         		i++;
         	}

         	// Save the position
         	positions.append((char) (curPos >= strLen ? strLen : ++curPos));
         }
         
         return positions;
    }

	public static int getHeight(Graphics g, StringBuffer positions) {
		Font font = g.getFont();
		return positions.length() * font.getHeight();
	}

	
}
package org.helyx.app.j2me.lib.ui.util;

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public final class WordWrapUtil {

	private WordWrapUtil() {
		super();
	}
	
	public static void paint(Graphics g, String text, int x, int y, int maxWidth, int style) {
		Vector positions = wrapText(g, text, maxWidth);
		paint(g, text, x, y, style, positions);
	}
	
	public static void paint(Graphics g, String text, int x, int y, int style, Vector positions) {

		Font font = g.getFont();
		int positionLength = positions.size();
		
		if ((style & Graphics.VCENTER) != 0) {
			y -= (positionLength * g.getFont().getHeight()) / 2; 
		}
		else if ((style & Graphics.BOTTOM) != 0) {
			y -= (positionLength * g.getFont().getHeight());
		}
		
		int pos = 0;
		
		for( int i = 0 ; i < positionLength ; i++ ) {

			final int cut = ((Integer)positions.elementAt(i)).intValue();

			int lineLength = cut > 0 && text.charAt(cut-1) <= ' ' ? cut - 1 - pos : cut - pos;
			g.drawSubstring(
				text, pos, lineLength,
				x, y, (style & (Graphics.LEFT | Graphics.HCENTER | Graphics.RIGHT)) | Graphics.TOP);
            pos = cut;
            y += font.getHeight();
        }
	}
	

    public static Vector wrapText(Graphics g, String text, int maxWidth) {
    	 Vector positions = new Vector();
    	
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
                int availableWidth = maxWidth;


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
         	positions.addElement(new Integer(curPos >= strLen ? strLen : ++curPos));
         }
         
         return positions;
    }
	
}
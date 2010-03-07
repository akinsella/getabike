package org.helyx.app.j2me.getabike.ui.view.renderer;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.data.contact.domain.Contact;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.list.AbstractListView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.list.ICellRenderer;
import org.helyx.logging4me.Logger;


public class ContactItemRenderer implements ICellRenderer {

	private Logger logger = Logger.getLogger("CONTACT_ITEM_RENDERER");
	
	private static final int BASE_LEFT_POS = 5;
	private static final int PADDING_TOP = 2;
	private static final int LINE_SPACING = 0;

	private int smallFontHeight = FontUtil.SMALL.getHeight();
	private int smallBoldFontHeight = FontUtil.SMALL_BOLD.getHeight();
    private static String stationSeparator = " - ";
    private static String stationSeparatorEmpty = "";
    private int stationSeparatorStrWidthSmallBold = FontUtil.SMALL_BOLD.stringWidth(stationSeparator);
	
	public ContactItemRenderer() {
		super();
	}

	public void paintItem(AbstractListView view, Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {

		Contact contact = (Contact)itemObject;
    	
    	boolean isSelected = view.isItemSelected(offset);
		int listFontSecondSelectedColor = view.getTheme().getColor(isSelected ? ThemeConstants.WIDGET_LIST_FONT_SECOND_SELECTED : ThemeConstants.WIDGET_LIST_FONT_SECOND).intValue();
   	
     	if (isSelected) {
    		Color listFontSelectedColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SELECTED);
    		g.setColor(listFontSelectedColor.intValue());
    	}
    	else {
    		Color listFontColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT);
    		g.setColor(listFontColor.intValue());
    	}

        int x = itemClientArea.location.x;
        int y = itemClientArea.location.y;

        g.setFont(FontUtil.SMALL_BOLD);
        
        int addToXPos = BASE_LEFT_POS;
        int addToYPos = PADDING_TOP;

        String stationName = contact.getName() != null ? contact.getName() : view.getMessage("renderer.contact.no.name");
        g.drawString(stationName, x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);

        addToXPos = BASE_LEFT_POS;
    	addToYPos += LINE_SPACING + smallFontHeight;
        if (contact.getPhoneNumber() != null && contact.getPhoneNumber().length() > 0) {

    		g.setColor(listFontSecondSelectedColor);
	        g.setFont(FontUtil.SMALL);
	    	g.drawString(contact.getPhoneNumber(), x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);

        	addToYPos += LINE_SPACING + smallFontHeight;
        }
	}

	public int computeHeight(AbstractListView view, Object itemObject, int offset) {

		Contact contact = (Contact)itemObject;
    	
//    	boolean isSelected = view.isItemSelected(offset);
        
        int itemHeight = 
        	PADDING_TOP + 
        	smallBoldFontHeight + LINE_SPACING + 
        	((contact.getPhoneNumber() != null && contact.getPhoneNumber().length() > 0) ? smallFontHeight : 0) + 
        	PADDING_TOP;
 	
        return itemHeight;
	}

}

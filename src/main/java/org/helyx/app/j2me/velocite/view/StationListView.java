package org.helyx.app.j2me.velocite.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.pref.Pref;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.task.ProgressListener;
import org.helyx.app.j2me.lib.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.displayable.view.AbstractView;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.graphics.Shade;
import org.helyx.app.j2me.lib.ui.util.DialogUtil;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.lib.ui.view.LoadTaskView;
import org.helyx.app.j2me.lib.ui.view.MenuListView;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.task.StationLoadTask;

public class StationListView extends AbstractView {
	
	private Station[] stationArray = null;

	private static final String CAT = "STATION_LIST_VIEW";
	
	private int selectedOffset = 0;
	private int topOffset = 0;
	private int visibleItemCount = 0;
	
	private Menu menu;
	private MenuListView prefMenuListView;

	public StationListView(AbstractMIDlet midlet) {
		super(midlet);
		init();
	}

	private void init() {
		setFullScreenMode(true);
		setTitle("Liste des stations");

		loadMenu();
		
		initActions();

	}
	
	private void initActions() {
		
		setPrimaryCommand(new Command("Voir", true, new IAction() {

			public void run(Object data) {
				showCurrentStationSelected();
			}
			
		}));

		setSecondaryCommand(new Command("Retour", true, new IAction() {

			public void run(Object data) {
				returnToPreviousDisplayable();
			}
			
		}));
		
		setThirdCommand(new Command("Menu", true, new IAction() {

			public void run(Object data) {
				prefMenuListView = new MenuListView(getMidlet(), false);
				prefMenuListView.setMenu(menu);
				prefMenuListView.setPreviousDisplayable(StationListView.this);
				showDisplayable(prefMenuListView, true, false);
			}
			
		}));
	}
	
	private void loadMenu() {
		menu = new Menu();
		
		menu.addMenuItem(new MenuItem("Chercher une station", new IAction() {
			public void run(Object data) {
				showDisplayable(new StationSearchView(getMidlet(), new IReturnCallback() {

					public void onReturn(Object data) {
						loadListContent();
					}
					
				}), StationListView.this);
			}
		}));
		
	}

	
	private String getStationNameFilter() {
		Pref stationNamePref = PrefManager.readPref(StationSearchView.PREF_STATION_NAME);
		if (stationNamePref != null) {
			String stationName = stationNamePref.value;
			
			return stationName;
		}
		
		return null;
	}
	
	private void showCurrentStationSelected() {
		int length = stationArray == null ? 0 : stationArray.length;
		boolean isEmpty = length == 0;
		
		if (!isEmpty) {
			showDisplayable(new StationDetailsView(getMidlet(), stationArray[selectedOffset]), this);
		}
	}
	
	public void loadListContent() {
		final StationLoadTask stationLoadTask = new StationLoadTask(this, getStationNameFilter());
		stationLoadTask.addProgressListener(loadStationListProgressListener);
		LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "Chargement des stations", stationLoadTask);
		showDisplayable(loadTaskView, this);
		selectedOffset = 0;
		topOffset = 0;
		loadTaskView.startTask();
		Log.info(CAT, "Load List Content...");
	}

	public void setStations(Station[] stationArray) {
		if (stationArray == null) {
			stationArray = new Station[0];
		}
		this.stationArray = stationArray;
		canvas.repaint();
	}

	protected void paint(Graphics g) {
		int length = stationArray == null ? 0 : stationArray.length;
		boolean isEmpty = length == 0;
		
		if (!isEmpty && topOffset > length) {
			topOffset = length - 1;
		}
		if (!isEmpty && selectedOffset > length) {
			selectedOffset = length - 1;
		}
			
		Rectangle clientArea = computeClientArea(g);
		
        g.setFont(FontUtil.SMALL);
        
        if (isEmpty) {
        	g.drawString("Aucune station chargée", clientArea.location.x + clientArea.size.width / 2, clientArea.location.y + clientArea.size.height / 2, Graphics.HCENTER | Graphics.BASELINE);
        	return ;
        }
        
        int smallFontHeight = FontUtil.SMALL.getHeight();
        int smallBoldFontHeight = FontUtil.SMALL_BOLD.getHeight();
        
        int itemHeight = 1 + smallFontHeight + 1 + smallBoldFontHeight + 1 + 1;
        
        visibleItemCount = clientArea.size.height / itemHeight;

        for (int i = 0 ; i < visibleItemCount + 1 ; i++) {
        	int y = clientArea.location.y + i * itemHeight;
        	Rectangle itemClientArea = new Rectangle(clientArea.location.x, y, clientArea.size.width, itemHeight);
        	paintItem(g, i, itemClientArea);
        }
        paintScrollBar(g, length, topOffset, selectedOffset, visibleItemCount, clientArea);
	}
	
	private void paintScrollBar(Graphics g, int length, int topOffset, int selectedOffset, int visibleItemCount, Rectangle clientArea) {
		if (length == 0 || length <= visibleItemCount) {
			return;
		}
		Color scrollBackgroundColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_SCROLL_BACKGROUND);
		g.setColor(scrollBackgroundColor.intValue());
		g.fillRect(clientArea.location.x + clientArea.size.width - 4, clientArea.location.y, 4, clientArea.size.height);
		
		int yHeight = Math.max(clientArea.size.height / 8, clientArea.size.height * visibleItemCount / length);
		int yPos = clientArea.location.y + (int)((clientArea.size.height - yHeight) * ((double)selectedOffset / ((double)length - 1)));
		
		Color shadeColor1 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_SCROLL_SHADE_DARK);
		Color shadeColor2 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_SCROLL_SHADE_LIGHT);
		Shade shade = new Shade(shadeColor1.intValue(), shadeColor2.intValue());
		Rectangle area = new Rectangle(clientArea.location.x + clientArea.size.width - 4, yPos, 4, yHeight);
		GraphicsUtil.fillShade(g, area, shade, true);

		Color scrollBorderColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_SCROLL_BORDER);
		g.setColor(scrollBorderColor.intValue());
		g.drawLine(clientArea.location.x + clientArea.size.width - 5, clientArea.location.y, clientArea.location.x + clientArea.size.width - 5, clientArea.location.y + clientArea.size.height);
	}

	private void paintItem(Graphics g, int offset, Rectangle itemClientArea) {
		if (topOffset + offset >= stationArray.length) {
			return ;
		}
		Station station = stationArray[topOffset + offset];
    	
    	boolean isSelected = selectedOffset == topOffset + offset;
    	
    	if (isSelected) {
//    		g.setColor(ColorUtil.WIDGET_LIST_FONT_SELECTED);
    		Color shadeColor1 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST_SELECTED_SHADE_LIGHT);
    		Color shadeColor2 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST_SELECTED_SHADE_DARK);
    		Shade shade = new Shade(shadeColor1.intValue(), shadeColor2.intValue());
    		GraphicsUtil.fillShade(g, itemClientArea, shade, false);
     	}
    	else {
    		Color listColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST);
    		g.setColor(listColor.intValue());
    		g.fillRect(itemClientArea.location.x, itemClientArea.location.y, itemClientArea.size.width, itemClientArea.size.height);
    	}
    	
		Color listSeparatorColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST_SEPARATOR);
    	g.setColor(listSeparatorColor.intValue());
    	g.drawLine(itemClientArea.location.x, itemClientArea.location.y + itemClientArea.size.height - 1, itemClientArea.location.x + itemClientArea.size.width, itemClientArea.location.y + itemClientArea.size.height - 1);
	
     	if (isSelected) {
    		Color listFontSelectedColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SELECTED);
    		g.setColor(listFontSelectedColor.intValue());
    	}
    	else {
    		Color listFontColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT);
    		g.setColor(listFontColor.intValue());
    	}
        if (!station.open) {
    		Color listErrorColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST_ERROR);
         	g.setColor(listErrorColor.intValue());
        }
        g.setFont(FontUtil.SMALL_BOLD);
        g.drawString(station.number + " - " + station.name, itemClientArea.location.x + 5, itemClientArea.location.y + 2, Graphics.LEFT | Graphics.TOP);
 
    	if (isSelected) {
    		Color listFontSecondSelectedColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND_SELECTED);
    		g.setColor(listFontSecondSelectedColor.intValue());
    	}
    	else {
    		Color listFontSecondColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND);
    		g.setColor(listFontSecondColor.intValue());
    	}
        g.setFont(FontUtil.SMALL);
    	g.drawString(station.fullAddress, itemClientArea.location.x + 5, itemClientArea.location.y + 2 + FontUtil.SMALL_BOLD.getHeight(), Graphics.LEFT | Graphics.TOP);
	}

	protected void onKeyPressed(int keyCode) {
		int gameAction = canvas.getGameAction(keyCode);
		Log.info(CAT, "[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
	    if (gameAction == GameCanvas.DOWN) {
	    	scrollDown();
	    }
	    else if (gameAction == GameCanvas.UP) {
			scrollUp();
		}
	    else if (gameAction == GameCanvas.LEFT) {
			returnToPreviousDisplayable();
		}
	    else if (gameAction == GameCanvas.RIGHT) {
			showCurrentStationSelected();
		}
	}

	protected void onKeyRepeated(int keyCode) {
		int gameAction = canvas.getGameAction(keyCode);
		Log.info(CAT, "[onKeyRepeated] gameAction: " + gameAction + ", keyCode: " + keyCode);
	    if (gameAction == GameCanvas.DOWN) {
	    	scrollDown();
	    }
	    else if (gameAction == GameCanvas.UP) {
			scrollUp();
		}
	}

	private void scrollUp() {
		if (selectedOffset > 0) {
			selectedOffset--;
		}
		if (selectedOffset < topOffset) {
			topOffset = selectedOffset;
		}
		Log.info(CAT, "Scroll Up - topOffset: " + topOffset + ", selectedOffset: " + selectedOffset);
		canvas.repaint();
	}

	private void scrollDown() {
		int length = stationArray == null ? 0 : stationArray.length;
		if (selectedOffset + 1 < length) {
			selectedOffset++;
		}
		if (length <= visibleItemCount) {
			topOffset = 0;
		}
		else {
			if (topOffset >= length - visibleItemCount) {
				topOffset = length - visibleItemCount;
			}
			if (topOffset + visibleItemCount <= selectedOffset) {
				topOffset = selectedOffset - visibleItemCount + 1;
			}	
		}
		Log.info(CAT, "Scroll Down - topOffset: " + topOffset + ", selectedOffset: " + selectedOffset + ", visibleItemCount: " + visibleItemCount + ", length: " + length);
		canvas.repaint();
	}
	
	private ProgressListener loadStationListProgressListener = new ProgressAdapter(CAT) {

		public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
			switch (eventType) {
				case ProgressEventType.ON_SUCCESS:
					showDisplayable(StationListView.this, true);
					break;
					
				case ProgressEventType.ON_CANCEL:
					showDisplayable(StationListView.this, true);
					break;
	
				case ProgressEventType.ON_ERROR:
					Throwable throwable = (Throwable)eventData;
					Log.warn(getCat(), throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
					DialogUtil.showAlertMessage(getMidlet(), getDisplayable(), "Erreur", throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
					showDisplayable(StationListView.this, true);
					break;
					
				default:
					break;
			}
		}
		
	};

}

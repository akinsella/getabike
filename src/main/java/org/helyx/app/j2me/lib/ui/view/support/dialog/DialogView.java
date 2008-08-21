package org.helyx.app.j2me.lib.ui.view.support.dialog;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.WordWrapUtil;
import org.helyx.app.j2me.lib.ui.view.AbstractView;
import org.helyx.app.j2me.lib.ui.widget.Command;

public class DialogView extends AbstractView {
	
	private static final Log log = LogFactory.getLog("MESSAGE_VIEW");
		
	private String text;
	
	private int resultCode = DialogResultConstants.NULL;
	
	private DialogCommand primaryDialogCommand;
	private DialogCommand secondaryDialogCommand;
	private DialogCommand thirdDialogCommand;

	public DialogView(AbstractMIDlet midlet, String title, String text, DialogCommand primaryCommand) {
		this(midlet, title, text, primaryCommand, null, null);
	}

	public DialogView(AbstractMIDlet midlet, String title, String text, DialogCommand primaryCommand, DialogCommand secondaryCommand) {
		this(midlet, title, text, primaryCommand, secondaryCommand, null);
	}
	
	public DialogView(AbstractMIDlet midlet, String title, String text, DialogCommand primaryDialogCommand, DialogCommand secondaryDialogCommand, DialogCommand thirdDialogCommand) {
		super(midlet);
	    setTitle(title);
	    this.text = text;
	    this.primaryDialogCommand = primaryDialogCommand;
	    this.secondaryDialogCommand = secondaryDialogCommand;
	    this.thirdDialogCommand = thirdDialogCommand;
		init();
	}

	private void init() {
		setFullScreenMode(true);
		if (primaryDialogCommand != null) {
			setPrimaryCommand(createCommand(primaryDialogCommand));
		}
		if (secondaryDialogCommand != null) {
			setSecondaryCommand(createCommand(secondaryDialogCommand));
		}
		if (thirdDialogCommand != null) {
			setThirdCommand(createCommand(thirdDialogCommand));
		}
	}
	
	private Command createCommand(final DialogCommand dialogCommand) {
		Command command = new Command(dialogCommand.getText(), new IAction() {

			public void run(Object data) {
				resultCode = dialogCommand.getResultCode();
				fireReturnCallback(DialogView.this);
			}
			
		});
		
		return command;
	}

	protected void paint(Graphics g) {
		Rectangle clientArea = computeClientArea(g);
        
		int x = clientArea.location.x;
		int y = clientArea.location.y;
		int width = clientArea.size.width;
        int height = clientArea.size.height;

        g.setColor(getTheme().getColor(ThemeConstants.WIDGET_FONT).intValue());
        g.setFont(FontUtil.MEDIUM_BOLD);
        WordWrapUtil.paint(g, text, x + width / 2, y + height / 2, width - 10, Graphics.HCENTER | Graphics.VCENTER);
	}

	public int getResultCode() {
		return resultCode;
	}

}

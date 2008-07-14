package org.helyx.app.j2me.lib.ui.widget;

import javax.microedition.lcdui.Image;

public class ImageSet {

	private Image disabledImage;
	private Image hoverImage;
	private Image image;
	private Image activeImage;
	
	public ImageSet() {
		super();
	}

	public Image getDisabledImage() {
		return disabledImage;
	}
	
	public void setDisabledImage(Image disabledImage) {
		this.disabledImage = disabledImage;
	}
	
	public Image getHoverImage() {
		return hoverImage;
	}
	
	public void setHoverImage(Image hoverImage) {
		this.hoverImage = hoverImage;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public Image getActiveImage() {
		return activeImage;
	}
	
	public void setActiveImage(Image activeImage) {
		this.activeImage = activeImage;
	}

}

/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.ui.widget;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.getabike.lib.stream.StreamUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.getabike.lib.ui.widget.ImageSet;
import org.helyx.app.j2me.getabike.lib.ui.widget.RuntimeIOException;
import org.helyx.logging4me.Logger;

public class ImageSet {

	private static final Logger logger = Logger.getLogger(ImageSet.class);
	
	private Image disabledImage;
	private Image image;
	private Image activeImage;
	
	public ImageSet() {
		super();
	}
	
	public ImageSet(String imagePath) {
		super();
		image = createImage(imagePath);
	}
	
	private Image createImage(String imagePath) {
		try {
			Image image = ImageUtil.createImageFromClassPath(imagePath);
			
			return image;
		}
		catch(IOException ioe) {
			logger.warn(ioe);
			throw new RuntimeIOException(ioe);
		}
	}

	public ImageSet(Image image) {
		super();
		this.image = image;
	}

	public Image getDisabledImage() {
		return disabledImage;
	}
	
	public void setDisabledImage(Image disabledImage) {
		this.disabledImage = disabledImage;
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

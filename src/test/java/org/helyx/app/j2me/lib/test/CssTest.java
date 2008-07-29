package org.helyx.app.j2me.lib.test;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.css.sac.helpers.ParserFactory;

public class CssTest extends TestCase {

	public CssTest() {
		super();
	}

	public void testCssParser() throws IOException, NullPointerException,
			ClassCastException, ClassNotFoundException, IllegalAccessException,
			InstantiationException {
		Class _class = org.w3c.flute.parser.Parser.class;
		String classpath = "/org/w3c/flute/spip_style.css";
		InputSource source = new InputSource();
		InputStream stream = getClass().getResourceAsStream(classpath);
		try {
			source.setByteStream(stream);
			source.setURI("classpath://" + classpath);
			Parser parser = new ParserFactory().makeParser(_class.getName());

			parser.setDocumentHandler(new DemoSAC());
			parser.parseStyleSheet(source);
		}
		finally {
			stream.close();
		}
	}

	public class DemoSAC implements DocumentHandler {
		boolean inMedia = false;
		boolean inStyleRule = false;
		int propertyCounter = 0;

		public void startMedia(SACMediaList media) throws CSSException {
			inMedia = true;
			System.out.println("startMedia: " + media);
			int mediaLength = media.getLength();
			for (int i = 0 ; i < mediaLength ; i++) {
				String mediaName = media.item(i);
				System.out.println("mediaName:  " + mediaName);
			}
		}

		public void endMedia(SACMediaList media) throws CSSException {
			inMedia = false;
			System.out.println("endMedia: " + media);
		}

		public void startSelector(SelectorList selectors) throws CSSException {
			if (!inMedia) {
				inStyleRule = true;
				propertyCounter = 0;
			}
			int length = selectors.getLength();
			System.out.println("[MockDocumentHandler#startSelector], selectors=" + selectors + ", length=" + length);
			for (int i = 0; i < length; i++) {
				Selector selector = selectors.item(i);
				if (selector instanceof ElementSelector) {
					ElementSelector elementSelector = (ElementSelector) selector;
					System.out.println("\tElementSelector=> localName="
							+ elementSelector.getLocalName());
				}
				else if (selector instanceof ConditionalSelector) {
					ConditionalSelector conditionalSelector = (ConditionalSelector) selector;
					ElementSelector simpleSelector = (ElementSelector) conditionalSelector.getSimpleSelector();
					System.out.println("\tSimpleSelector [name='" + simpleSelector.getLocalName() + "']");
					System.out.println("\tConditionalSelector");
					Condition condition = conditionalSelector.getCondition();
					if (condition instanceof AttributeCondition) {
						AttributeCondition attributeCondition = (AttributeCondition) condition;
						System.out
								.println("\t\tCondition (type=AttributeCondition)=> localName="
										+ attributeCondition.getLocalName()
										+ ", value="
										+ attributeCondition.getValue());
					} else {
						System.out.println("\t\tCondition=>" + condition);
					}
	 
				} else
					System.out.println(selector);
			}
		}

		public void endSelector(SelectorList selectors) throws CSSException {
			if (!inMedia) {
//				System.out.println("endSelector - Found " + propertyCounter + " properties.");
			}
			inStyleRule = false;

		}

		public void property(String name, LexicalUnit value, boolean important)
				throws CSSException {
			if (inStyleRule) {
				propertyCounter++;
			}
			System.out.println("Property: [name='" + name + "', value: '" + value + "', important:'" + important + "']");
		}

		public void comment(String text) throws CSSException {
			System.out.println("Found comment: " + text);
		}

		public void endDocument(InputSource source) throws CSSException {
			System.out.println("End Document");
		}

		public void endFontFace() throws CSSException {
			System.out.println("End Font Face");
		}

		public void endPage(String name, String pseudo_page) throws CSSException {
			System.out.println("End page [name='" + name + "', '" + pseudo_page + "']");
		}

		public void ignorableAtRule(String atRule) throws CSSException {
			System.out.println("ignorableAtRule[ atRule: '" + atRule + "'");
		}

		public void importStyle(String uri, SACMediaList media, String defaultNamespaceURI) throws CSSException {
			System.out.println("importStyle[ uri: '" + uri + "', media='" + media + "', defaultNamespaceURI='" + defaultNamespaceURI + "'");
		}

		public void namespaceDeclaration(String prefix, String uri) throws CSSException {
			System.out.println("namespaceDeclaration[ prefix: '" + prefix + "', uri='" + uri + "'");
		}

		public void startDocument(InputSource source) throws CSSException {
			System.out.println("Starting " + source);
		}

		public void startFontFace() throws CSSException {
			System.out.println("Start font face");
		}

		public void startPage(String name, String pseudo_page) throws CSSException {
			System.out.println("Start page [name='" + name + "', '" + pseudo_page + "']");
		}
	}
}

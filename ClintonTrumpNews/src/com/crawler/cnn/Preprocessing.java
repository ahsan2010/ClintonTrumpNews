package com.crawler.cnn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.chainsaw.Main;
import org.tartarus.snowball.ext.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import com.main.Properties;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

public class Preprocessing {

	private static final Set<String> DISALLOWED_HTML_TAGS = new HashSet<String>(
			Arrays.asList(HTMLElementName.PRE, HTMLElementName.CODE));

	private static OutputDocument removeNotAllowedTags(Source source) {

		OutputDocument outputDocument = new OutputDocument(source);
		List<Element> elements = source.getAllElements();

		for (Element element : elements) {

			if (DISALLOWED_HTML_TAGS.contains(element.getName())) {
				outputDocument.remove(element);
			}

		}

		return outputDocument;
	}

	private static OutputDocument onlyCodePart(Source source) {

		OutputDocument outputDocument = new OutputDocument(source);
		List<Element> elements = source.getAllElements();

		for (Element element : elements) {
			// System.out.println("Element Name " + element.getName());
			if (element.getName().equals(HTMLElementName.PRE) || element.getName().equals(HTMLElementName.CODE)) {
				continue;
			} else {
				outputDocument.remove(element);

			}

		}
		// System.out.println(outputDocument.toString());
		return outputDocument;
	}

	public static String htmlRemove(String body) {
		String result = "";
		Source htmlSource = new Source(body);
		List<StartTag> sTag = new ArrayList<StartTag>();
		htmlSource.getAllStartTags();
		OutputDocument outputDocument = removeNotAllowedTags(htmlSource);
		Source source = new Source(outputDocument.toString());
		Segment htmlSeg = new Segment(source, 0, outputDocument.toString().length());

		Renderer htmlRend = new Renderer(htmlSeg);

		return htmlRend.toString();
	}

	public static String htmlExtractCodePart(String body) {
		String result = "";
		Source htmlSource = new Source(body);
		List<StartTag> sTag = new ArrayList<StartTag>();
		htmlSource.getAllStartTags();
		OutputDocument outputDocument = onlyCodePart(htmlSource);
		Source source = new Source(outputDocument.toString());
		Segment htmlSeg = new Segment(source, 0, outputDocument.toString().length());

		Renderer htmlRend = new Renderer(htmlSeg);

		return htmlRend.toString();
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static Map<String, Integer> loadStopWord() {
		
		Map<String, Integer> stopWordList = new HashMap<String, Integer>();
		
		try {			
			FileReader fl = new FileReader(Properties.stop_word_path);
			BufferedReader br = new BufferedReader(fl);

			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					line = line.trim().toLowerCase();
					//System.out.println(line);
					stopWordList.put(line, 1);
				}
			}

		} catch (Exception e) {

		}
		return stopWordList;
	}

	public static String activateStemmer(String data) {

		long start = System.currentTimeMillis();

		String your_steemed_String = "";
		Map<String,Integer> stopWords = loadStopWord();
		RemoveStopWord rm = new RemoveStopWord(stopWords);
		String result = "";
		result += rm.doRemove(data);

		String lang = "english";
		Class stemClass;
		try {
			stemClass = Class.forName("org.tartarus.snowball.ext." + lang + "Stemmer");
			//stemClass.
           // SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();
			
            
            englishStemmer stemmer = new englishStemmer();
			
			String word[] = result.split("\\s+");
			String stemmedWord = "";
			for (String w : word) {
				stemmer.setCurrent(w);
				stemmer.stem();
				String st = stemmer.getCurrent().trim();
				if (st.length() > 2) {
					if (!isNumeric(st)) {
						stemmedWord += st + " ";
					}
				}
			}

			your_steemed_String += stemmedWord;

		} catch (Exception e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		// System.out.println("[Stemming takes: " + (end-start)+" ms]");
		// System.out.println();

		your_steemed_String = rm.doRemove(your_steemed_String);
		
		return your_steemed_String;
	}

}

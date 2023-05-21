package com.myrev.rp.load;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.myrev.rp.ex.InvalidNetworkException;


/**
 * Simple means of reading-in XML format data files for station listings.
 *
 * Parses either single line of station information (using getLineData()) or
 * complete network data file (using getNetworkData()).
 *
 * @author stuartj [Class included with InvalidNetworkException in original RoutePlanner.jar exercise]
 */
public class LineDataReader extends DefaultHandler {

    private List stations;
    private StringBuffer textBuffer;

    /**
	 * gets list of stations for given file of Underground line data.
	 *
	 * @param fileName	pathname of data file to parse
	 * @return List 	list of station names
	 */
	public List getLineData(String fileName) {
        try {
        	File file = new File(fileName);
        	getLineData(file);
        } catch (Throwable t) {
            throw new IllegalArgumentException(fileName + " caused " + t.toString());
        }
        return stations;
    }

	/**
	 * gets list of stations for given file of Underground line data.
	 *
	 * @param file		File to pass
	 * @return List	list of station names
	 */
	public List getLineData(File file) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(file, this);
		} catch (Throwable t) {
			throw new IllegalArgumentException(file.getName() + " caused " + t.toString());
		}
		return stations;
	}

    /**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String namespaceURI, String simpleName,
        String qualifiedName, Attributes attrs) throws SAXException {
        if (qualifiedName == "line") {
            stations = new ArrayList();
        }
    }

    /**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] buf, int offset, int len)
        throws SAXException {
        String s = new String(buf, offset, len);
        if (textBuffer == null) {
            textBuffer = new StringBuffer(s);
        } else {
            textBuffer.append(s);
        }
    }


	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String simpleName,
        String qualifiedName) throws SAXException {
        if (qualifiedName == "station") {
            // add content of station element to List
            stations.add(new String(textBuffer).trim());
            textBuffer = null;
        }
    }

	/**
	 * Parses data file to return information about complete network, as a Map where keys are line names
	 * on network, and corresponding value objects are Lists of strings representing stations on those lines.
	 *
	 * @param s	InputStream from file holding network data (NB: for use when bundling files).
	 * @return Map	the Map of Lists holding station name sequences as Strings.
	 * @throws InvalidNetWorkException
	 */
    public Map getNetworkData(InputStream s) throws InvalidNetworkException, IOException
    {
 	    SAXBuilder saxbuilder = new SAXBuilder();
		try {
			return generateNetwork( saxbuilder.build(s) );
		} catch (JDOMException e) {
			throw new InvalidNetworkException("The NetWork File is Invalid");
		}
    }

    /**
     * Parses data file to return information about complete network, as a Map where keys are line names
     * on network, and corresponding value objects are Lists of strings representing stations on those lines.
     *
	 * @param s File holding network data .
	 * @return Map	the Map of Lists holding station name sequences as Strings.
	 * @throws InvalidNetWorkException
	 */
	public Map getNetworkData(File networkFile) throws InvalidNetworkException, IOException
    {
		SAXBuilder saxbuilder = new SAXBuilder();
		try {
			return generateNetwork( saxbuilder.build(networkFile) );
		} catch (JDOMException e) {
			throw new InvalidNetworkException(e,"The NetWork File is Invalid");
		}
    }

	private Map generateNetwork(Document doc)
	{
		Map map = new HashMap();
		List lines, stations = null;
		String currentLineName, currentStationName = null;
		Element currentLine = null, currentStation = null;
		Element root = doc.getRootElement();
		lines = root.getChildren();

		for (Iterator iter = lines.iterator(); iter.hasNext();) {
			currentLine = (Element) iter.next();
			currentLineName = currentLine.getAttribute("name").getValue();
			stations = currentLine.getChildren();
			List stationNames = new ArrayList();
			for (Iterator iterator = stations.iterator();
				iterator.hasNext();
				) {
				currentStation = (Element) iterator.next();
				currentStationName = currentStation.getText();
				stationNames.add(currentStationName);
			}
			map.put(currentLineName, stationNames);
		}

		return map;
	}
}


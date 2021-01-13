import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader;

public class Treadmill {
    public static void main(String[] args) throws Exception {

    	/*
    	-> In TrainingCenterDatabase Activites -> Activity -> Lap -> Track -> Trackpoint
    	-> Read DistanceMeters
    	-> Multiply DistanceMeters by incline
    	-> Add <AltitudeMeters>110</AltitudeMeters> element before DistanceMeters
    	*/

    	try {
    			String fileName = args[0];
    			double elevationGain = 0.0;
    			double incline = 10;

    			System.out.println("File : " + fileName);
    			System.out.println("Incline on your treadmill (default = 10");

		        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
		        incline = Double.parseDouble(reader.readLine());  

    		    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		        Document document = documentBuilder.parse(fileName);


		        NodeList nList = document.getElementsByTagName("Trackpoint");
		        for (int temp = 0; temp < nList.getLength(); temp++) {
		        	Node nNode = nList.item(temp);

            		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            			Element eElement = (Element) nNode;
            

                  		String distanceMeters = eElement.getElementsByTagName("DistanceMeters").item(0).getTextContent();
                  		double distance = Double.parseDouble(distanceMeters);
            		
                  		elevationGain = distance*incline/100;
            		}

            		Element altitudeMeters = document.createElement("AltitudeMeters");
            		altitudeMeters.appendChild(document.createTextNode(Double.toString(elevationGain)));
		        	nNode.appendChild(altitudeMeters);

		        }

		        
		        DOMSource source = new DOMSource(document);

		        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		        Transformer transformer = transformerFactory.newTransformer();
		        StreamResult result = new StreamResult("out.tcx");
		        transformer.transform(source, result);

			}
			catch (Exception ex) 
			{
				System.out.println(ex);
			}
		}
	}
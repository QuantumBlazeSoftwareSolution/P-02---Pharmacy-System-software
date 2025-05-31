package com.qb.app.model;

import javafx.scene.Group;
import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import javafx.scene.paint.Color;

public class SVGIconGroup extends Group {

    /**
     * Constructor to create a Group containing SVGPath objects from an SVG
     * file.
     *
     * @param svgFileLocation The location of the SVG file (e.g.,
     * "/com/qb/app/assets/icons/users-solid.svg").
     */
    public SVGIconGroup(String svgFileLocation) {
        super();
        loadSVGFromFile(svgFileLocation);
    }

    /**
     * Loads the SVG content from the specified file location and creates
     * SVGPath objects.
     *
     * @param svgFileLocation The location of the SVG file.
     */
    private void loadSVGFromFile(String svgFileLocation) {
        try (InputStream inputStream = getClass().getResourceAsStream(svgFileLocation)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("SVG file not found: " + svgFileLocation);
            }

            // Parse the SVG content into a Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);

            // Get all <path> elements
            NodeList pathElements = doc.getElementsByTagName("path");

            // Create an SVGPath for each <path> element
            for (int i = 0; i < pathElements.getLength(); i++) {
                Node pathNode = pathElements.item(i);
                if (pathNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element pathElement = (Element) pathNode;

                    // Create an SVGPath object
                    SVGPath svgPath = new SVGPath();

                    // Set the path data (d attribute)
                    String dAttribute = pathElement.getAttribute("d");
                    svgPath.setContent(dAttribute);

                    // Set stroke color (if specified in the SVG)
                    String strokeColor = pathElement.getAttribute("stroke");
                    if (!strokeColor.isEmpty()) {
                        svgPath.setStroke(javafx.scene.paint.Color.web(strokeColor));
                    } else {
                        svgPath.setStroke(Color.TRANSPARENT);
                    }

                    // Set stroke width (if specified in the SVG)
                    String strokeWidth = pathElement.getAttribute("stroke-width");
                    if (!strokeWidth.isEmpty()) {
                        svgPath.setStrokeWidth(Double.parseDouble(strokeWidth));
                    }

                    // Set fill color (if specified in the SVG)
                    String fillColor = pathElement.getAttribute("fill");
                    if (!fillColor.isEmpty()) {
                        svgPath.setFill(javafx.scene.paint.Color.web(fillColor));
                    } else {
                        svgPath.setFill(Color.TRANSPARENT);
                    }

                    // Add the SVGPath to the Group
                    this.getChildren().add(svgPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

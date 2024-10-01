package com.campusdual.classroom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * La clase {@code Exercise27} crea dos archivos de lista de compras, uno en formato XML y otro en formato JSON.
 *
 * @version 1.0
 */
public class Exercise27 {

    public static void main(String[] args) {
        // Datos de la lista de la compra (usar LinkedHashMap para mantener el orden)
        Map<String, Integer> shoppingList = new LinkedHashMap<>();
        shoppingList.put("Manzana", 2);
        shoppingList.put("Leche", 1);
        shoppingList.put("Pan", 3);
        shoppingList.put("Huevo", 2);
        shoppingList.put("Queso", 1);
        shoppingList.put("Cereal", 1);
        shoppingList.put("Agua", 4);
        shoppingList.put("Yogur", 6);
        shoppingList.put("Arroz", 2);

        // Crear y guardar los ficheros XML y JSON
        createXMLFile(shoppingList);
        createJSONFile(shoppingList);
    }

    /**
     * Crea un archivo XML con los datos proporcionados en la lista de la compra.
     *
     * @param shoppingList Un mapa que contiene los artículos y sus cantidades.
     */
    public static void createXMLFile(Map<String, Integer> shoppingList) {
        try {
            // Asegurarse de que el directorio existe
            File directory = new File("src/main/resources");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // Nodo raíz <shoppinglist>
            Element root = document.createElement("shoppinglist");
            document.appendChild(root);

            // Nodo <items>
            Element itemsElement = document.createElement("items");
            root.appendChild(itemsElement);

            // Añadir cada artículo como un nodo <item> con atributo quantity
            for (Map.Entry<String, Integer> entry : shoppingList.entrySet()) {
                Element itemElement = document.createElement("item");
                itemElement.setAttribute("quantity", String.valueOf(entry.getValue()));
                itemElement.appendChild(document.createTextNode(entry.getKey()));
                itemsElement.appendChild(itemElement);
            }

            // Crear el archivo XML en src/main/resources/shoppingList.xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Configurar propiedades de salida
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            // Establecer cantidad de indentación (opcional)
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("src/main/resources/shoppingList.xml"));

            transformer.transform(domSource, streamResult);

            System.out.println("Archivo XML generado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un archivo JSON con los datos proporcionados en la lista de la compra.
     *
     * @param shoppingList Un mapa que contiene los artículos y sus cantidades.
     */
    public static void createJSONFile(Map<String, Integer> shoppingList) {
        try {
            // Asegurarse de que el directorio existe
            File directory = new File("src/main/resources");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Crear el objeto raíz para el JSON
            JsonObject rootObject = new JsonObject();
            JsonArray itemsArray = new JsonArray();

            // Añadir cada artículo como un objeto en el array de items
            for (Map.Entry<String, Integer> entry : shoppingList.entrySet()) {
                JsonObject itemObject = new JsonObject();
                itemObject.addProperty("quantity", entry.getValue());
                itemObject.addProperty("text", entry.getKey());
                itemsArray.add(itemObject);
            }

            // Añadir el array items al objeto JSON raíz
            rootObject.add("items", itemsArray);

            // Crear el archivo JSON en src/main/resources/shoppingList.json
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(rootObject);

            // Escribir el JSON en el archivo con codificación UTF-8
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream("src/main/resources/shoppingList.json"), "UTF-8")) {
                writer.write(jsonString);
            }

            System.out.println("Archivo JSON generado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

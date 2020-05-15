package com.ntdat;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public class DictionaryData {
    private static final String VI_EN_DATA_PATH = "./data/Viet_Anh.xml";
    private static final String EN_VI_DATA_PATH = "./data/Anh_Viet.xml";
    private static final String EN_FAVOURITE_PATH = "./data/favourite.en.dat";
    private static final String VI_FAVOURITE_PATH = "./data/favourite.vi.dat";
    private static final String EN_HISTORY_PATH = "./data/history.en.dat";
    private static final String VI_HISTORY_PATH = "./data/history.vi.dat";
    public static final int EN_TO_VI = 1;
    public static final int VI_TO_EN = 2;

    public static HashMap<String, String> LoadDictionary(int type) {
        HashMap<String,String> dictionaryHashMap = new HashMap<>();
        String path = null;
        switch (type) {
            case VI_TO_EN:
                path = VI_EN_DATA_PATH;
                break;
            case EN_TO_VI:
                path = EN_VI_DATA_PATH;
                break;
            default:
                break;
        }

        try {
            File inputFile = new File(path);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputFile);
            document.getDocumentElement().normalize();

            NodeList recordNodeList = document.getElementsByTagName("record");

            for (int i = 0; i < recordNodeList.getLength(); i++) {
                Node node = recordNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String key = element.getElementsByTagName("word").item(0).getTextContent();
                    String value = element.getElementsByTagName("meaning").item(0).getTextContent();
                    dictionaryHashMap.put(key,value);
                }
            }
        } catch (FileNotFoundException e) {
            UserInterface ui = new UserInterface();
            ui.ShowAlert("[!] Khong tim thay du lieu tu dien.");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dictionaryHashMap;
    }

    public static boolean StoreDictionary(int type, HashMap<String, String> dictionaryMap) {
        String path = ".";
        switch (type) {
            case VI_TO_EN:
                path = VI_EN_DATA_PATH;
                break;
            case EN_TO_VI:
                path = EN_VI_DATA_PATH;
                break;
            default:
                break;
        }

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder =documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element rootElement = document.createElement("dictionary");
            document.appendChild(rootElement);

            dictionaryMap.forEach((key, value) -> {
                Element record = document.createElement("record");
                Element word = document.createElement("word");
                word.appendChild(document.createTextNode(key));
                Element meaning = document.createElement("meaning");
                meaning.appendChild(document.createTextNode(value));
                record.appendChild(word);
                record.appendChild(meaning);
                rootElement.appendChild(record);
            });

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            return false;
        } catch (TransformerException e) {
            return false;
        }
        return true;
    }

    public static List<String> LoadFavouriteList(int type) {
        String path = null;
        switch (type) {
            case EN_TO_VI:
                path = EN_FAVOURITE_PATH;
                break;
            case VI_TO_EN:
                path = VI_FAVOURITE_PATH;
                break;
            default:
                break;
        }
        List<String> favList = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            favList = (List<String>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return favList;
        }
    }

    public static void StoreFavouriteList(int type, List<String> favList) {
        String path = null;
        switch (type) {
            case EN_TO_VI:
                path = EN_FAVOURITE_PATH;
                break;
            case VI_TO_EN:
                path = VI_FAVOURITE_PATH;
            default:
                break;
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(favList);
            oos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<HistoryRecord> LoadHistory(int type) {
        String path = null;
        switch (type) {
            case EN_TO_VI:
                path = EN_HISTORY_PATH;
                break;
            case VI_TO_EN:
                path = VI_HISTORY_PATH;
                break;
            default:
                break;
        }
        List<HistoryRecord> historyRecordList = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            historyRecordList = (List<HistoryRecord>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            return historyRecordList;
        }
    }

    public static void StoreHistory(int type, List<HistoryRecord> historyRecordList) {
        String path = null;
        switch (type) {
            case EN_TO_VI:
                path = EN_HISTORY_PATH;
                break;
            case VI_TO_EN:
                path = VI_HISTORY_PATH;
                break;
            default:
                break;
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(historyRecordList);
            oos.close();
        } catch (FileNotFoundException e) {
            UserInterface ui = new UserInterface();
            ui.ShowAlert("[!] Khong tim thay du lieu lich su.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

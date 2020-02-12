package com.example.kicinamartin.songsplayer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplications {

    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;

    public ParseApplications() {
        this.applications = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public void parse(String xmlData){
        //boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";
        String tmpTitle="";
        boolean first=true;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                if (first){
                    if ("title".equalsIgnoreCase(tagName)){
                        XmlPullParser tmpXpp = xpp;
                        tmpTitle = tmpXpp.nextText();
                        first=false;
                    }
                }

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if ("entry".equalsIgnoreCase(tagName)){
                            inEntry=true;
                            currentRecord = new FeedEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inEntry){

                            if ("entry".equalsIgnoreCase(tagName)){
                                applications.add(currentRecord);
                                inEntry = false;
                            }
                            else if ("title".equalsIgnoreCase(tagName)){
                                currentRecord.setName(textValue);
                            }
                            else if ("published".equals(tagName)){
                                String date = textValue;
                                date = date.substring(0,10);
                                String day,month,year;
                                year = date.substring(0,4);
                                month = date.substring(5,7);
                                day = date.substring(8,10);
                                String newDate = day+'.'+month+'.'+year;
                                currentRecord.setReleaseDate("Release date: "+newDate);
                            }
                            else if ("statistics".equals(tagName)){   //upravit formatovanie1
                                currentRecord.setViews("views: " + xpp.getAttributeValue(null,"views"));
                            }
                            else if ("thumbnail".equals(tagName)){
                                currentRecord.setImageURL(xpp.getAttributeValue(null,"url"));
                            }
                            else if ("videoId".equals(tagName)){
                                currentRecord.setSongURL(textValue);
                            }
                        }
                        break;
                    default:
                        //nic nerobi
                }
                eventType = xpp.next();
                if (currentRecord!=null) {
                    currentRecord.setTitle(tmpTitle);
                }
            }

        }catch (Exception e){
            //status = false;
            e.printStackTrace();
        }
        //return status;
    }
}
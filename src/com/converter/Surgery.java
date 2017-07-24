package com.converter;


import java.util.HashMap;
import java.util.Map;

public class Surgery extends JsonElement
{
    private int id;
    private String name;
    private String materials;
    private HashMap<String, String> responses;
    private String compatibles;
    private String[] specific;
    private String story;

    private String json;

    //<editor-fold desc="Getters and setters">

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public HashMap<String, String> getResponses() {
        return responses;
    }

    public boolean setResponses(String responses) {
        this.responses = new HashMap<String, String>();

        if (responses.equals(""))
            return true;

        responses = responses.substring(1);
        String[] splitted =  responses.split("},\\{");

        for (String response : splitted)
        {
            String[] subSplitted = response.split(":");
            if (subSplitted.length != 2)
                return false;

            if (subSplitted[0].charAt(subSplitted[0].length()-1) == ' ')
                subSplitted[0] = subSplitted[0].substring(0, subSplitted[0].length()-1);

            if (subSplitted[1].charAt(0) == ' ')
                subSplitted[1] = subSplitted[1].substring(1);

            this.responses.put(subSplitted[0], subSplitted[1]);
        }

        return true;
    }

    public String getCompatibles() {
        return compatibles;
    }

    public void setCompatibles(String compatibles) {
        this.compatibles = compatibles;
    }

    public String[] getSpecific() {
        return specific;
    }

    public boolean setSpecific(String specificStr) {
        if (specificStr.equals(""))
            return false;

        this.specific = specificStr.split(",");
        for (int i = 0; i < specific.length; ++i)
        {
            if (specific[i].charAt(0) == ' ')
            {
                specific[i] =  specific[i].substring(1);
            }
        }

        return true;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    //</editor-fold>

    public String toJson(int offset)
    {
        int i = 1;

        json = "";
        json += addChars('\t', offset) + "{\n";
        json += addChars('\t', offset+1) + "\"id\": " + id + ",\n";
        json += addChars('\t', offset+1) + "\"nom\": \"" + name + "\",\n";
        json += addChars('\t', offset+1) + "\"materiels\": [ " + materials + " ],\n";
        json += addChars('\t', offset+1) + "\"reponses\": [\n";
        for (Map.Entry<String, String> response : responses.entrySet())
        {
            json += addChars('\t', offset+3) + "{ \"" + response.getKey() + "\": \"" + response.getValue() + "\" }" + (responses.size() == i ? "\n" : ",\n");
            ++i;
        }
        json += addChars('\t', offset+2) + "],\n";
        json += addChars('\t', offset+1) + "\"compatibles\": [" + compatibles + "],\n";
        json += addChars('\t', offset+1) + "\"spec\": [";
        if (specific != null)
        {
            i = 1;
            for (String spec : specific)
            {
                json += " \"" + spec + "\"" + (i == specific.length ? " " : ",");
                ++i;
            }
        }
        json += "],\n";
        json += addChars('\t', offset+1) + "\"histoire\": \"" + story + "\"\n";
        json += addChars('\t', offset) + "}";
        return json;
    }
}

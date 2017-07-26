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

    //<editor-fold desc="Getters and setters">

    public void setId(Integer id) throws JsonElementException {
        if (id == null)
            throw new JsonElementException("Identifiant non renseigné");
        if (id < 0)
            throw new JsonElementException("Identifiant non valide (" + id + ")");

        this.id = id;
    }

    void setName(String name) throws JsonElementException {
        if (name.equals(""))
            throw new JsonElementException("Nom non renseigné");

        this.name = name;
    }

    void setMaterials(String materials) throws JsonElementException {
        if (materials.matches("[a-zA-Z]"))
            throw new JsonElementException("Texte detecté dans la liste des identifiants de matériels");

        this.materials = materials;
    }

    void setResponses(String responses) throws JsonElementException {
        this.responses = new HashMap<>();

        if (responses.equals(""))
            return;

        responses = responses.substring(1);
        String[] splitted =  responses.split("},\\{");
        int i = 1;

        for (String response : splitted)
        {
            String[] subSplitted = response.split(":");
            if (subSplitted.length != 2)
                throw new JsonElementException("Structure de la question " + i + " erronée, séparez l'identifiant de la question et la réponse par \":\"");

            if (subSplitted[0].charAt(subSplitted[0].length()-1) == ' ')
                subSplitted[0] = subSplitted[0].substring(0, subSplitted[0].length()-1);

            if (subSplitted[1].charAt(0) == ' ')
                subSplitted[1] = subSplitted[1].substring(1);

            this.responses.put(subSplitted[0], subSplitted[1]);
            ++i;
        }
    }

    public void setCompatibles(String compatibles) {
        this.compatibles = compatibles;
    }

    void setSpecific(String specificStr) {
        if (specificStr.equals(""))
            return;

        this.specific = specificStr.split(",");
        for (int i = 0; i < specific.length; ++i)
        {
            if (specific[i].charAt(0) == ' ')
            {
                specific[i] =  specific[i].substring(1);
            }
        }
    }

    public void setStory(String story) {
        this.story = story;
    }

    //</editor-fold>

    @Override
    String toJson(int offset)
    {
        int i = 1;

        StringBuilder json = new StringBuilder();
        json.append(addTabs(offset)).append("{\n");
        json.append(addTabs(offset + 1)).append("\"id\": ").append(id).append(",\n");
        json.append(addTabs(offset + 1)).append("\"nom\": \"").append(name).append("\",\n");
        json.append(addTabs(offset + 1)).append("\"materiels\": [ ").append(materials).append(" ],\n");
        json.append(addTabs(offset + 1)).append("\"reponses\": [");
        for (Map.Entry<String, String> response : responses.entrySet())
        {
            json.append("\n").append(addTabs(offset + 2)).append("{ \"").append(response.getKey()).append("\": \"").append(response.getValue()).append("\" }").append(responses.size() == i ? "\n" : ",\n");
            ++i;
        }
        if (responses.size() >= 1)
            json.append(addTabs(offset + 1));
        json.append("],\n");
        json.append(addTabs(offset + 1)).append("\"compatibles\": [ ").append(compatibles).append(" ],\n");
        json.append(addTabs(offset + 1)).append("\"spec\": [");
        if (specific != null)
        {
            i = 1;
            for (String spec : specific)
            {
                json.append(" \"").append(spec).append("\"").append(i == specific.length ? " " : ",");
                ++i;
            }
        }
        json.append("],\n");
        json.append(addTabs(offset + 1)).append("\"histoire\": \"").append(story).append("\"\n");
        json.append(addTabs(offset)).append("}");
        return json.toString();
    }
}

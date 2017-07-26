package com.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Patient extends JsonElement
{
    private int id;
    private String name;
    private String firstname;
    private String sex;
    private int age;
    private int height;
    private int weight;
    private String materials;
    private HashMap<String, String> responses;
    private String[] specific;

    //<editor-fold desc="Getters and setters">

    public void setId(Integer id) throws JsonElementException
    {
        if (id == null)
            throw new JsonElementException( "Identifiant non renseigné");
        if (id < 0)
            throw new JsonElementException("Identifiant non valide (" + id + ")");
        this.id = id;
    }

    void setName(String name) throws JsonElementException {
        if (name.equals(""))
            throw new JsonElementException("Nom non renseigné");

        this.name = name;
    }

    void setFirstname(String firstname) throws JsonElementException {
        if (firstname.equals(""))
            throw new JsonElementException("Prénom non renseigné");

        this.firstname = firstname;
    }

    void setSex(String sex) throws JsonElementException {
        if (sex.equals(""))
            throw new JsonElementException("Sexe non renseigné");

        if (!sex.equals("Homme") && !sex.equals(("Femme")))
            throw  new JsonElementException("Sexe inconnu");

        this.sex = sex;
    }

    void setAge(Integer age) throws JsonElementException {
        if (age == null)
            throw new JsonElementException("Age non renseigné");
        if (age < 0 || age > 200)
            throw new JsonElementException("Age non valide (" + age + ")");

        this.age = age;
    }

    void setHeight(Integer height) throws JsonElementException {
        if (height == null)
            throw new JsonElementException("Taille non renseignée");
        if (height < 0 || height > 300)
            throw new JsonElementException("Taille non valide (" + height + ")");

        this.height = height;
    }

    void setWeight(Integer weight) throws JsonElementException {
        if (weight == null)
            throw new JsonElementException("Taille non renseignée");
        if (weight < 0 || weight > 300)
            throw new JsonElementException("Taille non valide (" + weight + ")");

        this.weight = weight;
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

    //</editor-fold>

    @Override
    String toJson(int offset) {
        int i = 1;

        StringBuilder json = new StringBuilder();
        json.append(addTabs(offset)).append("{\n");
        json.append(addTabs(offset + 1)).append("\"id\": ").append(id).append(",\n");
        json.append(addTabs(offset + 1)).append("\"nom\": \"").append(name).append("\",\n");
        json.append(addTabs(offset + 1)).append("\"prenom\": \"").append(firstname).append("\",\n");
        json.append(addTabs(offset + 1)).append("\"sexe\": \"").append(sex).append("\",\n");
        json.append(addTabs(offset + 1)).append("\"age\": ").append(age).append(",\n");
        json.append(addTabs(offset + 1)).append("\"taille\": ").append(height).append(",\n");
        json.append(addTabs(offset + 1)).append("\"poid\": ").append(weight).append(",\n");
        json.append(addTabs(offset + 1)).append("\"materiels\": [ ").append(materials).append(" ],\n");
        json.append(addTabs(offset + 1)).append("\"responses\": [");
        if (responses.size() >= 1)
        {
            i = 1;
            json.append("\n");
            for (Map.Entry<String, String> response : responses.entrySet())
            {
                json.append(addTabs(offset + 2)).append("{ \"").append(response.getKey()).append("\": \"").append(response.getValue()).append("\" }").append(responses.size() == i ? "\n" : ",\n");
                ++i;
            }
            json.append(addTabs(offset + 1));
        }
        json.append("],\n");
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
        json.append("]\n");
        json.append(addTabs(offset)).append("}");
        return json.toString();
    }
}

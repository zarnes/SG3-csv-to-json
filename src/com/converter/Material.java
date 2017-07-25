package com.converter;

public class Material extends JsonElement {
    private int id;
    private String material;
    private String category;

    //<editor-fold desc="Getters and setters">

    public void setId(Integer id) throws JsonElementException
    {
        if (id == null)
            throw new JsonElementException("Ligne " + MainFrame.currentLine +  " : Identifiant non renseigné");
        if (id < 0)
            throw new JsonElementException("Ligne " + MainFrame.currentLine + " : Identifiant non valide (" + id + ")");
        this.id = id;
    }

    public void setMaterial(String material) throws JsonElementException {
        if (material.equals(""))
            throw new JsonElementException("Nom du matériel non renseigné");

        this.material = material;
    }

    public void setCategory(String category) throws JsonElementException {
        if (category.equals(""))
            throw new JsonElementException("Catégorie non renseigné");
        this.category = category;
    }

    //</editor-fold>

    @Override
    String toJson(int offset)
    {
        StringBuilder json = new StringBuilder();
        json.append(addTabs(offset)).append("{\n");
        json.append(addTabs(offset + 1)).append("\"id\": ").append(id).append(",\n");
        json.append(addTabs(offset + 1)).append("\"materiel\": \"").append(material).append("\",\n");
        json.append(addTabs(offset + 1)).append("\"cat\": \"").append(category).append("\"\n");
        json.append(addTabs(offset)).append("}");
        return json.toString();
    }
}

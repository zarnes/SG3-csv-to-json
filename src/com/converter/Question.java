package com.converter;

public class Question extends JsonElement
{
    private String id;
    private String question;
    private String response;

    //<editor-fold desc="Getters and setters">

    void setId(String id) throws JsonElementException {
        if (id.equals(""))
            throw new JsonElementException("Ligne " + MainFrame.currentLine + " : Identifiant non renseigné");

        this.id = id;
    }

    void setQuestion(String question) throws JsonElementException {
        if (question.equals(""))
            throw new JsonElementException("Ligne " + MainFrame.currentLine + " : Question non renseignée");

        this.question = question;
    }

    void setResponse(String response) throws JsonElementException {
        if (response.equals(""))
            throw new JsonElementException("Ligne " + MainFrame.currentLine + " : Réponse non renseignée");

        this.response = response;
    }

    //</editor-fold>

    @Override
    String toJson(int offset) {
        StringBuilder json = new StringBuilder();
        json.append(addTabs(offset)).append("{\n");
        json.append(addTabs(offset + 1)).append("\"id\": \"").append(id).append("\",\n");
        json.append(addTabs(offset + 1)).append("\"question\": \"").append(question).append("\",\n");
        json.append(addTabs(offset + 1)).append("\"reponse\": \"").append(response).append("\",\n");
        json.append(addTabs(offset)).append("}");
        return json.toString();
    }
}

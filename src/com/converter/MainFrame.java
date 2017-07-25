package com.converter;

import com.opencsv.CSVReader;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

class MainFrame extends JFrame {

    private enum ButtonType
    {
        surgeries,
        patients,
        materials,
        questions
    }

    private String encoding = "UTF-8";
    static JTextArea log;
    static int currentLine;

    MainFrame()
    {
        // General setup
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //<editor-fold desc="Menu Creation">

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Encodage des csv");
        ButtonGroup group = new ButtonGroup();

        JRadioButtonMenuItem button = new JRadioButtonMenuItem("UTF-8");
        button.doClick();
        button.addActionListener(e -> {
            encoding = "UTF-8";
            log.append("Encodage des csv configuré en UTF-8");
        });
        group.add(button);
        menu.add(button);

        button = new JRadioButtonMenuItem("Win-1252");
        group.add(button);
        menu.add(button);
        button.addActionListener(e -> {
            encoding = "Windows-1252";
            log.append("Encodage des csv configuré en Windows-1252");
        });

        button = new JRadioButtonMenuItem("MacRoman");
        group.add(button);
        menu.add(button);
        button.addActionListener(e -> {
            encoding = "MacRoman";
            log.append("Encodage des csv configuré en MacRoman");
        });

        bar.add(menu);
        setJMenuBar(bar);

        //</editor-fold>

        //<editor-fold desc="Elements creation and their constraints">

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 0.25;
        c.gridy = 0;
        c.gridwidth = 1;
        JButton surgeries = new JButton("Chirurgies");
        add(surgeries, c);

        c.gridx = 1;
        JButton patients = new JButton("Patients");
        add(patients, c);

        c.gridx = 2;
        JButton materials = new JButton("Materiels");
        add(materials, c);

        c.gridx = 3;
        JButton questions = new JButton("Questions");
        add(questions, c);

        c.weightx = 0;
        c.weighty = 2;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 0;
        c.fill = GridBagConstraints.BOTH;

        log = new JTextArea();
        log.setEditable(false);
        JScrollPane scroll = new JScrollPane(log);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scroll, c);

        //</editor-fold>

        //<editor-fold desc="Button events listeners attribution">

        surgeries.addActionListener(e -> LoadCsv(ButtonType.surgeries));

        patients.addActionListener(e -> LoadCsv(ButtonType.patients));

        materials.addActionListener(e -> LoadCsv(ButtonType.materials));

        questions.addActionListener(e -> LoadCsv(ButtonType.questions));

        //</editor-fold>

        this.setVisible(true);
    }

    private boolean LoadCsv(ButtonType button)
    {
        // TODO mieux verboser les log
        File file;
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
            file = fc.getSelectedFile();
        else
            return false;

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), encoding), ';', '"', 1);

            ArrayList<JsonElement> elements = new ArrayList<>();
            HashSet<String> hashID = new HashSet<>();
            HashSet<String> hashID2 = new HashSet<>();
            String[] CSVline;
            StringBuilder jsonOutput = new StringBuilder("{\n");
            currentLine = 2;

            // Switch loops fetch data from csv files
            switch (button) {
                case surgeries:
                    while ((CSVline = reader.readNext()) != null) {
                        Surgery surgery = new Surgery();
                        surgery.setId(CSVline[0].equals("") ? null : Integer.parseInt(CSVline[0]));
                        surgery.setName(CSVline[1]);
                        surgery.setMaterials(CSVline[2]);
                        surgery.setResponses(CSVline[3]);
                        surgery.setCompatibles(CSVline[4]);
                        surgery.setSpecific(CSVline[5]);
                        surgery.setStory(CSVline[6]);
                        elements.add(surgery);
//TODO a toast
                        hashID.add(CSVline[0]);
                        if (hashID.size() < elements.size())
                            throw new JsonElementException("L'identifiant " + CSVline[0] + " existe déjà");

                        hashID2.add(CSVline[1]);
                        if (hashID2.size() < elements.size())
                            throw new JsonElementException("La chirurgie " + CSVline[1] + " existe déjà");

                        ++currentLine;
                    }
                    jsonOutput.append("\t\"chirurgies\": [\n\n");
                    break;

                case patients:
                    while ((CSVline = reader.readNext()) != null) {
                        Patient patient = new Patient();
                        patient.setId(CSVline[0].equals("") ? null : Integer.parseInt(CSVline[0]));
                        patient.setName(CSVline[1]);
                        patient.setFirstname(CSVline[2]);
                        patient.setSex(CSVline[3]);
                        patient.setAge((CSVline[0].equals("") ? null : Integer.parseInt(CSVline[4])));
                        patient.setHeight(CSVline[0].equals("") ? null : Integer.parseInt(CSVline[5]));
                        patient.setWeight(CSVline[0].equals("") ? null : Integer.parseInt(CSVline[6]));
                        patient.setMaterials(CSVline[7]);
                        patient.setResponses(CSVline[8]);
                        patient.setSpecific(CSVline[9]);
                        elements.add(patient);

                        hashID.add(CSVline[0]);
                        if (hashID.size() < elements.size())
                            throw new JsonElementException("L'identifiant " + CSVline[0] + " existe déjà");

                        ++currentLine;
                    }
                    jsonOutput.append("\t\"materiels\": [\n\n");
                    break;

                case materials:
                    while ((CSVline = reader.readNext()) != null) {
                        Material material = new Material();
                        material.setId(CSVline[0].equals("") ? null : Integer.parseInt(CSVline[0]));
                        material.setMaterial(CSVline[1]);
                        material.setCategory(CSVline[2]);
                        elements.add(material);

                        hashID.add(CSVline[0]);
                        if (hashID.size() < elements.size())
                            throw new JsonElementException("L'identifiant " + CSVline[0] + " existe déjà");

                        hashID2.add(CSVline[1]);
                        if (hashID2.size() < elements.size())
                            throw new JsonElementException("Le matériel " + CSVline[1] + " existe déjà");

                        ++currentLine;
                    }
                    jsonOutput.append("\t\"materiels\": [\n\n");
                    break;

                case questions:
                    while ((CSVline = reader.readNext()) != null) {
                        Question question = new Question();
                        question.setId(CSVline[0]);
                        question.setQuestion(CSVline[1]);
                        question.setResponse(CSVline[2]);
                        elements.add(question);

                        hashID.add(CSVline[0]);
                        if (hashID.size() < elements.size())
                            throw new JsonElementException("L'identifiant " + CSVline[0] + " existe déjà");

                        hashID2.add(CSVline[1]);
                        if (hashID2.size() < elements.size())
                            throw new JsonElementException("La question " + CSVline[1] + " existe déjà");

                        ++currentLine;
                    }
                    jsonOutput.append("\t\"questions\": [\n\n");
                    break;

                default:
                    return false;
            }

            // Build the json
            for (int i = 0; i < elements.size(); ++i)
            {
                jsonOutput.append(elements.get(i).toJson(2));
                if (i < elements.size()-1)
                    jsonOutput.append(",\n\n");
                else
                    jsonOutput.append("\n");
            }
            jsonOutput.append("\t]\n}");

            PrintWriter writer = new PrintWriter(file.getParent() + "/" + file.getName().split("csv")[0] + "json", "UTF-8");
            writer.print(jsonOutput);
            writer.close();
            log.append("Fichier Json crée\n");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonElementException e) {}
        return false;
    }
}

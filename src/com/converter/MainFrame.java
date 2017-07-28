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
    static JTextArea log = new JTextArea();
    static int currentLine;

    MainFrame()
    {
        //<editor-fold desc="Menu Creation">

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Encodage des csv");
        ButtonGroup group = new ButtonGroup();

        JRadioButtonMenuItem button = new JRadioButtonMenuItem("UTF-8");
        button.addActionListener(e -> {
            encoding = "UTF-8";
            log.append("Encodage des csv configuré en UTF-8.\n");
        });
        group.add(button);
        button.doClick();
        menu.add(button);

        button = new JRadioButtonMenuItem("Win-1252");
        group.add(button);
        menu.add(button);
        button.addActionListener(e -> {
            encoding = "Windows-1252";
            log.append("Encodage des csv configuré en Windows-1252.\n");
        });

        button = new JRadioButtonMenuItem("MacRoman");
        group.add(button);
        menu.add(button);
        button.addActionListener(e -> {
            encoding = "MacRoman";
            log.append("Encodage des csv configuré en MacRoman.\n");
        });

        bar.add(menu);


        menu = new JMenu("Actions rapides");
        JMenuItem button2 = new JMenuItem("Générer tout les json");
        menu.add(button2);
        button2.addActionListener(e -> {
            if (LoadCsv(ButtonType.surgeries))
                if (LoadCsv(ButtonType.patients))
                    if (LoadCsv(ButtonType.materials))
                        LoadCsv(ButtonType.questions);
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
        c.fill = GridBagConstraints.BOTH;

        log.setEditable(false);
        log.setLineWrap(true);
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

        // General setup
        setSize(370, 200);
        setMinimumSize(new Dimension(370, 200));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private boolean LoadCsv(ButtonType button)
    {
        File file;
        JFileChooser fc = new JFileChooser();

        switch (button)
        {
            case surgeries:
                log.append("Sélection du fichier csv des chirurgies\n");
                break;
            case patients:
                log.append("Sélection du fichier csv des patients\n");
                break;
            case materials:
                log.append("Sélection du fichier csv du matériel\n");
                break;
            case questions:
                log.append("Sélection du fichier csv des questions\n");
                break;
            default:
                return false;
        }

        log.setCaretPosition(log.getDocument().getLength());

        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION)
            file = fc.getSelectedFile();
        else {
            log.append("Fichier non chargé\n");
            log.setCaretPosition(log.getDocument().getLength());
            return false;
        }

        String[] fileSplitted = file.getName().split("\\.");
        if (!fileSplitted[fileSplitted.length-1].equals("csv"))
        {
            log.append("Le fichier selectionné n'est pas un fichier csv.\n");
            return false;
        }

        try {
            log.append("Lecture du fichier " + file.getName() + "\n");
            log.setCaretPosition(log.getDocument().getLength());
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), encoding), ';', '"', 1);

            ArrayList<JsonElement> elements = new ArrayList<>();
            HashSet<String> hashID = new HashSet<>();
            HashSet<String> hashID2 = new HashSet<>();
            String[] CSVline;
            StringBuilder jsonOutput = new StringBuilder("{\n");
            currentLine = 2;

            //<editor-fold desc="Lecture du fichier">

            // Switch loops fetch data from csv files
            switch (button) {
                case surgeries:
                    while ((CSVline = CheckLine(reader, 7)) != null)
                    {
                        Surgery surgery = new Surgery();
                        surgery.setId(CSVline[0].equals("") ? null : Integer.parseInt(CSVline[0]));
                        surgery.setName(CSVline[1]);
                        surgery.setMaterials(CSVline[2]);
                        surgery.setResponses(CSVline[3]);
                        surgery.setCompatibles(CSVline[4]);
                        surgery.setSpecific(CSVline[5]);
                        surgery.setStory(CSVline[6]);
                        elements.add(surgery);

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
                    while ((CSVline = CheckLine(reader, 10)) != null)
                    {
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
                    jsonOutput.append("\t\"patients\": [\n\n");
                    break;

                case materials:
                    while ((CSVline = CheckLine(reader, 3)) != null)
                    {
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
                    while ((CSVline = CheckLine(reader, 4)) != null)
                    {
                        Question question = new Question();
                        question.setId(CSVline[0]);
                        question.setQuestion(CSVline[1]);
                        question.setResponse(CSVline[2]);
                        elements.add(question);

                        hashID.add(CSVline[0]);
                        if (hashID.size() < elements.size())
                            throw new JsonElementException("L'identifiant " + CSVline[1] + " existe déjà");

                        hashID2.add(CSVline[1]);
                        if (hashID2.size() < elements.size())
                            throw new JsonElementException("La question " + CSVline[2] + " existe déjà");

                        ++currentLine;
                    }
                    jsonOutput.append("\t\"questions\": [\n\n");
                    break;

                default:
                    return false;
            }

            //</editor-fold>

            // Build the json
            log.append("Génération du json");
            log.setCaretPosition(log.getDocument().getLength());
            for (int i = 0; i < elements.size(); ++i)
            {
                log.append(".");
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
            log.append("\nFichier Json créé\n");
            log.setCaretPosition(log.getDocument().getLength());

        } catch (JsonElementException e) {
            return false;
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String[] CheckLine(CSVReader reader, int length)
    {
        String[] CSVline = new String[0];
        try {
            CSVline = reader.readNext();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (CSVline == null)
        {
            return null;
        }

        /*if (CSVline.length != length)
            return null;*/

        for (String data : CSVline)
        {
            if (!data.equals(""))
                return CSVline;
        }

        return null;
    }
}

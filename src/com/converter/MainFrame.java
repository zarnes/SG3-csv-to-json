package com.converter;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MainFrame extends JFrame {
    private enum ButtonType
    {
        surgeries,
        patients,
        materials,
        questions
    };

    private String encoding;

    private HashMap<String, JComponent> ui;

    public MainFrame()
    {
        // General setup
        ui = new HashMap<>();
        setLayout(new GridBagLayout());
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //<editor-fold desc="Menu Creation">

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Encodage des csv");
        ButtonGroup group = new ButtonGroup();

        JRadioButtonMenuItem button = new JRadioButtonMenuItem("UTF-8");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encoding = "UTF-8";
            }
        });
        group.add(button);
        menu.add(button);

        button = new JRadioButtonMenuItem("Win-1252");
        group.add(button);
        menu.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encoding = "Windows-1252";
            }
        });

        button = new JRadioButtonMenuItem("MacRoman");
        group.add(button);
        menu.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encoding = "MacRoman";
            }
        });

        bar.add(menu);

        //</editor-fold>

        //<editor-fold desc="Elements creation and their constraints">

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        add(bar, c);

        c.weightx = 0.25;
        c.gridy = 1;
        c.gridwidth = 1;
        ui.put("surgeries", new JButton("Chirurgies"));
        add(ui.get("surgeries"), c);

        c.gridx = 1;
        ui.put("patients", new JButton("Patients"));
        add(ui.get("patients"), c);

        c.gridx = 2;
        ui.put("materials", new JButton("Materiel"));
        add(ui.get("materials"), c);

        c.gridx = 3;
        ui.put("questions", new JButton("Questions"));
        add(ui.get("questions"), c);

        c.weightx = 0;
        c.weighty = 2;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 0;
        c.fill = GridBagConstraints.BOTH;

        JTextArea logger = new JTextArea();
        logger.setEditable(false);
        JScrollPane scroll =  new JScrollPane(logger);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        ui.put("logger", logger);
        ui.put("scroll", scroll);
        add(scroll, c);
        //</editor-fold>

        //<editor-fold desc="Button events listeners attribution">

        JButton buttonBar = (JButton) ui.get("surgeries");
        buttonBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoadCsv(ButtonType.surgeries);
            }
        });

        buttonBar = (JButton) ui.get("patients");
        buttonBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoadCsv(ButtonType.patients);
            }
        });

        buttonBar = (JButton) ui.get("materials");
        buttonBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoadCsv(ButtonType.materials);
            }
        });

        buttonBar = (JButton) ui.get("questions");
        buttonBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoadCsv(ButtonType.questions);
            }
        });

        //</editor-fold>

        this.setVisible(true);
        System.out.println("frame ready");
    }

    private boolean LoadCsv(ButtonType button)
    {
        File file;
        JTextArea log = (JTextArea) ui.get("logger");
        JScrollPane scroll = (JScrollPane) ui.get("scroll");

        //scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum()); // TODO use this

        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
            file = fc.getSelectedFile();
        else
            return false;

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), encoding), ';', '"', 1);
            FileReader re = new FileReader(file);
            char[] buffer = new char[50];
            re.read(buffer);

            ArrayList<JsonElement> elements = new ArrayList<JsonElement>();
            String[] CSVline;

            switch (button) {
                case surgeries:
                    while ((CSVline = reader.readNext()) != null) {
                        Surgery surgery = new Surgery();
                        surgery.setId(Integer.parseInt(CSVline[0]));
                        surgery.setName(CSVline[1]);
                        surgery.setMaterials(CSVline[2]);
                        surgery.setResponses(CSVline[3]);
                        surgery.setCompatibles(CSVline[4]);
                        surgery.setSpecific(CSVline[5]);
                        surgery.setStory(CSVline[6]);
                        elements.add(surgery);
                    }
                    break;

                case patients:
                    return true;

                case materials:
                    return true;

                case questions:
                    return true;

                default:
                    return false;
            }

            String jsonOutput = "{\n";
            jsonOutput += "\t\"chirurgies\": [\n\n";
            for (int i = 0; i < elements.size(); ++i)
            {
                jsonOutput += elements.get(i).toJson(2);
                if (i < elements.size()-1)
                    jsonOutput += ",\n\n";
                else
                    jsonOutput += "\n";
            }
            jsonOutput += "\t]\n}";
            log.append(jsonOutput);

            PrintWriter writer = new PrintWriter(file.getAbsolutePath() + file.getName() + ".json", "UTF-8");
            writer.print(jsonOutput);
            writer.close();
            log.append("\nJson created\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

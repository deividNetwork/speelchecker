/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellchecker.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Normalizer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import spellchecker.dictionary.ManageDictionary;
import spellchecker.transposition.WordTransposition;

/**
 *
 * @author deividnetwork
 */
public class LauncherJFrame extends JFrame {

    JPanel contentPane = new JPanel();

    JTextArea inputTextArea = new JTextArea();

    JTextArea outputTextArea = new JTextArea();

    JButton button = new JButton("Validar texto");

    ManageDictionary dictionary = new ManageDictionary();

    public LauncherJFrame() {
        super("Corretor Ortográfico");
        initialize();
    }

    public void initialize() {
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(getContentPane());
        this.setVisible(true);
        this.setResizable(false);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                correctSpelling();
            }
        });

    }

    @Override
    public JPanel getContentPane() {
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(new Color(239, 240, 241));

        JLabel inputLabel = new JLabel("Digite abaixo para validar a ortografia");
        inputLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFocusable(true);
        inputTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane inputScroll = new JScrollPane(inputTextArea);
        inputScroll.setBorder(new EmptyBorder(0, 0, 10, 0));
        inputScroll.setBackground(null);

        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setEditable(false);
        outputTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane outputScroll = new JScrollPane(outputTextArea);
        outputScroll.setBorder(new EmptyBorder(0, 10, 10, 0));
        outputScroll.setBackground(null);

        contentPane.add(inputLabel, BorderLayout.PAGE_START);
        contentPane.add(inputScroll, BorderLayout.CENTER);
        contentPane.add(outputScroll, BorderLayout.LINE_END);
        contentPane.add(button, BorderLayout.PAGE_END);

        return contentPane;
    }

    /**
     * Correct word for compare
     *
     * @param word
     * @return
     */
    protected String trimWord(String word) {
        String str = Normalizer.normalize(word, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        str = str.replaceAll("\\W+", "");

        return str;
    }

    /**
     * Spelling correction
     */
    protected void correctSpelling() {
        String input = inputTextArea.getText();

        outputTextArea.setText(null);

        for (String word : input.split(" ")) {
            word = trimWord(word);

            if (!dictionary.wordExist(word)) {
                WordTransposition transposition = new WordTransposition(word);

                for (String wordT : transposition.fetchWords()) {
                    if (dictionary.wordExist(wordT)) {
                        if (outputTextArea.getText().isEmpty()) {
                            outputTextArea.setText(word + " (" + wordT.toLowerCase() + ")");
                        } else {
                            outputTextArea.setText(outputTextArea.getText() + "\n" + word + " (" + wordT.toLowerCase() + ")");
                        }

                        break;
                    }
                }
            }
        }

        if (inputTextArea.getText().isEmpty()) {
            outputTextArea.setText("Texto vazio!");
        } else {
            if (outputTextArea.getText().isEmpty()) {
                outputTextArea.setText("Nenhuma transposição correspondente no dicionário");
            }
        }
    }
}

package com;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SIG_GUI extends JFrame implements ActionListener {

    private JPanel panel1;
    private JTable invoicesTable;
    private JButton button1;
    private JButton button2;

    public SIG_GUI(){
        super("Sales Invoice Generator");
        this.setSize(900,700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel1);
//        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        new SIG_GUI().setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

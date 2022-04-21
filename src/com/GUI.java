package com;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GUI extends JFrame implements ActionListener, MouseListener
{
    // Menu Bar
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem loadFileItem;
    private JMenuItem saveFileItem;


    private JSplitPane splitPane;
    // Left Panel
    private JPanel leftPanel;

    private DefaultTableModel invoicesTableModel;
    private JTable invoicesTable;
    private String[] invoicesTableColumns = {"No.", "Date", "Customer", "Total"};
    private String[][] invoicesTableData = csvToArray("src/InvoiceHeader.csv", false);

    private JButton newInvoiceButton;
    private JButton deleteInvoiceButton;

    // Right Panel
    private JPanel rightPanel;
    private int invoiceNumber;

    private JTextField invoiceDateTF;
    private JTextField customerNameTF;

    private int invoiceTotal;

    DefaultTableModel invoiceDetailsTableModel;
    private JTable invoiceDetailsTable;
    private String[] invoiceDetailsTableColumns = {"No.", "Item Name", "Item Price", "Count", "Item Total"};
    private String[][] invoiceDetailsData = csvToArray("src/InvoiceLine.csv", true);

    private JButton saveButton;
    private JButton cancelButton;


    public GUI(){
        super("Sales Invoice Generator");
        setLayout(null);
        setSize(870,700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Menu Bar
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");

        loadFileItem = new JMenuItem("Load File", 'L');
        loadFileItem.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
        loadFileItem.addActionListener(this);
        loadFileItem.setActionCommand("loadFile");
        fileMenu.add(loadFileItem);

        saveFileItem = new JMenuItem("Save File", 'S');
        saveFileItem.setAccelerator(KeyStroke.getKeyStroke('S',KeyEvent.CTRL_DOWN_MASK));
        saveFileItem.addActionListener(this);
        saveFileItem.setActionCommand("saveFile");
        fileMenu.add(saveFileItem);

        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //Left Panel
        invoicesTableModel = new DefaultTableModel(invoicesTableData, invoicesTableColumns);
        invoicesTable = new JTable(invoicesTableModel);
        invoicesTable.addMouseListener(this);
        JScrollPane invoicesTableSP = new JScrollPane(invoicesTable);
        invoicesTableSP.setBounds(20,30,400,540);
        add(invoicesTableSP);

        newInvoiceButton = new JButton("Create New Invoice");
        newInvoiceButton.setBounds(70, 590, 150,20);
        add(newInvoiceButton);

        deleteInvoiceButton = new JButton("Delete Invoice");
        deleteInvoiceButton.setBounds(230, 590, 150,20);
        deleteInvoiceButton.addActionListener(this);
        deleteInvoiceButton.setActionCommand("deleteInvoice");
        add(deleteInvoiceButton);

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++

        // Right Panel
        JLabel invoiceNumberLabel = new JLabel("Invoice Number"+ invoiceNumber);
        invoiceNumberLabel.setBounds(440,20, 400, 20);
        add(invoiceNumberLabel);

        JLabel invoiceDateLabel = new JLabel("Invoice Date");
        invoiceDateLabel.setBounds(440,60, 100, 20);
        add(invoiceDateLabel);

        invoiceDateTF = new JTextField(20);
        invoiceDateTF.setBounds(540,60, 300, 20);
        add(invoiceDateTF);

        JLabel customerNameLabel = new JLabel("Customer Name");
        customerNameLabel.setBounds(440,100, 100, 20);
        add(customerNameLabel);

        customerNameTF = new JTextField(20);
        customerNameTF.setBounds(540,100, 300, 20);
        add(customerNameTF);

        JLabel invoiceTotalLabel = new JLabel("Invoice Total: " + invoiceTotal);
        invoiceTotalLabel.setBounds(440,140, 400, 20);
        add(invoiceTotalLabel);

        rightPanel = new JPanel(null);
        rightPanel.setBounds(440,180,400,400);

        Border blacklineRight = BorderFactory.createTitledBorder("Invoice Items"); // Create border for the panel with Title
        rightPanel.setBorder(blacklineRight);
        getContentPane().add(rightPanel, BorderLayout.CENTER);
        add(rightPanel);

        invoiceDetailsTableModel = new DefaultTableModel(invoiceDetailsData, invoiceDetailsTableColumns);
        invoiceDetailsTable = new JTable(invoiceDetailsTableModel);
        JScrollPane invoiceDetailsTableSP = new JScrollPane(invoiceDetailsTable);
        invoiceDetailsTableSP.setBounds(10,20,380,370);
        rightPanel.add(invoiceDetailsTableSP, BorderLayout.CENTER);

        saveButton = new JButton("Save");
        saveButton.setBounds(500,590,100,20);
        add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(670,590,100,20);
        add(cancelButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var i = invoicesTable.getSelectedRow();
        System.out.println(i);
        switch (e.getActionCommand()) {
            case "saveFile" -> saveInvoice();
            case "loadFile" -> loadInvoice();
            case "deleteInvoice" -> deleteInvoice();
            default -> {
            }
        }

    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void loadInvoice(){
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Comma Separated Values (.csv)", "csv");
        fc.addChoosableFileFilter(restrict);

        int result = fc.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION){
            String path = fc.getSelectedFile().getPath();
            String[][] data = csvToArray(path, true);

            invoiceDetailsTable.setModel(new DefaultTableModel(data,invoiceDetailsTableColumns));
        }

    }

    private void saveInvoice() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save your invoice");

        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Comma Separated Values (.csv)", "csv");
        fc.addChoosableFileFilter(restrict);

        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File csvFile = fc.getSelectedFile();
            System.out.println(csvFile.getName());

            FileWriter fw = null;
            BufferedWriter bw = null;

            try {
                fw = new FileWriter(csvFile);
                bw = new BufferedWriter(fw);
                
                // Iterate over each cell of the table
                for (int i = 0; i < invoiceDetailsTable.getRowCount(); i++) {
                    for (int j = 0; j < invoiceDetailsTable.getColumnCount(); j++) {
                        bw.write(invoiceDetailsTable.getValueAt(i, j).toString() + ","); // Save the value of each cell and follow it with ","
                    }
                    bw.newLine(); // Create a new line (new row)
                }
                JOptionPane.showMessageDialog(this, "Your file has been saved successfully", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "ERROR", "ERROR MESSAGE", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    assert bw != null;
                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void deleteInvoice() {
        int row = invoicesTable.getSelectedRow();
        invoicesTableModel.removeRow(row);
    }

    private String[][] csvToArray(String path, boolean details){

        String line = "";
        BufferedReader br = null;
        String[][] data = new String[5][100];
        try {
            br = new BufferedReader(new FileReader(path));
            int i =0;
            while ((line = br.readLine()) != null)
            {
                String[] arr = line.split(",");
                System.arraycopy(arr, 0, data[i], 0, arr.length);
                i++;
            }

            if(details){
                for(int j = 0; j < 5; j++) {
                    data[j][4] = Integer.toString(Integer.parseInt(data[j][3]) * Integer.parseInt(data[j][2]));
                    invoiceTotal += Integer.parseInt(data[j][4]);
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}

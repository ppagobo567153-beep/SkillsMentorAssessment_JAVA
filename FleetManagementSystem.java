package JavaProg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FleetManagementSystem extends JFrame {
	// set up all reusable variables n objects
	private static final String FILENAME = "CarsBRROOOMMMM.txt";
	private static final String DELIMITER = "#";

	// Use arrays to automatic storing and display in GUI
	String[] Buttons = { "Create", "Update", "Delete", "Display" };
	JButton[] ClickableButtons = new JButton[Buttons.length];

	String[] Labels = { "ID", "Make & Model", "License Plate", "Cost" };
	JLabel[] ActualLabels = new JLabel[Labels.length];
	JTextField[] ActualFields = new JTextField[Labels.length];

	// set panels for better GUI organization
	JPanel inputpnl, buttonpnl, tablepnl;
	// set up early for easier code readability after setting the panels below
	JTable table;
	DefaultTableModel model;
	JScrollPane scroll;

	// constructor for instant operation
	public FleetManagementSystem() {

		setSize(500, 500);
		setLayout(new BorderLayout(10, 10));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// set main panel first
		inputpnl = new JPanel(new GridLayout(4, 2, 5, 5));

		// create fields and labels then contain inside a panel located north
		for (int i = 0; i < Labels.length; i++) {
			ActualLabels[i] = new JLabel(Labels[i]);// per label gets displayed automatically after each loop finishes
			inputpnl.add(ActualLabels[i]);

			ActualFields[i] = new JTextField();// do not separate labels and fields in loop so it is displayed side by
			// side
			inputpnl.add(ActualFields[i]);
		}
		add(inputpnl, BorderLayout.NORTH);

		// create panel for buttons and contain them in southern part of GUI
		buttonpnl = new JPanel(new FlowLayout());

		for (int i = 0; i < Buttons.length; i++) {// separate buttons using flowlayout for fluid organization
			ClickableButtons[i] = new JButton(Buttons[i]);
			buttonpnl.add(ClickableButtons[i]);
		}
		add(buttonpnl, BorderLayout.SOUTH);

		// add final container
		tablepnl = new JPanel(new BorderLayout());

		model = new DefaultTableModel(Labels, 0);
		table = new JTable(model);
		scroll = new JScrollPane(table);
		tablepnl.add(scroll);
		add(tablepnl, BorderLayout.CENTER);

		// each button index gets assigned to a CRUD method
		ClickableButtons[0].addActionListener(e -> ADD());
		ClickableButtons[1].addActionListener(e -> UPDATE());
		ClickableButtons[2].addActionListener(e -> DELETE());
		ClickableButtons[3].addActionListener(e -> LOAD());

		setVisible(true);
	}

	private void LOAD() {
		model.setRowCount(0);
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
			String line;// assign line for readingline function and use delimiters to slice up before
			// displaying on tables
			while ((line = br.readLine()) != null) {
				String[] data = line.split(DELIMITER);
				if (data.length == model.getColumnCount()) {
					model.addRow(data);
				}
			}
			JOptionPane.showMessageDialog(this, "File loaded successfully");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error loading file");
		}
	}

	private void DELETE() {
		int row = table.getSelectedRow();
		if (row == -1) {// if there is no row selected, it stops the user and asks for input
			JOptionPane.showMessageDialog(this, "Select a row to delete");
			return;
		}
		model.removeRow(row);// there is row selected on the model
		SAVE();//always save input values per necessary method

	}

	private void CLEAR() {
		for (JTextField field : ActualFields) {
			field.setText("");
		}
		table.clearSelection();
	}

	private void UPDATE() {
		int row = table.getSelectedRow();

		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Select row first");
		}

		if (fieldsverification()) {
			return;
		}

		for (int i = 0; i < Labels.length; i++) {
			model.setValueAt(ActualFields[i].getText().trim(), row, i);//get field data, transform to text, and reflect on model
		}

		if (specialverification()) {// for verifying int inputs on field index 0 and 3
			return;
		}

		SAVE();
	}

	private void ADD() {
		String[] rowData = new String[Labels.length];

		if (fieldsverification()) {
			return;
		}

		if (specialverification()) {
			return;
		}

		for (int i = 0; i < ActualFields.length; i++) {
			rowData[i] = (ActualFields[i].getText().trim());
		}

		model.addRow(rowData);
		SAVE();
	}

	private boolean specialverification() {
		try {
			Integer.parseInt(ActualFields[0].getText().trim());
			Integer.parseInt(ActualFields[3].getText().trim());
			return false;
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "First and last field must be numbers");
			return true;
		}
	}

	private boolean fieldsverification() {

		for (int i = 0; i < Labels.length; i++) {
			if (ActualFields[i].getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "All fields must be filled first");
				return true;
			}

		}

		return false;

	}

	private void SAVE() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("CarsBRROOOMMMM.txt"))) {//more efficient than pure file writer and 
			//redid the filewriter txt file because it does not load with only global variable FILENAME
			for (int row = 0; row < model.getRowCount(); row++) {
				for (int col = 0; col < model.getColumnCount(); col++) {//same function as code but in reverse and to reflect in txt file
					bw.write(model.getValueAt(row, col).toString().trim());
					if (col < model.getColumnCount() - 1)
						bw.write(DELIMITER);
				}
				bw.newLine();//to not make the whole table a long horizontal list after each loop but shows same table organization
			}
			JOptionPane.showMessageDialog(this, "File saved successfully");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error saving file");
		}
	}

	public static void main(String[] args) {
		new FleetManagementSystem(); //wake up constructor
	}

}

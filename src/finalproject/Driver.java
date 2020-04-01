package finalproject;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Driver {
	
	public static void main(String[] args) {
		try {
		JOptionPane.showMessageDialog(null,"Welcome To D&R Vehicle Rental Services\n\n(Select one of the following options)");
		startUpMenu();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Unknown error encountered!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	public static void startUpMenu() {
		JFrame frame = new JFrame("D&R Vehicle Rental Service");
		JButton button1 = new JButton("View Vehicles");
		JButton button2 = new JButton("Search Vehicles");
		JButton button3 = new JButton("Check Rentals");
		JButton button4 = new JButton("Return Vehicle");
		JButton button5 = new JButton("EXIT");

		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent al) {
				if(al.getActionCommand() == "view") {
					viewRentalVehicles();
				}
				else if(al.getActionCommand() == "search") {
					searchVehicles();
				}
				else if(al.getActionCommand() == "check") {
					checkRentals();
				}
				else if(al.getActionCommand() == "return") {
					returnVehicle();
				}
				else if(al.getActionCommand()=="exit") {
					JOptionPane.showMessageDialog(null, "Have a nice day! ;-;");
					System.exit(0);
				}
			}
		};
		button1.addActionListener(al);
		button1.setActionCommand("view");
		button2.addActionListener(al);
		button2.setActionCommand("search");
		button3.addActionListener(al);
		button3.setActionCommand("check");
		button4.addActionListener(al);
		button4.setActionCommand("return");
		button5.addActionListener(al);
		button5.setActionCommand("exit");
		
		GridLayout gl = new GridLayout(5,1); 
		frame.setLayout(gl);
		
		frame.add(button1);
		frame.add(button2);
		frame.add(button3);
		frame.add(button4);
		frame.add(button5);
		
		frame.setSize(350,500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void searchVehicles() {
		Vehicle vehicle = new Vehicle();
		vehicle.searchVehicles();
	}
	
	public static void viewRentalVehicles() {
		Vehicle vehicle = new Vehicle();
		vehicle.selectVehicleType();
	}
	
	public static void checkRentals() {
		Vehicle vehicle = new Vehicle();
		vehicle.userRentals();
	}
	
	public static void returnVehicle() {
		Vehicle vehicle = new Vehicle();
		vehicle.returnVehicle();
	}
}

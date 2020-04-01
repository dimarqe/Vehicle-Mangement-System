package finalproject;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Vehicle {
	protected static int max = 5;
	protected int manufactureYear[] = new int[max];
	protected int mileage[] = new int[max];
	protected int rentalStatus[] = new int[max];
	protected int numOfSeats;
	protected float engineSize[] = new float[max];
	protected float rate[] = new float[max];
	protected char transmission[] = new char[max];
	protected String license[] = new String[max];
	protected String brand[] = new String[max];
	protected String model[] = new String[max];
	protected String colour[] = new String[max];
	protected User user = new User();
	
	//allows user to confirm if they want to rent any of the vehicles listed in the table
	public void confirmRental(Vehicle vehicle, int type) {
		int add = JOptionPane.showConfirmDialog(null,"Does the customer wish to rent one of the listed vehicles?",
				null, JOptionPane.YES_NO_OPTION);
		if(add==JOptionPane.YES_OPTION) {
			user.addUser(type);
		}
		else {
			JOptionPane.showMessageDialog(null, "Close the window to access start up menu");
		}
	}
	
	//does the same thing as confirmRental above, except without passing in the vehicle object
	//p.s the one above was only done to show polymorphism
	public void confirmRental(int type) {
		int add = JOptionPane.showConfirmDialog(null,"Does the customer wish to rent one of the listed vehicles?",
				null, JOptionPane.YES_NO_OPTION);
		if(add==JOptionPane.YES_OPTION) {
			user.addUser(type);
		}
		else {
			JOptionPane.showMessageDialog(null, "Close the window to access start up menu");
		}
	}
	
	//makes user choose the type of vehicle to display
	public void selectVehicleType() {
		JFrame frame = new JFrame("Select type to view list");
		JButton button1 = new JButton("BIKE");
		JButton button2 = new JButton("CAR");
		JButton button3 = new JButton("TRUCK");
		
		button1.setActionCommand("bike");
		button2.setActionCommand("car");
		button3.setActionCommand("truck");

		
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent al) {
				frame.dispose();
				if(al.getActionCommand() == "bike") {
					Bike bike = new Bike();
					bike.populateVehicleRecords();
					bike.viewBikes();
					bike.confirmRental(bike, 1);
				}
				else if(al.getActionCommand()=="car") {
					Car car = new Car();
					car.populateVehicleRecords();
					car.viewCars();
					car.confirmRental(car, 2);
				}
				else if(al.getActionCommand()=="truck") {
					Truck truck = new Truck();
					truck.populateVehicleRecords();
					truck.viewTrucks();
					truck.confirmRental(truck, 3);
				}
			}
		};
		
		button1.addActionListener(al);
		button2.addActionListener(al);
		button3.addActionListener(al);
		
		GridLayout gl = new GridLayout(3,1); 
		frame.setLayout(gl);
		
		frame.add(button1);
		frame.add(button2);
		frame.add(button3);
		
		frame.setSize(300,300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void populateVehicleRecords() {
		//override in child class
	}
	
	//displays the information passed as parameters
	public void viewVehicles(String[] coloumns, Object[][] data, String vehicleType) {
		JFrame frame = new JFrame("Rental Vehicles");
		
		JTable table = new JTable(data, coloumns);
		JLabel label = new JLabel(vehicleType);
        
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		label.setFont(new Font("Arial",Font.TRUETYPE_FONT, 24));
		label.setHorizontalAlignment(JLabel.CENTER);
		
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		frame.getContentPane().add(label,BorderLayout.PAGE_START);
		
		frame.setSize(1150, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
	//displays the records matching search criteria
	public int viewSearchResults(int searchType, String checkFor, String fileName) {
		String lastCol = "N/A";
		int checkYear, extraVal = 0, flag = 0, numSeats = 5;
		if(fileName.equals("BikeRentalRecords.txt")) {
			lastCol = "# of Helmets";
			numSeats = 2;
		}
		else if(fileName.equals("TruckRentalRecords.txt")) {
			lastCol = "Towing Capacity";
		}
		Object[][] rowData = {};
		Object[] columnNames = {"Rental Status","License Plate #","Brand","Model","Year","Color",
				"Engine Size(L)","Transmission","Mileage(km)","# of Seats",
				"Rate Per Day($)", lastCol};

	    DefaultTableModel listTableModel;
	    listTableModel = new DefaultTableModel(rowData, columnNames);

		File file = new File(fileName); 
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while (fileReader.hasNext()) {
			rentalStatus[0] = fileReader.nextInt();
			license[0] = fileReader.next();
			brand[0] = fileReader.next();
			model[0] = fileReader.next();
			manufactureYear[0] = fileReader.nextInt();
			colour[0] = fileReader.next();
			engineSize[0] = fileReader.nextFloat();
			transmission[0]  = fileReader.next().charAt(0);
			mileage[0] = fileReader.nextInt();
			rate[0] = fileReader.nextFloat();
			if(!fileName.equals("CarRentalRecords.txt")) {
				extraVal = fileReader.nextInt();
			}
			switch(searchType) {
				//executes when user is searching for a license plate #
				case 1:
					if(checkFor.equals(license[0])) {
						listTableModel.addRow(new Object[] {((rentalStatus[0]== 1)?"Available" : "Unavailable"),license[0],brand[0],model[0],
								manufactureYear[0],colour[0],engineSize[0],transmission[0],mileage[0], numSeats,rate[0], extraVal});
						flag = 1;
					}
					break;
				
				//executes when user is searching for a vehicle brand
				case 2:
					if(checkFor.equals(brand[0])) {
						listTableModel.addRow(new Object[] {((rentalStatus[0]== 1)?"Available" : "Unavailable"),license[0],brand[0],model[0],
								manufactureYear[0],colour[0],engineSize[0],transmission[0],mileage[0],numSeats,rate[0], extraVal});
						flag = 1;
					}
					break;
					
				//executes when user is searching for a vehicle model
				case 3:
					if(checkFor.equals(model[0])) {
						listTableModel.addRow(new Object[] {((rentalStatus[0]== 1)?"Available" : "Unavailable"),license[0],brand[0],model[0],
								manufactureYear[0],colour[0],engineSize[0],transmission[0],mileage[0],numSeats,rate[0], extraVal});
						flag = 1;
					}
					break;
					
				//executes when user is searching for vehicles built in a certain year
				case 4:
					checkYear = Integer.parseInt(checkFor);
					if(checkYear == manufactureYear[0]) {
						listTableModel.addRow(new Object[] {((rentalStatus[0]== 1)?"Available" : "Unavailable"),license[0],brand[0],model[0],
								manufactureYear[0], colour[0],engineSize[0],transmission[0],mileage[0],numSeats,rate[0], extraVal});
						flag = 1;
					}
					break;
			} 
		}
		
		if(flag == 0) {
			JOptionPane.showMessageDialog(null, "No records found matching criteria", "Notice!", JOptionPane.WARNING_MESSAGE);
			return -1;
		}
		else {
			JTable listTable = new JTable(listTableModel);;
		    
		    JFrame frame = new JFrame();
		    frame.add(new JScrollPane(listTable));
			
			frame.setSize(1150, 500);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
		}
		
		return 0;
	}
	
	//allows user to choose and enter search criteria
	public void searchBy(String fileName, int type) {
		JFrame frame = new JFrame("Search for a?");
		JButton button1 = new JButton("LICENSE PLATE#");
		JButton button2 = new JButton("BRAND");
		JButton button3 = new JButton("MODEL");
		JButton button4 = new JButton("YEAR");
		
		button1.setActionCommand("license");
		button2.setActionCommand("brand");
		button3.setActionCommand("model");
		button4.setActionCommand("year");

		
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent al) {
				frame.dispose();
				if(al.getActionCommand() == "license") {
					String licensePlateNum;
					licensePlateNum = JOptionPane.showInputDialog(null, "Enter the license Plate #", null, JOptionPane.OK_OPTION);
					int cont = viewSearchResults(1, licensePlateNum, fileName);
					if(cont == 0) {
						confirmRental(type);
					}
				}
				else if(al.getActionCommand()=="brand") {
					String brand;
					brand = JOptionPane.showInputDialog(null, "Enter the brand", null, JOptionPane.OK_OPTION);
					int cont = viewSearchResults(2, brand, fileName);
					if(cont == 0) {
						confirmRental(type);
					}
				}
				else if(al.getActionCommand()=="model") {
					String model;
					model = JOptionPane.showInputDialog(null, "Enter the model", null, JOptionPane.OK_OPTION);
					int cont = viewSearchResults(3, model, fileName);
					if(cont == 0) {
						confirmRental(type);
					}
				}
				else if(al.getActionCommand()=="year") {
					String year;
					year = JOptionPane.showInputDialog(null, "Enter the manufacture year", null, JOptionPane.OK_OPTION);
					int cont = viewSearchResults(4, year, fileName);
					if(cont == 0) {
						confirmRental(type);
					}
				}
			}
		};
		
		button1.addActionListener(al);
		button2.addActionListener(al);
		button3.addActionListener(al);
		button4.addActionListener(al);
		
		GridLayout gl = new GridLayout(4,1); 
		frame.setLayout(gl);
		
		frame.add(button1);
		frame.add(button2);
		frame.add(button3);
		frame.add(button4);
		
		frame.setSize(300,400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	
	}
	
	//allows user to choose which vehicle records they will be searching in 
	public void searchVehicles() {
		JFrame frame = new JFrame("Search in?");
		JButton button1 = new JButton("BIKE");
		JButton button2 = new JButton("CAR");
		JButton button3 = new JButton("TRUCK");
		
		button1.setActionCommand("bike");
		button2.setActionCommand("car");
		button3.setActionCommand("truck");

		
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent al) {
				frame.dispose();
				if(al.getActionCommand() == "bike") {
					searchBy("BikeRentalRecords.txt", 1);
				}
				else if(al.getActionCommand()=="car") {
					searchBy("CarRentalRecords.txt", 2);
				}
				else if(al.getActionCommand()=="truck") {
					searchBy("TruckRentalRecords.txt", 3);
				}
			}
		};
		
		button1.addActionListener(al);
		button2.addActionListener(al);
		button3.addActionListener(al);
		
		GridLayout gl = new GridLayout(3,1); 
		frame.setLayout(gl);
		
		frame.add(button1);
		frame.add(button2);
		frame.add(button3);
		
		frame.setSize(300,300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	//displays the records of vehicles rented by a user
	public void userRentals() {
		User user = new User();
		user.userRentals();
	}
	
	//allows a user to return a vehicle they rented
	public void returnVehicle() {
		User user = new User();
		user.returnVehicle();
	}
}

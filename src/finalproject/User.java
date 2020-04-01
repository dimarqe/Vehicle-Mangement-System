package finalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class User {
	private String licensePlateNum;
	private int numVehiclesRented;
	private int telephone;
	private int returnMileage;
	private String name;
	private String address;
	private Date dateBorrowed = new Date();
	private Date expectedReturn = new Date();
	Date actualReturnDate = new Date();
	
	//calculates the amount of days between 2 Dates assuming the second date is larger
	public int daysBetween(int d1, int m1, int y1, int d2, int m2, int y2) {
		int difference = 0, i;
		int months[] = new int[] {0,31,28,31,30,31,30,31,31,30,31,30,31};
		if(m1 == m2 && y1 == y2) {
			difference = d2 - d1;
		}
		else if(y1 == y2) {
			for(i = m1+1; i<m2; i++) {
				difference += months[i];
			}
			difference = difference + d2 + (months[m1]-d1);
		}
		else if(y2 > y1) {
			for(i = m1+1; i<13; i++) {
				difference+= months[i];
			}
			for(i=1; i<m2; i++) {
				difference+= months[i];
			}
			difference = difference + d2 + (months[m1]-d1);
		}
		return difference;
	}
	
	//uses rate + late fee to calculate bill amount
	public float calculations(float rate) {
		float amnt = 0;
		int days1 = 0, days2 = 0;
		
		days1 = daysBetween(dateBorrowed.getDay(),dateBorrowed.getMonth(),dateBorrowed.getYear(),
				actualReturnDate.getDay(),actualReturnDate.getMonth(),actualReturnDate.getYear());
		days2 = daysBetween(dateBorrowed.getDay(),dateBorrowed.getMonth(),dateBorrowed.getYear(),
				expectedReturn.getDay(),expectedReturn.getMonth(),expectedReturn.getYear());
		
		if(days1 <= days2) {
			amnt = days1*rate;
		}
		else {
			amnt = days1*rate;
			amnt = amnt + ((days1-days2)*1000);
		}
	    
		return amnt;
	}
	
	//gets vehicle rate and returns bill amount
	public float generateBill(int type, String licensePlateNum) {
		String fileName, checkLicensePlateNum;
		float bill = 0, rate;
		if(type == 1) {
			fileName = "BikeRentalRecords.txt";
		}
		else if(type == 2) {
			fileName = "CarRentalRecords.txt";
		}
		else {
			fileName = "TruckRentalRecords.txt";
		}
		
		File file = new File(fileName); 
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (fileReader.hasNext()) {
			fileReader.nextInt();
			checkLicensePlateNum = fileReader.next();
			fileReader.next();
			fileReader.next();
			fileReader.nextInt();
			fileReader.next();
			fileReader.nextFloat();
			fileReader.next().charAt(0);
			fileReader.nextInt();
			rate = fileReader.nextFloat();
			if(licensePlateNum.equals(checkLicensePlateNum)) {
				bill = calculations(rate);
				break;
			}
			if(!fileName.equals("CarRentalRecords.txt")) {
				fileReader.nextInt();
			}
		} 
		return bill;
	}
	
	//deletes a user record when a vehicle is returned
	public void deleteUserRecord(int pos) {
		File file = new File("UserData.txt");
		BufferedReader reader = null;
		PrintWriter pw = null;
		String oldContent = "", newContent = "";
		int i = 0;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while(line != null) {
				oldContent = line + System.lineSeparator();
				if(pos == i) {
					oldContent = "";
				}				
				newContent = newContent + oldContent;
				line = reader.readLine();
				i++;
			}
				
			pw = new PrintWriter(file);
			pw.write(newContent);
			
			pw.close();
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//loads in dates to be used for bill calculation and calls functions to modify records
	public void checkUserRecords() {
		int pos=0;
		String checkLicensePlateNum;
		
		File file = new File("UserData.txt"); 
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (fileReader.hasNext()) {
			name = fileReader.next();
			telephone =fileReader.nextInt();
			fileReader.next();
			checkLicensePlateNum = fileReader.next();
			dateBorrowed.setDay(fileReader.nextInt());
			dateBorrowed.setMonth(fileReader.nextInt());
			dateBorrowed.setYear(fileReader.nextInt());
			expectedReturn.setDay(fileReader.nextInt());
			expectedReturn.setMonth(fileReader.nextInt());
			expectedReturn.setYear(fileReader.nextInt());
			if(checkLicensePlateNum.equals(licensePlateNum)) {
				getNumRentals(name, telephone, 2);
				deleteUserRecord(pos);
				break;
			}
			pos++;
		}
	}
	
	//accepts information of vehicle being returned and executes based on information obtained
	public int returnVehicle() {
		int type = 0, noError;
		float bill;
		JTextField dayField = new JTextField();
		JTextField monthField = new JTextField();
		JTextField yearField = new JTextField();
		JTextField typeField = new JTextField();
		JTextField licenseField = new JTextField();
		JTextField mileageField = new JTextField();

		Object[] infoForm= {
				"Type of vehicle being returned?\n1 for Bike / 2 for Car / 3 for Truck", typeField,
				"License Plate #", licenseField,
				"Current Mileage", mileageField
		};
		
		Object[] dateform= {
				"Day:", dayField,
				"Month:", monthField,
				"Year:", yearField
		};
		
		int option = JOptionPane.showConfirmDialog(null, infoForm, "Fill out the following information", JOptionPane.DEFAULT_OPTION);
		if(option == JOptionPane.OK_OPTION){
			type = Integer.parseInt(typeField.getText());
			licensePlateNum = licenseField.getText();
			returnMileage = Integer.parseInt(mileageField.getText());
		}
		
		noError = checkLicensePlate(type, licensePlateNum, 2);
		if(noError == 1) {
			option = JOptionPane.showConfirmDialog(null, dateform, "Current date(2 digits each)", JOptionPane.DEFAULT_OPTION);
			if(option == JOptionPane.OK_OPTION){
				actualReturnDate.setDay(Integer.parseInt(dayField.getText()));
				actualReturnDate.setMonth(Integer.parseInt(monthField.getText()));
				actualReturnDate.setYear(Integer.parseInt(yearField.getText()));
			}
			checkUserRecords();
			bill = generateBill(type, licensePlateNum);
			JOptionPane.showMessageDialog(null, "Vehicle successfully returned");
			String message = String.format("Your Bill comes to: $%.2f", bill);
			JOptionPane.showMessageDialog(null, message);
		}
		else {
			return 0;
		}
		
		
		return 0;
	}
	
	//searches and displays information of vehicles rented by user
	public void userRentals() {
		JTextField nameField = new JTextField();
		JTextField telephoneField = new JTextField();
		
		Object[] infoForm = {
			"Name (use '_' for space)", nameField,
			"Telephone # (no symbols)", telephoneField
		};
		
		int option = JOptionPane.showConfirmDialog(null, infoForm, "Renter Information", JOptionPane.DEFAULT_OPTION);
		if(option== JOptionPane.OK_OPTION) {
			name = nameField.getText();
			telephone = Integer.parseInt(telephoneField.getText());
		}
		
		File file = new File("UserData.txt"); 
		Scanner fileReader = null;
		String savedName, message = "License Plate#     Borrow Date    Expected Return Date\n";
		int savedTelNum, count = 0;
		try {
			fileReader = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (fileReader.hasNext()) {
			savedName = fileReader.next();
			savedTelNum=fileReader.nextInt();
			fileReader.next();
			licensePlateNum = fileReader.next();
			dateBorrowed.setDate(fileReader.nextInt(), fileReader.nextInt(), fileReader.nextInt());
			expectedReturn.setDate(fileReader.nextInt(), fileReader.nextInt(), fileReader.nextInt());
			if(savedName.equals(name) && savedTelNum == telephone) {
				message = message + String.format("%-25s\t%d/%d/%-15d\t%d/%d/%d\n", licensePlateNum, dateBorrowed.getDay(), dateBorrowed.getMonth(),
						dateBorrowed.getYear(), expectedReturn.getDay(), expectedReturn.getMonth(), expectedReturn.getYear());
				count++;
				if(count == 3) {
					break;
				}
			}
		} 
		fileReader.close();
		if(count == 0) {
			message = String.format("No rentals found for %s", name);
			JOptionPane.showMessageDialog(null, message);
		}
		else {
			String heading = String.format("%s's Rentals", name);
			JOptionPane.showMessageDialog(null, message, heading, JOptionPane.OK_OPTION);
		}
	}
	
	//modifies file contents based on arguments passed as parameters
	public void modifyFile(String fileName, int pos, String original, String replacement) {
		File file = new File(fileName);
		BufferedReader reader = null;
		PrintWriter pw = null;
		String oldContent = "", newContent = "";
		int i = 0;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			//Erases user record when the number of vehicles rented by the user reaches 0
			if(fileName == "NumberOfRentals.txt" && replacement.equals("0")) {
				String line = reader.readLine();
				while(line != null) {
					oldContent = line + System.lineSeparator();
					if(pos == i) {
						oldContent = "";
					}				
					newContent = newContent + oldContent;
					line = reader.readLine();
					i++;
				}
			}
			//modifies a record field
			else {
				String line = reader.readLine();
				while(line != null) {
					oldContent = line + System.lineSeparator();
					if(pos == i) {
						oldContent = oldContent.replaceFirst(original, replacement);
					}				
					newContent = newContent + oldContent;
					line = reader.readLine();
					i++;
				}
			}

			pw = new PrintWriter(file);
			pw.write(newContent);
			
			pw.close();
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//checks vehicle file for valid license plate #, availability status and executes based on method passed
	public int fileCheck(String fileName, String licensePlateNum, int method) {
		int flag = 1, found = 0, pos=0, available, pastMileage = 0;
		String checkLicensePlateNum, message = "";
		
		File file = new File(fileName); 
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (fileReader.hasNext()) {
			available = fileReader.nextInt();
			checkLicensePlateNum=fileReader.next();
			fileReader.next();
			fileReader.next();
			fileReader.nextInt();
			fileReader.next();
			fileReader.nextFloat();
			fileReader.next().charAt(0);
			pastMileage = fileReader.nextInt();
			
			//method 1 is used when a vehicle is being rented, a vehicle with an unavailable status cannot be rented
			if(method == 1) {
				if(checkLicensePlateNum.equals(licensePlateNum)) {
					found = 1;
					if(available == 0) {
						message = "Vehicle unavailable";
						flag = 0;
					}
					break;
				}
				
			}
			//method 2 is used when a vehicle is being returned, a vehicle with an available status cannot be returned
			else if(method == 2) {
				if(checkLicensePlateNum.equals(licensePlateNum)) {
					found = 1;
					if(available == 1) {
						message = "Vehicle is not currently being rented";
						flag = 0;
					}
					break;
				}
			}
			fileReader.nextFloat();
			if(!fileName.equals("CarRentalRecords.txt")) {
				fileReader.nextInt();
			}
			pos++;
		} 
		
		if(flag == 0) {
			JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
		}
		else if(found == 0) {
			message = "Invalid license plate number";
			JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
			flag = 0;
		}
		//executes only when a vehicle is being rented
		else if(flag == 1 && method == 1){
			//changes the status of a vehicle being rented from available to unavailable
			modifyFile(fileName, pos, "1", "0");
		}
		//executes only when a vehicle is being returned
		else if(flag == 1 && method == 2) {
			//changes the status of a vehicle being returned from unavailable to available
			modifyFile(fileName, pos, "0", "1");
			//updates the mileage of the vehicle being returned
			modifyFile(fileName, pos, Integer.toString(pastMileage), Integer.toString(returnMileage));
		}
		
		fileReader.close();
		
		return flag;
	}
	
	//checks validity of license plate # entered
	public int checkLicensePlate(int type, String licensePlateNum, int method) {
		int noError = 0;
		
		if(type == 1) {
			noError = fileCheck("BikeRentalRecords.txt", licensePlateNum, method);
		}
		else if(type == 2) {
			noError = fileCheck("CarRentalRecords.txt", licensePlateNum, method);
		}
		else if(type == 3) {
			noError = fileCheck("TruckRentalRecords.txt", licensePlateNum, method);
		}
		
		return noError;
	}
	
	//gets the number of vehicles rented by a user
	public int getNumRentals(String name, int telNum, int method) {
		File file = new File("NumberOfRentals.txt"); 
		int currentNumRentals=1, pos = 0;
		
		if(file.exists()) {
			Scanner fileReader = null;
			int pastNumRentals, checkTelNum;
			String checkName;
			
			try {
				fileReader = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			while(fileReader.hasNext()) {
				pastNumRentals = fileReader.nextInt();
				checkName = fileReader.next();
				checkTelNum = fileReader.nextInt();

				//executes when a vehicle is being rented
				if(method == 1) {
					if(checkName.equals(name) && checkTelNum == telNum && pastNumRentals<3) {
						currentNumRentals = pastNumRentals+1;
						modifyFile("NumberOfRentals.txt", pos, Integer.toString(pastNumRentals), Integer.toString(currentNumRentals));
						break;
					}
					else if(checkName.equals(name) && checkTelNum == telNum && pastNumRentals >= 3) {
						currentNumRentals = -1;
						break;
					}
				}
				//executes when a vehicle is being returned
				else if(method == 2) {
					if(checkName.equals(name) && checkTelNum == telNum) {
						currentNumRentals = pastNumRentals-1;
						modifyFile("NumberOfRentals.txt", pos, Integer.toString(pastNumRentals), Integer.toString(currentNumRentals));
						break;
					}
				} 
				
				pos++;
			}
		}
		
		return currentNumRentals;
	}
	
	//saves user info to file
	public void saveUserInfo(String fileName) {
		try {
			FileWriter file = new FileWriter(fileName, true);

				PrintWriter pw = new PrintWriter(file);
				
				if(fileName.contentEquals("UserData.txt"))
				{
					pw.printf("%s %d %s %s %d %d %d %d %d %d\n", name, telephone, address, licensePlateNum,
						dateBorrowed.getDay(), dateBorrowed.getMonth(), dateBorrowed.getYear(),
						expectedReturn.getDay(),expectedReturn.getMonth(),expectedReturn.getYear());
				}
				else if(fileName.equals("NumberOfRentals.txt")) {
					pw.printf("%d %s %d\n", numVehiclesRented, name, telephone);
				}
				pw.close();	
			}
			
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//allows a users information to be added
	public int addUser(int type) {
		JTextField nameField = new JTextField();
		JTextField licenseField = new JTextField();
		JTextField telephoneField = new JTextField();
		JTextField addressField = new JTextField();
		JTextField dayField = new JTextField();
		JTextField monthField = new JTextField();
		JTextField yearField = new JTextField();

		Object[] infoForm = {
			"Name (Use '_' for space)", nameField,
			"License Plate#:", licenseField,
			"Telephone #: (no symbols)", telephoneField,
			"Address (Use '_' for space)", addressField
		};
		
		Object[] dateform= {
				"Day:", dayField,
				"Month:", monthField,
				"Year:", yearField
		};
		
		int option = JOptionPane.showConfirmDialog(null, infoForm, "Renter Information", JOptionPane.DEFAULT_OPTION);
		if(option== JOptionPane.OK_OPTION) {
			name = nameField.getText();
			licensePlateNum = licenseField.getText();
			telephone = Integer.parseInt(telephoneField.getText());
			address = addressField.getText();
		}
		//checks and returns number of vehicles user has rented
		numVehiclesRented = getNumRentals(name, telephone, 1);
  		if (numVehiclesRented == -1) {
			String message = String.format("%s has reached the max amount of rentals",name);
			JOptionPane.showMessageDialog(null,message,"Warning",JOptionPane.WARNING_MESSAGE);
			return 0;
  		}
  		
		//checks if license plate number entered is valid
		int noError = checkLicensePlate(type, licensePlateNum, 1);
		if(noError == 0) {
			return 0;
		}
		
		option = JOptionPane.showConfirmDialog(null, dateform, "Borrow date(2 digits each)", JOptionPane.DEFAULT_OPTION);
		
		if(option == JOptionPane.OK_OPTION) {
			int d = Integer.parseInt(dayField.getText());
			int m = Integer.parseInt(monthField.getText());
			int y = Integer.parseInt(yearField.getText());
			dateBorrowed.setDate(d,m,y);
		}
		
		option = JOptionPane.showConfirmDialog(null, dateform, "Expected return date(2 digits each)", JOptionPane.DEFAULT_OPTION);
		
		if(option == JOptionPane.OK_OPTION) {
			int d = Integer.parseInt(dayField.getText());
			int m = Integer.parseInt(monthField.getText());
			int y = Integer.parseInt(yearField.getText());
			expectedReturn.setDate(d,m,y);
		}		
		
		saveUserInfo("UserData.txt");
		if(numVehiclesRented == 1) {
			saveUserInfo("NumberOfRentals.txt");
		}
		JOptionPane.showMessageDialog(null, "User data successfully saved");
		
		return 0;
	}
}

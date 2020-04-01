package finalproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Car extends Vehicle{
	
	@Override
	//loads in vehicle records from file
	public void populateVehicleRecords(){
		File file = new File("CarRentalRecords.txt"); 
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		int i= 0;
		numOfSeats=5;
		while (fileReader.hasNext()) {
			rentalStatus[i] = fileReader.nextInt();
			license[i]=fileReader.next();
			brand[i]=fileReader.next();
			model[i]=fileReader.next();
			manufactureYear[i]=fileReader.nextInt();
			colour[i]=fileReader.next();
			engineSize[i]=fileReader.nextFloat();
			transmission[i]=fileReader.next().charAt(0);
			mileage[i]=fileReader.nextInt();
			rate[i]=fileReader.nextFloat();
			i++;
		} 
		
		fileReader.close();
	}
	
	//displays car records
	public void viewCars() {
		String vehicleType = "Car Rentals";
		
		String[] coloumns = {
			"Rental Status","License Plate #","Brand","Model","Year","Color",
			"Engine Size(L)","Transmission","Mileage(km)","# of Seats",
			"Rate Per Day($)"
		};
		Object [][] data = {
			{((rentalStatus[0]== 1)?"Available" : "Unavailable"),license[0],brand[0],model[0],manufactureYear[0],colour[0],
				engineSize[0],transmission[0],mileage[0],numOfSeats,rate[0]},
			{((rentalStatus[1]== 1)?"Available" : "Unavailable"),license[1],brand[1],model[1],manufactureYear[1],colour[1],
				engineSize[1],transmission[1],mileage[1],numOfSeats,rate[1]},
			{((rentalStatus[2]== 1)?"Available" : "Unavailable"),license[2],brand[2],model[2],manufactureYear[2],colour[2],
				engineSize[2],transmission[2],mileage[2],numOfSeats,rate[2]},
			{((rentalStatus[3]== 1)?"Available" : "Unavailable"),license[3],brand[3],model[3],manufactureYear[3],colour[3],
				engineSize[3],transmission[3],mileage[3],numOfSeats,rate[3]},
			{((rentalStatus[4]== 1)?"Available" : "Unavailable"),license[4],brand[4],model[4],manufactureYear[4],colour[4],
				engineSize[4],transmission[4],mileage[4],numOfSeats,rate[4]}
		};
		
		
		viewVehicles(coloumns, data, vehicleType);
	}
}

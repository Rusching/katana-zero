package edu.uchicago.gerber._03objects;


class Car {
    private final float fuelEfficiency;
    private float gasAmount;
    private float miles;
    public Car(float fuelEfficiency) {
        if (fuelEfficiency <= 0) throw new IllegalArgumentException("Fuel efficiency must be a number greater then 0.");
        this.fuelEfficiency = fuelEfficiency;
        this.gasAmount = 0;
    }

    public void addGas(float gas) {
        if (gas <= 0) {
            System.out.println("The gas to be added must be more than 0 gallons!");
        } else {
            this.gasAmount += gas;
        }
    }

    public float getGasLevel() {
        return this.gasAmount;
    }

    public void drive(float miles) {
        if (miles / this.fuelEfficiency > this.gasAmount) {
            float actualMiles = this.gasAmount * this.fuelEfficiency;
            this.miles += actualMiles;
            this.gasAmount = 0;
            System.out.println("Cannot drive specified miles as gas ran out.");
            System.out.println(String.format("Drove for %.2f miles, totally drove %.2f miles, gas remaining %.2f gallons.", actualMiles, this.miles, this.gasAmount));
        } else {
            this.miles += miles;
            this.gasAmount -= (miles / this.fuelEfficiency);
            System.out.println(String.format("Drove for %.2f miles, totally drove %.2f miles, gas remaining %.2f gallons.", miles, this.miles, this.gasAmount));
        }
    }


}
public class P8_6 {
    public static void main(String[] args) {
//        Here is the testing data:

        Car car = new Car(20);
        car.addGas(100);
        System.out.println(String.format("Remaining fuel level %.2f gallons", car.getGasLevel()));
        car.drive(10);
        car.drive(5);
        car.drive(6);
        car.addGas(5);
        car.drive(10000);
    }
}

package org.springframework.beans.factory;

public class Car {

    private String brand;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brandCar) {
        this.brand = brandCar;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                '}';
    }
}

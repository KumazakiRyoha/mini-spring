package org.springframework.beans.ioc.bean;

/**
 * 用于测试的Car类，包含对Person的引用
 */
public class Car {
    private String brand;
    private int price;
    private Person owner;

    public Car() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", price=" + price +
                ", owner=" + owner +
                '}';
    }
}

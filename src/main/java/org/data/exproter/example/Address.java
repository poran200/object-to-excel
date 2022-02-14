package org.data.exproter.example;

import org.data.exproter.annotations.Id;
import org.data.exproter.annotations.SheetEntity;

@SheetEntity
public class Address {
    @Id
    private Integer id;
    private String house;
    private String street;

    public Address(Integer id, String house, String street) {
        this.id = id;
        this.house = house;
        this.street = street;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", house='" + house + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}

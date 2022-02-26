package org.data.exproter.example;

import org.data.exproter.annotations.Column;
import org.data.exproter.annotations.Id;
import org.data.exproter.annotations.SheetEntity;

@SheetEntity("Address")
public class Address {
    @Column(name = "id",idField = true)
    private Integer id;
    @Column(name = "house")
    private String house;
    @Column(name = "street")
    private String street;
    @Column(name = "country",hasAggregate = true)
    private Country country;
    public Address(Integer id, String house, String street,Country country) {
        this.id = id;
        this.house = house;
        this.street = street;
        this.country = country;
    }

    public Address() {

    }

    public Country getCountry() {
        return country;
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

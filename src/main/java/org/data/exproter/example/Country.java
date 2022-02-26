package org.data.exproter.example;

import org.data.exproter.annotations.Column;
import org.data.exproter.annotations.SheetEntity;

@SheetEntity("country")
public class Country {
    @Column(idField = true)
    private String id;
    @Column
    private String countryName;
    @Column
    public String capital;
    @Column
    public String currency;

    public Country(String id, String countryName, String capital, String currency) {
        this.id = id;
        this.countryName = countryName;
        this.capital = capital;
        this.currency = currency;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryName='" + countryName + '\'' +
                ", capital='" + capital + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

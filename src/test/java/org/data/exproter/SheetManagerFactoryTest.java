package org.data.exproter;

import org.data.exproter.example.Address;
import org.data.exproter.example.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

class SheetManagerFactoryTest {
    private  SheetManagerFactory sheetManagerFactory;

    @BeforeEach
     void setUp() {
        SheetManagerBean sheetManagerBean = new SheetManagerBean();
        sheetManagerBean.scanMappedPackages("org.data.exproter.example");
        sheetManagerFactory = new SheetManagerFactory(sheetManagerBean);
    }

    @Test
    void testExportSheet() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Employee> employees = new ArrayList<Employee>();
        Employee e = new Employee();
        e.setName("Mainul");
        e.setAge(25);
        Address address = new Address();
        address.setId(3);
        address.setHouse("Forest Lodge");
        address.setStreet("S.S. Academy Road");
        e.setAddress(address);
        employees.add(e);

        sheetManagerFactory.export(employees);
    }
}
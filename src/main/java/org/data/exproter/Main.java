package org.data.exproter;

import org.data.exproter.example.Address;
import org.data.exproter.example.Employee;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static  SheetManagerFactory sheetManagerFactory;
    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SheetManagerBean sheetManagerBean = new SheetManagerBean();

        sheetManagerBean.scanMappedPackages("org.data.exproter.example");
        sheetManagerFactory = new SheetManagerFactory(sheetManagerBean);
        List<Employee> employees = new ArrayList<>();

        Address address = new Address(1, "32", "nikonja-2 khilkhat");
        Employee employee = new Employee("Jalal", 23, address);
        employees.add(employee);
        sheetManagerFactory.export(employees);
    }
}

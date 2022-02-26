package org.data.exproter;

import com.github.javafaker.Faker;
import org.data.exproter.example.Address;
import org.data.exproter.example.Country;
import org.data.exproter.example.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SheetExporterTest {
   private SheetExporter sheetExporter;
   private SheetManagerFactory sheetManagerFactory;
   @BeforeEach
   void setUp() {
      SheetManagerBean sheetManagerBean = new SheetManagerBean();
      sheetManagerBean.scanMappedPackages("org.data.exproter.example");
      sheetManagerFactory = new SheetManagerFactory(sheetManagerBean);
      sheetExporter = new SheetExporter();
   }
   @Test
   public void test_writeToExcel() throws IOException {
      List<String> columnNames = new ArrayList<>();
      columnNames.add("name");
      columnNames.add("age");
      columnNames.add("address_id");

      List<Map<String, Object>> valueMap = new ArrayList<>();
      Map<String, Object> map = new HashMap<>();
      map.put("name", "Mainul");
      map.put("age", "26");
      map.put("address_id", "1");
      map.put("name", "Hasan");
      map.put("age", "25");
      map.put("address_id", "2");
      valueMap.add(map);
      System.out.println(columnNames.toString());
      sheetExporter.writeExcel("employee", valueMap, columnNames);
   }

   @Test
   void test_writeToExcel_WithDynamicColumnValue() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
      List<Employee> employees = new ArrayList<>();
      Faker faker = new Faker();
      for (int i = 0; i < 100; i++) {
          employees.add(
                  new Employee((long) i,faker.name().firstName(),faker.number().numberBetween(18,50),
                          new Address(faker.number().randomDigit(),String.valueOf(faker.number().numberBetween(1,100)),faker.address().streetName(),
                                  new Country(String.valueOf(faker.number().numberBetween(1,10)), faker.country().name(),faker.country().capital(),faker.country().currency())))
          );
      }
      sheetManagerFactory.export(employees);
      List<Map<String, Object>> valueMap = sheetManagerFactory.getExported();
      List<String> columnNames = new ArrayList<>(sheetManagerFactory.getColumnNames());
//      System.out.println("columnNames = " + columnNames);
//      System.out.println("valueMap = " + valueMap);
      sheetExporter.writeExcel("employee",valueMap,columnNames);
   }
}
package org.data.exproter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetManagerFactory {
    private SheetManager sheetManager;
    private List<Map<String, String>> exported;

    public SheetManagerFactory(SheetManager sheetManager) {
        this.sheetManager = sheetManager;
        this.exported = new ArrayList<>();
    }
    public <T> void export(List<T> objects) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        String className = objects.get(0).getClass().getName();
        Map<String, List<String>> columnNamesMap = sheetManager.getMappedColumnNames();
        List<String> columnNames = columnNamesMap.get(className);
        for (T object : objects) {
            Map<String, String> columnValue = new HashMap<>();
            for (String columnName : columnNames) {
                Class<?> aClass = object.getClass();
                String value = "";
                if (columnName.contains("_")){
                    String propertyName = columnName.split("_")[0];
                    Object obj = aClass.getMethod(getMethodName(propertyName)).invoke(object);
                    String aggregateClassName = obj.getClass().getName();
                    Class<?> aggregateClass = Class.forName(aggregateClassName);
                     value = aggregateClass.getMethod("getId")
                             .invoke(obj)
                             .toString();
                }else {
                    value = aClass.getMethod(getMethodName(columnName))
                            .invoke(object)
                            .toString();
                }
                columnValue.put(columnName,value);
            }
            exported.add(columnValue);
        }
        System.out.println(exported.toString());
    }

    private String getMethodName(String columnName) {
        StringBuilder methodNameBuilder = new StringBuilder();
        return methodNameBuilder.append("get")
                .append(columnName.substring(0, 1).toUpperCase())
                .append(columnName.substring(1))
                .toString();
    }
}

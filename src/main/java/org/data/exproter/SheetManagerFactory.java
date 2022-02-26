package org.data.exproter;

import org.data.exproter.model.ColumnMetadata;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SheetManagerFactory {
    private SheetManager sheetManager;
    private List<Map<String, Object>> exported;
    private Set<String> columnNames;
    public SheetManagerFactory(SheetManager sheetManager) {
        this.sheetManager = sheetManager;
        this.exported = new ArrayList<>();
        this.columnNames = new LinkedHashSet<>();
    }

    public <T> void export(List<T> objects) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        String className = objects.get(0).getClass().getName();
        Map<String, List<ColumnMetadata>> columnNamesMap = sheetManager.getMappedColumnMetadata();
        List<ColumnMetadata> columnMetadataList = columnNamesMap.get(className);

        for (T object : objects) {
            Map<String, Object> columnValue = new HashMap<>();
            getColumnValue(columnMetadataList, object, columnValue);
            exported.add(columnValue);
            System.out.println(exported.toString());
        }

    }

    private <T> void getColumnValue(List<ColumnMetadata> columnMetadataList, T object, Map<String, Object> columnValue) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        for (ColumnMetadata metadata : columnMetadataList) {
            Class<?> aClass = object.getClass();
            String value = "";
            if (metadata.hasAggregate()) {
                Object obj = aClass.getMethod(getMethodName(metadata.getMappedPropertyName())).invoke(object);
                String aggregateClassName = obj.getClass().getName();
                Class<?> aggregateClass = Class.forName(aggregateClassName);
                value = getIdValue(obj, aggregateClass);
                columnNames.add(metadata.getName()+"_Id");
                columnValue.put(metadata.getName()+"_Id", value);
                getColumnValue(sheetManager.getMappedColumnMetadata().get(aggregateClassName),obj,columnValue);
            } else {
                value = aClass.getMethod(getMethodName(metadata.getName()))
                        .invoke(object)
                        .toString();
                columnNames.add(metadata.getName());
                columnValue.put(metadata.getMappedPropertyName(), value);
            }


        }
    }

    private String getIdValue(Object obj, Class<?> aggregateClass)  {
        try {
            return aggregateClass.getMethod("getId")
                    .invoke(obj)
                    .toString();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return "Entity Id not present";
        }

    }

    public List<Map<String, Object>> getExported() {
        return exported;
    }

    public Set<String> getColumnNames() {
        return columnNames;
    }

    private String getMethodName (String columnName){
        StringBuilder methodNameBuilder = new StringBuilder();
        return methodNameBuilder.append("get")
                .append(columnName.substring(0, 1).toUpperCase())
                .append(columnName.substring(1))
                .toString();
    }

}

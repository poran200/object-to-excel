package org.data.exproter;

import org.data.exproter.annotations.Column;
import org.data.exproter.annotations.SheetEntity;
import org.data.exproter.exceptions.InvalidPackageException;
import org.data.exproter.model.ColumnMetadata;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class SheetManagerBean implements SheetManager {
    private static final Logger logger = Logger.getLogger(String.valueOf(SheetManager.class));
   private final Map<String, List<ColumnMetadata>> mappedFields;
    public SheetManagerBean() {
        this.mappedFields = new HashMap<>();
    }

    @Override
    public void scanMappedPackages(String... packages) {
       logger.info("Scanning Sheet entities");
       Stream.of(packages).forEach(pkg->{
           try {
               scanClasses(pkg);
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }
       });
       logger.info("Sheet entities Scanning complete");
    }

    @Override
    public Map<String, List<ColumnMetadata>> getMappedColumnMetadata() {
        return this.mappedFields;
    }

    private void scanClasses(String packageName) throws ClassNotFoundException {
        List<File> dirs = null;
        try {
            dirs = getDirectoryListInPackage(packageName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assert dirs != null;
        for (File directory : dirs){
            if (directory.isDirectory()){
                System.out.println(directory.getName());
            }
            findClasses(packageName, directory);
        }
    }

    private List<File> getDirectoryListInPackage(String packageName) throws IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert  classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        if (!resources.hasMoreElements()){
            String message = String.format("Package \"%s\" could not be found in class path", packageName);
            logger.info(message);
            throw  new InvalidPackageException(message);
        }
        while (resources.hasMoreElements()){
            URL resource = resources.nextElement();
            dirs.add(new File(resource.toURI().getPath()));
        }
        return dirs;
    }

    private void findClasses(String packageName, File directory) throws ClassNotFoundException {

        File [] files = directory.listFiles();;
        assert files != null;
        for (File file : files) {
            if (file.getName().endsWith(".class")){
                String fullyQualifyingClassName = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> aClass = Class.forName(fullyQualifyingClassName);
                if (aClass.isAnnotationPresent(SheetEntity.class)){
                   collectEntityMetadata(aClass);
                }
            }

        }
    }

    private void collectEntityMetadata(Class<?> aClass) {
        List<ColumnMetadata> propertyMetadataList = new ArrayList<>();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            Column aggregateFieldAnnotation = field.getAnnotation(Column.class);
            ColumnMetadata columnMetadata = new ColumnMetadata();
            String columnName = aggregateFieldAnnotation.name();
            columnMetadata.setName(columnName.equals("")? field.getName(): columnName);
            columnMetadata.setIdField(aggregateFieldAnnotation.idField());
            columnMetadata.setMappedPropertyName(field.getName());
            columnMetadata.setType(field.getType());
            columnMetadata.setHasAggregate(aggregateFieldAnnotation.hasAggregate());
            propertyMetadataList.add(columnMetadata);
        }
        System.out.println(propertyMetadataList);
        mappedFields.put(aClass.getName(),propertyMetadataList);
    }

}

package org.data.exproter;

import org.data.exproter.annotations.Id;
import org.data.exproter.annotations.SheetEntity;
import org.data.exproter.exceptions.InvalidPackageException;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class SheetManagerBean implements SheetManager {
    private static final Logger logger = Logger.getLogger(String.valueOf(SheetManager.class));
    private final Map<String, List<String>> mappedColumnNames;
    private final Map<String ,Class<?>> scannedSheetEntities;
    private final Map<String ,List<String >> methodNames;

    public SheetManagerBean() {
        this.mappedColumnNames = new HashMap<>();
        this.scannedSheetEntities = new HashMap<>();
        this.methodNames = new HashMap<>();
    }

    @Override
    public void scanMappedPackages(String... packages) throws IOException, ClassNotFoundException {
       logger.info("Scanning Sheet entities");
       Stream.of(packages).forEach(pkg->{
           try {
               scanClasses(pkg);
           } catch (IOException | ClassNotFoundException | URISyntaxException e) {
               e.printStackTrace();
           }
       });
       logger.info("Sheet entities Scanning complete");
    }
    private void scanClasses(String packageName) throws IOException, ClassNotFoundException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert  classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        if (!resources.hasMoreElements()){
            String message = String.format("Package \"%s\" could not be found in class path",packageName);
            logger.info(message);
            throw  new InvalidPackageException(message);
        }
        while (resources.hasMoreElements()){
            URL resource = resources.nextElement();
            dirs.add(new File(resource.toURI().getPath()));
        }
        for (File directory : dirs){
            if (directory.isDirectory()){
                System.out.println(directory.getName());
            }
            findClasses(packageName, directory);
        }
    }

    private void findClasses(String packageName, File directory) throws ClassNotFoundException {

        File [] files = directory.listFiles();;
        assert files != null;
        for (File file : files) {
            if (file.getName().endsWith(".class")){
                String fullyQualifyingClassName = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> aClass = Class.forName(fullyQualifyingClassName);
                if (aClass.isAnnotationPresent(SheetEntity.class)){
                    scannedSheetEntities.put(fullyQualifyingClassName,aClass);
                    setMappedColumnNames(aClass, packageName);
                }
            }

        }
    }

    private void setMappedColumnNames(Class<?> aClass, String basePackageName) throws ClassNotFoundException {
        LinkedList<String> columnNames = new LinkedList<>();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().getName().startsWith(basePackageName)){
                Class<?> aggregateClass = Class.forName(field.getType().getName());
                Field[] aggregateFields = aggregateClass.getDeclaredFields();
                for (Field aggregateField : aggregateFields) {
                    Annotation aggregateFieldAnnotation = aggregateField.getAnnotation(Id.class);
                    if (aggregateField.getAnnotation(Id.class) !=null){
                        String value = ((Id) aggregateFieldAnnotation).value();
                        columnNames.add(field.getName()+ "_"+ value);
                    }
                }
            }else {
                columnNames.add(field.getName());
            }
        }
        mappedColumnNames.put(aClass.getName(),columnNames);
    }
    private  void scanGetterMethods(Class<?> aClass,String basePackageName){
        List<String> methods = new ArrayList<>();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
             methods.add(method.getName());
        }
        methodNames.put(aClass.getName(),methods);
    }

    @Override
    public Map<String, List<String>> getMappedColumnNames() {
        return this.mappedColumnNames;
    }

    @Override
    public Map<String, Class<?>> getScannedSheetEntities() {
        return this.scannedSheetEntities;
    }

    @Override
    public Map<String, List<String>> getMethodNames() {
        return this.methodNames;
    }
}

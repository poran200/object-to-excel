import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;

class FileReaderTestTest {
    @Test
    void folderTest() throws IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources("org/data/exproter/example");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();

            System.out.println("url = " + url.getFile());
            String path = url.toURI().getPath();
            System.out.println("path = " + path);
            File file = new File(path);
            boolean directory = file.isDirectory();
            System.out.println("directory = " + directory);
            System.out.println("file = " + Arrays.toString(file.listFiles()));
        }
////        String file = resources.nextElement().getFile();
////        System.out.println("file = " + file);
////        File[] files1 = new File(file).listFiles();
////        System.out.println("files1 = " + files1.toString());
////        Collection<File> files = FileUtils.listFiles(new File(file), null, false);
////        System.out.println("files = " + files.toString());

////        File file = new File("C://Users//Poran%20chowdury//IdeaProjects//object-to-excel//target//classes//org//data//exproter//example");
//        File file = new File("C://Users//Poran%20chowdury//IdeaProjects//object-to-excel//target//classes//org//data//exproter//example");
//        File file = new File("C:\\Users\\Poran chowdury\\IdeaProjects\\object-to-excel\\target\\classes\\org\\data\\exproter\\example");
//        System.out.println("file = " + Arrays.toString(file.listFiles()));

    }
}
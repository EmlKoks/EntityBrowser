package emlkoks.entitybrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by koks on 10.03.17.
 */
public class Util {
    public static String driversDir = "drivers/";

    static List<File> driverList(){
        List<File> driverList = new ArrayList<>();
        for(File file : new File("./../drivers").listFiles()){
            String fileName = file.getName();
            if ("JAR".equals(fileName.substring(fileName.lastIndexOf(".")+1).toUpperCase()))
                driverList.add(file);
        }
        return driverList;
    }

    static void printClasses(File driver) throws IOException {
        List<String> classNames = new ArrayList<String>();
        ZipInputStream zip = new ZipInputStream(new FileInputStream(driver));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                // This ZipEntry represents a class. Now, what class does it represent?
                String className = entry.getName().replace('/', '.'); // including ".class"
                classNames.add(className.substring(0, className.length() - ".class".length()));
                System.out.println("className = " + className);
            }
        }
    }
}

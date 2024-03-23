package lt.viko.eif.pi21e;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.trace("Hello world!");

        String filepath = load_file();
        logger.trace("Filepath: {}", filepath);

        readAndPrintFileContents(filepath);

        DecisionTreeGame dtg = new DecisionTreeGame(load_file());
        dtg.load();
        dtg.game();
    }

    private static String load_file() {
        String filePath = "res/data";
        File file = new File(filePath);

        if (file.exists()) {
            logger.info("File found in build folder: {}", file.getAbsolutePath());
            return file.getAbsolutePath();
        }

        // DO NOT USE THIS AS THIS REPLACES THE FILE IN BUILD EITHER WAY
        // If file is not found in build folder, try to load from resources
        //try {
        //    java.net.URL resourceUrl = Main.class.getClassLoader().getResource("data");
        //
        //    if (resourceUrl != null) {
        //        Path path = Paths.get(resourceUrl.toURI());
        //        if (Files.exists(path)) {
        //            logger.info("File found in resources: {}", path);
        //            return path.toString();
        //        } else {
        //            logger.error("File not found in resources: {}", path);
        //        }
        //    } else {
        //        logger.error("Resource 'data' not found.");
        //    }
        //} catch (Exception e) {
        //    logger.error("Error loading file: {}", e.getMessage());
        //}

        // If file doesn't exist, create it
        try {
            Files.createDirectories(Paths.get("res"));
            Files.write(Paths.get(filePath), getFileContent().getBytes(), StandardOpenOption.CREATE);
            logger.info("New file created at: {}", filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("Error creating file: {}", e.getMessage());
        }

        logger.error("File not found in build folder or resources. Exiting.");
        System.exit(1);
        return "";
    }

    private static String getFileContent() {
        return "3\n" +
                "1 2 3 Is it an animal\n" +
                "2 0 0 Does it bark\n" +
                "3 0 0 Is it a bird";
    }

    private static void readAndPrintFileContents(String filePath) {
//        logger.trace("Reading file:");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
//                logger.trace(line);
            }
        } catch (IOException e) {
            logger.error("Error reading file: {}", e.getMessage());
        }
    }
}
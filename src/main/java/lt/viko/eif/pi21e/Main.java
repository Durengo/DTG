package lt.viko.eif.pi21e;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.trace("Hello world!");

        DecisionTreeGame dtg = new DecisionTreeGame(load_file());

        dtg.load();
        dtg.game();
    }

    private static String load_file() {
// Try to find the data file in the build folder
        File file = new File("build/resources/main/data");
        if (file.exists()) {
            logger.info("File found in build folder: {}", file.getAbsolutePath());
            return file.getAbsolutePath();
        }

        // If file is not found in build folder, try to load from resources
        try {
            java.net.URL resourceUrl = Main.class.getClassLoader().getResource("data");

            if (resourceUrl != null) {
                Path path = Paths.get(resourceUrl.toURI());
                if (Files.exists(path)) {
                    logger.info("File found in resources: {}", path);
                    return path.toString();
                } else {
                    logger.error("File not found in resources: {}", path);
                }
            } else {
                logger.error("Resource 'data' not found.");
            }
        } catch (Exception e) {
            logger.error("Error loading file: {}", e.getMessage());
        }

        logger.error("File not found in build folder or resources. Exiting.");
        System.exit(1);
        return "";
    }

}
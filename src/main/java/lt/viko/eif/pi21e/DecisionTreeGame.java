package lt.viko.eif.pi21e;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
@NoArgsConstructor
public class DecisionTreeGame {
    private List<Element> data = new ArrayList<>();
    private int data_length;
    private String filename;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public DecisionTreeGame(String filename) {
        this.filename = filename;
    }

    void load() {
        try (BufferedReader file = new BufferedReader(new FileReader(this.filename))) {
            data_length = Integer.parseInt(file.readLine());
            data = new ArrayList<>(data_length + 1);
            for (int i = 0; i < data_length; ++i) {
                String line = file.readLine();
//                logger.info("Read line: {}", line);
                String[] fields = line.split(" ", 4);
                int id = Integer.parseInt(fields[0]);
                int lid = Integer.parseInt(fields[1]);
                int rid = Integer.parseInt(fields[2]);
                String info = fields[3].trim();

                logger.debug("id: {}, lid: {}, rid: {}, info: {}", id, lid, rid, info);
                data.add(new Element(info, id, lid, rid));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    void save() {
        try (BufferedWriter file = new BufferedWriter(new FileWriter(this.filename))) {
            file.write(data_length + "\n");
            for (int i = 0; i < data_length; ++i) {
                Element element = data.get(i);
                file.write(element.getId() + " " + element.getLid() + " " + element.getRid() + " " + element.getInfo() + "\n");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    void updateTree(int index) {
        Scanner scanner = new Scanner(System.in);

        // Ask for the new question
        logger.trace("What is the new question?");
        String newQuestion = scanner.nextLine();

        // Ask for the outcome if the answer to the new question is "yes"
        logger.trace("What is the outcome if the answer to the new question is Yes?");
        String yesOutcome = scanner.nextLine();

        // Ask for the outcome if the answer to the new question is "no"
        logger.trace("What is the outcome if the answer to the new question is No?");
        String noOutcome = scanner.nextLine();

        // Determine indices for new elements
        int newQuestionIndex = data.size();
        int yesOutcomeIndex = newQuestionIndex + 1;
        int noOutcomeIndex = yesOutcomeIndex + 1;

        // Create new question node
        Element newQuestionNode = new Element(newQuestion, newQuestionIndex + 1, yesOutcomeIndex + 1, noOutcomeIndex + 1);
        data.add(newQuestionNode);

        // Create yes outcome node
        Element yesOutcomeNode = new Element(yesOutcome, yesOutcomeIndex + 1, 0, 0); // Assuming it's a leaf node
        data.add(yesOutcomeNode);

        // Create no outcome node
        Element noOutcomeNode = new Element(noOutcome, noOutcomeIndex + 1, 0, 0); // Assuming it's a leaf node
        data.add(noOutcomeNode);

        // Link the existing node to the new question node
        data.get(index).setRid(newQuestionIndex + 1);

        // Update data length
        data_length += 3;

        save();
    }

    void game() {
        Scanner scanner = new Scanner(System.in);
        int index = 0;

        while (true) {
            if (index < 0 || index >= data.size()) {
                logger.error("Invalid index: " + index);
                break;
            }

            Element currentElement = data.get(index);

            if (currentElement.getLid() == 0 && currentElement.getRid() == 0) {
                logger.info(currentElement.getInfo() + "? Yes/No");
                char response = Character.toUpperCase(scanner.next().charAt(0));
                scanner.nextLine();

                if (response == 'N') {
                    updateTree(index);
                    index = 0;
                } else {
                    break;
                }
            } else {
                logger.info(currentElement.getInfo() + "? Yes/No");
                char response = Character.toUpperCase(scanner.next().charAt(0));
                scanner.nextLine();

                if (response == 'Y') {
                    index = currentElement.getLid() - 1;
                } else {
                    index = currentElement.getRid() - 1;
                }
            }
        }
    }
}

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
        logger.trace("What is the correct answer? ");
        String correctAnswer = scanner.nextLine();
        logger.trace("What is the question distinguishing it? ");
        String question = scanner.nextLine();

        int newIndex = data.size(); // Get the new index for the added elements

        // Update the current node's right child to point to the new element for "Is it a thingy"
        int currentRightChildIndex = data.get(index).getRid();
        data.get(index).setRid(newIndex);

        // Add the new element for "Is it a thingy"
        data.add(new Element(correctAnswer, newIndex, 0, currentRightChildIndex));

        // Add the new element for the question distinguishing it
        data.add(new Element(question, newIndex + 1, 0, 0));

        data_length += 2; // Increment the data length by 2 for the two added elements

        save();
        //        Scanner scanner = new Scanner(System.in);
//        logger.trace("What is the correct answer? ");
//        String correctAnswer = scanner.nextLine();
//        logger.trace("What is the question distinguishing it? ");
//        String question = scanner.nextLine();
//
//        int newIndex = data.size(); // Get the new index for the added elements
//
//        data_length++;
//        data.add(new Element(data.get(index).getInfo(), newIndex, 0, 0)); // Use newIndex here
//        data.get(index).setRid(newIndex);
//
//        data_length++;
//        data.add(new Element(correctAnswer, data_length, 0, 0)); // Keep data_length for the next index
//        data.get(index).setLid(data_length);
//
//        data.get(index).setInfo(question);
//
//        save();
    }

    void game() {
        Scanner scanner = new Scanner(System.in);
        int index = 1; // Start at the initial question
        char c;

        // Display the initial question
        logger.info(data.get(index).getInfo() + "? Yes/No ");

        while (index != 0) {
            c = scanner.next().charAt(0);
            scanner.nextLine();
            if (Character.toUpperCase(c) == 'Y') {
                index = data.get(index).getLid();
            } else {
                index = data.get(index).getRid();
            }

            // Check if we've reached a potential leaf node
            if (index != 0) {
                logger.info(data.get(index).getInfo() + "? Yes/No ");
            } else {
                // We've reached a leaf, so ask the final guess
                logger.info(data.get(index).getInfo() + "? Yes/No ");
                c = scanner.next().charAt(0);
                scanner.nextLine();
                if (Character.toUpperCase(c) == 'N') {
                    updateTree(index);
                }
            }
        }
    }


}

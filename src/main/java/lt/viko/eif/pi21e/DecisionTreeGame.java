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
        logger.trace("What is the correct answer?");
        String correctAnswer = scanner.nextLine();
        logger.trace("What is the question distinguishing it?");
        String question = scanner.nextLine();

        int newQuestionIndex = data.size();
        int newAnswerIndex = newQuestionIndex + 1;

        Element newQuestion = new Element(question, newQuestionIndex + 1, index + 1, newAnswerIndex + 1);
        data.add(newQuestion);

        Element newAnswer = new Element(correctAnswer, newAnswerIndex + 1, 0, 0);
        data.add(newAnswer);

        data.get(index).setRid(newQuestionIndex + 1);

        data_length += 2;

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

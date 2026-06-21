package bg.tu_varna.sit.f24621815.nfa.service;

import bg.tu_varna.sit.f24621815.nfa.exception.FileOperationException;
import bg.tu_varna.sit.f24621815.nfa.exception.InvalidFileFormatException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.validation.AutomatonValidator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Услуга за управление на заредените автомати и файловото им състояние.
 *
 * <p>Поддържа в паметта списък от {@link Automaton} обекти, зареден
 * от текущо отворения файл. Осигурява операции за отваряне, затваряне
 * и записване на файлове, както и за достъп до отделни автомати по
 * техния идентификатор.</p>
 *
 * @author Стоил Атанасов
 * @see Automaton
 * @see XmlAutomatonOperations
 */
public class AutomatonService {

    /**
     * Списък от всички заредени в паметта автомати.
     */
    private final List<Automaton> automata = new ArrayList<>();

    /**
     * Пътят към текущо отворения файл.
     */
    private String currentFilePath;

    /**
     * Указва дали в момента има отворен файл.
     */
    private boolean fileOpen;

    /**
     * Следващият свободен идентификатор за нов автомат.
     */
    private int nextId = 1;

    /**
     * Отваря файл и зарежда намерените в него автомати.
     *
     * <p>Ако файлът не съществува, се създава нов празен файл.</p>
     *
     * @param filePath пътят към файла
     * @throws FileOperationException    ако файлът не може да бъде прочетен или създаден
     * @throws InvalidFileFormatException ако съдържанието на файла не е валиден XML формат
     */
    public void openFile(String filePath) {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                Files.createFile(Paths.get(filePath));
                currentFilePath = filePath;
                fileOpen = true;
                return;
            }
            String content = Files.readString(Paths.get(filePath));
            XmlAutomatonOperations xmlOps = new XmlAutomatonOperations();
            List<Automaton> loaded = xmlOps.parse(content, nextId);
            AutomatonValidator validator = new AutomatonValidator();
            for (Automaton a : loaded) {
                validator.validate(a);
                if (a.getId() >= nextId) {
                    nextId = a.getId() + 1;
                }
            }
            automata.addAll(loaded);
            currentFilePath = filePath;
            fileOpen = true;
        } catch (InvalidFileFormatException e) {
            throw e;
        } catch (Exception e) {
            throw new FileOperationException("Failed to open file: " + e.getMessage());
        }
    }

    /**
     * Затваря текущо отворения файл и изчиства заредените данни.
     */
    public void closeFile() {
        automata.clear();
        currentFilePath = null;
        fileOpen = false;
    }

    /**
     * Записва текущите промени обратно в отворения файл.
     *
     * @throws FileOperationException ако файлът не може да бъде записан
     */
    public void saveFile() {
        saveFileAs(currentFilePath);
    }

    /**
     * Записва текущите промени в посочения файл.
     *
     * @param filePath пътят към файла, в който ще се записват данните
     * @throws FileOperationException ако файлът не може да бъде записан
     */
    public void saveFileAs(String filePath) {
        try {
            XmlAutomatonOperations xmlOps = new XmlAutomatonOperations();
            Files.writeString(Paths.get(filePath), xmlOps.serialize(automata));
            currentFilePath = filePath;
        } catch (Exception e) {
            throw new FileOperationException("Failed to save file: " + e.getMessage());
        }
    }

    /**
     * Връща списъка от всички заредени автомати.
     *
     * @return списъкът от автомати
     */
    public List<Automaton> getAutomata() {
        return automata;
    }

    /**
     * Търси автомат по идентификатор.
     *
     * @param id идентификаторът за търсене
     * @return намереният автомат или {@code null} ако не е намерен
     */
    public Automaton findById(int id) {
        for (Automaton a : automata) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    /**
     * Добавя нов автомат към списъка с заредени автомати.
     *
     * @param automaton автоматът за добавяне
     */
    public void addAutomaton(Automaton automaton) {
        automata.add(automaton);
    }

    /**
     * Проверява дали в момента има отворен файл.
     *
     * @return true ако файлът е отворен
     */
    public boolean isFileOpen() {
        return fileOpen;
    }

    /**
     * Връща пътя към текущо отворения файл.
     *
     * @return пътят към файла или {@code null} ако няма отворен файл
     */
    public String getCurrentFilePath() {
        return currentFilePath;
    }

    /**
     * Връща следващия свободен идентификатор и го увеличава с 1.
     *
     * @return следващият свободен идентификатор
     */
    public int getNextId() {
        return nextId++;
    }
}
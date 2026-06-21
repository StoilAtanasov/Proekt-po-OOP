package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.exception.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.service.XmlAutomatonOperations;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

import java.io.File;
import java.util.Collections;

/**
 * Команда за запазване на конкретен автомат в отделен файл.
 *
 * <p>Изпълнява командата {@code save <id> <file>}, която записва
 * автомата с посочения идентификатор в посочения файл.</p>
 *
 * <p>Пример за употреба:</p>
 * <pre>
 *     save 1 result.xml
 *     Successfully saved automaton 1 to result.xml
 * </pre>
 *
 * @author Стоил Атанасов
 * @see Command
 * @see AutomatonService
 */
public class SaveAutomatonCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Създава нова инстанция на {@code SaveAutomatonCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     */
    public SaveAutomatonCommand(AutomatonService automatonService) {
        this.automatonService = automatonService;
    }

    /**
     * Изпълнява командата {@code save <id> <file>}.
     *
     * @param args масивът от аргументи; {@code args[1]} е идентификаторът,
     *             {@code args[2]} е пътят към файла
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако няма отворен файл, аргументите са недостатъчни или ID е невалидно
     * @throws AutomatonNotFoundException
     *         ако автомат с посочения идентификатор не съществува
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.FileOperationException
     *         ако файлът не може да бъде записан
     */
    @Override
    public void execute(String[] args) {
        CommandValidator.requireFileOpen(automatonService.isFileOpen());
        CommandValidator.requireArgs(args, 3);
        int id = CommandValidator.parseId(args[1]);
        String filePath = args[2];
        Automaton automaton = automatonService.findById(id);
        if (automaton == null) {
            throw new AutomatonNotFoundException("Automaton with ID " + id + " not found.");
        }
        XmlAutomatonOperations xmlOps = new XmlAutomatonOperations();
        xmlOps.writeToFile(Collections.singletonList(automaton), filePath);
        System.out.println("Successfully saved automaton " + id + " to " + new File(filePath).getName());
    }
}

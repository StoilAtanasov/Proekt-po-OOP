package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

import java.io.File;

/**
 * Команда за отваряне на файл с автомати.
 *
 * <p>Изпълнява командата {@code open <file>}, която зарежда автоматите
 * от посочения файл. Ако файлът не съществува, се създава нов празен.</p>
 *
 * <p>Пример за употреба:</p>
 * <pre>
 *     open automata.xml
 *     Successfully opened automata.xml
 * </pre>
 *
 * @author Стоил Атанасов
 * @see Command
 * @see AutomatonService
 */
public class OpenCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Създава нова инстанция на {@code OpenCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     */
    public OpenCommand(AutomatonService automatonService) {
        this.automatonService = automatonService;
    }

    /**
     * Изпълнява командата {@code open}.
     *
     * @param args масивът от аргументи; {@code args[1]} е пътят към файла
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако не е подаден път към файл
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.FileOperationException
     *         ако файлът не може да бъде отворен или прочетен
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidFileFormatException
     *         ако съдържанието на файла е невалидно
     */
    @Override
    public void execute(String[] args) {
        CommandValidator.requireArgs(args, 2);
        String filePath = args[1];
        automatonService.openFile(filePath);
        System.out.println("Successfully opened " + new File(filePath).getName());
    }
}

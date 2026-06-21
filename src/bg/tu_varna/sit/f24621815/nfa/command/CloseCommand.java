package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

import java.io.File;

/**
 * Команда за затваряне на текущо отворения файл.
 *
 * <p>Изпълнява командата {@code close}, която изчиства всички заредени
 * автомати и освобождава текущото файлово състояние.</p>
 *
 * <p>Пример за употреба:</p>
 * <pre>
 *     close
 *     Successfully closed automata.xml
 * </pre>
 *
 * @author Стоил Атанасов
 * @see Command
 * @see AutomatonService
 */
public class CloseCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Създава нова инстанция на {@code CloseCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     */
    public CloseCommand(AutomatonService automatonService) {
        this.automatonService = automatonService;
    }

    /**
     * Изпълнява командата {@code close}.
     *
     * @param args масивът от аргументи; не се използват допълнителни аргументи
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако няма отворен файл
     */
    @Override
    public void execute(String[] args) {
        CommandValidator.requireFileOpen(automatonService.isFileOpen());
        String name = new File(automatonService.getCurrentFilePath()).getName();
        automatonService.closeFile();
        System.out.println("Successfully closed " + name);
    }
}

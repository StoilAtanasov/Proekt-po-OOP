package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

import java.io.File;

/**
 * Команда за запазване на промените в текущо отворения файл.
 *
 * <p>Изпълнява командата {@code save}, която записва всички заредени
 * автомати обратно в оригиналния файл.</p>
 *
 * <p>Пример за употреба:</p>
 * <pre>
 *     save
 *     Successfully saved automata.xml
 * </pre>
 *
 * @author Стоил Атанасов
 * @see Command
 * @see AutomatonService
 */
public class SaveCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Създава нова инстанция на {@code SaveCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     */
    public SaveCommand(AutomatonService automatonService) {
        this.automatonService = automatonService;
    }

    /**
     * Изпълнява командата {@code save}.
     *
     * @param args масивът от аргументи; не се използват допълнителни аргументи
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако няма отворен файл
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.FileOperationException
     *         ако файлът не може да бъде записан
     */
    @Override
    public void execute(String[] args) {
        CommandValidator.requireFileOpen(automatonService.isFileOpen());
        automatonService.saveFile();
        System.out.println("Successfully saved " + new File(automatonService.getCurrentFilePath()).getName());
    }
}

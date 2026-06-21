package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

import java.io.File;

/**
 * Команда за запазване на промените в нов файл.
 *
 * <p>Изпълнява командата {@code save as <file>}, която записва всички
 * заредени автомати в посочения файл и го задава като текущ.</p>
 *
 * <p>Пример за употреба:</p>
 * <pre>
 *     save as backup.xml
 *     Successfully saved backup.xml
 * </pre>
 *
 * @author Стоил Атанасов
 * @see Command
 * @see AutomatonService
 */
public class SaveAsCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Създава нова инстанция на {@code SaveAsCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     */
    public SaveAsCommand(AutomatonService automatonService) {
        this.automatonService = automatonService;
    }

    /**
     * Изпълнява командата {@code save as}.
     *
     * @param args масивът от аргументи; {@code args[2]} е пътят към новия файл
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако няма отворен файл или не е подаден целеви път
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.FileOperationException
     *         ако файлът не може да бъде записан
     */
    @Override
    public void execute(String[] args) {
        CommandValidator.requireFileOpen(automatonService.isFileOpen());
        CommandValidator.requireArgs(args, 3);
        String filePath = args[2];
        automatonService.saveFileAs(filePath);
        System.out.println("Successfully saved " + new File(filePath).getName());
    }
}

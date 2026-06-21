package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.service.DeterminizationService;
import bg.tu_varna.sit.f24621815.nfa.service.LanguageService;
import bg.tu_varna.sit.f24621815.nfa.service.OperationService;
import bg.tu_varna.sit.f24621815.nfa.service.RegexService;

/**
 * Фабрика за командни обекти.
 *
 * <p>По подадения масив от токени създава и връща подходящия {@link Command} обект.
 * Всички услуги се инициализират веднъж в конструктора и се подават чрез dependency
 * injection към конкретните команди.</p>
 *
 * @author Стоил Атанасов
 * @see Command
 */
public class CommandFactory {

    /**
     * Услугата за управление на заредените автомати и файловото им състояние.
     */
    private final AutomatonService automatonService;

    /**
     * Услугата за проверка на детерминираност и детерминизация.
     */
    private final DeterminizationService determinizationService;

    /**
     * Услугата за проверка на езикови свойства.
     */
    private final LanguageService languageService;

    /**
     * Услугата за езикови операции над автомати.
     */
    private final OperationService operationService;

    /**
     * Услугата за изграждане на НКА от регулярен израз.
     */
    private final RegexService regexService;

    /**
     * Създава нова инстанция на фабриката с подадена услуга за автомати.
     *
     * @param automatonService услугата за управление на заредените автомати
     */
    public CommandFactory(AutomatonService automatonService) {
        this.automatonService = automatonService;
        this.determinizationService = new DeterminizationService();
        this.languageService = new LanguageService();
        this.operationService = new OperationService();
        this.regexService = new RegexService();
    }

    /**
     * Създава и връща командния обект, съответстващ на подадения масив от токени.
     *
     * @param tokens масивът от токени; {@code tokens[0]} е името на командата
     * @return командният обект
     * @throws InvalidCommandException ако командата не е разпозната или
     *         масивът от токени е {@code null} или празен
     */
    public Command create(String[] tokens) {
        if (tokens == null || tokens.length == 0) {
            throw new InvalidCommandException("No command provided.");
        }
        String cmd = tokens[0].toLowerCase();
        switch (cmd) {
            case "open":
                return new OpenCommand(automatonService);
            case "close":
                return new CloseCommand(automatonService);
            case "save":
                if (tokens.length >= 3 && "as".equalsIgnoreCase(tokens[1])) {
                    return new SaveAsCommand(automatonService);
                }
                if (tokens.length == 3) {
                    return new SaveAutomatonCommand(automatonService);
                }
                return new SaveCommand(automatonService);
            case "help":
                return new HelpCommand();
            case "exit":
                return new ExitCommand();
            case "list":
                return new ListCommand(automatonService);
            case "print":
                return new PrintCommand(automatonService);
            case "empty":
                return new EmptyCommand(automatonService, languageService);
            case "deterministic":
                return new DeterministicCommand(automatonService, determinizationService);
            case "determinize":
                return new DeterminizeCommand(automatonService, determinizationService);
            case "recognize":
                return new RecognizeCommand(automatonService, languageService);
            case "union":
                return new UnionCommand(automatonService, operationService);
            case "concat":
                return new ConcatCommand(automatonService, operationService);
            case "un":
                return new UnCommand(automatonService, operationService);
            case "reg":
                return new RegCommand(automatonService, regexService);
            case "finite":
                return new FiniteCommand(automatonService, languageService);
            default:
                throw new InvalidCommandException("Unknown command: " + tokens[0]);
        }
    }
}

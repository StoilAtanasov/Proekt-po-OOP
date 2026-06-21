package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.exception.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.service.DeterminizationService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

/**
 * Команда за проверка дали автоматът е детерминиран.
 *
 * @author Стоил Атанасов
 * @see Command
 * @see DeterminizationService
 */
public class DeterministicCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Услугата за проверка на детерминираност.
     */
    private final DeterminizationService determinizationService;

    /**
     * Създава нова инстанция на {@code DeterministicCommand}.
     *
     * @param automatonService        услугата за управление на заредените автомати
     * @param determinizationService  услугата за проверка на детерминираност
     */
    public DeterministicCommand(AutomatonService automatonService,
                                DeterminizationService determinizationService) {
        this.automatonService = automatonService;
        this.determinizationService = determinizationService;
    }

    /**
     * Проверява дали автоматът с посочения идентификатор е детерминиран.
     *
     * @param args масивът от аргументи; {@code args[1]} е идентификаторът
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако няма отворен файл или аргументите са недостатъчни
     * @throws AutomatonNotFoundException
     *         ако автомат с посочения идентификатор не съществува
     */
    @Override
    public void execute(String[] args) {
        CommandValidator.requireFileOpen(automatonService.isFileOpen());
        CommandValidator.requireArgs(args, 2);
        int id = CommandValidator.parseId(args[1]);
        Automaton automaton = automatonService.findById(id);
        if (automaton == null) {
            throw new AutomatonNotFoundException("Automaton with ID " + id + " not found.");
        }
        boolean det = determinizationService.isDeterministic(automaton);
        System.out.println("Automaton " + id + " is " + (det ? "deterministic." : "non-deterministic."));
    }
}

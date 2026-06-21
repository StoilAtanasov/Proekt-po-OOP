package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.exception.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.service.DeterminizationService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

/**
 * Команда за детерминизация на автомат чрез мутиране на място.
 *
 * <p>Изпълнява командата {@code determinize <id>}, която заменя
 * структурата на автомата с еквивалентен детерминиран вариант,
 * запазвайки неговия идентификатор.</p>
 *
 * @author Стоил Атанасов
 * @see Command
 * @see DeterminizationService
 */
public class DeterminizeCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Услугата за детерминизация.
     */
    private final DeterminizationService determinizationService;

    /**
     * Създава нова инстанция на {@code DeterminizeCommand}.
     *
     * @param automatonService       услугата за управление на заредените автомати
     * @param determinizationService услугата за детерминизация
     */
    public DeterminizeCommand(AutomatonService automatonService,
                              DeterminizationService determinizationService) {
        this.automatonService = automatonService;
        this.determinizationService = determinizationService;
    }

    /**
     * Детерминизира автомата с посочения идентификатор на място.
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
        Automaton dfa = determinizationService.determinize(automaton);
        automaton.setStates(dfa.getStates());
        automaton.setTransitions(dfa.getTransitions());
        System.out.println("Automaton " + id + " has been determinized.");
    }
}

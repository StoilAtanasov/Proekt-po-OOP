package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.exception.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.model.State;
import bg.tu_varna.sit.f24621815.nfa.model.Transition;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

/**
 * Команда за извеждане на структурата на автомат.
 *
 * <p>Извежда всички състояния и преходи на автомата с посочения идентификатор.</p>
 *
 * @author Стоил Атанасов
 * @see Command
 * @see AutomatonService
 */
public class PrintCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Създава нова инстанция на {@code PrintCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     */
    public PrintCommand(AutomatonService automatonService) {
        this.automatonService = automatonService;
    }

    /**
     * Извежда структурата на автомата с посочения идентификатор.
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
        System.out.println("Automaton " + id + ":");
        System.out.println("States:");
        for (State s : automaton.getStates()) {
            System.out.println("  " + s);
        }
        System.out.println("Transitions:");
        for (Transition t : automaton.getTransitions()) {
            System.out.println("  " + t);
        }
    }
}

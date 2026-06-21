package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

import java.util.List;

/**
 * Команда за извеждане на идентификаторите на всички заредени автомати.
 *
 * @author Стоил Атанасов
 * @see Command
 * @see AutomatonService
 */
public class ListCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Създава нова инстанция на {@code ListCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     */
    public ListCommand(AutomatonService automatonService) {
        this.automatonService = automatonService;
    }

    /**
     * Извежда идентификаторите на всички заредени автомати.
     *
     * @param args масивът от аргументи; не се използват допълнителни
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако няма отворен файл
     */
    @Override
    public void execute(String[] args) {
        CommandValidator.requireFileOpen(automatonService.isFileOpen());
        List<Automaton> automata = automatonService.getAutomata();
        if (automata.isEmpty()) {
            System.out.println("No automata loaded.");
            return;
        }
        System.out.println("Loaded automata IDs:");
        for (Automaton a : automata) {
            System.out.println("  " + a.getId());
        }
    }
}

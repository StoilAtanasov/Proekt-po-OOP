package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.exception.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.service.OperationService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

/**
 * Команда за построяване на конкатенация на два автомата.
 *
 * @author Стоил Атанасов
 * @see Command
 * @see OperationService
 */
public class ConcatCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Услугата за езикови операции.
     */
    private final OperationService operationService;

    /**
     * Създава нова инстанция на {@code ConcatCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     * @param operationService услугата за езикови операции
     */
    public ConcatCommand(AutomatonService automatonService, OperationService operationService) {
        this.automatonService = automatonService;
        this.operationService = operationService;
    }

    /**
     * Строи конкатенацията на двата автомата и я добавя към списъка.
     *
     * @param args масивът от аргументи; {@code args[1]} и {@code args[2]} са идентификаторите
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако няма отворен файл или аргументите са недостатъчни
     * @throws AutomatonNotFoundException
     *         ако някой от двата автомата не е намерен
     */
    @Override
    public void execute(String[] args) {
        CommandValidator.requireFileOpen(automatonService.isFileOpen());
        CommandValidator.requireArgs(args, 3);
        int id1 = CommandValidator.parseId(args[1]);
        int id2 = CommandValidator.parseId(args[2]);
        Automaton a1 = automatonService.findById(id1);
        if (a1 == null) {
            throw new AutomatonNotFoundException("Automaton with ID " + id1 + " not found.");
        }
        Automaton a2 = automatonService.findById(id2);
        if (a2 == null) {
            throw new AutomatonNotFoundException("Automaton with ID " + id2 + " not found.");
        }
        int newId = automatonService.getNextId();
        Automaton result = operationService.concat(a1, a2, newId);
        automatonService.addAutomaton(result);
        System.out.println("Created automaton with ID: " + newId);
    }
}

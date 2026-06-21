package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.exception.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.service.OperationService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

/**
 * Команда за построяване на позитивното затваряне на автомат (A+).
 *
 * @author Стоил Атанасов
 * @see Command
 * @see OperationService
 */
public class UnCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Услугата за езикови операции.
     */
    private final OperationService operationService;

    /**
     * Създава нова инстанция на {@code UnCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     * @param operationService услугата за езикови операции
     */
    public UnCommand(AutomatonService automatonService, OperationService operationService) {
        this.automatonService = automatonService;
        this.operationService = operationService;
    }

    /**
     * Строи позитивното затваряне на автомата и го добавя към списъка.
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
        int newId = automatonService.getNextId();
        Automaton result = operationService.positiveClosure(automaton, newId);
        automatonService.addAutomaton(result);
        System.out.println("Created automaton with ID: " + newId);
    }
}

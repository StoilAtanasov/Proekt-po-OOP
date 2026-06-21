package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.exception.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.service.LanguageService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

/**
 * Команда за проверка дали езикът на автомата е празен.
 *
 * @author Стоил Атанасов
 * @see Command
 * @see LanguageService
 */
public class EmptyCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Услугата за проверка на езикови свойства.
     */
    private final LanguageService languageService;

    /**
     * Създава нова инстанция на {@code EmptyCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     * @param languageService  услугата за проверка на езикови свойства
     */
    public EmptyCommand(AutomatonService automatonService, LanguageService languageService) {
        this.automatonService = automatonService;
        this.languageService = languageService;
    }

    /**
     * Проверява дали езикът на автомата с посочения идентификатор е празен.
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
        boolean empty = languageService.isEmpty(automaton);
        System.out.println("The language of automaton " + id + " is " + (empty ? "empty." : "not empty."));
    }
}

package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.exception.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.service.LanguageService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

/**
 * Команда за проверка дали дума се разпознава от автомата.
 *
 * @author Стоил Атанасов
 * @see Command
 * @see LanguageService
 */
public class RecognizeCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Услугата за проверка на езикови свойства.
     */
    private final LanguageService languageService;

    /**
     * Създава нова инстанция на {@code RecognizeCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     * @param languageService  услугата за проверка на езикови свойства
     */
    public RecognizeCommand(AutomatonService automatonService, LanguageService languageService) {
        this.automatonService = automatonService;
        this.languageService = languageService;
    }

    /**
     * Проверява дали подадената дума се разпознава от автомата.
     *
     * @param args масивът от аргументи; {@code args[1]} е идентификаторът,
     *             {@code args[2]} е думата за проверка
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако няма отворен файл или аргументите са недостатъчни
     * @throws AutomatonNotFoundException
     *         ако автомат с посочения идентификатор не съществува
     */
    @Override
    public void execute(String[] args) {
        CommandValidator.requireFileOpen(automatonService.isFileOpen());
        CommandValidator.requireArgs(args, 3);
        int id = CommandValidator.parseId(args[1]);
        String word = args[2];
        Automaton automaton = automatonService.findById(id);
        if (automaton == null) {
            throw new AutomatonNotFoundException("Automaton with ID " + id + " not found.");
        }
        boolean recognized = languageService.recognize(automaton, word);
        System.out.println("Word " + word + " is " + (recognized ? "" : "not ") + "in the language of automaton " + id + ".");
    }
}

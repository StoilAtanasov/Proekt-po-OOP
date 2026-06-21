package bg.tu_varna.sit.f24621815.nfa.command;

import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;
import bg.tu_varna.sit.f24621815.nfa.service.RegexService;
import bg.tu_varna.sit.f24621815.nfa.validation.CommandValidator;

/**
 * Команда за построяване на НКА от регулярен израз.
 *
 * <p>Изпълнява командата {@code reg <regex>} и строи НКА чрез
 * конструкцията на Томпсън.</p>
 *
 * @author Стоил Атанасов
 * @see Command
 * @see RegexService
 */
public class RegCommand implements Command {

    /**
     * Услугата за управление на заредените автомати.
     */
    private final AutomatonService automatonService;

    /**
     * Услугата за изграждане на НКА от регулярен израз.
     */
    private final RegexService regexService;

    /**
     * Създава нова инстанция на {@code RegCommand}.
     *
     * @param automatonService услугата за управление на заредените автомати
     * @param regexService     услугата за изграждане на НКА от регулярен израз
     */
    public RegCommand(AutomatonService automatonService, RegexService regexService) {
        this.automatonService = automatonService;
        this.regexService = regexService;
    }

    /**
     * Строи НКА от регулярния израз и го добавя към списъка.
     *
     * @param args масивът от аргументи; {@code args[1]} е регулярният израз
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако няма отворен файл или аргументите са недостатъчни
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidRegexException
     *         ако регулярният израз съдържа синтактична грешка
     */
    @Override
    public void execute(String[] args) {
        CommandValidator.requireFileOpen(automatonService.isFileOpen());
        CommandValidator.requireArgs(args, 2);
        String regex = args[1];
        int newId = automatonService.getNextId();
        Automaton result = regexService.fromRegex(regex, newId);
        automatonService.addAutomaton(result);
        System.out.println("Created automaton with ID: " + newId);
    }
}

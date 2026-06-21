package bg.tu_varna.sit.f24621815.nfa.validation;

import bg.tu_varna.sit.f24621815.nfa.exception.AutomatonValidationException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.model.State;

/**
 * Валидатор за проверка на структурата на автомат.
 *
 * <p>Проверява дали даден {@code Automaton} обект отговаря на
 * минималните изисквания за коректна структура: непразен списък
 * от състояния и наличие на поне едно начално състояние.</p>
 *
 * @author Стоил Атанасов
 * @see Automaton
 */
public class AutomatonValidator {

    /**
     * Проверява дали подаденият автомат е структурно валиден.
     *
     * @param automaton автоматът за проверка
     * @throws AutomatonValidationException ако автоматът е {@code null},
     *         няма състояния или няма начално състояние
     */
    public void validate(Automaton automaton) {
        if (automaton == null) {
            throw new AutomatonValidationException("Automaton cannot be null.");
        }
        if (automaton.getStates() == null || automaton.getStates().isEmpty()) {
            throw new AutomatonValidationException("Automaton must have at least one state.");
        }
        boolean hasStart = false;
        for (State s : automaton.getStates()) {
            if (s.isStart()) {
                hasStart = true;
                break;
            }
        }
        if (!hasStart) {
            throw new AutomatonValidationException("Automaton must have a start state.");
        }
    }
}

package bg.tu_varna.sit.f24621815.nfa.service;

import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.model.State;
import bg.tu_varna.sit.f24621815.nfa.model.Transition;

/**
 * Услуга за езикови операции над автомати.
 *
 * <p>Предоставя методи за изграждане на нов автомат по дадена операция:
 * обединение ({@link #union(Automaton, Automaton, int)}),
 * конкатенация ({@link #concat(Automaton, Automaton, int)}) и
 * позитивно затваряне ({@link #positiveClosure(Automaton, int)}).
 * Имената на копираните състояния се префиксират, за да се избегнат конфликти.</p>
 *
 * @author Стоил Атанасов
 */
public class OperationService {

    /**
     * Изгражда нов автомат, разпознаващ обединението на езиците на двата входни.
     *
     * <p>Добавя ново начално състояние {@code q_u_start} с eps-преходи
     * към началните състояния на двата автомата. Приемащите са тези
     * на всеки от входните автомати.</p>
     *
     * @param a1    първият автомат
     * @param a2    вторият автомат
     * @param newId идентификаторът на новия автомат
     * @return автоматът, разпознаващ обединението
     */
    public Automaton union(Automaton a1, Automaton a2, int newId) {
        Automaton result = new Automaton(newId);
        String newStart = "q_u_start";
        result.addState(new State(newStart, true, false));
        copyStatesNoStart(a1, result, "a1_");
        copyStatesNoStart(a2, result, "a2_");
        copyTransitions(a1, result, "a1_");
        copyTransitions(a2, result, "a2_");
        if (a1.getStartState() != null) {
            result.addTransition(new Transition(newStart, "", "a1_" + a1.getStartState().getId()));
        }
        if (a2.getStartState() != null) {
            result.addTransition(new Transition(newStart, "", "a2_" + a2.getStartState().getId()));
        }
        return result;
    }

    /**
     * Изгражда нов автомат, разпознаващ конкатенацията на езиците на двата входни.
     *
     * <p>Добавя eps-преходи от всяко приемащо на {@code a1} към началото на {@code a2}.
     * Приемащите на {@code a1} стават неприемащи в резултата.</p>
     *
     * @param a1    първият автомат
     * @param a2    вторият автомат
     * @param newId идентификаторът на новия автомат
     * @return автоматът, разпознаващ конкатенацията
     */
    public Automaton concat(Automaton a1, Automaton a2, int newId) {
        Automaton result = new Automaton(newId);
        copyStates(a1, result, "a1_");
        copyStatesNoStart(a2, result, "a2_");
        copyTransitions(a1, result, "a1_");
        copyTransitions(a2, result, "a2_");
        if (a2.getStartState() != null) {
            for (State s : a1.getAcceptingStates()) {
                result.addTransition(new Transition(
                        "a1_" + s.getId(), "", "a2_" + a2.getStartState().getId()));
                for (State rs : result.getStates()) {
                    if (rs.getId().equals("a1_" + s.getId())) {
                        rs.setAccepting(false);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Изгражда нов автомат, разпознаващ позитивното затваряне на входния (A+).
     *
     * <p>Запазва оригиналната структура и добавя eps-преходи от всяко
     * приемащо обратно към началото, за да позволи повтаряне.</p>
     *
     * @param a     входният автомат
     * @param newId идентификаторът на новия автомат
     * @return автоматът, разпознаващ позитивното затваряне
     */
    public Automaton positiveClosure(Automaton a, int newId) {
        Automaton result = new Automaton(newId);
        copyStates(a, result, "");
        copyTransitions(a, result, "");
        if (a.getStartState() != null) {
            for (State s : a.getAcceptingStates()) {
                result.addTransition(new Transition(s.getId(), "", a.getStartState().getId()));
            }
        }
        return result;
    }

    /**
     * Копира всички състояния от {@code src} в {@code dest} с добавен префикс,
     * запазвайки флаговете за начало и приемане.
     *
     * @param src    изходният автомат
     * @param dest   целевият автомат
     * @param prefix префиксът, добавян пред всеки идентификатор на състояние
     */
    private void copyStates(Automaton src, Automaton dest, String prefix) {
        for (State s : src.getStates()) {
            dest.addState(new State(prefix + s.getId(), s.isStart(), s.isAccepting()));
        }
    }

    /**
     * Копира всички състояния от {@code src} в {@code dest} с добавен префикс,
     * принудително задавайки {@code isStart = false} на копията.
     *
     * @param src    изходният автомат
     * @param dest   целевият автомат
     * @param prefix префиксът, добавян пред всеки идентификатор на състояние
     */
    private void copyStatesNoStart(Automaton src, Automaton dest, String prefix) {
        for (State s : src.getStates()) {
            dest.addState(new State(prefix + s.getId(), false, s.isAccepting()));
        }
    }

    /**
     * Копира всички преходи от {@code src} в {@code dest} с добавен префикс.
     *
     * @param src    изходният автомат
     * @param dest   целевият автомат
     * @param prefix префиксът, добавян пред идентификаторите на началното и целевото
     */
    private void copyTransitions(Automaton src, Automaton dest, String prefix) {
        for (Transition t : src.getTransitions()) {
            dest.addTransition(new Transition(
                    prefix + t.getFromState(), t.getSymbol(), prefix + t.getToState()));
        }
    }
}

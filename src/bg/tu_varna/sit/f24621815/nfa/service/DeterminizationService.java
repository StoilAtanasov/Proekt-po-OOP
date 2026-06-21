package bg.tu_varna.sit.f24621815.nfa.service;

import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.model.State;
import bg.tu_varna.sit.f24621815.nfa.model.Transition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Услуга за проверка на детерминираност и детерминизация на автомати.
 *
 * <p>Предоставя метода {@link #isDeterministic(Automaton)} за проверка дали
 * автоматът е детерминиран, и {@link #determinize(Automaton)} за преобразуване
 * на недетерминиран автомат в еквивалентен детерминиран чрез алгоритъма
 * на подмножествата (subset construction).</p>
 *
 * <p>Помощните методи {@link #epsilonClosure(Set, Automaton)} и
 * {@link #move(Set, String, Automaton)} се използват и от {@code LanguageService}.</p>
 *
 * @author Стоил Атанасов
 * @see LanguageService
 */
public class DeterminizationService {

    /**
     * Проверява дали автоматът е детерминиран.
     *
     * <p>Автоматът е детерминиран ако: има точно едно начално състояние,
     * не съдържа eps-преходи и за всяка двойка (състояние, символ) има
     * най-много един преход.</p>
     *
     * @param automaton автоматът за проверка
     * @return true ако автоматът е детерминиран
     */
    public boolean isDeterministic(Automaton automaton) {
        long startCount = 0;
        for (State s : automaton.getStates()) {
            if (s.isStart()) {
                startCount++;
            }
        }
        if (startCount != 1) {
            return false;
        }
        for (Transition t : automaton.getTransitions()) {
            if (t.getSymbol() == null || t.getSymbol().isEmpty()) {
                return false;
            }
        }
        Set<String> seen = new HashSet<>();
        for (Transition t : automaton.getTransitions()) {
            String key = t.getFromState() + ":" + t.getSymbol();
            if (!seen.add(key)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Изгражда еквивалентен детерминиран автомат чрез алгоритъма на подмножествата.
     *
     * <p>Всяко ДКА-състояние представлява непразно подмножество от НКА-състояния,
     * затворено под eps-преходи. Наименованието на ДКА-състоянието е сортираните
     * идентификатори, обвити в фигурни скоби, например {@code {q0,q1}}.</p>
     *
     * @param nfa недетерминираният автомат
     * @return еквивалентният детерминиран автомат с идентификатора на входния
     */
    public Automaton determinize(Automaton nfa) {
        Automaton dfa = new Automaton(nfa.getId());
        State nfaStart = nfa.getStartState();
        if (nfaStart == null) {
            return dfa;
        }
        Set<String> alphabet = alphabet(nfa);
        Map<String, Set<String>> dfaMap = new LinkedHashMap<>();
        Queue<Set<String>> workList = new LinkedList<>();
        Set<String> startSet = new HashSet<>();
        startSet.add(nfaStart.getId());
        Set<String> startClosure = epsilonClosure(startSet, nfa);
        String startName = toStateName(startClosure);
        dfaMap.put(startName, startClosure);
        workList.add(startClosure);
        boolean startAccepts = intersectsAccepting(startClosure, nfa);
        dfa.addState(new State(startName, true, startAccepts));
        while (!workList.isEmpty()) {
            Set<String> current = workList.poll();
            String currentName = toStateName(current);
            for (String sym : alphabet) {
                Set<String> moved = move(current, sym, nfa);
                Set<String> closure = epsilonClosure(moved, nfa);
                if (closure.isEmpty()) {
                    continue;
                }
                String nextName = toStateName(closure);
                dfa.addTransition(new Transition(currentName, sym, nextName));
                if (!dfaMap.containsKey(nextName)) {
                    dfaMap.put(nextName, closure);
                    workList.add(closure);
                    boolean accepts = intersectsAccepting(closure, nfa);
                    dfa.addState(new State(nextName, false, accepts));
                }
            }
        }
        return dfa;
    }

    /**
     * Изчислява eps-затварянето на подадено множество от идентификатори на състояния.
     *
     * @param states множеството от идентификатори на начални състояния
     * @param nfa    автоматът, чиито eps-преходи се следват
     * @return eps-затварянето като множество от идентификатори
     */
    public Set<String> epsilonClosure(Set<String> states, Automaton nfa) {
        Set<String> closure = new HashSet<>(states);
        Queue<String> worklist = new LinkedList<>(states);
        while (!worklist.isEmpty()) {
            String state = worklist.poll();
            for (Transition t : nfa.getTransitions()) {
                if (t.getFromState().equals(state)
                        && (t.getSymbol() == null || t.getSymbol().isEmpty())
                        && closure.add(t.getToState())) {
                    worklist.add(t.getToState());
                }
            }
        }
        return closure;
    }

    /**
     * Изчислява множеството от достижими НКА-състояния при четене на символ.
     *
     * @param states множеството от текущи НКА-идентификатори
     * @param symbol символът за четене
     * @param nfa    автоматът, чиито преходи се следват
     * @return множеството от достижими НКА-идентификатори (без eps-затваряне)
     */
    public Set<String> move(Set<String> states, String symbol, Automaton nfa) {
        Set<String> result = new HashSet<>();
        for (Transition t : nfa.getTransitions()) {
            if (states.contains(t.getFromState()) && symbol.equals(t.getSymbol())) {
                result.add(t.getToState());
            }
        }
        return result;
    }

    /**
     * Връща множеството от всички символи в азбуката на автомата.
     *
     * @param automaton автоматът, чиято азбука се извлича
     * @return множеството от символи; eps-преходите не се включват
     */
    public Set<String> alphabet(Automaton automaton) {
        Set<String> alpha = new HashSet<>();
        for (Transition t : automaton.getTransitions()) {
            if (t.getSymbol() != null && !t.getSymbol().isEmpty()) {
                alpha.add(t.getSymbol());
            }
        }
        return alpha;
    }

    /**
     * Строи каноничното наименование на ДКА-състояние от множество НКА-идентификатори.
     *
     * @param states множеството от НКА-идентификатори
     * @return сортираните идентификатори, разделени със запетаи и обвити в скоби
     */
    private String toStateName(Set<String> states) {
        List<String> sorted = new ArrayList<>(states);
        Collections.sort(sorted);
        return "{" + String.join(",", sorted) + "}";
    }

    /**
     * Проверява дали подаденото множество съдържа поне едно приемащо НКА-състояние.
     *
     * @param stateIds множеството от НКА-идентификатори
     * @param nfa      автоматът, чиито приемащи състояния се проверяват
     * @return true ако множеството съдържа поне едно приемащо НКА-състояние
     */
    private boolean intersectsAccepting(Set<String> stateIds, Automaton nfa) {
        for (State s : nfa.getAcceptingStates()) {
            if (stateIds.contains(s.getId())) {
                return true;
            }
        }
        return false;
    }
}

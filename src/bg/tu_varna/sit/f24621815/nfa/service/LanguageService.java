package bg.tu_varna.sit.f24621815.nfa.service;

import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.model.State;
import bg.tu_varna.sit.f24621815.nfa.model.Transition;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Услуга за проверка на езикови свойства на автомат.
 *
 * <p>Предоставя методи за проверка дали езикът на автомата е празен
 * ({@link #isEmpty(Automaton)}), дали дадена дума се разпознава
 * ({@link #recognize(Automaton, String)}) и дали езикът е краен
 * ({@link #isFinite(Automaton)}).</p>
 *
 * @author Стоил Атанасов
 * @see DeterminizationService
 */
public class LanguageService {

    /**
     * Услугата за детерминизация, използвана при изчисляването на eps-затваряния.
     */
    private final DeterminizationService detService = new DeterminizationService();

    /**
     * Проверява дали езикът на автомата е празен.
     *
     * <p>Използва обхождане в ширина, започващо от eps-затварянето на началното
     * състояние. Ако бъде достигнато приемащо състояние, езикът не е празен.</p>
     *
     * @param automaton автоматът за проверка
     * @return true ако езикът е празен
     */
    public boolean isEmpty(Automaton automaton) {
        State start = automaton.getStartState();
        if (start == null) {
            return true;
        }
        Set<String> visited = new HashSet<>();
        Queue<String> worklist = new LinkedList<>();
        Set<String> init = new HashSet<>();
        init.add(start.getId());
        Set<String> closure = detService.epsilonClosure(init, automaton);
        worklist.addAll(closure);
        visited.addAll(closure);
        while (!worklist.isEmpty()) {
            String state = worklist.poll();
            for (State s : automaton.getAcceptingStates()) {
                if (s.getId().equals(state)) {
                    return false;
                }
            }
            for (Transition t : automaton.getTransitions()) {
                if (t.getFromState().equals(state)) {
                    Set<String> next = new HashSet<>();
                    next.add(t.getToState());
                    for (String ns : detService.epsilonClosure(next, automaton)) {
                        if (visited.add(ns)) {
                            worklist.add(ns);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Проверява дали езикът на автомата е краен.
     *
     * <p>Детерминизира автомата, след което открива "полезните" ДКА-състояния
     * (едновременно достижими от началото и водещи до приемащо). Ако в
     * подграфа на полезните състояния има цикъл, езикът е безкраен.</p>
     *
     * @param automaton автоматът за проверка
     * @return true ако езикът е краен
     */
    public boolean isFinite(Automaton automaton) {
        Automaton dfa = detService.determinize(automaton);
        if (dfa.getStartState() == null) {
            return true;
        }
        Set<String> reachable = reachableStates(dfa);
        Set<String> productive = productiveStates(dfa);
        Set<String> useful = new HashSet<>(reachable);
        useful.retainAll(productive);
        return !hasCycle(dfa, useful);
    }

    /**
     * Проверява дали дадена дума се разпознава от автомата.
     *
     * <p>Симулира НКА чрез eps-затваряне: за всеки символ от думата се изчислява
     * множеството от достижими НКА-състояния, затворено под eps-преходи.</p>
     *
     * @param automaton автоматът
     * @param word      думата за проверка; празен низ проверява дали се приема eps
     * @return true ако думата се разпознава
     */
    public boolean recognize(Automaton automaton, String word) {
        State start = automaton.getStartState();
        if (start == null) {
            return false;
        }
        Set<String> init = new HashSet<>();
        init.add(start.getId());
        Set<String> current = detService.epsilonClosure(init, automaton);
        for (char c : word.toCharArray()) {
            current = detService.epsilonClosure(
                detService.move(current, String.valueOf(c), automaton),
                automaton
            );
            if (current.isEmpty()) {
                return false;
            }
        }
        for (State s : automaton.getAcceptingStates()) {
            if (current.contains(s.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Открива всички ДКА-състояния, достижими от началното чрез обхождане в ширина.
     *
     * @param dfa детерминираният автомат
     * @return множеството от идентификатори на достижимите състояния
     */
    private Set<String> reachableStates(Automaton dfa) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        State start = dfa.getStartState();
        if (start == null) {
            return visited;
        }
        visited.add(start.getId());
        queue.add(start.getId());
        while (!queue.isEmpty()) {
            String s = queue.poll();
            for (Transition t : dfa.getTransitions()) {
                if (t.getFromState().equals(s) && visited.add(t.getToState())) {
                    queue.add(t.getToState());
                }
            }
        }
        return visited;
    }

    /**
     * Открива всички ДКА-състояния, от които може да се достигне приемащо,
     * чрез обратно обхождане в ширина от приемащите.
     *
     * @param dfa детерминираният автомат
     * @return множеството от идентификатори на продуктивните състояния
     */
    private Set<String> productiveStates(Automaton dfa) {
        Map<String, Set<String>> reverse = new HashMap<>();
        for (Transition t : dfa.getTransitions()) {
            reverse.computeIfAbsent(t.getToState(), k -> new HashSet<>())
                   .add(t.getFromState());
        }
        Set<String> productive = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        for (State s : dfa.getAcceptingStates()) {
            productive.add(s.getId());
            queue.add(s.getId());
        }
        while (!queue.isEmpty()) {
            String s = queue.poll();
            for (String pred : reverse.getOrDefault(s, Collections.emptySet())) {
                if (productive.add(pred)) {
                    queue.add(pred);
                }
            }
        }
        return productive;
    }

    /**
     * Проверява дали в подграфа, индуциран от {@code useful}, има насочен цикъл.
     *
     * <p>Използва DFS с 3-оцветяване: бяло (0), сиво (1 - на стека), черно (2).</p>
     *
     * @param dfa    детерминираният автомат
     * @param useful множеството от полезни идентификатори на състояния
     * @return true ако в подграфа има цикъл
     */
    private boolean hasCycle(Automaton dfa, Set<String> useful) {
        Map<String, Integer> color = new HashMap<>();
        for (String s : useful) {
            color.put(s, 0);
        }
        for (String s : useful) {
            if (color.get(s) == 0 && dfsHasCycle(dfa, s, color, useful)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Рекурсивна DFS стъпка за намиране на обратна дъга.
     *
     * @param dfa    детерминираният автомат
     * @param s      текущото ДКА-състояние
     * @param color  картата с цветовете
     * @param useful множеството от полезни идентификатори
     * @return true ако е открита обратна дъга (цикъл)
     */
    private boolean dfsHasCycle(Automaton dfa, String s,
                                 Map<String, Integer> color, Set<String> useful) {
        color.put(s, 1);
        for (Transition t : dfa.getTransitions()) {
            if (!t.getFromState().equals(s)) {
                continue;
            }
            String next = t.getToState();
            if (!useful.contains(next)) {
                continue;
            }
            int c = color.getOrDefault(next, 0);
            if (c == 1) {
                return true;
            }
            if (c == 0 && dfsHasCycle(dfa, next, color, useful)) {
                return true;
            }
        }
        color.put(s, 2);
        return false;
    }
}

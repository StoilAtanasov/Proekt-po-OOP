package bg.tu_varna.sit.f24621815.nfa.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Представлява недетерминиран краен автомат с eps-преходи.
 *
 * <p>Автоматът съдържа списък от {@link State} обекти и списък от
 * {@link Transition} обекти. Всеки автомат притежава уникален числов
 * идентификатор, присвоен от {@code AutomatonService} при зареждането му.</p>
 *
 * @author Стоил Атанасов
 * @see State
 * @see Transition
 */
public class Automaton {

    /**
     * Уникален числов идентификатор на автомата.
     */
    private int id;

    /**
     * Списък от всички състояния на автомата.
     */
    private List<State> states;

    /**
     * Списък от всички преходи на автомата.
     */
    private List<Transition> transitions;

    /**
     * Създава нов автомат с празни списъци от състояния и преходи.
     */
    public Automaton() {
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
    }

    /**
     * Създава нов автомат с подаден идентификатор и празни списъци.
     *
     * @param id уникалният идентификатор на автомата
     */
    public Automaton(int id) {
        this.id = id;
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
    }

    /**
     * Връща идентификатора на автомата.
     *
     * @return идентификаторът на автомата
     */
    public int getId() {
        return id;
    }

    /**
     * Задава идентификатора на автомата.
     *
     * @param id новият идентификатор
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Връща списъка от всички състояния.
     *
     * @return списъкът от състояния
     */
    public List<State> getStates() {
        return states;
    }

    /**
     * Задава списъка от всички състояния.
     *
     * @param states новият списък от състояния
     */
    public void setStates(List<State> states) {
        this.states = states;
    }

    /**
     * Връща списъка от всички преходи.
     *
     * @return списъкът от преходи
     */
    public List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Задава списъка от всички преходи.
     *
     * @param transitions новият списък от преходи
     */
    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    /**
     * Добавя ново състояние към автомата.
     *
     * @param state състоянието за добавяне
     */
    public void addState(State state) {
        states.add(state);
    }

    /**
     * Добавя нов преход към автомата.
     *
     * @param transition преходът за добавяне
     */
    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    /**
     * Връща началното състояние на автомата.
     *
     * @return началното състояние или {@code null} ако не е дефинирано
     */
    public State getStartState() {
        for (State s : states) {
            if (s.isStart()) {
                return s;
            }
        }
        return null;
    }

    /**
     * Връща списък от всички приемащи (финални) състояния.
     *
     * @return списъкът от приемащи състояния; може да е празен
     */
    public List<State> getAcceptingStates() {
        List<State> result = new ArrayList<>();
        for (State s : states) {
            if (s.isAccepting()) {
                result.add(s);
            }
        }
        return result;
    }
}

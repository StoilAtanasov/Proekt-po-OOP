package bg.tu_varna.sit.f24621815.nfa.service;

import bg.tu_varna.sit.f24621815.nfa.exception.InvalidRegexException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.model.State;
import bg.tu_varna.sit.f24621815.nfa.model.Transition;
import bg.tu_varna.sit.f24621815.nfa.validation.RegexValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * Услуга за изграждане на НКА от регулярен израз чрез конструкцията на Томпсън.
 *
 * <p>Поддържана граматика (приоритет от нисък към висок):</p>
 * <pre>
 *   regex  ::= term ( '|' term )*
 *   term   ::= factor*
 *   factor ::= atom ( '*' | '+' | '?' )?
 *   atom   ::= char | '(' regex ')' | 'eps'
 * </pre>
 *
 * <p>Азбуката се състои от малки латински букви и цифри {@code [a-z0-9]}.
 * Ключовата дума {@code eps} (последвана от неазбучен символ) означава eps-преход.
 * Имплицитната конкатенация се моделира в {@code parseTerm}.</p>
 *
 * <p>Пример за употреба:</p>
 * <pre>
 *     reg (a|b)*ab
 * </pre>
 *
 * @author Стоил Атанасов
 */
public class RegexService {

    /**
     * Глобален брояч, осигуряващ уникални имена на НКА-състоянията.
     */
    private int stateCounter;

    /**
     * Регулярният израз, който се разбира в момента.
     */
    private String regex;

    /**
     * Текущата позиция на четене в регулярния израз.
     */
    private int pos;

    /**
     * Изгражда НКА за подадения регулярен израз чрез конструкцията на Томпсън.
     *
     * @param regex       регулярният израз
     * @param automatonId идентификаторът на новия автомат
     * @return НКА, разпознаващ езика на израза
     * @throws InvalidRegexException ако изразът съдържа синтактична грешка
     */
    public Automaton fromRegex(String regex, int automatonId) {
        this.stateCounter = 0;
        this.regex = regex.trim();
        this.pos = 0;
        RegexValidator regexValidator = new RegexValidator();
        if (!regexValidator.isValid(this.regex)) {
            throw new InvalidRegexException("Invalid regex syntax: '" + regex + "'");
        }
        try {
            Automaton result = parseRegex();
            result.setId(automatonId);
            if (pos < this.regex.length()) {
                throw new InvalidRegexException(
                        "Unexpected character '" + this.regex.charAt(pos) + "' at position " + pos);
            }
            return result;
        } catch (InvalidRegexException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidRegexException("Invalid regex: " + e.getMessage());
        }
    }

    /**
     * Разбира израз от вида {@code term ( '|' term )*}.
     *
     * @return НКА-фрагментът, съответстващ на израза
     */
    private Automaton parseRegex() {
        Automaton result = parseTerm();
        while (pos < regex.length() && regex.charAt(pos) == '|') {
            pos++;
            Automaton right = parseTerm();
            result = buildUnion(result, right);
        }
        return result;
    }

    /**
     * Разбира терм от вида {@code factor*} с имплицитна конкатенация.
     *
     * <p>Спира при достигане на {@code )} или {@code |}. Празен терм дава eps.</p>
     *
     * @return НКА-фрагментът за терма
     */
    private Automaton parseTerm() {
        Automaton result = null;
        while (pos < regex.length()
                && regex.charAt(pos) != ')'
                && regex.charAt(pos) != '|') {
            Automaton factor = parseFactor();
            if (result == null) {
                result = factor;
            } else {
                result = buildConcat(result, factor);
            }
        }
        if (result == null) {
            result = buildForEpsilon();
        }
        return result;
    }

    /**
     * Разбира фактор от вида {@code atom ( '*' | '+' | '?' )?}.
     *
     * @return НКА-фрагментът за фактора
     */
    private Automaton parseFactor() {
        Automaton atom = parseAtom();
        if (pos < regex.length()) {
            char op = regex.charAt(pos);
            if (op == '*') {
                pos++;
                return buildStar(atom);
            }
            if (op == '+') {
                pos++;
                return buildPlus(atom);
            }
            if (op == '?') {
                pos++;
                return buildOptional(atom);
            }
        }
        return atom;
    }

    /**
     * Разбира атом: ключова дума {@code eps}, групиран израз или единичен символ.
     *
     * @return НКА-фрагментът за атома
     * @throws InvalidRegexException ако символът на текущата позиция е неочакван
     */
    private Automaton parseAtom() {
        if (pos >= regex.length()) {
            throw new InvalidRegexException("Unexpected end of regex");
        }
        char c = regex.charAt(pos);
        if (c == 'e' && pos + 2 < regex.length()
                && regex.substring(pos, pos + 3).equals("eps")) {
            char after = (pos + 3 < regex.length()) ? regex.charAt(pos + 3) : 0;
            if (!Character.isLetterOrDigit(after)) {
                pos += 3;
                return buildForEpsilon();
            }
        }
        if (c == '(') {
            pos++;
            Automaton inner = parseRegex();
            if (pos >= regex.length() || regex.charAt(pos) != ')') {
                throw new InvalidRegexException("Expected ')' at position " + pos);
            }
            pos++;
            return inner;
        }
        if (Character.isLetterOrDigit(c)) {
            pos++;
            return buildForChar(c);
        }
        throw new InvalidRegexException("Unexpected character '" + c + "' at position " + pos);
    }

    /**
     * Строи НКА-фрагмент за единичен символ.
     *
     * @param c символът от азбуката
     * @return НКА-фрагмент с два върха и един преход по символа
     */
    private Automaton buildForChar(char c) {
        Automaton a = new Automaton();
        State s0 = new State("s" + stateCounter++, true, false);
        State s1 = new State("s" + stateCounter++, false, true);
        a.addState(s0);
        a.addState(s1);
        a.addTransition(new Transition(s0.getId(), String.valueOf(c), s1.getId()));
        return a;
    }

    /**
     * Строи НКА-фрагмент за eps: едно начално и приемащо единствено състояние.
     *
     * @return НКА-фрагмент за eps
     */
    private Automaton buildForEpsilon() {
        Automaton a = new Automaton();
        State s0 = new State("s" + stateCounter++, true, true);
        a.addState(s0);
        return a;
    }

    /**
     * Строи НКА-фрагмент за обединение на два фрагмента.
     *
     * @param a1 първият НКА-фрагмент
     * @param a2 вторият НКА-фрагмент
     * @return НКА-фрагмент за обединението
     */
    private Automaton buildUnion(Automaton a1, Automaton a2) {
        String nsId = "s" + stateCounter++;
        String naId = "s" + stateCounter++;
        Automaton result = new Automaton();
        result.addState(new State(nsId, true, false));
        result.addState(new State(naId, false, true));
        for (State s : a1.getStates()) {
            result.addState(new State(s.getId(), false, false));
        }
        for (State s : a2.getStates()) {
            result.addState(new State(s.getId(), false, false));
        }
        result.addTransition(new Transition(nsId, "", a1.getStartState().getId()));
        result.addTransition(new Transition(nsId, "", a2.getStartState().getId()));
        for (State s : a1.getAcceptingStates()) {
            result.addTransition(new Transition(s.getId(), "", naId));
        }
        for (State s : a2.getAcceptingStates()) {
            result.addTransition(new Transition(s.getId(), "", naId));
        }
        copyTransitions(a1, result);
        copyTransitions(a2, result);
        return result;
    }

    /**
     * Строи НКА-фрагмент за конкатенация на два фрагмента.
     *
     * @param a1 първият НКА-фрагмент
     * @param a2 вторият НКА-фрагмент
     * @return НКА-фрагмент за конкатенацията
     */
    private Automaton buildConcat(Automaton a1, Automaton a2) {
        Automaton result = new Automaton();
        for (State s : a1.getStates()) {
            result.addState(new State(s.getId(), s.isStart(), false));
        }
        for (State s : a2.getStates()) {
            result.addState(new State(s.getId(), false, s.isAccepting()));
        }
        for (State s : a1.getAcceptingStates()) {
            result.addTransition(new Transition(s.getId(), "", a2.getStartState().getId()));
        }
        copyTransitions(a1, result);
        copyTransitions(a2, result);
        return result;
    }

    /**
     * Строи НКА-фрагмент за затварянето на Клини (r*).
     *
     * @param a входният НКА-фрагмент
     * @return НКА-фрагмент за r*
     */
    private Automaton buildStar(Automaton a) {
        String nsId = "s" + stateCounter++;
        Automaton result = new Automaton();
        result.addState(new State(nsId, true, true));
        for (State s : a.getStates()) {
            result.addState(new State(s.getId(), false, false));
        }
        result.addTransition(new Transition(nsId, "", a.getStartState().getId()));
        for (State s : a.getAcceptingStates()) {
            result.addTransition(new Transition(s.getId(), "", nsId));
        }
        copyTransitions(a, result);
        return result;
    }

    /**
     * Строи НКА-фрагмент за позитивното затваряне (r+), реализирано като r.(r*).
     *
     * @param a входният НКА-фрагмент
     * @return НКА-фрагмент за r+
     */
    private Automaton buildPlus(Automaton a) {
        Automaton copy = deepCopy(a);
        Automaton star = buildStar(copy);
        return buildConcat(a, star);
    }

    /**
     * Строи НКА-фрагмент за незадължителния оператор (r?), реализирано като r|eps.
     *
     * @param a входният НКА-фрагмент
     * @return НКА-фрагмент за r?
     */
    private Automaton buildOptional(Automaton a) {
        return buildUnion(a, buildForEpsilon());
    }

    /**
     * Копира всички преходи от {@code src} в {@code dest}.
     *
     * @param src  изходният НКА-фрагмент
     * @param dest целевият НКА-фрагмент
     */
    private void copyTransitions(Automaton src, Automaton dest) {
        for (Transition t : src.getTransitions()) {
            dest.addTransition(new Transition(t.getFromState(), t.getSymbol(), t.getToState()));
        }
    }

    /**
     * Прави дълбоко копие на НКА-фрагмент с нови уникални имена на всички състояния.
     *
     * @param src изходният НКА-фрагмент
     * @return копието с преименувани състояния
     */
    private Automaton deepCopy(Automaton src) {
        Map<String, String> rename = new HashMap<>();
        for (State s : src.getStates()) {
            rename.put(s.getId(), "s" + stateCounter++);
        }
        Automaton copy = new Automaton();
        for (State s : src.getStates()) {
            copy.addState(new State(rename.get(s.getId()), s.isStart(), s.isAccepting()));
        }
        for (Transition t : src.getTransitions()) {
            copy.addTransition(new Transition(
                    rename.get(t.getFromState()), t.getSymbol(), rename.get(t.getToState())));
        }
        return copy;
    }
}
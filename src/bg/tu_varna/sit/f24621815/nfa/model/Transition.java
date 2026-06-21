package bg.tu_varna.sit.f24621815.nfa.model;

/**
 * Представлява един преход в недетерминиран краен автомат.
 *
 * <p>Преходът свързва начално и целево състояние чрез символ от азбуката.
 * Когато символът е {@code null} или празен низ, преходът е eps преход.</p>
 *
 * @author Стоил Атанасов
 */
public class Transition {

    /**
     * Идентификатор на началното състояние.
     */
    private String fromState;

    /**
     * Символ на прехода. Празен низ означава eps преход.
     */
    private String symbol;

    /**
     * Идентификатор на целевото състояние.
     */
    private String toState;

    /**
     * Създава нов, неинициализиран преход.
     */
    public Transition() {
    }

    /**
     * Създава нов преход с подадени параметри.
     *
     * @param fromState идентификаторът на началното състояние
     * @param symbol    символът на прехода; празен низ за eps преход
     * @param toState   идентификаторът на целевото състояние
     */
    public Transition(String fromState, String symbol, String toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }

    /**
     * Връща идентификатора на началното състояние.
     *
     * @return идентификаторът на началното състояние
     */
    public String getFromState() {
        return fromState;
    }

    /**
     * Задава идентификатора на началното състояние.
     *
     * @param fromState новият идентификатор на началното състояние
     */
    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    /**
     * Връща символа на прехода.
     *
     * @return символът на прехода; празен низ за eps преход
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Задава символа на прехода.
     *
     * @param symbol новият символ; празен низ за eps преход
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Връща идентификатора на целевото състояние.
     *
     * @return идентификаторът на целевото състояние
     */
    public String getToState() {
        return toState;
    }

    /**
     * Задава идентификатора на целевото състояние.
     *
     * @param toState новият идентификатор
     */
    public void setToState(String toState) {
        this.toState = toState;
    }

    /**
     * Връща текстово представяне на прехода.
     *
     * @return низ от вида {@code fromState --symbol--> toState}
     */
    @Override
    public String toString() {
        String sym = (symbol == null || symbol.isEmpty()) ? "eps" : symbol;
        return fromState + " --" + sym + "--> " + toState;
    }
}

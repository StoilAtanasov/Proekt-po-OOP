package bg.tu_varna.sit.f24621815.nfa.model;

/**
 * Представлява едно състояние на недетерминиран краен автомат.
 *
 * <p>Всяко състояние притежава уникален идентификатор и два булеви флага,
 * указващи дали е начално и дали е приемащо.</p>
 *
 * @author Стоил Атанасов
 */
public class State {

    /**
     * Уникален идентификатор на състоянието.
     */
    private String id;

    /**
     * Указва дали е начално състояние.
     */
    private boolean start;

    /**
     * Указва дали е приемащо (финално) състояние.
     */
    private boolean accepting;

    /**
     * Създава ново, неинициализирано състояние.
     */
    public State() {
    }

    /**
     * Създава ново състояние с подадени параметри.
     *
     * @param id        уникален идентификатор на състоянието
     * @param start     true ако е начално състояние
     * @param accepting true ако е приемащо състояние
     */
    public State(String id, boolean start, boolean accepting) {
        this.id = id;
        this.start = start;
        this.accepting = accepting;
    }

    /**
     * Връща идентификатора на състоянието.
     *
     * @return идентификаторът на състоянието
     */
    public String getId() {
        return id;
    }

    /**
     * Задава идентификатора на състоянието.
     *
     * @param id новият идентификатор
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Проверява дали е начално състояние.
     *
     * @return true ако е начално
     */
    public boolean isStart() {
        return start;
    }

    /**
     * Задава дали е начално състояние.
     *
     * @param start true ако е начално
     */
    public void setStart(boolean start) {
        this.start = start;
    }

    /**
     * Проверява дали е приемащо състояние.
     *
     * @return true ако е приемащо
     */
    public boolean isAccepting() {
        return accepting;
    }

    /**
     * Задава дали е приемащо състояние.
     *
     * @param accepting true ако е приемащо
     */
    public void setAccepting(boolean accepting) {
        this.accepting = accepting;
    }

    /**
     * Връща текстово представяне на състоянието.
     *
     * @return низ от вида {@code id [start] [accepting]}
     */
    @Override
    public String toString() {
        return id + (start ? " [start]" : "") + (accepting ? " [accepting]" : "");
    }
}

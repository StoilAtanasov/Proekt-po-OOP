package bg.tu_varna.sit.f24621815.nfa.exception;

/**
 * Изключение, хвърляно при невалиден синтаксис на регулярен израз.
 *
 * @author Стоил Атанасов
 * @see NfaException
 */
public class InvalidRegexException extends NfaException {

    /**
     * Създава ново изключение с подадено съобщение.
     *
     * @param message описание на грешката в регулярния израз
     */
    public InvalidRegexException(String message) {
        super(message);
    }
}

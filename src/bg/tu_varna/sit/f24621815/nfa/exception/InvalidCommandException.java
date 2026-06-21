package bg.tu_varna.sit.f24621815.nfa.exception;

/**
 * Изключение, хвърляно при невалидна команда или недостатъчен брой аргументи.
 *
 * @author Стоил Атанасов
 * @see NfaException
 */
public class InvalidCommandException extends NfaException {

    /**
     * Създава ново изключение с подадено съобщение.
     *
     * @param message описание на грешката в командата
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}

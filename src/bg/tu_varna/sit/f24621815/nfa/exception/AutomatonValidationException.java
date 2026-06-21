package bg.tu_varna.sit.f24621815.nfa.exception;

/**
 * Изключение, хвърляно когато структурата на автомат не е валидна.
 *
 * @author Стоил Атанасов
 * @see NfaException
 */
public class AutomatonValidationException extends NfaException {

    /**
     * Създава ново изключение с подадено съобщение.
     *
     * @param message описание на валидационната грешка
     */
    public AutomatonValidationException(String message) {
        super(message);
    }
}

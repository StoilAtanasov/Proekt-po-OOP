package bg.tu_varna.sit.f24621815.nfa.exception;

/**
 * Изключение, хвърляно когато автомат с подадения идентификатор
 * не е намерен в списъка с заредените автомати.
 *
 * @author Стоил Атанасов
 * @see NfaException
 */
public class AutomatonNotFoundException extends NfaException {

    /**
     * Създава ново изключение с подадено съобщение.
     *
     * @param message описание на грешката
     */
    public AutomatonNotFoundException(String message) {
        super(message);
    }
}

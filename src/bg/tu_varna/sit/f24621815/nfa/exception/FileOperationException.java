package bg.tu_varna.sit.f24621815.nfa.exception;

/**
 * Изключение, хвърляно при грешка при четене или запис на файл.
 *
 * @author Стоил Атанасов
 * @see NfaException
 */
public class FileOperationException extends NfaException {

    /**
     * Създава ново изключение с подадено съобщение.
     *
     * @param message описание на грешката при файловата операция
     */
    public FileOperationException(String message) {
        super(message);
    }
}

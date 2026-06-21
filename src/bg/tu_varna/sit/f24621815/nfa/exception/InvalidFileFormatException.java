package bg.tu_varna.sit.f24621815.nfa.exception;

/**
 * Изключение, хвърляно когато прочетеният файл не отговаря
 * на очаквания формат за автомати.
 *
 * @author Стоил Атанасов
 * @see NfaException
 */
public class InvalidFileFormatException extends NfaException {

    /**
     * Създава ново изключение с подадено съобщение.
     *
     * @param message описание на форматната грешка
     */
    public InvalidFileFormatException(String message) {
        super(message);
    }
}

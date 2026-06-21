package bg.tu_varna.sit.f24621815.nfa.exception;

/**
 * Основен изключителен клас за приложението за недетерминирани крайни автомати.
 *
 * <p>Всички специфични изключения в пакета наследяват този клас,
 * за да позволят единна обработка на грешки.</p>
 *
 * @author Стоил Атанасов
 */
public class NfaException extends RuntimeException {

    /**
     * Създава ново изключение с подадено съобщение.
     *
     * @param message описание на грешката
     */
    public NfaException(String message) {
        super(message);
    }
}

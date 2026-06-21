package bg.tu_varna.sit.f24621815.nfa.command;

/**
 * Интерфейс за командата в шаблона Command.
 *
 * <p>Всяка конкретна команда имплементира този интерфейс и реализира
 * метода {@link #execute(String[])}, съдържащ логиката на командата.</p>
 *
 * @author Стоил Атанасов
 */
public interface Command {

    /**
     * Изпълнява командата с подадените аргументи.
     *
     * @param args масивът от токени на командния ред; {@code args[0]} е името на командата
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException
     *         ако аргументите са невалидни или файлът не е отворен
     * @throws bg.tu_varna.sit.f24621815.nfa.exception.AutomatonNotFoundException
     *         ако посоченият идентификатор не съответства на зареден автомат
     */
    void execute(String[] args);
}

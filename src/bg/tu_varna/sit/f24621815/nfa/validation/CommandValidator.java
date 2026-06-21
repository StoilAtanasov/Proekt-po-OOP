package bg.tu_varna.sit.f24621815.nfa.validation;

import bg.tu_varna.sit.f24621815.nfa.exception.InvalidCommandException;

/**
 * Валидатор за проверка на аргументите на команди.
 *
 * <p>Предоставя статични помощни методи, използвани от командните класове
 * за проверка на предусловия: отворен файл, брой аргументи и формат на ID.</p>
 *
 * <p>Пример за употреба:</p>
 * <pre>
 *     CommandValidator.requireFileOpen(automatonService.isFileOpen());
 *     CommandValidator.requireArgs(args, 2);
 *     int id = CommandValidator.parseId(args[1]);
 * </pre>
 *
 * @author Стоил Атанасов
 */
public class CommandValidator {

    /**
     * Проверява дали има отворен файл.
     *
     * @param fileOpen {@code true} ако има отворен файл
     * @throws InvalidCommandException ако {@code fileOpen} е {@code false}
     */
    public static void requireFileOpen(boolean fileOpen) {
        if (!fileOpen) {
            throw new InvalidCommandException("No file is currently open. Use 'open <file>' first.");
        }
    }

    /**
     * Проверява дали масивът от аргументи съдържа поне {@code min} елемента.
     *
     * @param args масивът от аргументи на командата
     * @param min  минималният изискван брой аргументи
     * @throws InvalidCommandException ако {@code args} е {@code null}
     *         или съдържа по-малко от {@code min} елемента
     */
    public static void requireArgs(String[] args, int min) {
        if (args == null || args.length < min) {
            throw new InvalidCommandException(
                "Too few arguments. Expected at least " + (min - 1) + ".");
        }
    }

    /**
     * Разбира подадения низ като числов идентификатор на автомат.
     *
     * @param value низ, представляващ целочислен идентификатор
     * @return разбраната целочислена стойност
     * @throws InvalidCommandException ако {@code value} не е валидно цяло число
     */
    public static int parseId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Invalid automaton ID: '" + value + "'");
        }
    }
}

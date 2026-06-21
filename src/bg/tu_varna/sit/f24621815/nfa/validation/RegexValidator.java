package bg.tu_varna.sit.f24621815.nfa.validation;

/**
 * Валидатор за синтактична проверка на регулярен израз.
 *
 * <p>Проверява дали регулярният израз съдържа само допустими символи и дали
 * скобите в него са балансирани. Методите могат да бъдат ползвани преди
 * подаване на израза към {@code RegexService}.</p>
 *
 * @author Стоил Атанасов
 */
public class RegexValidator {

    /**
     * Проверява дали регулярният израз е синтактично валиден.
     *
     * @param regex регулярният израз за проверка
     * @return {@code true} ако изразът е непразен, съдържа само допустими символи
     *         и скобите му са балансирани
     */
    public boolean isValid(String regex) {
        if (regex == null || regex.isEmpty()) {
            return false;
        }
        return hasBalancedParens(regex) && hasValidChars(regex);
    }

    /**
     * Проверява дали скобите в низа са балансирани.
     *
     * @param regex регулярният израз за проверка
     * @return {@code true} ако всяка отваряща скоба има съответна затваряща
     */
    private boolean hasBalancedParens(String regex) {
        int depth = 0;
        for (char c : regex.toCharArray()) {
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            }
            if (depth < 0) {
                return false;
            }
        }
        return depth == 0;
    }

    /**
     * Проверява дали всички символи в израза са от допустимата азбука.
     *
     * <p>Допустими символи: малки латински букви, цифри и операторите
     * {@code *}, {@code +}, {@code |}, {@code (}, {@code )}, {@code .}.</p>
     *
     * @param regex регулярният израз за проверка
     * @return {@code true} ако всички символи са допустими
     */
    private boolean hasValidChars(String regex) {
        for (char c : regex.toCharArray()) {
            boolean lower = c >= 'a' && c <= 'z';
            boolean digit = c >= '0' && c <= '9';
            boolean op = c == '*' || c == '+' || c == '|'
                      || c == '(' || c == ')' || c == '.';
            if (!lower && !digit && !op) {
                return false;
            }
        }
        return true;
    }
}

package bg.tu_varna.sit.f24621815.nfa.command;

/**
 * Команда за изход от приложението.
 *
 * <p>Изпълнява командата {@code exit}, която извежда прощално съобщение
 * и прекратява изпълнението на програмата.</p>
 *
 * @author Стоил Атанасов
 * @see Command
 */
public class ExitCommand implements Command {

    /**
     * Създава нова инстанция на {@code ExitCommand}.
     */
    public ExitCommand() {
    }

    /**
     * Извежда прощално съобщение и прекратява изпълнението.
     *
     * @param args масивът от аргументи; не се използват
     */
    @Override
    public void execute(String[] args) {
        System.out.println("Exiting the program...");
        System.exit(0);
    }
}

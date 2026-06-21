package bg.tu_varna.sit.f24621815.nfa;

import bg.tu_varna.sit.f24621815.nfa.command.Command;
import bg.tu_varna.sit.f24621815.nfa.command.CommandFactory;
import bg.tu_varna.sit.f24621815.nfa.exception.NfaException;
import bg.tu_varna.sit.f24621815.nfa.service.AutomatonService;

import java.util.Scanner;

/**
 * Входна точка на приложението за работа с недетерминирани крайни автомати.
 *
 * <p>Стартира интерактивен команден ред, в който потребителят въвежда
 * команди за управление на автомати. Всяка команда се разбира, обработва
 * и изпълнява до въвеждане на {@code exit}.</p>
 *
 * <p>Пример за стартиране:</p>
 * <pre>
 *     java bg.tu_varna.sit.f24621815.nfa.Application
 * </pre>
 *
 * @author Стоил Атанасов
 * @see CommandFactory
 * @see AutomatonService
 */
public class Application {

    /**
     * Стартира приложението и обработва командите на потребителя.
     *
     * @param args аргументите от командния ред; не се използват
     */
    public static void main(String[] args) {
        AutomatonService automatonService = new AutomatonService();
        CommandFactory factory = new CommandFactory(automatonService);
        Scanner scanner = new Scanner(System.in);
        System.out.println("NFA Application. Type 'help' for available commands.");
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] tokens = line.trim().split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            for (int i = 0; i < tokens.length; i++) {
                tokens[i] = tokens[i].replace("\"", "");
            }
            try {
                Command command = factory.create(tokens);
                command.execute(tokens);
            } catch (NfaException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

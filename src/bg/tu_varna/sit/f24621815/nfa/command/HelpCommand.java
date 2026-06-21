package bg.tu_varna.sit.f24621815.nfa.command;

/**
 * Команда за извеждане на помощна информация за всички поддържани команди.
 *
 * <p>Изпълнява командата {@code help} и извежда списък с наличните команди
 * и кратко описание на всяка от тях.</p>
 *
 * @author Стоил Атанасов
 * @see Command
 */
public class HelpCommand implements Command {

    /**
     * Създава нова инстанция на {@code HelpCommand}.
     */
    public HelpCommand() {
    }

    /**
     * Извежда списъка с поддържаните команди.
     *
     * @param args масивът от аргументи; не се използват
     */
    @Override
    public void execute(String[] args) {
        System.out.println("The following commands are supported:");
        System.out.println("open <file>               opens <file>");
        System.out.println("close                     closes currently opened file");
        System.out.println("save                      saves the currently open file");
        System.out.println("save as <file>            saves the currently open file in <file>");
        System.out.println("help                      prints this information");
        System.out.println("exit                      exits the program");
        System.out.println("list                      lists all loaded automata IDs");
        System.out.println("print <id>                prints all transitions of automaton <id>");
        System.out.println("save <id> <file>          saves automaton <id> to <file>");
        System.out.println("empty <id>                checks if the language of automaton <id> is empty");
        System.out.println("deterministic <id>        checks if automaton <id> is deterministic");
        System.out.println("determinize <id>          determinizes automaton <id> in place");
        System.out.println("recognize <id> <word>     checks if <word> is in the language of <id>");
        System.out.println("union <id1> <id2>         union of two automata; prints new ID");
        System.out.println("concat <id1> <id2>        concatenation of two automata; prints new ID");
        System.out.println("un <id>                   positive closure of automaton <id>; prints new ID");
        System.out.println("reg <regex>               creates automaton from regex; prints new ID");
        System.out.println("finite <id>               checks if the language of automaton <id> is finite");
    }
}

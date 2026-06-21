package bg.tu_varna.sit.f24621815.nfa.service;

import bg.tu_varna.sit.f24621815.nfa.exception.FileOperationException;
import bg.tu_varna.sit.f24621815.nfa.exception.InvalidFileFormatException;
import bg.tu_varna.sit.f24621815.nfa.model.Automaton;
import bg.tu_varna.sit.f24621815.nfa.model.State;
import bg.tu_varna.sit.f24621815.nfa.model.Transition;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Предоставя операции за разбор и сериализация на автомати в XML формат.
 *
 * <p>Файловият формат обгражда всеки автомат в тег {@code <automaton>},
 * съдържащ {@code <states>} и {@code <transitions>} секции. Разборът
 * се извършва чрез {@code String.indexOf} и {@code String.substring}
 * без използване на XML библиотеки.</p>
 *
 * <p>Пример за формат:</p>
 * <pre>
 * automaton id="1"
 *   states
 *     state id="q0" start="true" accepting="false"/
 *   /states
 *   transitions
 *     transition from="q0" symbol="a" to="q1"/
 *   /transitions
 * /automaton
 * </pre>
 *
 * @author Стоил Атанасов
 */
public class XmlAutomatonOperations {

    /**
     * Разбира XML съдържание и връща списък от автомати.
     *
     * @param content  XML съдържанието като низ
     * @param startId  началният идентификатор за автомати без явен id атрибут
     * @return списъкът от разпознати автомати; може да е празен
     * @throws InvalidFileFormatException ако съдържанието не може да бъде разбрано
     */
    public List<Automaton> parse(String content, int startId) {
        List<Automaton> result = new ArrayList<>();
        if (content == null || content.trim().isEmpty()) {
            return result;
        }
        try {
            int pos = 0;
            while (true) {
                int start = content.indexOf("<automaton", pos);
                if (start == -1) {
                    break;
                }
                int end = content.indexOf("</automaton>", start);
                if (end == -1) {
                    break;
                }
                end += "</automaton>".length();
                String block = content.substring(start, end);
                result.add(parseAutomaton(block, startId++));
                pos = end;
            }
        } catch (Exception e) {
            throw new InvalidFileFormatException("Failed to parse automaton file: " + e.getMessage());
        }
        return result;
    }

    /**
     * Разбира XML блок, описващ един автомат.
     *
     * @param block     XML блокът между тагове {@code <automaton>} и {@code </automaton>}
     * @param defaultId стандартен идентификатор, ако атрибутът {@code id} липсва
     * @return разбраният автомат
     */
    private Automaton parseAutomaton(String block, int defaultId) {
        Automaton automaton = new Automaton();
        String idAttr = getAttribute(block, "id");
        automaton.setId(idAttr != null ? Integer.parseInt(idAttr) : defaultId);

        int ss = block.indexOf("<states>");
        int se = block.indexOf("</states>");
        if (ss != -1 && se != -1) {
            parseStates(block.substring(ss + "<states>".length(), se), automaton);
        }

        int ts = block.indexOf("<transitions>");
        int te = block.indexOf("</transitions>");
        if (ts != -1 && te != -1) {
            parseTransitions(block.substring(ts + "<transitions>".length(), te), automaton);
        }
        return automaton;
    }

    /**
     * Разбира секцията от състояния и ги добавя към автомата.
     *
     * @param block     XML съдържанието на секцията {@code <states>}
     * @param automaton автоматът, към който се добавят състоянията
     */
    private void parseStates(String block, Automaton automaton) {
        int pos = 0;
        while (true) {
            int start = block.indexOf("<state", pos);
            if (start == -1) {
                break;
            }
            int end = block.indexOf("/>", start);
            if (end == -1) {
                break;
            }
            end += "/>".length();
            String tag = block.substring(start, end);
            automaton.addState(new State(
                getAttribute(tag, "id"),
                "true".equals(getAttribute(tag, "start")),
                "true".equals(getAttribute(tag, "accepting"))
            ));
            pos = end;
        }
    }

    /**
     * Разбира секцията от преходи и ги добавя към автомата.
     *
     * @param block     XML съдържанието на секцията {@code <transitions>}
     * @param automaton автоматът, към който се добавят преходите
     */
    private void parseTransitions(String block, Automaton automaton) {
        int pos = 0;
        while (true) {
            int start = block.indexOf("<transition", pos);
            if (start == -1) {
                break;
            }
            int end = block.indexOf("/>", start);
            if (end == -1) {
                break;
            }
            end += "/>".length();
            String tag = block.substring(start, end);
            String sym = getAttribute(tag, "symbol");
            automaton.addTransition(new Transition(
                getAttribute(tag, "from"),
                sym != null ? sym : "",
                getAttribute(tag, "to")
            ));
            pos = end;
        }
    }

    /**
     * Извлича стойността на XML атрибут от таг.
     *
     * @param tag  XML тагът като низ
     * @param name името на атрибута
     * @return стойността на атрибута или {@code null} ако не е намерен
     */
    private String getAttribute(String tag, String name) {
        String search = name + "=\"";
        int start = tag.indexOf(search);
        if (start == -1) {
            return null;
        }
        start += search.length();
        int end = tag.indexOf("\"", start);
        return end == -1 ? null : tag.substring(start, end);
    }

    /**
     * Сериализира списък от автомати в XML низ.
     *
     * @param automata списъкът от автомати за сериализация
     * @return XML представянето на всички автомати
     */
    public String serialize(List<Automaton> automata) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<automata>\n");
        for (Automaton a : automata) {
            serializeAutomaton(a, sb);
        }
        sb.append("</automata>\n");
        return sb.toString();
    }

    /**
     * Добавя XML представянето на един автомат към подадения {@code StringBuilder}.
     *
     * @param a  автоматът за сериализация
     * @param sb {@code StringBuilder}-ът, към който се добавя съдържанието
     */
    private void serializeAutomaton(Automaton a, StringBuilder sb) {
        sb.append("  <automaton").append(xmlAttr("id", a.getId())).append(">\n");
        sb.append("    <states>\n");
        for (State s : a.getStates()) {
            sb.append("      <state")
              .append(xmlAttr("id", s.getId()))
              .append(xmlAttr("start", s.isStart()))
              .append(xmlAttr("accepting", s.isAccepting()))
              .append("/>\n");
        }
        sb.append("    </states>\n");
        sb.append("    <transitions>\n");
        for (Transition t : a.getTransitions()) {
            sb.append("      <transition")
              .append(xmlAttr("from", t.getFromState()))
              .append(xmlAttr("symbol", t.getSymbol()))
              .append(xmlAttr("to", t.getToState()))
              .append("/>\n");
        }
        sb.append("    </transitions>\n");
        sb.append("  </automaton>\n");
    }

    /**
     * Форматира XML атрибут като низ.
     *
     * @param name  името на атрибута
     * @param value стойността на атрибута
     * @return низ от вида {@code  name="value"}
     */
    private String xmlAttr(String name, Object value) {
        return " " + name + "=\"" + value + "\"";
    }

    /**
     * Записва сериализираните автомати във файл.
     *
     * @param automata списъкът от автомати за запис
     * @param filePath пътят към файла
     * @throws FileOperationException ако файлът не може да бъде записан
     */
    public void writeToFile(List<Automaton> automata, String filePath) {
        try {
            Files.writeString(Paths.get(filePath), serialize(automata));
        } catch (Exception e) {
            throw new FileOperationException("Failed to write file: " + e.getMessage());
        }
    }
}

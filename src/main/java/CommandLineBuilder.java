import org.apache.commons.cli.*;
import java.util.Arrays;

public class CommandLineBuilder {


    private static final Options options = new Options();
    private final Option option0 = Option.builder("a").required(true).hasArg().desc("Rodzaj API np: gov, airly").build();
    private final Option option1 = Option.builder("b").required(false).hasArgs().desc("Wypisanie aktualnego indeksu jakości powietrza" +
            " dla podanej (nazwy) stacji pomiarowej \n Argument: nazwa stacji.").build();
    private final Option option2 = Option.builder("c").required(false).hasArgs().desc("Wypisanie dla podanego dnia, " +
            "godziny oraz stacji pomiarowej  aktualnej wartości danego parametru (np. PM10) \n Argumenty: data, nazwa stacji, nazwa parametru").build();
    private final Option option3 = Option.builder("d").required(false).hasArgs().desc("Obliczenie średniej wartości " +
            "zanieczyszczenia / parametru (np. SO2) za podany okres dla danej stacji \n Argumenty: data startowa, data końcowa, nazwa stacji," +
            " nazwaparametru.").build();
    private final Option option4 = Option.builder("e").required(false).hasArgs().desc("Odszukanie, dla wymienionych stacji, " +
            "parametru którego wartość, począwszy od podanej godziny (danego dnia), uległa największym wahaniom \n Argumenty:" +
            "data startu, lista nazw stacji.").build();
    private final Option option5 = Option.builder("f").required(false).hasArgs().desc("Odszukanie parametru, którego wartość" +
            "była najmniejsza o podanej godzinie podanego dnia \n Argumenty: ").build();
    private final Option option6 = Option.builder("g").required(false).hasArgs().desc("Dla podanej stacji, wypisanie N stanowisk " +
            "pomiarowych, posortowanych (rosnąco), które o podanej godzinie określonego dnia, " +
            "zanotowały największą wartość przekroczenia normy parametru \n Argumenty: nazwa stacji, data startowa, liczba N.").build();
    private final Option option7 = Option.builder("i").required(false).hasArgs().desc("Dla podanego parametru wypisanie informacji: " +
            "kiedy (dzień, godzina) i gdzie (stacja), miał on największą wartość, a kiedy i gdzie najmniejszą \n" +
            "Argument: nazwa parametru.").build();
    private final Option option8 = Option.builder("j").required(false).hasArgs().desc("Rysowanie wspólnego wykresu zmian wartości " +
            "podanego parametru w układzie godzinowym \n Argumenty: lista nazw Stacji, godzina startu, godzina końca").build();
    private final Option optionh = Option.builder("h").required(false).desc("Pokaż pomoc").build();

    public CommandLineBuilder(){
        options.addOption(option0);
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);
        options.addOption(option6);
        options.addOption(option7);
        options.addOption(option8);
        options.addOption(optionh);
    }

    private static CommandLine generateCommandLine(String[] args){
        final CommandLineParser lineParser = new DefaultParser();
        CommandLine commandLine = null;
        try
        {
            commandLine = lineParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(
                    "Unable to parse command-line arguments "
                            + Arrays.toString(args) + " due to: "
                            + e);
        }
        return commandLine;
    }

    private static void generateHelp(){
        String header = "Sprawdź jakość powietrza";
        String footer = "Jakość powietrza by Ania Banaszak";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Jakość powietrza", header, options, footer,true);
    }

    /**
     * @param args to argumenty z linii komend przekazane do programu
     *  w zależności od arguementów wywołuje odpowiednie metody
     */
   public static void runParser(String[] args){
        if(args.length == 0){
            generateHelp();
            return;
        }
        CommandLine commandLine = generateCommandLine(args);
        Fun fun = new Fun(commandLine.getOptionValue("a"));
        fun.getStations();

        if(commandLine.hasOption("b")){
            try {
                fun.showIndex(commandLine.getOptionValues("b"));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if(commandLine.hasOption("c")){
            try{
                fun.showParam(commandLine.getOptionValues("c"));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if(commandLine.hasOption("d")){
            try{
                fun.showAvg(commandLine.getOptionValues("d"));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if(commandLine.hasOption("e")){
            try{
                fun.showFluctoations(commandLine.getOptionValues("e"));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if(commandLine.hasOption("f")){
            try {
                fun.showMinParam(commandLine.getOptionValues("f"));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        if(commandLine.hasOption("g")){
            try{
                fun.showNorms(commandLine.getOptionValues("g"));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if(commandLine.hasOption("i")){
            try{
                fun.paramMaxMinValue(commandLine.getOptionValues("i"));
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if(commandLine.hasOption("j")){
            try {
                fun.drawValues(commandLine.getOptionValues("j"));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        if(commandLine.hasOption("h")){
            generateHelp();
        }
    }
}
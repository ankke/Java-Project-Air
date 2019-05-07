import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Fun {

    private Cache cache;
    private Strategy strategy;

    public Fun(String strategy){
        if(strategy.equals("gov")) this.strategy = new Parser();
        cache = new Cache(this.strategy);
    }

    public void getStations() { cache.getStations();}

    public void showIndex(String[] args) throws UnknownStationName {
        StringBuilder arg = new StringBuilder();
        for(String a : args){
            arg.append(a + " ");
        }
        String stationName = arg.toString().trim();
        Integer stationId = Info.stationID.get(stationName);
        if(stationId==null) throw new UnknownStationName ("Nieznana nazwa satacji: " + stationName);
        strategy.parseIndex(stationId);
        System.out.println("Indeks jakości powietrza dla stacji " + stationName + " jest " + Info.stationIndex.get(stationId).toUpperCase() + ".");
    }

    public void showMinParam(String[] args) throws DateOutOfRange{
        StringBuilder arg = new StringBuilder();
        for(String a : args){
            arg.append(a + " ");
        }
        String date = arg.toString().trim();
        Double min = Double.MAX_VALUE;
        String paramName = null;
        cache.getSensors();
        for (Sensor s : Info.sensorsList){
            ParamValue val = cache.parseParamCache(s.getId());
            Values[] v = val.getValues();
            for(int i = 0; i<v.length; i++){
                if(v[i].getDate().equals(date) && v[i].getValue()<min){
                    min = v[i].getValue();
                    paramName = s.getParam().getParamName();
                    i = v.length;
                }
                //if(i==v.length-1) throw new DateOutOfRange("Data: " + date+ " prawdopodobnie spoza zakresu danych w wywołaniu " +
                 //       "showMinParam dla  " + paramName +".");
                if(min==0) break;

            }
            if(min==0) break;
        }
        System.out.println(date+ " najmniejszą wartość osiągnął parametr: " + paramName +".");

    }

    public void showParam(String[] args) throws UnknownStationName, DateOutOfRange, LackOfSensorOnStation, UnknownParameterName{
        StringBuilder arg = new StringBuilder();
        for(String a : args){
            arg.append(a + " ");
        }
        Scanner s = new Scanner(arg.toString());
        s.useDelimiter("\\|");
        String date = s.next().trim();
        String stationName = s.next().trim();
        String paramName = s.next().trim();
        check(paramName);
        if (cache.paramValuesOnStation(paramName, stationName) != null) {
            Values[] val = cache.paramValuesOnStation(paramName, stationName);
            for (int i = 0; i<val.length; i++) {
                if (val[i].getDate().equals(date)) {
                    System.out.println("Wartość parametru: " + paramName + " w stacji " + stationName + " dnia " + date + " to "
                            + val[i].getValue() + ".");
                    i=val.length;
                }
                if(i==val.length-1) throw new DateOutOfRange("Data: " + date+ " prawdopodobnie spoza zakresu danych w wywołaniu " +
                        "showParam dla danych " +stationName +": " + paramName +".");
            }
        } else throw new LackOfSensorOnStation("Nie ma sensora parametru "+paramName+ " na stacji " + stationName+ "." );
    }

    public void showAvg(String[] args) throws UnknownStationName, UnknownParameterName {
        StringBuilder arg = new StringBuilder();
        for(String a : args){
            arg.append(a + " ");
        }
        Scanner s = new Scanner(arg.toString());
        s.useDelimiter("\\|");
        String start = s.next().trim();
        String end = s.next().trim();
        String stationName = s.next().trim();
        String paramName = s.next().trim();
        check(paramName);
        double sum = 0;
        double number = 0;
        if (cache.paramValuesOnStation(paramName, stationName) != null) {
            for (Values val : cache.paramValuesOnStation(paramName, stationName)) {
                if (in(val.getDate(), start, end)) {
                    sum += val.getValue();
                    number++;
                }
            }
        }
        double avg = sum / number;

        System.out.println("Srednia wartość parametru: " + paramName + " w stacji " + stationName + " od " +
                start + " do " + end + " wynosi: " + avg + ".");
    }

    private Boolean in(String test, String start, String end) {
        LocalDateTime testtime = LocalDateTime.parse(test, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime startttime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endttime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return ((testtime.isAfter(startttime) || testtime.isEqual(startttime)) && (testtime.isBefore(endttime) || testtime.isEqual(endttime)));
    }

    private boolean after(String test, String start) {
        LocalDateTime testtime = LocalDateTime.parse(test, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime startttime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return ((testtime.isAfter(startttime) || testtime.isEqual(startttime)));
    }
    private boolean equals(String test, LocalDateTime time){
        LocalDateTime testtime = LocalDateTime.parse(test, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return (testtime.equals(time));
    }

    public void showFluctoations(String[] args) throws UnknownStationName{
        StringBuilder arg = new StringBuilder();
        for(String a : args){
            arg.append(a + " ");
        }
        Scanner s = new Scanner(arg.toString());
        s.useDelimiter("\\|");
        String start = s.next().trim();
        Double min;
        double max;
        double fluct;
        String param = null;
        StringBuilder result = new StringBuilder("| ");
        String station;
        while(s.hasNext()) {
            station = s.next().trim();
            fluct = 0;
            for (Sensor sensor : cache.sensorsOnStation(station)) {
                min = Double.MAX_VALUE;
                max = 0;
                for (Values val : cache.parseParamCache(sensor.getId()).getValues()) {
                    if (after(val.getDate(), start)&& val.getValue()>0) { //&& val.getValue()>0
                        if (val.getValue() > max) max = val.getValue();
                        if (val.getValue() < min) min = val.getValue();
                    }
                }

                if (max - min > fluct) {
                    fluct = max - min;

                    param = sensor.getParam().getParamName();

                }
            }
            result.append(station + " : " + param + " : " + fluct + " | ");

        }
        Scanner s1 = new Scanner(result.toString());
        s1.useDelimiter("\\|");
        System.out.println("Maksymalne wahania: ");
        while(s1.hasNext()){
            System.out.println(s1.next());
        }
    }

    public void showNorms(String[] args) throws UnknownStationName, DateOutOfRange{
        StringBuilder arg = new StringBuilder();
        for(String a : args){
            arg.append(a + " ");
        }
        Scanner s1 = new Scanner(arg.toString());
        s1.useDelimiter("\\|");
        String date = s1.next().trim();
        String stationName = s1.next().trim();
        Integer n = Integer.parseInt(s1.next().trim());
        Integer stationId = Info.stationID.get(stationName);
        if(stationId==null) throw new UnknownStationName("Nieznana nazwa stacji: " + stationName);
        class NormDiff {
            double diff;
            String paramName;

            NormDiff(String paramName) {
                this.paramName = paramName;
            }
        }

        List<NormDiff> result = new ArrayList<>();
        for (Sensor s : cache.sensorsOnStation(stationName)) {
            for (Values val : cache.parseParamCache(s.getId()).getValues()) {
                NormDiff nd = new NormDiff(s.getParam().getParamName());
                if (val.getDate().equals(date)) {
                    nd.diff = countDiff(s, val.getValue());
                    result.add(nd);
                }
            }
        }
        Collections.sort(result, new Comparator<NormDiff>() {
            @Override
            public int compare(NormDiff o1, NormDiff o2) {
                return (int) (o2.diff - o1.diff);
            }
        });
        if(result.isEmpty()) throw new DateOutOfRange("Data " + date + " prawdopodobnie spoza zakresu danych.");
        System.out.println("Stacja pomiarowa: " + stationName + ".");
        for (int it = 0; it < n; it++) {
            System.out.println("     Norma " + result.get(it).paramName + " przekroczona o: "  + result.get(it).diff + ".");
        }

    }

    private double countDiff(Sensor s, double val) {
        switch (s.getParam().getParamName()) {
            case "dwutlenek azotu": {
                if (val > Norms.no2) return val - Norms.no2;
                else break;
            }
            case "tlenek węgla": {
                if (val > Norms.co) return val - Norms.co;
                else break;
            }
            case "dwutlenek siarki": {
                if (val > Norms.so2) return val - Norms.so2;
                else break;
            }
            case "pył zawieszony PM10": {
                if (val > Norms.pm10) return val - Norms.pm10;
                else break;
            }
            case "pył zawieszony PM2.5": {
                if (val > Norms.pm25) return val - Norms.pm25;
                else break;
            }
            case "ołów": {
                if (val > Norms.pb) return val - Norms.pb;
                else break;
            }
            default:
                return (double) 0;
        }
        return 0;
    }

    public void paramMaxMinValue(String[] args) throws UnknownParameterName{
        StringBuilder arg = new StringBuilder();
        for(String a : args){
            arg.append(a + " ");
        }
        String paramName = arg.toString().trim();
        check(paramName);
        double maxValue = 0;
        Double minValue = Double.MAX_VALUE;
        String datemax = null;
        int stationIdmax = -1;
        cache.getSensors();
        String datemin = null;
        int stationIdmin = -1;
        for(Sensor s : Info.sensorsList){
            if(s.getParam().getParamName().equals(paramName)){
                for (Values val : cache.parseParamCache(s.getId()).getValues()){
                    if(val.getValue()>maxValue) {
                        maxValue = val.getValue();
                        datemax = val.getDate();
                        stationIdmax = s.getStationId();
                    }
                    if(val.getValue()<minValue) {
                        minValue = val.getValue();
                        datemin = val.getDate();
                        stationIdmin = s.getStationId();
                    }
                }
            }
        }
        String stationNamemax = null;
        String stationNamemin = null;
        for(Station s : Info.stationsList){
            if(s.getId()==stationIdmax) stationNamemax = s.getStationName();
            if(s.getId()==stationIdmin) stationNamemin = s.getStationName();
        }
        System.out.println("Maksymalna wartość parametru: " + paramName + " została osiągnięta w stacji " + stationNamemax + " " + datemax + " o wartości " + maxValue + ".\n" +
                "Minimalna wartość parametru " + paramName + " została osiągnieta w stacji " + stationNamemin + " " + datemin + " o wartości " + minValue + ".");
    }

    public void drawValues(String[] args) throws UnknownStationName, UnknownParameterName{
        StringBuilder arg = new StringBuilder();
        for(String a : args){
            arg.append(a + " ");
        }
        Scanner s1 = new Scanner(arg.toString());
        s1.useDelimiter("\\|");
        String start = s1.next().trim();
        LocalDateTime start1 = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = s1.next().trim();
        String paramName = s1.next().trim();
        check(paramName);
        double maxValue = 0;
        String stationName;
        String[] stations = new String[20];
        int i= 0;
        while(s1.hasNext()){
            stationName = s1.next().trim();
            stations[i] = stationName;
            i++;
            Integer stationId = Info.stationID.get(stationName);
            if(stationId==null) throw new UnknownStationName("Nieznana nazwa stacji w drawValues: " +stationName);
            cache.parseSensorCache(stationId);
        }
        for (String name : stations) {
            if(name !=null) {
                for (Values val : cache.paramValuesOnStation(paramName, name)) {
                    if(in(val.getDate(), start, end)){
                        if(val.getValue()>maxValue  ) {
                        maxValue = val.getValue();
                        }
                    }
                }
            }
        }
        while(!equals(end, start1)) {
            for (String name : stations) {
                if(name !=null) {
                    for (Values val : cache.paramValuesOnStation(paramName, name)) {
                        if (in(val.getDate(), start, end) && equals(val.getDate(), start1)) {
                            StringBuilder result = new StringBuilder();
                            for (int i1 = (int) (val.getValue() / maxValue * 10); i1 >= 0; i1--) result.append("∎");
                            System.out.printf("%10s %-20s %-25s \n", val.getDate(), "(" + name + ")", result.toString() + " " + val.getValue());
                        }
                    }
                }
            }
            start1 = start1.plusHours(1);
        }


    }

    private boolean check(String paramName) throws UnknownParameterName{
        String[] names = new String[]{"pył zawieszony PM2.5", "pył zawieszony PM10", "benzen", "dwutlenek azotu",
                "tlenek węgla", "dwutlenek siarki", "ołów" };
        for(String name : names){
            if(name.equals(paramName)) return true;
        }
        throw new UnknownParameterName("Nieznana nazwa parametru " + paramName);
    }


}


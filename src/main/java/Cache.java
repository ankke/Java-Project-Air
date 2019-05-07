import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class Cache {

    private Strategy parser;

    public Cache(Strategy parser){
        this.parser = parser;
    }

    public ParamValue parseParamCache(int sensorId) {
        parser.parseParam(sensorId);
        if (Info.sensorCache.get(sensorId) == null) parser.parseParamfromURL(sensorId);
        else if (Info.sensorCache.get(sensorId).getValues().length>0 && iftooold(Info.sensorCache.get(sensorId).getValues()[0].getDate())) parser.parseParamfromURL(sensorId);
        return Info.sensorCache.get(sensorId);
    }

    public Values[] paramValuesOnStation(String paramName, String stationName) throws UnknownStationName{
        if(Info.stationID.get(stationName)==null) throw new UnknownStationName("Nieznana nazwa stacji: " + stationName);
        for(Sensor s : sensorsOnStation(stationName)){
            if(s.getParam().getParamName().trim().equals(paramName)){
                parseParamCache(s.getId());
                return  Info.sensorCache.get(s.getId()).getValues();
            }
        }
        return null;
    }

    public List<Sensor> sensorsOnStation(String stationName) throws UnknownStationName{
        List<Sensor> list = new LinkedList<>();
        Integer stationId = Info.stationID.get(stationName);
        if(stationId==null) throw new UnknownStationName("Nieznana nazwa stacji: " + stationName);
        parseSensorCache(stationId);
        for(Sensor s : Info.sensorsList) {
            if (s.getStationId() == stationId) {
                list.add(s);
            }
        }
        return list;
    }

    public void parseSensorCache (int stationId){
        if(Info.ifstationparsed.get(stationId)==null || !(Info.ifstationparsed.get(stationId))) {
           parser.parseSensor(stationId);
        }
    }

    public  void getSensors(){
        for(Station s : Info.stationsList){
            parseSensorCache(s.getId());
        }
    }

    public boolean iftooold(String date) {
        LocalDateTime test = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime test1 = test.withMinute(0);
        LocalDateTime time1 = time.withMinute(0).withNano(0).withSecond(0);
        if(time1.isEqual(test1)) return false;
        if(time1.isAfter(test1)) return true;
        return true;
    }

    public void getStations(){
        parser.parseStation();
        if(Info.stationID.isEmpty()){
            parser.parseStationfromURL();
        }

    }


}

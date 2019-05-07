
import java.util.*;

public abstract class Info {

    public static Map<String, Integer> stationID = new HashMap<>();
    public static Map<Integer, String> sensorID = new HashMap<>(); //klucz to SensorID, wartość to rodzaj param
    public static Map<Integer, String> stationIndex = new HashMap<>();
    public static Map<Integer, Boolean> ifstationparsed = new HashMap<>(); //klucz to id stacji, wartość to true/false
    // czy zostały sparsowane jej sensory do listy sensorsList
    public static Map<Integer, ParamValue> sensorCache = new HashMap<>(); // klucz to sensorid
    public static List<Station> stationsList = new LinkedList<>();
    public static List<Sensor> sensorsList = new LinkedList<>();


}

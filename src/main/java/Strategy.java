
public interface Strategy {

    public void parseStation();
    public void parseParam (Integer sensorId);
    public void parseIndex (Integer stationId);
    public void parseSensor (Integer StationId);
    public void parseParamfromURL(Integer sensorId);
    public void parseSensorfromURL(Integer StationId);
    public void parseStationfromURL();

}

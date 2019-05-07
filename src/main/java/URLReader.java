import javax.sound.midi.Soundbank;
import java.net.*;
import java.io.*;
import java.nio.file.*;
public class URLReader {

    public static String readStations() throws Exception {
        URL stations = new URL("http://api.gios.gov.pl/pjp-api/rest/station/findAll");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(stations.openStream()));
        StringBuilder s = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            s.append(inputLine);
        in.close();
        Path file = Paths.get("C:\\Users\\Aneczka\\Desktop\\studia\\INFA\\obiektowe\\powietrze\\findAll.txt");
        Files.deleteIfExists(file);
        Files.createFile(file);
        byte [] url = s.toString().getBytes();
        Files.write(file, url);
        //System.out.println("read stations");
        return s.toString();
    }

    public static String readSensors(String stationId) throws Exception {

        URL sensors = new URL("http://api.gios.gov.pl/pjp-api/rest/station/sensors/" + stationId);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(sensors.openStream()));
        StringBuilder s = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            s.append(inputLine);
        in.close();
        Path file = Paths.get("C:\\Users\\Aneczka\\Desktop\\studia\\INFA\\obiektowe\\powietrze\\stations\\"+stationId+".txt");
        Files.deleteIfExists(file);
        Files.createFile(file);
        byte [] url = s.toString().getBytes();
        Files.write(file, url);
        //System.out.println("read Sensors");
        return s.toString();
    }

    public static String readParam(String sensorId) throws Exception {

        URL param = new URL("http://api.gios.gov.pl/pjp-api/rest/data/getData/" + sensorId);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(param.openStream()));
        StringBuilder s = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            s.append(inputLine);
        in.close();
        Path file = Paths.get("C:\\Users\\Aneczka\\Desktop\\studia\\INFA\\obiektowe\\powietrze\\sensors\\"+sensorId+".txt");
        Files.deleteIfExists(file);
        Files.createFile(file);
        byte [] url = s.toString().getBytes();
        Files.write(file, url);
        //System.out.println("read Parameters");
        return s.toString();
    }

    public static String readIndex(String stationId) throws Exception {

        URL index = new URL("http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/" + stationId);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(index.openStream()));
        StringBuilder s = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            s.append(inputLine);
        in.close();
        //System.out.println("read index");

        return s.toString();
    }





}
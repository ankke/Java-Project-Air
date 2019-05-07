import com.google.gson.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Parser implements Strategy {


    public void parseStation() {
        try {
            Path path = Paths.get("C:\\Users\\Aneczka\\Desktop\\studia\\INFA\\obiektowe\\powietrze\\findAll.txt");
            if (Files.exists(path)) {
                String data;
                data = new String(Files.readAllBytes(path));
                Station[] stations = new Gson().fromJson(data, Station[].class);
                Info.stationsList = Arrays.asList(stations);
                for (Station s : Info.stationsList) {
                    Info.stationID.put(s.getStationName(), s.getId());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "nie można sparsować stacji.");

        }
    }

    public void parseStationfromURL() {
        try {
            String input = URLReader.readStations();
            Station[] stations = new Gson().fromJson(input, Station[].class);
            Info.stationsList = Arrays.asList(stations);
            for (Station s : Info.stationsList) {
                Info.stationID.put(s.getStationName(), s.getId());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + "nie można sparsować stacji.");

        }
    }


    public void parseParam(Integer sensorId) {
        try {
            Path path = Paths.get("C:\\Users\\Aneczka\\Desktop\\studia\\INFA\\obiektowe\\powietrze\\sensors\\" + sensorId + ".txt");
            if (Files.exists(path)) {
                String data;
                data = new String(Files.readAllBytes(path));
                ParamValue pv = new Gson().fromJson(data, ParamValue.class);
                Info.sensorCache.put(sensorId, pv);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + " problem z parsowanie parametrow sensora: " + sensorId);

        }

    }

    public void parseParamfromURL(Integer sensorId) {
        try {
            String input = URLReader.readParam(sensorId.toString());
            ParamValue pv = new Gson().fromJson(input, ParamValue.class);
            Info.sensorCache.put(sensorId, pv);

        } catch (Exception e) {
            System.out.println(e.getMessage() + " problem z parsowanie parametrow sensora: " + sensorId);

        }
    }

    public void parseIndex(Integer stationId) {
        try {
            String input = URLReader.readIndex(stationId.toString());
            Index index = new Gson().fromJson(input, Index.class);
            Info.stationIndex.put(stationId, index.getStIndexLevel().getIndexLevelName());

        } catch (Exception e) {
            System.out.println(e.getMessage() + " problem z parsowaniem indexu stacji: " + stationId);

        }
    }

    public void parseSensorfromURL (Integer StationId) {
        try {
            String input = URLReader.readSensors(StationId.toString());
            Sensor[] sensors = new Gson().fromJson(input, Sensor[].class);
            Info.sensorsList.addAll(Arrays.asList(sensors));
            for (Sensor s : Info.sensorsList) {
                Info.sensorID.put(s.getId(), s.getParam().getParamName());
            }
            Info.ifstationparsed.put(StationId, true);

        } catch (Exception e) {
            System.out.println(e.getMessage() + " problem z parsowaniem sensora: " + StationId);
        }

    }

    public void parseSensor (Integer StationId) {
        try {
            Path path = Paths.get("C:\\Users\\Aneczka\\Desktop\\studia\\INFA\\obiektowe\\powietrze\\stations\\" + StationId + ".txt");
            if (Files.exists(path)) {
                String data;
                data = new String(Files.readAllBytes(path));
                Sensor[] sensors = new Gson().fromJson(data, Sensor[].class);
                Info.sensorsList.addAll(Arrays.asList(sensors));
                for (Sensor s : Info.sensorsList) {
                    Info.sensorID.put(s.getId(), s.getParam().getParamName());
                }
                Info.ifstationparsed.put(StationId, true);
            }
            else parseSensorfromURL(StationId);

        } catch (Exception e) {
            System.out.println(e.getMessage() + " problem z parsowaniem sensora: " + StationId);
        }
    }
}
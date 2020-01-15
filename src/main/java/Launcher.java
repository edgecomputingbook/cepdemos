


public class Launcher {

    public static void main(String[] args) {

        try {


            CEPEngine cepEngine = new CEPEngine();


            /*
            String inputStreamName = "UserStream";
            String inputStreamAttributesString = "source string, urn string, metric string, ts long, value double";

            String outputStreamName = "BarStream";
            String outputStreamAttributesString = "source string, avgValue double";

            String queryString = " " +
                    "from UserStream#window.timeBatch(5 sec) " +
                    "select source, avg(value) as avgValue " +
                    "  group by source " +
                    "insert into BarStream; ";

            */
            /*
            String inputStreamName = "StockStream";
            String inputStreamAttributesString = "source string, value double";

            String outputStreamName = "AlertStream";
            String outputStreamAttributesString = "source string, value double";

            String queryString = " " +
                    "from StockStream[value > 100]" +
                    "insert into AlertStream; ";
            */


            String inputStreamName = "SensorStream";
            String inputStreamAttributesString = "sensor_type string, sensor_id string, timestamp long, value double, metric string";

            String outputStreamName = "AVGStream";
            String outputStreamAttributesString = "sensor_id string, avg_value double";

            String queryString = " " +
                    "from SensorStream#window.timeBatch(15 sec) " +
                    "select sensor_id, avg(value) as avg_value " +
                    "  group by sensor_id " +
                    "insert into AVGStream; ";

            cepEngine.createCEP(inputStreamName, outputStreamName, inputStreamAttributesString , outputStreamAttributesString, queryString);

            //String inputEvent = cepEngine.getStringPayload();
            //cepEngine.input(inputStreamName, inputEvent);

            while (true) {
                String inputEvent = cepEngine.getStringPayloadSensor();
                System.out.println("INPUT EVENT: " + inputEvent);
                cepEngine.input(inputStreamName, inputEvent);
                Thread.sleep(1000);
            }

            //cepEngine.shutdown();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}

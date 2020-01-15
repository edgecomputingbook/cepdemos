


public class Launcher {

    public static void main(String[] args) {

        try {

            if (args.length != 1) {
                System.out.println("Demos:");
                System.out.println("\tstocks");
                System.out.println("\tsensors");
                System.out.println("\n");
                System.out.println("Enter the type of demo:");
                System.out.println("\t java -jar cepdemos.jar [name of demo]");

            } else {


                switch (args[0].toLowerCase()) {
                    case "stocks":
                        System.out.println("Stocks Demo:");
                        stocksDemo();
                        break;
                    case "sensors":
                        System.out.println("Sensors Demo:");
                        sensorsDemo();
                        break;
                    default:
                        System.out.println("no demo selected");

                }

            }

            } catch(Exception ex){
                ex.printStackTrace();
            }
    }

    public static void stocksDemo() {

        try {

            CEPEngine cepEngine = new CEPEngine();


            String inputStreamName = "StockStream";
            String inputStreamAttributesString = "source string, value double";

            String outputStreamName = "AlertStream";
            String outputStreamAttributesString = "source string, value double";

            String queryString = " " +
                    "from StockStream[value > 100]" +
                    "insert into AlertStream; ";

            cepEngine.createCEP(inputStreamName, outputStreamName, inputStreamAttributesString, outputStreamAttributesString, queryString);

            while (true) {
                String inputEvent = cepEngine.getStringPayloadStock("MYCO");
                String inputEvent2 = cepEngine.getStringPayloadStock("YOURCO");
                System.out.println("INPUT EVENT: " + inputEvent);
                cepEngine.input(inputStreamName, inputEvent);
                cepEngine.input(inputStreamName, inputEvent2);
                Thread.sleep(1000);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void sensorsDemo() {

        try {

            CEPEngine cepEngine = new CEPEngine();

            String inputStreamName = "SensorStream";
            String inputStreamAttributesString = "sensor_type string, sensor_id string, timestamp long, value double, metric string";

            String outputStreamName = "AVGStream";
            String outputStreamAttributesString = "sensor_id string, avg_value double";

            String queryString = " " +
                    "from SensorStream#window.timeBatch(15 sec) " +
                    "select sensor_id, avg(value) as avg_value " +
                    "  group by sensor_id " +
                    "insert into AVGStream; ";

            cepEngine.createCEP(inputStreamName, outputStreamName, inputStreamAttributesString, outputStreamAttributesString, queryString);

            while (true) {
                String inputEvent = cepEngine.getStringPayloadSensor();
                System.out.println("INPUT EVENT: " + inputEvent);
                cepEngine.input(inputStreamName, inputEvent);
                Thread.sleep(1000);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}

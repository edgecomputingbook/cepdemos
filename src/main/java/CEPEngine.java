import com.google.gson.Gson;
import io.siddhi.core.SiddhiAppRuntime;
import io.siddhi.core.SiddhiManager;
import io.siddhi.core.util.transport.InMemoryBroker;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CEPEngine {

    private SiddhiManager siddhiManager;
    private SiddhiAppRuntime siddhiAppRuntime;
    private Map<String,String> topicMap;


    private Gson gson;

    public CEPEngine() {

        topicMap = new ConcurrentHashMap<>();

        // Creating Siddhi Manager
        siddhiManager = new SiddhiManager();
        gson = new Gson();
    }

    public void shutdown() {
        try {

            if(siddhiAppRuntime != null) {
                siddhiAppRuntime.shutdown();
            }

            if(siddhiManager != null) {
                siddhiManager.shutdown();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createCEP(String inputStreamName, String outputStreamName, String inputStreamAttributesString, String outputStreamAttributesString,String queryString) {

        try {

            String inputTopic = UUID.randomUUID().toString();
            String outputTopic = UUID.randomUUID().toString();

            topicMap.put(inputStreamName,inputTopic);
            topicMap.put(outputStreamName,outputTopic);

            String sourceString = getSourceString(inputStreamAttributesString, inputTopic, inputStreamName);
            String sinkString = getSinkString(outputTopic,outputStreamName,outputStreamAttributesString);

            //Generating runtime
            siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(sourceString + " " + sinkString + " " + queryString);

            InMemoryBroker.Subscriber subscriberTest = new OutputSubscriber(outputTopic,outputStreamName);

            //subscribe to "inMemory" broker per topic
            InMemoryBroker.subscribe(subscriberTest);

            //Starting event processing
            siddhiAppRuntime.start();

            } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void input(String streamName, String jsonPayload) {
        try {

            if (topicMap.containsKey(streamName)) {
                //InMemoryBroker.publish(topicMap.get(streamName), getByteGenericDataRecordFromString(schemaMap.get(streamName),jsonPayload));
                InMemoryBroker.publish(topicMap.get(streamName), jsonPayload);

            } else {
                System.out.println("input error : no schema");
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void test() {


        try {

            String inputStreamName = "UserStream";
            String inputTopic = "user";

            String outputStreamName = "BarStream";
            String outputTopic = "user2";
            String outputSchemaString = "source string, avgValue double";

            String queryString = " " +
                    //from TempStream#window.timeBatch(10 min)
                    //"from UserStream#window.time(5 sec) " +
                    "from UserStream#window.timeBatch(5 sec) " +
                    "select source, avg(value) as avgValue " +
                    "  group by source " +
                    "insert into BarStream; ";

            String sourceString = getSourceString("blah", inputTopic, inputStreamName);
            String sinkString = getSinkString(outputTopic,outputStreamName,outputSchemaString);



            //create during init;
            // Creating Siddhi Manager
            //siddhiManager = new SiddhiManager();

            //Generating runtime
            siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(sourceString + " " + sinkString + " " + queryString);


            InMemoryBroker.Subscriber subscriberTest = new OutputSubscriber(outputTopic,outputStreamName);

            //subscribe to "inMemory" broker per topic
            InMemoryBroker.subscribe(subscriberTest);


            //Starting event processing
            siddhiAppRuntime.start();

            while(true) {
            InMemoryBroker.publish("user", "blah");
            //Thread.sleep(1000);
            }

            //Shutting down the runtime
            //siddhiAppRuntime.shutdown();

            //Shutting down Siddhi
            //siddhiManager.shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String getStringPayload() {

        String rec = null;

        try{

            String source = "mysource";
            String urn = "myurn";
            String metric = "mymetric";
            long ts = System.currentTimeMillis();

            Random r = new Random();
            double value = r.nextDouble();

            Ticker tick = new Ticker(source, urn, metric, ts, value);

            rec = gson.toJson(tick);

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return rec;
    }

    public String getStringPayloadSensor() {

        String rec = null;

        try{

            String source = "pressure";
            String urn = "ABCD-1234";
            String metric = "psi";
            long ts = System.currentTimeMillis();

            Random r = new Random();
            double value = 1000 + (r.nextDouble()) *200;

            Sensor tick = new Sensor(source, urn, metric, ts, value);

            rec = gson.toJson(tick);

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return rec;
    }


    public String getStringPayloadStock(String source) {

        String rec = null;

        try{

            //String source = "MYCO";

            Random r = new Random();
            double value = 99.5 + r.nextDouble();

            StockTicker tick = new StockTicker(source, value);

            rec = gson.toJson(tick);

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return rec;
    }

    private String getSourceString(String inputStreamAttributesString, String topic, String streamName) {
        String sourceString = null;
        try {

            sourceString  = "@source(type='inMemory', topic='" + topic + "', @map(type='json')) " +
                    "define stream " + streamName + " (" + inputStreamAttributesString + "); ";

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sourceString;
    }

    private String getSinkString(String topic, String streamName, String outputSchemaString) {
        String sinkString = null;
        try {

            sinkString = "@sink(type='inMemory', topic='" + topic + "', @map(type='json')) " +
                    "define stream " + streamName + " (" + outputSchemaString + "); ";

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sinkString;
    }

}

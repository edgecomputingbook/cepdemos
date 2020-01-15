public class Sensor {
    String sensor_type;
    String sensor_id;
    String metric;
    long timestamp;
    double value;

    public Sensor(String sensor_type, String sensor_id, String metric, long timestamp, double value) {
        this.sensor_type = sensor_type;
        this.sensor_id = sensor_id;
        this.metric = metric;
        this.timestamp = timestamp;
        this.value = value;
    }
    public String getSource() {return sensor_type;}
    public String getUrn() {return sensor_id;}
    public String getMetric() {return metric;}
    public long getTs() {return timestamp;}
    public double getValue() {return value;}

    @Override
    public String toString() {
        return "sensor_type:" + sensor_type + " sensor_id:" + sensor_id + " metric:" + metric + " timestamp:" + timestamp + " value:" + value;
    }
}

/*
{
        "sensor_type":  "pressure",								#A
        "sensor_id": "ABCD-1234",								#B
        "timestamp": "1576600245000",						 #C
        "value": "1500.0",									#D
        "metric": "psi"										#E
        }
*/
package com.example.fragmento.biolabcloud;
public class PointValue {

    float Bmp;
    float Humidity;
    float Temp;
    long time;

    public PointValue() {
    }

    public PointValue(float humid,float tempC,float tempF, long time) {
        this.Humidity = tempC;
        this.Temp = tempF;
        this.Bmp = humid;
        this.time = time;
    }

    public float getTempC() {
        return Humidity;
    }

    public float getTempF() {
        return Temp;
    }

    public float gethumid() {
        return Bmp;
    }

    public long gettime() {
        return time;
    }
}

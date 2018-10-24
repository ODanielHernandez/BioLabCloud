package com.example.fragmento.biolabcloud;
public class PointValue {

    float Bmp;
    float Humidity;
    float Temp;
    long time;

    public PointValue() {
    }

    public PointValue(float Humidity,float Temp,float Bmp, long time) {
        this.Humidity = Humidity;
        this.Temp = Temp;
        this.Bmp = Bmp;
        this.time = time;
    }

    public float getTemp() {
        return Temp;
    }

    public float getBmp() {
        return Bmp;
    }

    public float getHumidity() {
        return Humidity;
    }

    public long gettime() {
        return time;
    }
}

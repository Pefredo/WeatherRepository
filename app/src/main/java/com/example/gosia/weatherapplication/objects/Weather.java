package com.example.gosia.weatherapplication.objects;

public class Weather {

    private String country;
    private String city;
    private String temperature;
    private String temperatureMin;
    private String temperatureMax;
    private String humidity;
    private String pressure;
    private Double windSpeed;

    public Weather(String c, String city, String t, String tMin, String tMax, String h, String p, Double wS){
        this.country = c;
        this.city = city;
        this.temperature = t;
        this.temperatureMin= tMin;
        this.temperatureMax = tMax;
        this.humidity= h;
        this.pressure = p;
        this.windSpeed = wS;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(String temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public String getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(String temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String toString(){
        String weathInfo = "temperature: "  + this.temperature+ "\n" +
                "temperature min: "	+ this.temperatureMin + "\n" +
                "temperature max: " + this.temperatureMax + "\n" +
                "humidity: " + this.humidity + "\n" +
                "pressure: " + this.pressure + "\n" +
                "wind speed: " + this.windSpeed + "\n";
        return weathInfo;
    }
}

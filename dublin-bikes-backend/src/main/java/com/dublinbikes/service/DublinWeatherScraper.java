package com.dublinbikes.service;

import com.dublinbikes.model.Weather;
import com.dublinbikes.model.WeatherId;
import com.dublinbikes.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@Service
public class DublinWeatherScraper {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final WeatherRepository weatherRepo;
    private final RestTemplate restTemplate = new RestTemplate();

    public DublinWeatherScraper(WeatherRepository weatherRepo) {
        this.weatherRepo = weatherRepo;
    }

    public void fetchAndStoreData() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Dublin,ie&appid=" + apiKey;

        try {
            // Fetch weather data from OpenWeatherMap API
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> weatherData = response.getBody();

            if (weatherData == null) {
                System.out.println("No data returned from OpenWeatherMap API.");
                return;
            }

            // Extract latitude and longitude
            @SuppressWarnings("unchecked")
            Map<String, Object> coordData = (Map<String, Object>) weatherData.get("coord");
            double lon = (double) coordData.get("lon");
            double lat = (double) coordData.get("lat");

            // Extract weather details (e.g., main weather type, description)
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) weatherData.get("weather");
            Map<String, Object> weatherInfo = weatherList.get(0); // Assuming weather array has one element
            short weatherId = Short.parseShort(weatherInfo.get("id").toString());
            String weatherMain = (String) weatherInfo.get("main");
            String weatherDesc = (String) weatherInfo.get("description");

            // Extract main data (temperature, pressure, humidity)
            @SuppressWarnings("unchecked")
            Map<String, Object> mainData = (Map<String, Object>) weatherData.get("main");
            float temp = Float.parseFloat(mainData.get("temp").toString());
            float feelsLike = Float.parseFloat(mainData.get("feels_like").toString());
            float tempMin = Float.parseFloat(mainData.get("temp_min").toString());
            float tempMax = Float.parseFloat(mainData.get("temp_max").toString());
            short pressure = Short.parseShort(mainData.get("pressure").toString());
            short humidity = Short.parseShort(mainData.get("humidity").toString());

            // Extract wind data
            @SuppressWarnings("unchecked")
            Map<String, Object> windData = (Map<String, Object>) weatherData.get("wind");
            float windSpeed = Float.parseFloat(windData.get("speed").toString());
            short windDeg = Short.parseShort(windData.get("deg").toString());

            // Extract cloud data
            @SuppressWarnings("unchecked")
            Map<String, Object> cloudData = (Map<String, Object>) weatherData.get("clouds");
            short clouds = Short.parseShort(cloudData.get("all").toString());

            // Extract timestamp (dt), sys information (sunrise, sunset), and timezone
            long dt = Long.parseLong(weatherData.get("dt").toString());
            @SuppressWarnings("unchecked")
            Map<String, Object> sysData = (Map<String, Object>) weatherData.get("sys");
            short sysType = Short.parseShort(sysData.get("type").toString());
            int sysId = Integer.parseInt(sysData.get("id").toString());
            String sysCountry = (String) sysData.get("country");
            int sysSunrise = Integer.parseInt(sysData.get("sunrise").toString());
            int sysSunset = Integer.parseInt(sysData.get("sunset").toString());
            int timezone = Integer.parseInt(weatherData.get("timezone").toString());
            String cod = weatherData.get("cod").toString();

            // Create WeatherId (composite key)
            WeatherId weatherIdObj = new WeatherId("Dublin", LocalDateTime.now());

            // Create Weather entity and map data
            Weather weather = new Weather();
            weather.setId(weatherIdObj);
            weather.setLon(lon);
            weather.setLat(lat);
            weather.setWeatherId(weatherId);
            weather.setWeatherMain(weatherMain);
            weather.setWeatherDesc(weatherDesc);
            weather.setMainTemp(temp);
            weather.setFeelsLike(feelsLike);
            weather.setTempMin(tempMin);
            weather.setTempMax(tempMax);
            weather.setPressure(pressure);
            weather.setHumidity(humidity);
            weather.setWindSpeed(windSpeed);
            weather.setWindDeg(windDeg);
            weather.setClouds(clouds);
            weather.setDt(dt);
            weather.setSysType(sysType);
            weather.setSysId(sysId);
            weather.setSysCountry(sysCountry);
            weather.setSysSunrise(sysSunrise);
            weather.setSysSunset(sysSunset);
            weather.setTimezone(timezone);
            weather.setCod(cod);

            // Save the Weather entity to the database
            weatherRepo.save(weather);

            System.out.println("✅ Successfully fetched and saved weather data.");

        } catch (Exception e) {
            System.err.println("❌ Error fetching weather data: " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void runScheduledWeatherScraper() {
        System.out.println("🔁 Running scheduled weather scraper...");
        fetchAndStoreData();
    }
}
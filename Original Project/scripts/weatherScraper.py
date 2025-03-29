from sqlalchemy import create_engine
import simplejson as json
import requests
from datetime import datetime
import traceback

"""
This script scrapes live weather data from the OpenWeather API and stores it in the MySQL database.

Security Notice:
- Removed hardcoded API keys and database credentials.
- If using this script in production, store sensitive credentials in a secure location (e.g., environment variables).
"""

# Configuration Variables (Replace with actual values or use environment variables)
DB_CONNECTION_STRING = "mysql+mysqldb://USERNAME:PASSWORD@HOST:PORT/DB_NAME"
APIKEY = "..."
ENDPOINT = "https://api.openweathermap.org/data/2.5/weather?q=Dublin,ie&appid=" + APIKEY

# Create database connection
engine = create_engine(DB_CONNECTION_STRING, echo=True)

def store(weather):
    """
    Reads in dynamic weather data and stores it in the database.
    This script is designed to run continuously with crontab.
    """
    weather = json.loads(weather)

    engine.execute("USE dublinBikes;")
    
    lon = weather.get("coord", {}).get("lon")
    lat = weather.get("coord", {}).get("lat")
    weather_id = weather.get("weather", [{}])[0].get("id")
    weather_main = weather.get("weather", [{}])[0].get("main")
    weather_desc = weather.get("weather", [{}])[0].get("description")
    temp = weather.get("main", {}).get("temp")
    feels_like = weather.get("main", {}).get("feels_like")
    temp_min = weather.get("main", {}).get("temp_min")
    temp_max = weather.get("main", {}).get("temp_max")
    pressure = weather.get("main", {}).get("pressure")
    humidity = weather.get("main", {}).get("humidity")
    visibility = weather.get("visibility")
    wind_speed = weather.get("wind", {}).get("speed")
    wind_deg = weather.get("wind", {}).get("deg")
    clouds_all = weather.get("clouds", {}).get("all")
    dt = weather.get("dt")
    sys_type = weather.get("sys", {}).get("type")
    sys_id = weather.get("sys", {}).get("id")
    sys_country = weather.get("sys", {}).get("country")
    sys_sunrise = weather.get("sys", {}).get("sunrise")
    sys_sunset = weather.get("sys", {}).get("sunset")
    timezone = weather.get("timezone")
    name = weather.get("name")
    cod = weather.get("cod")
    now = datetime.now()

    vals = (lon, lat, weather_id, weather_main, weather_desc, temp, feels_like, temp_min, temp_max,
            pressure, humidity, visibility, wind_speed, wind_deg, clouds_all, dt, sys_type, sys_id, sys_country, sys_sunrise, sys_sunset,
            timezone, name, cod, now)

    engine.execute("INSERT INTO weather_scraper VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", vals)

def main():
    """
    Scrapes live weather data.
    Designed to be scheduled using crontab or a similar task scheduler.
    """
    try:
        response = requests.get(ENDPOINT)
        response.raise_for_status()  # Raise an error if the request fails
        store(response.text)
        print("Weather data stored successfully.")
    except requests.exceptions.RequestException:
        print("Error fetching data from OpenWeather API:", traceback.format_exc())

if __name__ == "__main__":
    main()
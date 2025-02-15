from flask import Flask, render_template, jsonify
import json
from sqlalchemy import create_engine 
import config
import datetime
from datetime import timedelta

engine = create_engine(config.keys['engine'], echo=True)

# Gets Google Maps API key from config file
google_key = config.keys['google_maps_api_key']

# Gets OpenWeather API key from config file
weather_key = config.keys['weather_api_key']

bikes_key = config.keys['bikes_api_key']

sql = """
USE dublinBikes;
"""

sql1 = """
SELECT Station_ID, Available_bike_stands, Available_bikes, Status 
FROM availability 
WHERE (Station_ID, Scraper_input_dateTime) IN 
    (SELECT Station_ID, MAX(Scraper_input_dateTime) 
     FROM availability 
     GROUP BY Station_ID);
"""

sql2 = """
SELECT lon, lat, weather_id, weather_main, weather_desc, main_temp, feels_like, temp_min, temp_max, pressure, humidity, visibility, wind_speed, wind_deg, clouds, dt, sys_type, sys_id, sys_country, sys_sunrise, sys_sunset, timezone, station_name, cod
FROM weather_scraper
order by scraper_input_dateTime desc
LIMIT 1; 
"""

# fetches all station availabiltiy info
def getAvailability():
    try:
        engine.execute(sql)
    except Exception as e:
        print(e)

    try:
        result = engine.execute(sql1)
        rows = result.fetchall()
        result = []

        # required to make the query result json serialisable
        for row in rows:
            dict_row = {
                "Station_ID": row[0],
                "Available_bike_stands": row[1],
                "Available_bikes": row[2],
                "Status": row[3]
            }
            result.append(dict_row)
        return result

    except Exception as e:
        print(e) 

# fetches current weather info
def getWeather():
    try:
        engine.execute(sql)
    except Exception as e:
        print(e)

    try:
        result = engine.execute(sql2)
        rows = result.fetchall()
        result = []
        for row in rows:
            dict_row = {
                # lon, lat, weather_id, weather_main, weather_desc, main_temp, feels_like, temp_min, temp_max, pressure, humidity, visibility, wind_speed, wind_deg, clouds, dt, sys_type, sys_id, sys_country, sys_sunrise, sys_sunset, timezone, station_name, cod, scraper_input_dateTime
                "lon": row[0],
                "lat": row[1],
                "weather_id": row[2],
                "weather_main": row[3],
                "weather_desc": row[4],
                "main_temp": row[5],
                "feels_like": row[6],
                "temp_min": row[7],
                "temp_max": row[8],
                "pressure": row[9],
                "humidity": row[10],
                "visibility": row[11],
                "wind_speed": row[12],
                "wind_deg": row[13],
                "clouds": row[14],
                "dt": row[15],
                "sys_type": row[16],
                "sys_id": row[17],
                "sys_country": row[18],
                "sys_sunrise": row[19],
                "sys_sunset": row[20],
                "timezone": row[21],
                "station_name": row[22],
                "cod": row[23]
            }
            result.append(dict_row)
        return result

    except Exception as e:
        print(e)  

# queries the database for average occupancy between 2 given times (format hh:mm), on the same days as the given day (format yyyy-mm-dd), for a given station
def getAverage(time1, time2, date, stationID):
    # Query the database for availability within the time range
    try:
        engine.execute(sql)
    except Exception as e:
        print(e)

    query = f"SELECT AVG(Available_bikes) as avg_available_bikes, AVG(Available_bike_stands) as avg_available_bike_stands FROM availability WHERE DAYOFWEEK(Scraper_input_dateTime) = DAYOFWEEK('{date}') AND TIME(Scraper_input_dateTime) BETWEEN '{time1}' AND '{time2}' AND Station_ID = '{stationID}'"

    try:
        result = engine.execute(query)
        rows = result.fetchall()
        result = []

        # required to make the query result json serialisable
        for row in rows:
            dict_row = {
                "AverageBikes": row[0],
                "AvailableStands": row[1]
            }
            result.append(dict_row)
        return result

    except Exception as e:
        print(e) 

# adds ten minutes to a time in format hh:mm
def add_ten_minutes(time):
    hour, minute = map(int, time.split(':'))
    total_minutes = hour * 60 + minute + 10
    if hour == 23 and minute >= 50:
        return '23:59'
    new_hour, new_minute = divmod(total_minutes, 60)
    new_hour %= 24
    return f'{new_hour:02d}:{new_minute:02d}'

# takes ten minutes from a time in format hh:mm
def minus_ten_minutes(time):
    hour, minute = map(int, time.split(':'))
    total_minutes = hour * 60 + minute - 10
    if hour == 0 and minute < 10:
        return '00:00'
    if total_minutes < 0:
        total_minutes += 1440  # Add 24 hours worth of minutes
    new_hour, new_minute = divmod(total_minutes, 60)
    return f'{new_hour:02d}:{new_minute:02d}'

app = Flask(__name__, static_url_path='')

with open('flask/static/stations.json') as s:
          station_data = json.load(s)

@app.route('/')
def stations():
     #with open('stations.json') as s:
     #     station_data = json.load(s)
          # use print to test if data is being loaded
          # print(station_data)
     # station_data = r"C:\Users\James\Desktop\Dropbox\UCD\Semester 2\Comp30830 Software Engineering\Assignments\Assignment 2 Group\Working Files\Database Backups\station_data.json"     
     return render_template("index.html", station_data=station_data, google_maps_api_key=config.keys['google_maps_api_key'])

@app.route('/api/stations')
def show_stations():
     return jsonify(station_data)
     
@app.route('/api/availability')
def availability():
    return jsonify(getAvailability())

@app.route('/api/weather')
def weather():
    return jsonify(getWeather())

@app.route('/api/availability/<time>/<date>/<stationID>')
def availability_time(time, date, stationID):
    time1 = minus_ten_minutes(time)
    time2 = add_ten_minutes(time)
    result = getAverage(time1, time2, date, stationID)
    return result

if __name__ == '__main__':
    # SECURITY: only run with host 0.0.0.0 to test on a trusted LAN. Test by opening browser on another device and visiting {host_machine_IP:5000}
    # app.run(host='0.0.0.0', port=5000)

    app.run(host='127.0.0.1', port=5000)

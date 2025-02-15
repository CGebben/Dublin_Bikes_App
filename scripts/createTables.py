from sqlalchemy import create_engine
import traceback
import simplejson as json
import requests

"""
This script initializes the MySQL database by creating necessary tables 
and populating them with station data from the JCDecaux API.
"""

# Configuration Variables (Replace with actual values or use environment variables)
DB_CONNECTION_STRING = "mysql+mysqldb://USERNAME:PASSWORD@HOST:PORT/DB_NAME"
APIKEY = "..."
NAME = "Dublin"  # Name of contract
STATIONS_URL = "https://api.jcdecaux.com/vls/v1/stations"  # JCDecaux API Endpoint

# Create database connection
engine = create_engine(DB_CONNECTION_STRING, echo=True)

# SQL statements to create schema and tables
sqlSchema = """
CREATE SCHEMA IF NOT EXISTS `dublinBikes`;
"""

sqlTables = """
USE dublinBikes;

CREATE TABLE IF NOT EXISTS `station` (
  `Station_ID` int NOT NULL,
  `Contract_name` varchar(256) NOT NULL,
  `Station_name` varchar(256) NOT NULL,
  `Station_address` varchar(256) NOT NULL,
  `Position_lat` double NOT NULL,
  `Position_long` double NOT NULL,
  `Banking` int NOT NULL,
  `Bonus` int NOT NULL,
  `Bike_stands` int NOT NULL,
  `Last_update` int NOT NULL,
  PRIMARY KEY (`Station_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `availability` (
  `Station_ID` int NOT NULL,
  `Available_bike_stands` int NOT NULL,
  `Available_bikes` int NOT NULL,
  `Status` varchar(256) NOT NULL,
  `Scraper_input_dateTime` datetime NOT NULL,
  PRIMARY KEY (`Station_ID`,`Scraper_input_dateTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
"""

# Execute schema creation
try:
    engine.execute(sqlSchema)
    engine.execute(sqlTables)
    print("Database schema and tables created successfully.")
except Exception as e:
    print("Error creating database schema:", e)

def storeStation(stations):
    """
    Reads in static station data from the API and inserts it into the database.
    This function should only be run once for initial population.
    """
    stations = json.loads(stations)
    
    engine.execute("USE dublinBikes;")
    
    for station in stations:
        vals = (
            station.get("number"), station.get("contract_name"), station.get("name"),
            station.get("address"), station.get("position").get("lat"), station.get("position").get("lng"),
            int(station.get("banking")), int(station.get("bonus")), int(station.get("bike_stands")),
            station.get("last_update")
        )

        engine.execute("INSERT INTO station VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", vals)

def main():
    """
    Fetches station data from JCDecaux API and stores it in the database.
    """
    try:
        response = requests.get(STATIONS_URL, params={"apiKey": APIKEY, "contract": NAME})
        response.raise_for_status()  # Raise an error for bad responses (4xx, 5xx)
        storeStation(response.text)
        print("Station data successfully inserted into database.")
    except requests.exceptions.RequestException as e:
        print("Error fetching data from JCDecaux API:", traceback.format_exc())

if __name__ == "__main__":
    main()
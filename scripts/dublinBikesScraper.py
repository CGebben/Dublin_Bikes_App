from sqlalchemy import create_engine
import simplejson as json
import requests
import time
from datetime import datetime
import traceback

"""
This file scrapes live bike availability data from the JCDecaux API and stores it in the MySQL database.

Security Notice:
- Removed hardcoded API keys and database credentials.
- If using this script in production, store sensitive credentials in a secure location (e.g., environment variables).
"""

# Configuration Variables (Replace with actual values or use environment variables)
DB_CONNECTION_STRING = "mysql+mysqldb://USERNAME:PASSWORD@HOST:PORT/DB_NAME"
APIKEY = "..."
NAME = "Dublin"  # Name of contract
STATIONS = "https://api.jcdecaux.com/vls/v1/stations"  # JCDecaux API Endpoint

# Create database connection
engine = create_engine(DB_CONNECTION_STRING, echo=True)

def store(stations):
    """
    Reads in dynamic data of stations and stores it in the database.
    This script is designed to run continuously.
    """
    stations = json.loads(stations)

    engine.execute("USE dublinBikes;")

    for station in stations:
        now = datetime.now()  # Timestamp for database entry

        vals = (
            station.get("number"), int(station.get("available_bike_stands")),
            int(station.get("available_bikes")), station.get("status"), now
        )

        engine.execute("INSERT INTO availability VALUES (%s,%s,%s,%s,%s)", vals)
    return

def main():
    """
    Scrapes dynamic station availability data and stores it in the database.
    
    This script is intended to be run repeatedly every five minutes. 
    It can be scheduled using:
    1) `time.sleep(5*60)` within a loop (as shown below).
    2) `crontab` or a similar task scheduler.

    Uncomment the loop below if running as a continuous process.
    """

    r = requests.get(STATIONS, params={"apiKey": APIKEY, "contract": NAME})

    # Optionally, enable continuous data collection
    """
    while True:
        try:
            store(r.text)  # Store data in a database
            time.sleep(5*60)  # Sleep for 5 minutes
        except:
            print(traceback.format_exc())
            time.sleep(1*60)  # Wait 1 minute before retrying in case of failure
    """

    try:
        store(r.text)  # Store data in the database
    except:
        print(traceback.format_exc())
    return

if __name__ == "__main__":
    main()
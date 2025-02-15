from sqlalchemy import create_engine

"""
This script initializes the Weather table in the MySQL database.
"""

# Configuration Variables (Replace with actual values or use environment variables)
DB_CONNECTION_STRING = "mysql+mysqldb://USERNAME:PASSWORD@HOST:PORT/DB_NAME"

# Create database connection
engine = create_engine(DB_CONNECTION_STRING, echo=True)

# SQL statement to create the weather_scraper table
sql = """
CREATE TABLE IF NOT EXISTS Weather (
  lon DOUBLE,
  lat DOUBLE,
  weather_id SMALLINT,
  weather_main VARCHAR(255),
  weather_desc VARCHAR(255),
  main_temp FLOAT,
  feels_like FLOAT,
  temp_min FLOAT,
  temp_max FLOAT,
  pressure SMALLINT,
  humidity SMALLINT,
  visibility SMALLINT,
  wind_speed FLOAT,
  wind_deg SMALLINT,
  clouds SMALLINT,
  dt BIGINT,
  sys_type SMALLINT,
  sys_id SMALLINT,
  sys_country VARCHAR(255),
  sys_sunrise INT,
  sys_sunset INT,
  timezone INT,
  station_name VARCHAR(255),
  cod VARCHAR(255),
  scraper_input_dateTime DATETIME NOT NULL,
  PRIMARY KEY (`station_name`,`scraper_input_dateTime`)
);
"""

def main():
    """
    Connects to the database and creates the Weather table.
    """
    engine.execute("USE dublinBikes;")
    engine.execute(sql)
    print("Weather table created successfully.")

if __name__ == "__main__":
    main()
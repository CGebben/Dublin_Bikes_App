from sqlalchemy import create_engine
import simplejson as json
from datetime import datetime

"""
This file creates a local machine backup of the availability and weather_scraper tables
edits required in create_engine, file_path in main() and engine.execute() in main
"""

# edit USERNAME, PASSWORD, ENDPOINT and PORT as appropriate
engine = create_engine('mysql+mysqldb://USERNAME:PASSWORD@ENDPOINT:PORT', echo=True)

sql = """
USE dublinBikes;
"""

sql1 = """
SELECT * FROM availability;
"""

sql2 = """
SELECT * FROM weather_scraper;
"""

def datetime_converter(o):
    if isinstance(o, datetime):
        return o.__str__()


def main():
    try:
        engine.execute(sql)
    except Exception as e:
        print(e)

    try:
        batch_size = 1000
        rows = []
        #run with sql1 or sql2 depending on what data needs backing up
        result = engine.execute(sql2)
        while True:
            #fetchall for <=1000 rows, fetchmany allows us to retrieve all data
            batch = result.fetchmany(batch_size)
            if not batch:
                break
            for row in batch:
                rows.append(row)
        # Convert the fetched rows to a JSON string
        json_data = json.dumps(rows, default=datetime_converter)
        # replace file location as appropriate
        file_path = r"C:\Users\User\folder\filename.json" 
        with open(file_path, 'w') as outfile:
            outfile.write(json_data)
    except Exception as e:
        print(e) 
    return


if __name__== "__main__":
    main()
USE sys;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='	';

CREATE TABLE IF NOT EXISTS `availability` (
  `Station_ID` int NOT NULL,
  `Available_bike_stands` int NOT NULL,
  `Available_bikes` int NOT NULL,
  `Status` varchar(256) NOT NULL,
  `Scraper_input_dateTime` datetime NOT NULL,
  PRIMARY KEY (`Station_ID`,`Scraper_input_dateTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='	';

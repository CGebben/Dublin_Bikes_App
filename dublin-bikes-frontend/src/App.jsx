import { useEffect, useState } from "react";
import MapWrapper from "./components/MapWrapper";
import WeatherBar from "./components/WeatherBar";
import {
  fetchStations,
  fetchAvailability,
  fetchLatestTimestamp,
  fetchWeather,
} from "./services/api";

function App() {
  const [stations, setStations] = useState([]);
  const [availability, setAvailability] = useState([]);
  const [weather, setWeather] = useState(null);
  const [lastTimestamp, setLastTimestamp] = useState("");

  // Load static station data once.
  useEffect(() => {
    async function loadStations() {
      try {
        const data = await fetchStations();
        setStations(data);
      } catch (error) {
        console.error("Error loading stations:", error);
      }
    }

    loadStations();
  }, []);

  // Poll for updates every 60 seconds
  useEffect(() => {
    async function checkForUpdates() {
      try {
        const { timestamp } = await fetchLatestTimestamp();
        console.log("Checked timestamp:", timestamp);

        if (timestamp !== lastTimestamp) {
          console.log("New timestamp detected! Refreshing data...");
          setLastTimestamp(timestamp);

          const [availabilityData, weatherData] = await Promise.all([
            fetchAvailability(),
            fetchWeather(),
          ]);

          setAvailability(availabilityData);
          setWeather(weatherData);
        } else {
          console.log("Timestamp unchanged. No need to refresh.");
        }
      } catch (error) {
        console.error("Polling error:", error);
      }
    }

    // Initial load
    checkForUpdates();

    // Poll every 60 seconds
    const interval = setInterval(checkForUpdates, 60000);
    return () => clearInterval(interval);
  }, [lastTimestamp]);

  return (
    <div className="App">
      <h1>Dublin Bikes</h1>
      <WeatherBar weather={weather} />
      <MapWrapper stations={stations} availability={availability} />
    </div>
  );
}

export default App;

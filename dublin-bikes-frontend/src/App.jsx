import { useEffect, useState } from "react";
import MapWrapper from "./components/MapWrapper";
import WeatherBar from "./components/WeatherBar";
import { fetchStations, fetchAvailability, fetchWeather } from "./services/api";

function App() {
  const [stations, setStations] = useState([]);
  const [availability, setAvailability] = useState([]);
  const [weather, setWeather] = useState(null);

  // Track last successful update time for conditional requests
  const [lastModifiedAvailability, setLastModifiedAvailability] =
    useState(null);
  const [lastModifiedWeather, setLastModifiedWeather] = useState(null);

  // Load static station data once
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

  // Poll every 60 seconds using If-Modified-Since headers.
  useEffect(() => {
    async function pollData() {
      console.log("⏳ Polling for updates...");

      try {
        const [availabilityResult, weatherResult] = await Promise.all([
          fetchAvailability(lastModifiedAvailability),
          fetchWeather(lastModifiedWeather),
        ]);

        if (availabilityResult.data) {
          console.log(
            "✅ Availability updated:",
            availabilityResult.data.length,
            "stations"
          );
          setAvailability(availabilityResult.data);
          setLastModifiedAvailability(availabilityResult.lastModified);
        } else {
          console.log("ℹ️ Availability not modified.");
        }

        if (weatherResult.data) {
          console.log("✅ Weather updated:", weatherResult.data.weatherMain);
          setWeather(weatherResult.data);
          setLastModifiedWeather(weatherResult.lastModified);
        } else {
          console.log("ℹ️ Weather not modified.");
        }
      } catch (error) {
        console.error("❌ Polling error:", error);
      }
    }

    pollData(); // Initial call

    const interval = setInterval(pollData, 60000);
    return () => clearInterval(interval);
  }, [lastModifiedAvailability, lastModifiedWeather]);

  return (
    <div className="App">
      <h1>Dublin Bikes</h1>
      <WeatherBar weather={weather} />
      <MapWrapper stations={stations} availability={availability} />
    </div>
  );
}

export default App;

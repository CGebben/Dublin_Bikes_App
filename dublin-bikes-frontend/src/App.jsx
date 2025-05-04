import { useEffect, useState } from "react";
import MapWrapper from "./components/MapWrapper";
import WeatherBar from "./components/WeatherBar";
import { fetchStations, fetchAvailability, fetchWeather } from "./services/api";

function App() {
  // Static station data (loaded once on mount)
  const [stations, setStations] = useState([]);

  // Dynamic availability data (updates every 60s if backend changes)
  const [availability, setAvailability] = useState([]);

  // Latest weather snapshot
  const [weather, setWeather] = useState(null);

  // Track timestamps for conditional fetching
  const [lastModifiedAvailability, setLastModifiedAvailability] =
    useState(null);
  const [lastModifiedWeather, setLastModifiedWeather] = useState(null);

  // Load station data once on initial load
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

  // Poll weather and availability every 60s using If-Modified-Since
  useEffect(() => {
    async function pollData() {
      console.log("⏳ Polling for updates...");

      try {
        const [availabilityResult, weatherResult] = await Promise.all([
          fetchAvailability(lastModifiedAvailability),
          fetchWeather(lastModifiedWeather),
        ]);

        // Update availability if backend data changed
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

        // Update weather if backend data changed
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

    pollData(); // Initial fetch on mount

    // Poll every 60 seconds
    const interval = setInterval(pollData, 60000);
    return () => clearInterval(interval); // Cleanup on unmount
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

import { useEffect, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import {
  fetchStations,
  fetchAvailability,
  fetchLatestTimestamp,
} from "../services/api";

function MapWrapper() {
  const [stations, setStations] = useState([]);
  const [availability, setAvailability] = useState([]);
  const [lastTimestamp, setLastTimestamp] = useState("");

  // Load static station data once on mount
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

  // Load availability and setup polling
  useEffect(() => {
    async function checkForUpdates() {
      try {
        const { timestamp } = await fetchLatestTimestamp();
        console.log("Checked timestamp:", timestamp);

        if (timestamp !== lastTimestamp) {
          console.log("New timestamp detected! Refreshing availability...");
          setLastTimestamp(timestamp);
          const data = await fetchAvailability();
          setAvailability(data);
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
    return () => clearInterval(interval); // Clean up interval on component unmount
  }, [lastTimestamp]);

  return (
    <MapContainer
      center={[53.34491, -6.26266]}
      zoom={13}
      style={{ height: "100%", width: "100%" }}
    >
      <TileLayer
        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {availability.map((entry) => (
        <Marker
          key={entry.station.stationId}
          position={[entry.station.latitude, entry.station.longitude]}
        >
          <Popup>
            {entry.station.stationName}
            <br />
            Station ID: {entry.station.stationId}
            <br />
            Bikes Available: {entry.availableBikes}
            <br />
            Stands Available: {entry.availableBikeStands}
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}

export default MapWrapper;

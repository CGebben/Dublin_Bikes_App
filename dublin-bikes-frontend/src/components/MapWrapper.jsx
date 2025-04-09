import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import { useEffect, useState } from "react";
import { fetchStations } from "../services/api";

function MapWrapper() {
  const [stations, setStations] = useState([]);

  useEffect(() => {
    async function loadStations() {
      try {
        const data = await fetchStations();
        console.log("Fetched stations:", data); // 👈 Add this
        setStations(data);
      } catch (error) {
        console.error("Error loading stations:", error);
      }
    }

    loadStations();
  }, []);

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

      {stations.map((station) => (
        <Marker
          key={station.stationId}
          position={[station.latitude, station.longitude]}
        >
          <Popup>
            {station.stationName}
            <br />
            Station ID: {station.stationId}
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}

export default MapWrapper;

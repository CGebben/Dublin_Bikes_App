import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";

// Function to return a custom marker icon based on bike availability
function getMarkerIcon(availableBikes) {
  let fillColor = "green";
  let size = 30;

  if (availableBikes === 0) {
    fillColor = "red";
    size = 22;
  } else if (availableBikes <= 3) {
    fillColor = "orange";
    size = 26;
  }

  return L.divIcon({
    className: "custom-div-icon",
    html: `<div style="
      background-color:${fillColor};
      width:${size}px;
      height:${size}px;
      border-radius:50%;
      border:1px solid black;">
    </div>`,
    iconSize: [size, size],
    iconAnchor: [size / 2, size / 2],
  });
}

// Displays a Leaflet map with one marker per station from availability data
function MapWrapper({ stations, availability }) {
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
          icon={getMarkerIcon(entry.availableBikes)}
        >
          <Popup>
            <strong>{entry.station.stationName}</strong>
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

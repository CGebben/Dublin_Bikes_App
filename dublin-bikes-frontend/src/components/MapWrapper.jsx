import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";

// Displays a Leaflet map with one marker per station from availability data
function MapWrapper({ stations, availability }) {
  return (
    <MapContainer
      center={[53.34491, -6.26266]} // Centered on Dublin
      zoom={13}
      style={{ height: "100%", width: "100%" }}
    >
      <TileLayer
        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {/* Render markers for all available stations */}
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

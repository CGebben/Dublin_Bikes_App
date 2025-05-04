// Displays key weather data above the map
// If no data is available yet, returns null (renders nothing)
function WeatherBar({ weather }) {
  if (!weather) return null;

  return (
    <div className="weather-bar">
      <div className="weather-item">
        Temp: {(weather.mainTemp - 273.15).toFixed(1)}°C
      </div>
      <div className="weather-item">Humidity: {weather.humidity}%</div>
      <div className="weather-item">Wind: {weather.windSpeed} m/s</div>
      <div className="weather-item">Clouds: {weather.clouds}%</div>
      <div className="weather-item">{weather.weatherMain}</div>
    </div>
  );
}

export default WeatherBar;

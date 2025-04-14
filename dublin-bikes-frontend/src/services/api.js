export async function fetchStations() {
  const response = await fetch('/stations');
  if (!response.ok) {
    throw new Error("Failed to fetch stations");
  }
  return await response.json();
}

export async function fetchAvailability() {
  const response = await fetch('/availability/latest');
  if (!response.ok) {
    throw new Error("Failed to fetch availability");
  }
  return await response.json();
}

export async function fetchLatestTimestamp() {
  const response = await fetch('/availability/latest-timestamp');
  if (!response.ok) {
    throw new Error("Failed to fetch latest timestamp");
  }
  return await response.json(); // Expected to return: { timestamp: "2025-04-10T..." }
}

export async function fetchWeather() {
  const response = await fetch('/weather/latest');
  if (!response.ok) {
    throw new Error('Failed to fetch weather');
  }
  return await response.json();
}
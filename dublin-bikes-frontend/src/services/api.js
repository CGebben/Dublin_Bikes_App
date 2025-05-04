// API call to fetch static station data
export async function fetchStations() {
  const response = await fetch('/stations');
  if (!response.ok) throw new Error("Failed to fetch stations");
  return await response.json();
}

// Fetches latest availability data, using If-Modified-Since if provided
export async function fetchAvailability(lastModified) {
  const headers = lastModified ? { 'If-Modified-Since': lastModified } : {};
  const response = await fetch('/availability/latest', { headers });

  if (response.status === 304) return { data: null, lastModified };
  if (!response.ok) throw new Error("Failed to fetch availability");

  const data = await response.json();
  const lastModifiedHeader = response.headers.get('Last-Modified');
  return { data, lastModified: lastModifiedHeader };
}

// Fetches latest weather data, also using If-Modified-Since
export async function fetchWeather(lastModified) {
  const headers = lastModified ? { 'If-Modified-Since': lastModified } : {};
  const response = await fetch('/weather/latest', { headers });

  if (response.status === 304) return { data: null, lastModified };
  if (!response.ok) throw new Error("Failed to fetch weather");

  const data = await response.json();
  const lastModifiedHeader = response.headers.get('Last-Modified');
  return { data, lastModified: lastModifiedHeader };
}
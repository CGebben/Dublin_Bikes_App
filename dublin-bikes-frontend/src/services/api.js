export async function fetchStations() {
    const response = await fetch('/stations'); // This is correct if your proxy is working
    if (!response.ok) {
      throw new Error("Failed to fetch stations");
    }
    return await response.json();
  }
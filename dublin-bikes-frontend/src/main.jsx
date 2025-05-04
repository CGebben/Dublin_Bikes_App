import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import "leaflet/dist/leaflet.css"; // Required for Leaflet map styling

// Mount the main App component inside the #root div
createRoot(document.getElementById("root")).render(
  <StrictMode>
    <App />
  </StrictMode>
);

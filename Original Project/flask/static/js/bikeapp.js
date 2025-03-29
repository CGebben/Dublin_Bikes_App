//global vars
var map; 
var markers = [];
var allStationData = [];
var allAvailabilityData = [];
var visitedStations = []; 
var predictedBikes;
var predictedStands;

//on load: loads visitedStations from local storage, sets checkboxes to unchecked
window.addEventListener('load', function() {
  visitedStations = JSON.parse(localStorage.getItem('visitedStations')) || [];
  console.log('window loading functions loaded')
  //console.log("function to load visitedStations called");
  document.getElementById('only-available-checkbox').checked = false;
  document.getElementById('datetime-toggle').checked = false;
});

//function that returns the availability data (json), also adds availability info into a globally accessible variable
async function getAvailability () {
  let fetchedAvailabilityData = null;
  //wait for the request to complete before moving on
  const [availabilityResponse] = await Promise.all([
    fetch('/api/availability')
  ]);
  fetchedAvailabilityData = await availabilityResponse.json();
  return fetchedAvailabilityData;
}

//function that returns station data (json), adds info into globally accessible variable
async function getStations () {
  let fetchedStationData = null;
  //wait for both requests to complete before moving on
  const [stationsResponse] = await Promise.all([
    fetch('/api/stations'),
  ]);
  fetchedStationData = await stationsResponse.json();
  return fetchedStationData;
}

//converts temperature in Kelvin to Celsius (2 decimal places)
function kelvinToCelsius(temp) {
  return (temp - 273.15).toFixed(2);
}

//gets today's date, formats for user station prediction
function getTodaysFormattedDate(){
  const today = new Date();
  const year = today.getFullYear();
  const month = (today.getMonth() + 1).toString().padStart(2, '0');
  const day = today.getDate().toString().padStart(2, '0');
  const formattedDate = `${year}-${month}-${day}`;
  return formattedDate;
}

//gets today's time, formats for user station prediction
function getCurrentFormattedTime() {
  const now = new Date();
  const hours = now.getHours().toString().padStart(2, '0');
  const minutes = now.getMinutes().toString().padStart(2, '0');
  return `${hours}:${minutes}`;
}

//function to populate the dropdown station filter, calls setHistoryDropdown() to populate the history dropdown
function setMainDropDown(stations){
  //console.log("setDropDown called")
  // Sort stationData by Station_name for dropdown menu
  stations.sort((a, b) => a.Station_name.localeCompare(b.Station_name));

  //creates a show all option
  var select = document.getElementById("station-select");
  select.innerHTML = "";
  var option = document.createElement("option");
  option.value = "showall";
  option.text = "SHOW ALL STATIONS";
  select.add(option);

  //creates options for each station
  stations.forEach(station => {
    //console.log(station);
    var option = document.createElement("option");
    option.value = station['Station_ID'];
    option.text = station['Station_name'];
    select.add(option);
  });

  var selectPrevious = document.getElementById("previous-station-select");
  var submitPrevious = document.getElementById("submit-previous-station-button");

  if(visitedStations.length > 0){
    setHistoryDropdown();
  }else{
    selectPrevious.disabled = true;
    selectPrevious.style.opacity = 0.5;
    submitPrevious.disabled = true;
    submitPrevious.style.opacity = 0.5;
  }
}

//populates the history dropdown
function setHistoryDropdown() {
  var selectPrevious = document.getElementById("previous-station-select");
  var submitPrevious = document.getElementById("submit-previous-station-button");
  selectPrevious.disabled = false;
  selectPrevious.style.opacity = 1;
  submitPrevious.disabled = false;
  submitPrevious.style.opacity = 1;
  selectPrevious.innerHTML = '';
  console.log("data found in local storage");
  visitedStations.sort((a, b) => a.Station_name.localeCompare(b.Station_name));
  visitedStations.forEach(station => {
    //console.log(station);
    var option = document.createElement("option");
    option.value = station['Station_ID'];
    option.text = station['Station_name'];
    selectPrevious.add(option);
});
}

//initiates the map on screen, gets station and availability information, fills station dropdown, creates infoWindows
async function initMap() {

  //console.log("initMap called")
  const { Map } = await google.maps.importLibrary("maps");
  map = new Map(document.getElementById('map'), {
    center: {lat: 53.34491, lng: -6.26266}, // set the center of the map a little south of ha'penny bridge, all icons in view
    zoom: 13, // sets zoom
    clickableIcons: false // turns off clickable icons for other landmarks on the map
  });

  //calls functions to fetch station and availability info, sets to appropriate variables
  let stationData = await getStations();
  allStationData = stationData;
  let availabilityData = await getAvailability();
  allAvailabilityData = availabilityData;

  // Create an InfoWindow
  const infoWindow = new google.maps.InfoWindow();

   //Loops through the station data and create a marker for each station
  allStationData.forEach(station => {
    var marker = new google.maps.Marker({
      position: {lat: station['Position_lat'], lng: station['Position_long']},
      map: map,
      icon: {
        path: google.maps.SymbolPath.CIRCLE, //icons are now circle
        fillColor: 'green', //default green, this will be changed based on availability
        fillOpacity: 0.8, //slightly see-through
        strokeWeight: 0.5, //weight of line surrounding icon
        scale: 13,
        strokeColor: 'black'//colour of line surrounding icon
      }
    });

    // Check availabilityData for the current station, set icon colours depending on current availability
    var availability = allAvailabilityData.find(data => data.Station_ID === station.Station_ID);
    if (availability && availability.Available_bikes === 0) {
        marker.setIcon({
            path: google.maps.SymbolPath.CIRCLE,
            fillColor: 'red',
            fillOpacity: .8,
            strokeWeight: 0.5,
            scale: 8,
            strokeColor: 'black'
        });
    }    else if(availability && availability.Available_bikes <= 3){
      marker.setIcon({
        path: google.maps.SymbolPath.CIRCLE,
        fillColor: 'orange',
        fillOpacity: .8,
        strokeWeight: 0.5,
        scale: 10,
        strokeColor: 'black'
        }
  )}

    // Add the Station_ID as a property of the marker
    marker.stationID = station['Station_ID'];

    // Create an infoWindow for each marker
    marker.infoWindow = new google.maps.InfoWindow({
      content: '<div class="info-window">' + station['Station_name'] + '</div>'
    });

    //adds a listener: when icon clicked the associated infoWindow will display
    marker.addListener('click', function() {
      var availability = allAvailabilityData.find(data => data.Station_ID === station.Station_ID);
      var content = '<div class="info-window">' + station['Station_name'] + '</div>';
      if (availability) {
        content += '<div class="info-window" id="infoBike">Available Bikes: ' + availability.Available_bikes + '</div>';
        content += '<div class="info-window" id="infoStand">Available Bike Stands: ' + availability.Available_bike_stands + '</div>';
      } else {
        content += '<div class="info-window">Availability data not available</div>';
      }
      infoWindow.setContent(content);
      infoWindow.open(map, marker);
    });

    //adds an event listener for 'submitStationCalled' which closes any open infoWindow
    document.addEventListener('submitStationCalled', function() {
      infoWindow.close();
    });
    
    //adds an event listener for 'stationsFiltered' which closes any open infoWindow
    document.addEventListener('stationsFiltered', function() {
      infoWindow.close();
    });
    
    // Add the marker to the markers array
    markers.push(marker);

  });
  setMainDropDown(allStationData);
}

//fetches and displays current weather data
async function initWeather(){
  //console.log("initWeather() called")
  let weatherData = null;
  //wait for both requests to complete before moving on
  const [weatherResponse] = await Promise.all([
    fetch('/api/weather')
  ]);

  weatherData = await weatherResponse.json();
  //console.log(weatherData)

  //display weather information in the top bar
  const weatherInfo = document.getElementById('weather-data');
  //weatherInfo.innerHTML = 'JS TEST';
  weatherInfo.innerHTML = `
    <span>Cloud Cover: ${weatherData[0].clouds}%</span>
    <span>Temp: ${kelvinToCelsius(weatherData[0].main_temp)}&#8451;</span>
    <span>Highs: ${kelvinToCelsius(weatherData[0].temp_max)}&#8451;</span>
    <span>Lows: ${kelvinToCelsius(weatherData[0].temp_min)}&#8451;</span>
    <span>Weather: ${weatherData[0].weather_main} (${weatherData[0].weather_desc})</span>
    <span>Wind Speed: ${weatherData[0].wind_speed} kmph</span>
    `;
}

function submitStation() {
  // Triggers an event when submitStation has been called
  const event = new Event('submitStationCalled');
  document.dispatchEvent(event);
  var select = document.getElementById("station-select");
  var stationID = select.options[select.selectedIndex].value;
  var selectedDate = document.getElementById("date-select").value;
  var selectedTime = document.getElementById("time-select").value;
  //console.log(selectedDate);
  //console.log("selected time " + selectedTime);
  //to update. handle if time is near midnight night or morning (this could be in the route)
  var checkBox = document.getElementById("datetime-toggle");
  //if user wants prediction
  if(checkBox.checked==true){
    //console.log("prediction checkbox checked recognised")
    //if chosen date is today
    if(selectedDate == getTodaysFormattedDate()){
      //console.log("today selected")
      //if time is earlier than now
      if(selectedTime <= getCurrentFormattedTime()){
        //console.log("time too early recognised")
        //error message, try again, time must be in future
        alert("Error: Please choose a time in the future.");
        return;
      }
    }
    //if user has chosen a station as required 
    if(stationID != "showall"){
      //console.log("station not showall recognised")
      fetch(`/api/availability/${selectedTime}/${selectedDate}/${stationID}`)
      .then(response => response.json())
      .then(data => {
        var predictedAvailableStands = data[0].AvailableStands;
        predictedStands = Math.round(predictedAvailableStands);
        var predictedAverageBikes = data[0].AverageBikes;
        predictedBikes = Math.round(predictedAverageBikes);
        //console.log("api call complete - predicted stands "+ predictedAvailableStands)
        //console.log("predicted bikes "+ predictedAverageBikes)
        
        // Creates content to show the predicted stands and bikes info
        const popupContent = document.createElement('div');
        popupContent.innerHTML = `
          <div class="popup">
            <div class="popup-title">Predicted Availability</div>
            <div class="popup-content">
              <div class="popup-row">
                <div class="popup-label">Available Bikes:</div>
                <div class="popup-value">${predictedBikes}</div>
              </div>
              <div class="popup-row">
                <div class="popup-label">Available Stands:</div>
                <div class="popup-value">${predictedStands}</div>
              </div>
            </div>
          </div>
        `;

        //creates a new infoWindow for the predicted info
        const popup = new google.maps.InfoWindow({
          content: popupContent,
          pixelOffset: new google.maps.Size(165, 0)
        });

        //ensures predicted InfoWindow is on top of other content
        popup.setZIndex(9999);

        // Show the popup in the middle of the map
        popup.setPosition(map.getCenter());
        popup.open(map);

        // Add a click event listener to the submit button to close the popup
        document.getElementById('submit-button').addEventListener('click', () => {
          popup.close();
        });

        //adds event listener to the 'view again' button to close the popup
        document.getElementById('previous-station-select').addEventListener('click', () => {
          popup.close();
        });

        //adds event listener to the 'show available only' button to close the popup
        document.getElementById('only-available-checkbox').addEventListener('click', () => {
          popup.close();
        });

      })
      .catch(error => console.error(error));
    } else{
        alert("Error: Please choose a station to see predicted info.");
        return;
    } 
  }

  //if user chose 'Show all stations', add all markers back onto map and exit function
  if (stationID == "showall") {
    markers.forEach(marker => {
      marker.setMap(map);
      marker.infoWindow.close();
    });
    map.setZoom(13);
    map.setCenter({lat: 53.34491, lng: -6.26266})
    return; 
  }

  // Add all markers back to the map
  markers.forEach(marker => {
    marker.setMap(map);
  });

  // Remove all markers from the map except the selected one
  markers.forEach(marker => {
    if (marker.stationID != stationID) {
      marker.setMap(null);
      marker.infoWindow.close();
    }
  });

  // Zoom in on the selected marker
  var selectedMarker = markers.find(marker => marker.stationID == stationID);
  map.setZoom(16);
  map.setCenter(selectedMarker.getPosition());
  google.maps.event.trigger(selectedMarker, 'click');

  //handles adding visited staitons to the station history in local storage
  allStationData.forEach(station => {
    if(station.Station_ID == stationID) {
      // Check if the station is already in visitedStations
      if (visitedStations && visitedStations.find(visitedStation => visitedStation.Station_ID == stationID)) {
        return; // Do nothing if the station is already in visitedStations
      }
      if (visitedStations && visitedStations.length >= 10) {
        // remove the oldest station and add the new one
        visitedStations.shift();
        visitedStations.push(station);
      } else {
        // add the new station
        visitedStations.push(station);
      }
      localStorage.setItem('visitedStations', JSON.stringify(visitedStations));
    } 
  })
  setHistoryDropdown();
}

//handles when user chooses to view a station from their history
function submitPreviousStation() {
  var select = document.getElementById("previous-station-select");
  var previousStationID = select.options[select.selectedIndex].value;
  var selectedPreviousMarker = markers.find(marker => marker.stationID == previousStationID);
  map.setZoom(16);
  map.setCenter(selectedPreviousMarker.getPosition());
  google.maps.event.trigger(selectedPreviousMarker, 'click');
  //set all markers to visible first (required in case users were viewing another station)
  markers.forEach(marker => {
    marker.setMap(map);
    //unchecks predicted checkbox with a simulated click
    const checkbox = document.getElementById("datetime-toggle");
    if (checkbox.checked) {
      checkbox.click();
    } 
  })
  //remove all markers from map except for the selected station
  markers.forEach(marker => {
    if (marker.stationID != previousStationID) {
      marker.setMap(null);
      marker.infoWindow.close();
    } else {
      if(marker.getVisible()==false){
      document.getElementById('only-available-checkbox').click();
      }
    }
  });

  document.getElementById("station-select").value = "showall";
}

//handles what occurs when user decides to only view stations with available bikes
function chooseAvailabilityOnly() {
  var filteredStationIDs = [];
  var filteredStations = [];
  //displays on markers of stands with available bikes
  if (document.getElementById('only-available-checkbox').checked == true) {
    for (let i = 0; i < markers.length; i++) {
      if (markers[i].getIcon().fillColor === 'green' || markers[i].getIcon().fillColor === 'orange') {
        markers[i].setVisible(true);
        filteredStationIDs.push(markers[i].stationID);
      } else {
        markers[i].setVisible(false);
      }
    }
    //only display stations with available bikes in the dropdown when required
    for (var i = 0; i < allStationData.length; i++) {
      var station = allStationData[i];
      //console.log(allStationData[i]['Station_ID']);
      //dispatches an event when stations are filtered
      if (filteredStationIDs.indexOf(station.Station_ID) >= 0) {
        filteredStations.push(station);
        const event = new Event('stationsFiltered');
        document.dispatchEvent(event);
      }
    }

   setMainDropDown(filteredStations);
  
  } else {
    markers.forEach(marker => {
      marker.setVisible(true);
    });
    //repopulates the dropdown with all stations
    setMainDropDown(allStationData)
  }
  //insert logic here to repopulate dropdown with all stations
}

//a function to show all markers.
function showAllMarkers() {
  markers.forEach(marker => {
    marker.setVisible(true);
  });
}

//handles what occurs when user checks the prediction checkbox
function choosePrediction() {
  var checkBox = document.getElementById("datetime-toggle");
  var datetimePicker = document.getElementById("dtPicker");

  if (checkBox.checked) {
    datetimePicker.style.display = "block";
    var currentDate = new Date();
    var minDate = currentDate.toISOString().split("T")[0];    
    var maxDate = new Date(currentDate.getTime() + (4 * 24 * 60 * 60 * 1000)).toISOString().split("T")[0]; // set max date to 3 days from tomorrow
    var dateInput = document.getElementById("date-select");
    dateInput.min = minDate;
    dateInput.max = maxDate;
    dateInput.value = minDate; // set default value to tomorrow
    var timeInput = document.getElementById("time-select");
    timeInput.value = "11:00"; // set default time to 11:00 AM
  } else {
    datetimePicker.style.display = "none";
  }
}

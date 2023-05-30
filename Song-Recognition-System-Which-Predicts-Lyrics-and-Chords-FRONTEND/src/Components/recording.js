import React from "react";
import "./recording.css";
import { FaPlayCircle } from "react-icons/fa";

function RecordingComponent(props){
    return (
      <div className="container">
        <div className="container-record">
          <div className="record-content d-flex flex-column align-items-center justify-content-center">
            <FaPlayCircle className="recording-icon" />
            <h5 className="text-center">Listening for music...</h5><br />
            <h5 className="text-center">Make sure your device can hear the song clearly.</h5>
          </div>
        </div>
      </div>
    );
}

export default RecordingComponent;

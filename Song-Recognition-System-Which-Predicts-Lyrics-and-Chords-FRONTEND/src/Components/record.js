import React from "react";
import "./record.css";
import { FaPlayCircle } from "react-icons/fa";

function RecordComponent(props) {
  return (
    <div className="container">
      <div className="container-record">
        <div className="record-content d-flex flex-column align-items-center justify-content-center">
          <FaPlayCircle className="record-icon" onClick={() => props.recordMode()} />
          <h5 className="text-center">Tap To Start</h5>
        </div>
      </div>
    </div>
  );
}

export default RecordComponent;

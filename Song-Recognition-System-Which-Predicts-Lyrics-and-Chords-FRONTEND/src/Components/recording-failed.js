import React from "react";
import "./recording.css";

function RecordFailedComponent(props) {
  return (
    <div className="container">
      <div className="container-record">
        <div className="record-content d-flex flex-column align-items-center justify-content-center">
          <h5 className="text-center">No Result</h5>
          <button className="btn btn-dark mt-3" onClick={() => props.retryFunction()}>Try Again</button>
        </div>
      </div>
    </div>
  );
}

export default RecordFailedComponent;

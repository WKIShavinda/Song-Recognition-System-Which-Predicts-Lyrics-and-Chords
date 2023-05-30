import React from "react";
import "./loading.css";
import { FaCircle } from "react-icons/fa";

function LoadingComponent(props){
    return (
      <div className="container">
        <div className="container-loading">
          <div className="loading-content d-flex flex-column align-items-center justify-content-center">
            <div className="loading-animation">
              <FaCircle  />
              <FaCircle  />
              <FaCircle  />
            </div>
            <h5 className="text-center">Loading details...</h5>
          </div>
        </div>
      </div>
    );
}

export default LoadingComponent;

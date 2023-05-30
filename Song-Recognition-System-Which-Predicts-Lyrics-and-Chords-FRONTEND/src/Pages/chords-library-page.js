import React from "react";

import handImg from "../assets/HAND.png";

function ChordsLibrary() {
  const majorChords = require.context(
    "../assets/chords-Images/Major",
    false,
    /\.(png|jpe?g|svg)$/
  );

  const minorChords = require.context(
    "../assets/chords-Images/Minor",
    false,
    /\.(png|jpe?g|svg)$/
  );

  const seventhChords = require.context(
    "../assets/chords-Images/7th",
    false,
    /\.(png|jpe?g|svg)$/
  );

  const majorChordUrls = majorChords.keys().map(majorChords);
  const minorChordUrls = minorChords.keys().map(minorChords);
  const seventhChordUrls = seventhChords.keys().map(seventhChords);

  return (
    <div className="container">
      <div className="mt-4 mt-4 d-flex justify-content-center align-items-center flex-column">
        <h3 className="fw-bolder mb-4">Hand Positions For Chords</h3>
        <img src={handImg} alt="hand" style={{ width: "300px" }}></img>
      </div>
      <div className="mt-4">
        <h3 className="fw-bolder">Major Chords</h3>
        <div
          className="mt-4"
          style={{
            display: "grid",
            gridTemplateColumns: "1fr 1fr 1fr",
            gridGap: "3px",
            justifyItems: "center",
          }}
        >
          {majorChordUrls.map((url, index) => (
            <div>
              <img src={url} alt={`${index}`} style={{ width: "200px" }} />
              <p className="mt-2 fw-semibold">
                {url.split("/").pop().split(".")[0]}
              </p>
            </div>
          ))}
        </div>
      </div>
      <div className="mt-4">
        <h3 className="fw-bolder">Minor Chords</h3>
        <div
          className="mt-4"
          style={{
            display: "grid",
            gridTemplateColumns: "1fr 1fr 1fr",
            gridGap: "3px",
            justifyItems: "center",
          }}
        >
          {minorChordUrls.map((url, index) => (
            <div>
              <img src={url} alt={`${index}`} style={{ width: "200px" }} />
              <p className="mt-2 fw-semibold">
                {url.split("/").pop().split(".")[0]}
              </p>
            </div>
          ))}
        </div>
      </div>
      <div className="mt-4">
        <h3 className="fw-bolder">7th Chords</h3>
        <div
          className="mt-4"
          style={{
            display: "grid",
            gridTemplateColumns: "1fr 1fr 1fr",
            gridGap: "3px",
            justifyItems: "center",
          }}
        >
          {seventhChordUrls.map((url, index) => (
            <div>
              <img src={url} alt={`${index}`} style={{ width: "200px" }} />
              <p className="mt-2 fw-semibold">
                {url.split("/").pop().split(".")[0]}
              </p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default ChordsLibrary;

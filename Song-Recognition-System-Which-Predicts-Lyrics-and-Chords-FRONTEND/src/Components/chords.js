import React from "react";
// import { useState, useEffect } from "react";

import "./song-details.css";

function ChordsComponent(props) {
  // const lyrics = [
  //   "I found a love,",
  //   "for meDarling,",
  //   "just dive right in and follow my leadWell,",
  //   "I found a girl, beautiful and sweetOh,",
  // ];

  const data = props.Chords;
  return (
    <div className="mt-4 text-center" id="chords">
      <h1>Lyrics & Chords</h1>
      <div className="card px-5 py-5 text-center d-flex align-items-center" style={{ backgroundColor: "#a5ade2" }}>
        {data.map((item, index) => (
          <>
            <h4 class="text-capitalize">[{item.Part}]</h4>
            <p>Chords: {item.Chords}</p>
            <p key={index} className="fw-semibold text-left" style={{width: "250px"}}>
              {item.Lyrics}
            </p>
            <br/>
          </>
        ))} 
      </div>
    </div>
  );
}

export default ChordsComponent;

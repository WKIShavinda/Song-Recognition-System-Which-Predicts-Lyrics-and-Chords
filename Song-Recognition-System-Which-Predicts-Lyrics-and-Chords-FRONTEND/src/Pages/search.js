import React from "react";
import { FaSpinner } from "react-icons/fa";
import "./search-song-details.css";
import ChordsComponent from "../Components/chords";

class Search extends React.Component{
  // const lyrics = [
  //   "I found a love,",
  //   "for meDarling,",
  //   "just dive right in and follow my leadWell,",
  //   "I found a girl, beautiful and sweetOh,",
  // ];
  constructor(props){
    super(props);
    this.state = {
        searchParam: new URLSearchParams(window.location.search).get("query"),
        searchData: undefined,
        searchStatus: "loading",
    };
  }
  componentDidMount(){
    const data ={
      "searchKey": this.state.searchParam,
    };
    fetch(`http://localhost:8080/findSong`,
      {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
          "Content-Type": "application/json",
        },
      }
    ).then(
      res => {
        if(res.status !== 200){
          throw new Error('Failed to fetch data')
        }
        return res.json()
      }).then(data =>{
        this.setState({searchData: data, searchStatus: "success"});
      }).catch(err => {
      console.error('error:' + err);
      this.setState({searchStatus: "error"});
    });
  }

  render(){
    return (
      <div className="conatiner mt-4 mx-4">
        {this.state.searchStatus === "loading" && ( 
          <div className="d-flex justify-content-center align-items-center">
            <FaSpinner className="spinner" />
          </div>
        )}
        {
          this.state.searchStatus === "success" && (
          <div className="mt-4 text-center" id="chords">
            <br/>
            <h1>{this.state.searchData[0]["Song Title"]}</h1>
            <h2>{this.state.searchData[0]["Song Artist"]}</h2>
            <br/>
            <ChordsComponent Chords={this.state.searchData} />
          </div>
        )}
        {this.state.searchStatus === "error" && (
          <div className="d-flex justify-content-center align-items-center">
            <h3>Failed to fetch data</h3>
          </div>
        )}
      </div>
    );
  }
}

export default Search;

import React from "react";
import "./home.css";

import MicRecorder from 'mic-recorder-to-mp3';


import RecordComponent from "../Components/record";
import RecordingComponent from "../Components/recording";
import RecordFailedComponent from "../Components/recording-failed";
import SongDetailsComponent from "../Components/song-details";
import LoadingComponent from "../Components/loading";

class Home extends React.Component {
  Mp3Recorder = new MicRecorder({ bitRate: 128 });

  constructor(props) {
    super(props);
    // const [screenMode, setScreenMode] = useState("start");
    // Audio recorder
    this.recordMode = this.recordMode.bind(this);
    this.sendRecording = this.sendRecording.bind(this);
    this.restartSite = this.restartSite.bind(this);

    this.state = {
      isRecording: false,
      blobURL: '',
      blob: null,
      isBlocked: false,
      isRecordClicked: false,
      screenMode: "start",
      // Response states
      songName: "Song Name",
      trackName: "Track Name",
      artistName: "Artist Name",
      albumName: "Album Name",
      label: "Label",
      releaseYear: "Release Year",
      imageLink: "ImageLink.png",
      videoLink: "link",
      videoThumbnail: "Thumbnail",
      lyrics: null,
    };
  }

  render(){
    return (
      <div className="container">
        {this.state.screenMode==="start" && <RecordComponent recordMode={this.recordMode} />}
        {this.state.screenMode==="listening" && <RecordingComponent /> }
        {this.state.screenMode==="loading" && <LoadingComponent /> }
        {this.state.screenMode==="failed" && <RecordFailedComponent retryFunction={this.restartSite} /> }
        {this.state.screenMode==="successful" && <SongDetailsComponent
          Song={this.state.songName}
          Artist={this.state.artistName}
          Track={this.state.trackName}
          Album={this.state.albumName}
          Label={this.state.label}
          Released={this.state.releaseYear}
          ImageLink={this.state.imageLink}
          VideoLink={this.state.videoLink}
          VideoThumbnail={this.state.videoThumbnail}
          Lyrics={this.state.lyrics}
          Chords={this.state.chords}
          /> }
      </div>
    );
  }

  componentDidMount(){
    navigator.getUserMedia({ audio: true },
      () => {
        console.log('Mic Permission Granted');
        this.setState({ isBlocked: false });
      },
      () => {
        console.log('Mic Permission Denied');
        this.setState({ isBlocked: true })
      },
    );
  }


  // Start recording audio
  startAudio() {
    if (this.state.isBlocked) {
      console.log('Permission Denied');
    } else {
      this.Mp3Recorder
      .start()
        .then(() => {
          this.setState({ isRecording: true });
        }).catch((e) => console.error(e));
      }
    }
    // Stop recording audio
  stopAudio() {
    this.Mp3Recorder
    .stop()
    .getMp3()
    .then(([buffer, blob]) => {
      const blobURL = URL.createObjectURL(blob)
      this.setState({ blob, blobURL, isRecording: false }, () => {
        console.log(this.state.blobURL);
        console.log(this.state.blob);
        this.sendRecording();
      });
    }).catch((e) => console.log(e));
  }
  // Change to recording mode
  recordMode(){
    let record_duration = 7000; 
    this.setState({screenMode: "listening"});
    this.startAudio();
    setTimeout(async () => { // Stops Audio record after 7 seconds
      await this.stopAudio()
    }, record_duration);
  }
  // Send file to API
  sendRecording(){
    this.setState({screenMode: "loading"});
    const data = new FormData();
    data.append("upload_file", this.state.blob, 'recording.mp3');
    fetch(`http://localhost:8080/song`, 
      {
        body: data,
        method: "POST",
      }
    ).then(
      res => {
        if(res.status !== 200){
          throw new Error('Failed to fetch data')
        }
        return res.json()
      }).then(data =>{
        if(data.status === "success"){
          // set data
          this.setState(
            {
              songName: data.song_name,
              trackName: data.song_name,
              artistName: data.artist,
              albumName: data.album,
              label: data.label,
              releaseYear: data.release_year,
              imageLink: data.img_link,
              lyrics: data.lyrics,
              chords: data.chords,
              videoLink: data.vid_link,
              videoThumbnail: data.vid_thumbnail,
            }, ()=>{
              // swap component
              this.setState({screenMode: "successful"}, () => {
                document.getElementsByClassName("album-art-image")[0].src = this.state.imageLink;
                document.getElementsByClassName("video-thumbnail")[0].src = this.state.videoThumbnail;
                document.getElementsByClassName("video-link")[0].href = this.state.videoLink;
              })
            }
            )
        } else{
          this.setState({screenMode: "failed"})
        }
      }).catch(err => {
      console.error('error:' + err)
      this.setState({screenMode: "failed"})
    });
  }
  // Restart the site
  restartSite(){
    // reset all the states
    this.setState({
      isRecording: false,
      blobURL: '',
      isBlocked: false,
      isRecordClicked: false,
      screenMode: "start",
      // Response states
      songName: "Song Name",
      trackName: "Track Name",
      artistName: "Artist Name",
      albumName: "Album Name",
      label: "Label",
      releaseYear: "Release Year",
      imageLink: "Image Link",
      videoLink: "link",
      videoThumbnail: "Thumbnail",
      lyrics: null,
      chords: null,
    });
  }

}

export default Home;

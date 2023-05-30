package com.fyp.backend.Controller;

import com.fyp.backend.Service.FetchService;
import com.fyp.backend.model.SearchKey;
import com.fyp.backend.model.Song;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@EnableAutoConfiguration
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SongController {

    private final RestTemplate restTemplate;
    @Autowired
    FetchService fetchService;


    @Autowired
    public SongController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping(path = "/song", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> recognizeSong(@RequestParam("upload_file") MultipartFile file) {
        // Song recognization request
        String url = "https://shazam-core.p.rapidapi.com/v1/tracks/recognize";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("X-RapidAPI-Host", "shazam-core.p.rapidapi.com");
        headers.add("X-RapidAPI-Key", "261695cbbfmshf99df7837066497p1f59f3jsna9e5a1181b06");
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            // Handle error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read file data");
        }
        ByteArrayResource fileResource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        HttpEntity<MultiValueMap<String, Object>> entity1 = new HttpEntity<>(body, headers);

        // Song recognition response
        ResponseEntity<String> response1 = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity1,
                String.class);

        // Song youtube url request
        ResponseEntity<String> response2;

        JSONObject resp1json = new JSONObject(response1.getBody());
        JSONObject mergeResp = new JSONObject();
        // Checks if recognized
        if (!resp1json.has("track")) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String track_id = (String) (resp1json.getJSONObject("track")).get("key");
        String track_name = (String) (resp1json.getJSONObject("track")).get("title");

        url = "https://shazam-core.p.rapidapi.com/v1/tracks/youtube-video?name=" + track_name + "&track_id=" + track_id;
        headers.setContentType(null);
        HttpEntity<Resource> entity2 = new HttpEntity<>(null, headers);
        // Song youtube url response
        response2 = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity2,
                String.class
        );
        List<Map<String, Object>> searchResults = fetchService.executeRequest(track_name);

        JSONObject resp2json = new JSONObject(response2.getBody());
        // Response creator
        mergeResp.put("status", "success");
        mergeResp.put("song_name", (String) (resp1json.getJSONObject("track")).get("title"));
        mergeResp.put("artist", (String) (resp1json.getJSONObject("track")).get("subtitle"));
        mergeResp.put("album", (((((resp1json.getJSONObject("track")).getJSONArray("sections")).getJSONObject(0)).getJSONArray("metadata")).getJSONObject(0)).get("text"));
        mergeResp.put("label", (((((resp1json.getJSONObject("track")).getJSONArray("sections")).getJSONObject(0)).getJSONArray("metadata")).getJSONObject(1)).get("text"));
        mergeResp.put("release_year", (((((resp1json.getJSONObject("track")).getJSONArray("sections")).getJSONObject(0)).getJSONArray("metadata")).getJSONObject(2)).get("text"));
        mergeResp.put("img_link", (((((resp1json.getJSONObject("track")).getJSONArray("sections")).getJSONObject(0)).getJSONArray("metapages")).getJSONObject(1)).get("image"));
        mergeResp.put("vid_thumbnail", (resp2json.getJSONObject("image")).get("url"));
        mergeResp.put("vid_link", ((resp2json.getJSONArray("actions")).getJSONObject(0)).get("uri"));
        boolean is_lyrics_available = Objects.equals((String) (((resp1json.getJSONObject("track")).getJSONArray("sections")).getJSONObject(1)).get("type"), "LYRICS");
        if (is_lyrics_available) {
            mergeResp.put("lyrics", (((resp1json.getJSONObject("track")).getJSONArray("sections")).getJSONObject(1)).get("text"));
        }
        if (searchResults != null) {
            mergeResp.put("chords", searchResults);
        }
        System.out.println(mergeResp.toString());
        System.out.println(track_name);
        System.out.println(searchResults);
        return ResponseEntity.ok(mergeResp.toString());
    }


    @PostMapping(path = "/findSong")
    public ResponseEntity<List<Map<String, Object>>> findSong(@RequestBody @NotNull SearchKey searchKey) {

        List<Map<String, Object>> response = fetchService.executeRequest(searchKey.getSearchKey());
        return ResponseEntity.ok(response);
    }



}
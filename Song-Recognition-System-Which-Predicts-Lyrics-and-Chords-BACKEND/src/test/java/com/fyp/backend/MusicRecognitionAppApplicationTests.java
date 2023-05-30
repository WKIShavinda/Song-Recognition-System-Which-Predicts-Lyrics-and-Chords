package com.fyp.backend;

import com.fyp.backend.Controller.SongController;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
@AutoConfigureMockMvc
@SpringBootTest
public class
MusicRecognitionAppApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void testRecognizeSong() throws Exception {
		MockMultipartFile upload_file = new MockMultipartFile("/C:/Users/shavi/Desktop/whatsapp.mp3", "upload_file", MediaType.MULTIPART_FORM_DATA_VALUE, "test data".getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart("/song")
						.file(upload_file)
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
	}

	@Test
	void testFindSong() throws Exception {
		String searchKey = "song";

		mockMvc.perform(MockMvcRequestBuilders.post("/findSong")
						.content(searchKey)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))

				.andDo(MockMvcResultHandlers.print());
	}


	@Test
	void contextLoads() {

	}

}




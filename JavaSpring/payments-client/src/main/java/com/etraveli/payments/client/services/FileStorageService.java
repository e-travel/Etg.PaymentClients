package com.etraveli.payments.client.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FileStorageService {
	private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

	public synchronized <T extends Object> T loadData(String filename) {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<T> typeRef = new TypeReference<T>() {
		};
		Path dataFilePath = Paths
				.get(System.getProperty("user.home"), "payments_client", filename).toAbsolutePath();

		if (!dataFilePath.toFile().exists())
			return null;

		String jsonValue = null;
		try {
			jsonValue = new String(Files.readAllBytes(dataFilePath));
		} catch (IOException ex) {
			jsonValue = null;
			logger.error("Exception wile reading {}", dataFilePath);
			logger.error(ex.getMessage(), ex);
		}

		T result;
		if (jsonValue == null) {
			result = null;
		} else {
			try {
				result = mapper.readValue(jsonValue, typeRef);
			} catch (Exception e) {
				result = null;
				logger.error("Exception wile deserialize {} {}", dataFilePath , jsonValue);
				logger.error(e.getMessage(), e);
			}
		}
		return result;
	}

	public synchronized <T extends Object> void saveData(String filename, T data) {
		if (data == null)
			return;

		ObjectMapper mapper = new ObjectMapper();
		String jsonValue;
		try {
			jsonValue = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			logger.error("Error while serialize as json the provided {}. Error: {}", data,
					e.getMessage());
			logger.error(e.getMessage(), e);
			return;
		}

		Path dataFilePath = Paths
				.get(System.getProperty("user.home"), "payments_client", filename).toAbsolutePath();

		try {
			if (!dataFilePath.toFile().exists()) {
				
				Files.createDirectories(dataFilePath.getParent());

				Files.createFile(dataFilePath);

			}else {
				Files.delete(dataFilePath);
				Files.createFile(dataFilePath);
			}

			Files.write(dataFilePath, jsonValue.getBytes());
			
		} catch (IOException e) {
			logger.error("Error wile saving to path: {}", dataFilePath.toUri());
			logger.error(e.getMessage(),e);
		}
	}
}

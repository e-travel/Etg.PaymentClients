package com.etraveli.payments.client.services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileStorageService {
	private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
	
	public synchronized Map<String, List<String>> loadGatewaysPerCurrency(){
		
		Path homeFolderPath = Paths
				.get(System.getProperty("user.home"),"payments_client","gateways_per_currency.json")
				.toAbsolutePath();
		
		
		logger.debug("Home folder is: {}", homeFolderPath.toString());
		
		return new HashMap<>();
	}

}

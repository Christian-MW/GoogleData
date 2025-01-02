package GoogleData.sheet.service;

import org.springframework.http.ResponseEntity;

import GoogleData.sheet.dto.request.processBasecamAPIRequest;

public interface BasecampAPIService {
	ResponseEntity<?> ProcessData (processBasecamAPIRequest request);
}

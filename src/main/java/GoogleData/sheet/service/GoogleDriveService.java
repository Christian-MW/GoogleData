package GoogleData.sheet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.SaveFileDriveRequest;

@Component
public interface GoogleDriveService {
	ResponseEntity<?> saveFile(SaveFileDriveRequest request);
	ResponseEntity<?> saveDatafile(SaveFileDriveRequest request);
}

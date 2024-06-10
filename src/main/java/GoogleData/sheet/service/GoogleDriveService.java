package GoogleData.sheet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.SaveFileDriveRequest;

@Component
public interface GoogleDriveService {
	ResponseEntity<?> fileUpload(SaveFileDriveRequest request);
	ResponseEntity<?> saveDatafile(SaveFileDriveRequest request);
}

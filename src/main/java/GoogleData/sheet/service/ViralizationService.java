package GoogleData.sheet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.GetDataAssignamentRequest;
import GoogleData.sheet.dto.request.SaveDataAssignamentRequest;
import GoogleData.sheet.dto.request.UpdatePostMessageRequest;
import GoogleData.sheet.dto.request.VerifyDataBinnacleRequest;
import GoogleData.sheet.dto.request.ViralizationRequest;
import GoogleData.sheet.dto.request.ViralizationUpdatePostRequest;

@Component
public interface ViralizationService {
	ResponseEntity<?> saveBinnacle (ViralizationRequest request);
	ResponseEntity<?> verifyBinnacle (VerifyDataBinnacleRequest request);
	ResponseEntity<?> updatePost (ViralizationUpdatePostRequest request);
	ResponseEntity<?> getDataAssignament (GetDataAssignamentRequest request);
	ResponseEntity<?> saveDataAssignament (SaveDataAssignamentRequest request);
	ResponseEntity<?> updateDataAssignament(SaveDataAssignamentRequest request);
	ResponseEntity<?> updatePostMessage(UpdatePostMessageRequest request);
}

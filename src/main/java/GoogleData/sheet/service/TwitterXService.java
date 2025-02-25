package GoogleData.sheet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.ConfigGoogleRequest;
import GoogleData.sheet.dto.request.GetTweetsRequest;
import GoogleData.sheet.dto.request.UpdateTweetRequest;

@Component
public interface TwitterXService {
	ResponseEntity<?> saveConfiguration(ConfigGoogleRequest request);
	ResponseEntity<?> GetTweets(GetTweetsRequest request);
	ResponseEntity<?> UpdatePost(UpdateTweetRequest request);
}

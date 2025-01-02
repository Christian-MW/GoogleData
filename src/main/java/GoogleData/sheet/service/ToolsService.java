package GoogleData.sheet.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;

@Component
public interface ToolsService {
	SendMessageResponse sendMessage(SendMessageRequest request);
	AIResponse sendChatgpt(AIRequest request);
	ResponseProcessLinesChatGPT procesLines(RequestProcessLinesChatGPT request);
	SendMessageChatGPTResponse processMessageChatgpt(SendMessageChatGPTRequest request);
	ResponseEntity<?> saveTeams(SaveTeamsRequest request);
}

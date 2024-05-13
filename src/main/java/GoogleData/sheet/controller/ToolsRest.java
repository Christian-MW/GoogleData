package GoogleData.sheet.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;
import GoogleData.sheet.service.ToolsService;

@RestController
@RequestMapping(value="/Tools")
public class ToolsRest {

	private static Logger log = Logger.getLogger(GoogleRest.class);
	@Autowired
	ToolsService toolsService;
	
	
	@PostMapping(value="/sendMessage", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SendMessageResponse test(@RequestBody SendMessageRequest request) {
		return toolsService.sendMessage(request);
   }
	
	@PostMapping(value="/sendChatgpt", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AIResponse test(@RequestBody AIRequest request) {
		return toolsService.sendChatgpt(request);
   }
	
	@PostMapping(value="/process/lineschatgpt", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseProcessLinesChatGPT processLines(@RequestBody RequestProcessLinesChatGPT request) {
		return toolsService.procesLines(request);
   }
	
	@CrossOrigin(origins = "*")
	@PostMapping(value="/process/messageChatgpt", 
	consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SendMessageChatGPTResponse processLines(@RequestBody SendMessageChatGPTRequest request) {
		return toolsService.processMessageChatgpt(request);
   }
}

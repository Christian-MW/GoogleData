package GoogleData.sheet.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import GoogleData.sheet.model.DiscursiveLinesM;
import GoogleData.sheet.model.DiscursiveLinesModel;
import GoogleData.sheet.dto.request.*;
import GoogleData.sheet.dto.response.*;
import GoogleData.sheet.model.TelegramSendModel;
import GoogleData.sheet.service.ToolsService;
import GoogleData.sheet.utils.Utilities;

@Service("ToolsImpl")
public class ToolsImpl implements ToolsService {
	private static Logger log = Logger.getLogger(ToolsImpl.class);
	
     @Value("${file.headers.tweets}")
     private String HEADERS_TWEETS;
     @Value("${file.headers.tweets.sheet}")
     private String SHEET_TWEETS;
	 @Autowired
	 Utilities utilities;
	 @Autowired
	 GoogleImpl googleImpl;
	 

	public SendMessageResponse sendMessage(SendMessageRequest request) {
		log.info("###################################");
		log.info("##########__sendMessage__####$$$$##");
		log.info("###################################");
    	Gson gson = new Gson();
    	log.info(gson.toJson("====> Request__sendMessage: " + request));
		SendMessageResponse result = new SendMessageResponse();
		SendResponse response = new SendResponse("200", "OK", false, null);
		try {
			
			TelegramSendModel telegram = new TelegramSendModel();
			telegram.setCaption(request.getCaption());
			telegram.setDescription(request.getDescription());
			telegram.setText(request.getText());
			telegram.setTo(request.getTo());
			telegram.setToken(request.getToken());
			telegram.setFrom(request.getFrom());
			telegram.setUrl(request.getUrl());
			telegram.setUrl_thumb(request.getUrlThumb());
			telegram.setReplica(request.getReplica());
			
			response = utilities.sendTelegram(telegram, request.getType());
			
			return result;
		} catch (Exception e) {
			return result;
		}
	}

	public AIResponse sendChatgpt(AIRequest request) {
		log.info("############################################");
		log.info("##########__PROCESAR CON CHAT-GPT__#########");
		log.info("############################################");
		log.info("==> Type Message ChatGPT: " + request.getTypeMessage());
		log.info("==> Message ChatGPT: " + request.getMessageCHATGPT());
		log.info("==> Prompt: " + request.getPrompt());
		AIResponse result = new AIResponse();
		try {
			// ###########_TIPOS DE MENSAJE CHATGPT
			// ###########_line = obtiene los tweets del archivo y los manda para obtener la línea discursiva
			// ########_message = un mensaje normal para obtener respuesta
			// ####_Obtener tweets del archivo
			/*SheetResponse restGet = googleImpl.getDataSheetByFilter("COLUMNS", request.getSpreadsheet_id(),
					SHEET_TWEETS.toLowerCase().trim());
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(),
					SHEET_TWEETS.toLowerCase().trim());*/
			
			//utilities.GenerateImage("Un hermoso bebé");
			//utilities.GetModelsChatGPT();
			if (request.getTypeMessage().equals("line")) {
				List<String> dataFile = googleImpl.getDataByColumn(request.getSpreadsheet_id(), "Tweets",SHEET_TWEETS);
				String resFil = dataFile.get(dataFile.size()-1);
				if (resFil.equals("200")) {
					String data = request.getPrompt() + " \n";
					for (Integer i = 0; i < dataFile.size(); i++) {
						if(!dataFile.get(i).equals("200")){
							data += dataFile.get(i).toString() + "\n";
						}
					}
					result = utilities.sendTextChatGPT3Turbo(data);
					String [] listLines= result.getMessageChatGTP().split("\n");
					//result = utilities.sendTextChatGPT(data);
					log.info("==> RESULT CHATGPT: " + new Gson().toJson(result));
				}
				else
					return result;
				
			}
			else {
				result = utilities.sendTextChatGPT3Turbo(request.getMessageCHATGPT());
				//result = utilities.sendTextChatGPT(request.getMessageCHATGPT());
			}

			return result;
		} catch (Exception e) {
			return result;
		}
	}

	public ResponseProcessLinesChatGPT procesLines (RequestProcessLinesChatGPT request) {
		log.info("#########################################################");
		log.info("##################___--procesLines--___##################");
		log.info("#########################################################");
		ResponseProcessLinesChatGPT result = new ResponseProcessLinesChatGPT();
		AIResponse resAI = new AIResponse();
		List<DiscursiveLinesM> allLines = new ArrayList<DiscursiveLinesM>();
		try {
			 for (Entry<String, List<Map<String, Object>>> Objuser: request.getAccounts().entrySet()) {
				 String[] promptLines = request.getPrompt().split("\\|");
				 
				 for (Integer h=0; h<promptLines.length; h++) {
					 //String data = "Redacta las tres líneas discursivas más destacadas de los siguientes tweets " + " \n";//request.getPrompt() + " \n";
					 String data = promptLines[h] + " \n";;
					 
						List<DiscursiveLinesModel> listLinesB = new ArrayList<DiscursiveLinesModel>();
						DiscursiveLinesM linesByAccount = new DiscursiveLinesM();
						linesByAccount.setAccount(Objuser.getKey().toString());
						//String userAcount = Objuser.getKey();
						List<Map<String, Object>> listTweets = Objuser.getValue();
						System.out.println("");
						for (Map<String, Object> item : listTweets) {
							System.out.println("");
							data += item.get("hitSentence").toString() + "\n";
						}
						resAI = utilities.sendTextChatGPT3Turbo(data);
						String [] listLines= resAI.getMessageChatGTP().split("\n");
						for(Integer l=0; l<listLines.length; l++) {
							DiscursiveLinesModel lines = new DiscursiveLinesModel();
							lines.setTitle("");
							lines.setType("Positivas");
							lines.setValue(listLines[l].toString());
							listLinesB.add(lines);
							
						}
						linesByAccount.setLines(listLinesB);
						allLines.add(linesByAccount);
						
						log.info("==> RESULT CHATGPT: " + new Gson().toJson(allLines));
						Thread.sleep(500);
				}

			}
			 result.setCode(200);
			 result.setMessage("OK");
			 result.setLines(allLines);
		    Gson gson = new Gson();
			System.out.println(gson.toJson("====> RESULT: " + result));
			return result;
		} catch (Exception ex) {
			log.error("##################___ERROR--procesLines___##################");
			log.error(ex.getMessage());
			result.setCode(500);
			return result;
		}
	}
	
	public SendMessageChatGPTResponse processMessageChatgpt(SendMessageChatGPTRequest request) {
		log.info("############################################################");
		log.info("##############____processMessageChatgpt____#################");
		log.info("############################################################");
		log.info("=>Request-processMessageChatgpt: " + new Gson().toJson(request));
		SendMessageChatGPTResponse result = new SendMessageChatGPTResponse();
		try {
			AIResponse resultMessage = new AIResponse();
			String data = request.getPrompt() + " \n" + request.getMessage();
			//resultMessage = utilities.sendTextChatGPT(data);
			resultMessage = utilities.sendTextChatGPT3Turbo(data);
			
			if(resultMessage.getCode() == 200) {
				result.setMessageProcessed(resultMessage.getMessageChatGTP());
				result.setCode(200);
				result.setMessage("OK");
			}
			else {
				result.setMessageProcessed(resultMessage.getMessageChatGTP());
				result.setCode(resultMessage.getCode());
				result.setMessage("ERROR");
			}

			return result;
		} catch (Exception ex) {
			log.error("#####################___ERROR__processMessageChatgpt__#################");
			log.error(ex.getMessage());
			result.setCode(500);
			result.setMessage(ex.getMessage());
			return result;
		}
	}
}

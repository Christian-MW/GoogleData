package GoogleData.sheet.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.Sheets;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import GoogleData.sheet.config.GoogleAuthorizationConfig;
import GoogleData.sheet.dto.response.SendResponse;
import GoogleData.sheet.dto.response.AIResponse;
import GoogleData.sheet.dto.response.AutomatizationResponse;
import GoogleData.sheet.dto.response.ResponseBase;
import GoogleData.sheet.impl.CampaignImpl;
import GoogleData.sheet.model.*;

@Component
public class Utilities {
    @Value("${credentials.file.path}")
    private String credentialsFilePath;
    @Value("${credentials.file.path}")
    private static String credentialsFilePathStatic;
    @Autowired
    private static GoogleAuthorizationConfig googleAuthorizationConfig;
    @Value("${application.name}")
    private String APPLICATIONNAME;
    @Value("${application.name}")
    private static String APPLICATIONNAME_STATIC;
    /*@Value("${url.add.json.stream}")
    private String URL_ADD_JSON_STREAM;
    @Value("${url.add.stream}")
    private String URL_ADD_STREAM;
    @Value("${url.get.users.stream}")
    private String URL_GET_USERS_STREAM;
    @Value("${url.scrapper.melt}")
    private String URL_SCRAPPER_MELT;*/
    @Value("${url.save.users.campaign}")
    private String URL_USERS_SAVE;
    @Value("${url.base.stream}")
    private String URL_BASE_STREAM;
    @Value("${url.chatgpt.v1}")
    private String URL_CHATGPT_V1;
    @Value("${token.chatgpt}")
    private String TOKEN_CHATGPT;
	@Value("${api.telegram}")
	String apiTelegram;
	@Value("${telegram.sendchat}")
	String telegramSendchat;
	@Value("${telegram.bot}")
	String telegramBot;
	@Autowired
	private CodesHtml codesHtml;
    private static Logger log = Logger.getLogger(CampaignImpl.class);
    
	public String numToLetter(Integer num) {
		String letter = "";
		HashMap<Integer,String>map=new HashMap<>();
		try {
			//System.out.println("Start numToLetter: " + System.currentTimeMillis() / 1000);
			Integer pos = 0;
			String AlphabetS=" ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			char[] charArrS = AlphabetS.toCharArray();
			String Alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			char[] charArr = Alphabet.toCharArray();
			String Letter = "";
			String Letter2 = "";
			String Letter3 = "";
			
			for (pos = 0; pos <= charArr.length; pos++) {
				Letter = String.valueOf(charArrS[pos]);
				map.put(pos,Letter);
			}
			
			for (Integer i = 0; i < charArr.length; i++) {
				Letter = String.valueOf(charArr[i]);
				for (Integer i2 = 0; i2 < charArr.length; i2++) {
					Letter2 = String.valueOf(charArr[i2]);
					map.put(pos++,Letter+Letter2);
				}
			}
			
			for (Integer i = 0; i < charArr.length; i++) {
				Letter = String.valueOf(charArr[i]);
				for (Integer i2 = 0; i2 < charArr.length; i2++) {
					Letter2 = String.valueOf(charArr[i2]);
					for (Integer i3 = 0; i3 < charArr.length; i3++) {
						Letter3 = String.valueOf(charArr[i3]);
						map.put(pos++,Letter+Letter2+Letter3);
					}
				}
			}
			
		    System.out.println(map.get(num)+" ");
		    letter = map.get(num);
		    //System.out.println("END numToLetter: " + System.currentTimeMillis() / 1000);
		    return letter;
		} catch (Exception ex) {
			return letter;
		}
	}
	
	public String getnumToLetter(Integer number) {
		//System.out.println("Start getnumToLetter: " + System.currentTimeMillis() / 1000);
        StringBuilder columnName = new StringBuilder();
        
        while (number > 0) {
            number--; // Ajustar a índice cero
            int remainder = number % 26;
            columnName.append((char) (remainder + 'A'));
            number = number / 26;
        }
        //System.out.println(columnName.reverse().toString());
        //System.out.println("end getnumToLetter: " + System.currentTimeMillis() / 1000);
        return columnName.reverse().toString();
    }
	
	public Sheets getServiceSheet() {
		try {
			//log.info("#########_Current Working Directory is = " +this.getClass().getClassLoader().getResource("").getPath() + credentialsFilePath);
			
	        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
	        Sheets service = new Sheets.Builder(httpTransport, jsonFactory, 
	        		googleAuthorizationConfig.getCredentialsServiceAccount(httpTransport, jsonFactory, credentialsFilePath))
	        		.setApplicationName(APPLICATIONNAME).build();
	        
	        return service;
		} catch (Exception e) {
			System.out.println("=> ERROR__getServiceSheet__: " + e.getMessage());
			return new Sheets(null, null, null);
		}
	}
	
	public Drive getServiceDrive() {
		try {
	        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
	        Credential credential = googleAuthorizationConfig.getCredentialsDrive(HTTP_TRANSPORT, jsonFactory, credentialsFilePath);
	        return new Drive.Builder(HTTP_TRANSPORT, jsonFactory, credential)
	                .setApplicationName(APPLICATIONNAME)
	                .build();
		} catch (Exception e) {
			System.out.println("=> ERROR__getServiceSheet__: " + e.getMessage());
			return null;
		}
	}
	
	public Boolean sendItemStream(CampaignStreamModel req, String type) {
		
		Boolean result = false;
		Gson gson = new Gson();
		try {
			String urlSend ="";
			if (type.equals("json"))
				urlSend = URL_BASE_STREAM + "/search/add";
			else if (type.equals("search"))
				urlSend = URL_BASE_STREAM + "/rules/add";
			
			var request = HttpRequest.newBuilder()
					.uri(URI.create(urlSend))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(req)))
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____sendItemStream: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			
			log.info("########========> RESPONSE---sendItemStream__" + gson.toJson(map));
			return result;
		} catch (Exception ex) {
			log.error("#####__ERROR AL AGREGAR O ACTUALIZAR EL STREAM##");
			log.error(ex.getMessage());
			return result;
		}
	}
	
	public void sendUsersProcessAverage(List<String> users, String sheetID) {
		log.info("#########################################");
		log.info("#####___sendUsersProcessAverage___#######");
		log.info("#########################################");
		Gson gson = new Gson();
		try {
			ReachScrapperModel req = new ReachScrapperModel();
			req.setAccount(users);
			req.setSpreadsheetId(sheetID);
			System.out.println("req: " + new Gson().toJson(req));
			
			String path = URL_BASE_STREAM + "/scrapper/meltwater/api/process/average";
			var request = HttpRequest.newBuilder()
					.uri(URI.create(path))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(req)))
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____sendUsersProcessAverage: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			log.info("########========> RESPONSE---sendUsersProcessAverage__" + gson.toJson(map));
			
			/*if (response.statusCode() == 200)
				return true;
			else 
				return false;*/
			
			
		} catch (Exception e) {
			log.error("#####__ERROR___sendUsersProcessAverage___#######");
			log.error(e.getMessage());
		}
	}
	
	public boolean saveUsersDB(List<UsersDB> users) {
		Boolean result = false;
		log.info("#####################################");
		log.info("##########__SAVE_USERS__#############");
		try {
			Gson gson = new Gson();
			log.info("USERS: " + new Gson().toJson(users));
			System.out.println("USERS: " + new Gson().toJson(users));
			String path = URL_USERS_SAVE + "account/add";
			var request = HttpRequest.newBuilder()
					.uri(URI.create(path))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(users)))
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____saveUsersDB: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			log.info("########========> RESPONSE---saveUsersDB__" + gson.toJson(map));
			
			if (response.statusCode() == 200)
				return true;
			else 
				return false;
			
			
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean saveUsersRankBD(AccountsRankingModel users) {
		log.info("#####################################");
		log.info("##########__SAVE_USERS__RANKING__DB__#############");
		try {
			Gson gson = new Gson();
			String path = URL_USERS_SAVE + "ranking/add";
			var request = HttpRequest.newBuilder()
					.uri(URI.create(path))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(users)))
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____saveUsersRANKING__DB: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			log.info("########========> RESPONSE---saveUsersDB__" + gson.toJson(map));
			
			if (response.statusCode() == 200)
				return true;
			else 
				return false;
			
		} catch (Exception ex) {
			log.error("#################################");
			log.error("#####__SAVE_USERS__RANKING__DB__######");
			log.error(ex.getMessage());
			return false;
		}
	}
	
	public void getJSONusers(String campaign) {
		try {
			CampaignStreamModel request = new CampaignStreamModel();
			request.setTheme(campaign);
		    var req = HttpRequest.newBuilder()
					.uri(URI.create(URL_BASE_STREAM + "/rules/get"))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(request)))			
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(req, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____sendItemStream: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			
		} catch (Exception ex) {
			log.error("#####__ERROR AL OBTENER LOS USUARIOS DEL STREAM##");
			log.error(ex.getMessage());
		}
	}

	public void createFile(String url, String nameFile, String content) {
		log.info("####Crear archivo de usuarios####");
		try {
			Gson gson = new Gson();
            String ruta = "../" + nameFile + ".json";
            File file = new File(ruta);
            Boolean isnew = false;
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
                isnew = true;
            }
            
            
            String fichero = "";
            
            try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    fichero += linea;
                }
             
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            
            Properties properties = gson.fromJson(fichero, Properties.class);
            System.out.println(properties.get("date_start"));
            
            
            
            
            //Leer archivo 
            FileReader fr = new FileReader (file);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            String conFile = "";
            while((linea=br.readLine())!=null)
            	conFile += linea;	
            
            if(!isnew)
            	conFile += "," + content;
            else
            	conFile = content;
            /*List<Object> itemsJSON = new ArrayList<Object>();
            Object object = new Object();
            if (!conFile.isBlank() || !conFile.isEmpty()) {
            	object = conFile;
            	itemsJSON.add(object);
			}
            Object objectNew = content;
            itemsJSON.add(objectNew);*/
            
            //Almacenar contenido en el archivo
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(new Gson().toJson(conFile));
            bw.close();
			
		} catch (Exception ex) {
			log.info("#####__ERROR AL CREAR EL ARCHIVO");
		}
	}

	public List<Object> getUsersInfoStream(List<String> accounts) {
		try {
			//accounts = new ArrayList<String>();
			//accounts.add("@Barbara_Jacob");
			
			GetInfoAccountsModel request = new GetInfoAccountsModel();
			Gson gson = new Gson();
			request.setType("busqueda");
			request.setUsers(accounts);
			
		    var req = HttpRequest.newBuilder()
					.uri(URI.create(URL_BASE_STREAM + "/search/twarc/v2/account"))
					.header("Content-type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(request)))			
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(req, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____sendItemStream: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			
			String code = map.get("code").toString();
			if (map.get("code").toString().equals("200")) {
				List<Object> users =  (List<Object>) map.get("result");
				
				return users;
			}
			else {
				log.info("error");
				return null;
			}
			
		} catch (Exception ex) {
			log.error("#############_____ERROR__getUsersInfoStream##############");
			log.error(ex.getMessage());
			return null;
		}
	}
	public void saveUsersDBLocal(List<UsersRankingModel> users) {
		log.info("################################################");
		log.info("#####__GUARDAR USUARIOS EN BASE DE DATOS___#####");
		log.info("################################################");
		
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public String cleanNameSheet(String sheetName) {
		String result = "";
		try {
			return sheetName.replaceAll("[áÁ]", "a")
                    .replaceAll("[éÉ]", "e")
                    .replaceAll("[íÍ]", "i")
                    .replaceAll("[óÓ]", "o")
                    .replaceAll("[úÚ]", "u");
		} catch (Exception ex) {
			return result;
		}
	}
	
	public ResponseEntity<?> getResponseEntity(Map<String, Object> map){
		log.info("getResponseEntity.....");
		ResponseEntity<Object> response = new ResponseEntity<Object>(map,HttpStatus.OK);
		log.info("Procesando el code...");
		int code = (int) map.get("code");
		if(code == 500)
			response = new ResponseEntity<Object>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		if(code == 204)
			response = new ResponseEntity<Object>(map,HttpStatus.NO_CONTENT);
		if(code == 201)
			response = new ResponseEntity<Object>(map,HttpStatus.CREATED);
		if(code == 409)
			response = new ResponseEntity<Object>(map,HttpStatus.CONFLICT);
		if(code == 401)
			response = new ResponseEntity<Object>(map,HttpStatus.UNAUTHORIZED);
		if(code == 404)
			response = new ResponseEntity<Object>(map,HttpStatus.NOT_FOUND);
		if(code == 402)
			response = new ResponseEntity<Object>(map,HttpStatus.UNAUTHORIZED);
		if(code == 411)
			response = new ResponseEntity<Object>(map,HttpStatus.LENGTH_REQUIRED);
		
		
		return response;
	}
	public String readFile(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			byte[] buffer = new byte[10];
			StringBuilder sb = new StringBuilder();
			while (fis.read(buffer) != -1) {
				sb.append(new String(buffer));
				buffer = new byte[10];
			}
			fis.close();

			String content = sb.toString();
			return content;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
 	public String convertDate (String date) {
		String result="";
		try {
			String[] arrDate = date.split("-");
			String month = "";
			switch (arrDate[1]) {
			case "1":
			case "01":
				month = "Enero";
				break;
			case "2":
			case "02":
				month = "Febrero";
				break;
			case "3":
			case "03":
				month = "Marzo";
				break;
			case "4":
			case "04":
				month = "Abril";
				break;
			case "5":
			case "05":
				month = "Mayo";
				break;
			case "6":
			case "06":
				month = "Junio";
				break;
			case "7":
			case "07":
				month = "Julio";
				break;
			case "8":
			case "08":
				month = "Agosto";
				break;
			case "9":
			case "09":
				month = "Septiembre";
				break;
			case "10":
				month = "Octubre";
				break;
			case "11":
				month = "Noviembre";
				break;
			case "12":
				month = "Diciembre";
				break;
			default:
				break;
			}
			return arrDate[2]+" " + month;
		} catch (Exception ex) {
			log.error("PROBLEMAS AL CONVERTIR LA FECHA " + date);
			log.error(ex.getMessage());
			return result;
		}
	}
 	//Obtener fecha en formato dd-mm-yyyy
 	public String getDateString() {
 		try {
 			java.util.Date fechaActual = new java.util.Date();
 			java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat("dd-MM-yyyy");
 	        String fechaFormateada = formatoFecha.format(fechaActual);
 			return fechaFormateada;
		} catch (Exception e) {
			log.error("===>Problemas al formatear la fecha");
			log.error(e.getMessage());
			return "";
		}
 	}
 	public String getMonth(String date) {

		try {
			String[] arrDate = date.split("/");
			switch (arrDate[1]) {
				case "1":
				case "01":
					return "Enero";
				case "2":
				case "02":
					return "Febrero";
				case "3":
				case "03":
					return "Marzo";
				case "4":
				case "04":
					return "Abril";
				case "5":
				case "05":
					return "Mayo";
				case "6":
				case "06":
					return "Junio";
				case "7":
				case "07":
					return "Julio";
				case "8":
				case "08":
					return "Agosto";
				case "9":
				case "09":
					return "Septiembre";
				case "10":
					return "Octubre";
				case "11":
					return "Noviembre";
				case "12":
					return "Diciembre";
				default:
					return "";
			}
		} catch (Exception ex) {
			return "";
		}
	}
	public String utfToUnicode(String text, String typeCode) {
		try {
			//Caracteres especiales
			if(typeCode=="unicode") {
				for (CodesHtmlCode code : codesHtml.codesUnicode) {
					text = text.replaceAll(code.getCharacter(),code.getCode() );
				}
			}else {
				for (CodesHtmlCode code : codesHtml.codesHtml) {
					text = text.replaceAll(code.getCharacter(),code.getCode() );
				}
			}
		    return text;
		} catch (Exception e) {
			return text;
		}
	}
	// Método para transponer la lista de listas
	public List<List<Object>> transpose(List<List<Object>> values) {
	    List<List<Object>> transposed = new ArrayList<>();
	    if (values.isEmpty()) {
	        return transposed;
	    }

	    int numRows = values.size();
	    int numCols = values.get(0).size();

	    for (int col = 0; col < numCols; col++) {
	        List<Object> newRow = new ArrayList<>();
	        for (int row = 0; row < numRows; row++) {
	            newRow.add(values.get(row).get(col));
	        }
	        transposed.add(newRow);
	    }
	    return transposed;
	}
	
	//###########CHAT-GPT
	public void GetModelsChatGPT() {
		
		log.info("######___OBTENER MODELOS DE CHAT-GPT___#######");
		try {
			String url = URL_CHATGPT_V1 + "/models";
		    var req = HttpRequest.newBuilder()
					.uri(URI.create("https://api.openai.com/v1/models"))
					.header("Content-type", "application/json")
					.header("Authorization", TOKEN_CHATGPT)
					.GET()		
					.build();
			var client = HttpClient.newHttpClient();
			var response = client.send(req, HttpResponse.BodyHandlers.ofString());
			
			log.info("##=>Response_____GetModelsChatGPT: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
	        log.info("");
	        
		} catch (Exception e) {
			log.error("Problemas al obtener los modelos de Chat-GPT: ");
			log.error(e.getMessage());
		}
	}
	public void GenerateImage(String prompt) {
		
		AIResponse result = new AIResponse();
		try {
			String url = URL_CHATGPT_V1 + "/images/generations";
			java.net.HttpURLConnection con = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
	        con.setRequestMethod("POST");
	        con.setRequestProperty("Content-Type", "application/json");
	        con.setRequestProperty("Authorization", TOKEN_CHATGPT);
	        org.json.JSONObject data = new org.json.JSONObject();
	        data.put("n", 1);
	        data.put("prompt", prompt);
	        data.put("size", "1024x1024");
	        con.setDoOutput(true);
	        con.getOutputStream().write(data.toString().getBytes());

	        String output = new BufferedReader(new java.io.InputStreamReader(con.getInputStream())).lines()
	                .reduce((a, b) -> a + b).get();
	        result.setMessageChatGTP(new org.json.JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
	        result.setCode(200);
	        log.info(result);
			
		} catch (Exception ex) {
			log.error("#########__GenerateImage_CHAT-GPT__########");
			log.error(ex.getMessage());
		}
		
	}
	public AIResponse sendTextChatGPT(String text) {
		
		log.info("###########___ENVÍAR MENSAJE A CHAT-GPT___###########");
		AIResponse result = new AIResponse();
		String url = URL_CHATGPT_V1 + "/completions";
		//String url = URL_CHATGPT_V1 + "/completions";
		try {
			log.info(text);
			
			java.net.HttpURLConnection con = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
	        con.setRequestMethod("POST");
	        con.setRequestProperty("Content-Type", "application/json");
	        con.setRequestProperty("Authorization", TOKEN_CHATGPT);
	        org.json.JSONObject data = new org.json.JSONObject();
	        data.put("model", "text-davinci-003");
	        data.put("prompt", text);
	        data.put("max_tokens", 4000);
	        data.put("temperature", 0.5);
			
	        con.setDoOutput(true);
	        con.getOutputStream().write(data.toString().getBytes());

	        String output = new BufferedReader(new java.io.InputStreamReader(con.getInputStream())).lines()
	                .reduce((a, b) -> a + b).get();

	        result.setMessageChatGTP(new org.json.JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
	        result.setCode(200);
	        log.info(result);
	        return result;
			
		} catch (Exception e) {
			log.error("#####__ERROR_SEND_TEXT_CHATGPT___#####");
			log.error(e.getMessage());
			result.setCode(500);
			return result;
		}
	}

	public AIResponse sendTextChatGPT3Turbo(String text) {

		log.info("###########___ENVÍAR MENSAJE A CHAT-GPT___###########");
		AIResponse result = new AIResponse();
		String url = URL_CHATGPT_V1 + "/chat/completions";
		try {
			log.info(text);

			java.net.HttpURLConnection con = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", TOKEN_CHATGPT);
			org.json.JSONObject message = new org.json.JSONObject();
			message.put("role", "user");
			message.put("content", text);
			List<org.json.JSONObject> obj = new ArrayList<org.json.JSONObject>();
			obj.add(message);

			org.json.JSONObject data = new org.json.JSONObject();
			data.put("model", "gpt-3.5-turbo");
			data.put("messages", obj);

			con.setDoOutput(true);
			con.getOutputStream().write(data.toString().getBytes());

			String output = new BufferedReader(new java.io.InputStreamReader(con.getInputStream())).lines()
					.reduce((a, b) -> a + b).get();

			result.setMessageChatGTP(new org.json.JSONObject(output).getJSONArray("choices").getJSONObject(0)
					.getJSONObject("message").getString("content"));
			result.setCode(200);
			log.info(result);
			return result;

		} catch (Exception e) {
			log.error("#####__ERROR_SEND_TEXT_CHATGPT___#####");
			log.error(e.getMessage());
			result.setMessageChatGTP(e.getMessage());
			result.setCode(500);
			return result;
		}
	}

	public AIResponse getInfoWikipedia(String search) {

		AIResponse res = new AIResponse();
		try {
			search = search.replace(" ", "%20");
			var req = HttpRequest.newBuilder()
					.uri(URI.create("https://es.wikipedia.org/w/api.php?action=query&"
							+ "prop=revisions&rvprop=content&format=json&titles=" + search))
					.header("Content-type", "application/json").GET().build();
			var client = HttpClient.newHttpClient();
			var response = client.send(req, HttpResponse.BodyHandlers.ofString());

			log.info("##=>Response_____sendItemStream: ");
			log.info(response.statusCode());
			log.info(response.body());
			log.info(response.body().getClass());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(response.body(), Map.class);
			res.setMessageWikipedia(map.get("query"));
			res.setCode(200);

			return res;
		} catch (Exception e) {
			log.error("#####__ERROR_GET_INFORMATION_WIKIPEDIA___#####");
			log.error(e.getMessage());
			res.setCode(500);
			return res;
		}
	}

	public SendResponse sendTelegram(TelegramSendModel request, String type) {
		SendResponse response = new SendResponse("200", "OK", false, "");

		try {

			String urlString = apiTelegram;
			String apiToken = request.getFrom() + ":" + request.getToken();
			String chatId = request.getTo();

			if (Strings.isEmpty(request.getToken())) {
				apiToken = telegramBot;
			}

			switch (type.toUpperCase()) {
			case "CHAT":
				urlString += telegramSendchat;
				String text = URLEncoder.encode(request.getText(), StandardCharsets.UTF_8);
				urlString = String.format(urlString, apiToken, chatId, text);
				break;

			case "LINK":

				break;

			case "IMAGEN":
			case "IMG":
				break;

			default:
				break;
			}

			URL _url = new URL(urlString);
			URLConnection conn = _url.openConnection();
			InputStream is = new BufferedInputStream(conn.getInputStream());

			if (request.getReplica() > 0) {
				for (int i = 0; i < request.getReplica(); i++) {
					conn = _url.openConnection();
					is = new BufferedInputStream(conn.getInputStream());
				}
			}

			response.setSended(true);
			response.setUid("");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setCode("500");
			response.setMessage(e.getMessage());
		}

		return response;
	}

	// Función para leer el JSON de datos y regresar su contenido
	public List<ProyectModel> getDataFile() {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			// Leer el archivo JSON
			File file = new File("Data.json");
			File file2 = new File("../../Data.json");
			DataResultService dataResponse = objectMapper.readValue(file, DataResultService.class);
			System.out.println("Código: " + dataResponse.getCode());
			if (dataResponse.getCode() == 200) {
				return dataResponse.getResult();
			} else {
				return null;
			}
		} catch (Exception ex) {
			log.error("####################--PROBLEMAS AL LEER EL JSON--");
			log.error(ex.getMessage());
			return null;
		}
	}
}

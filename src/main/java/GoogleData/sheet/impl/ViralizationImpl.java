package GoogleData.sheet.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import GoogleData.sheet.dto.request.GetDataAssignamentRequest;
import GoogleData.sheet.dto.request.SaveDataAssignamentRequest;
import GoogleData.sheet.dto.request.UpdatePostMessageRequest;
import GoogleData.sheet.dto.request.VerifyDataBinnacleRequest;
import GoogleData.sheet.dto.request.ViralizationRequest;
import GoogleData.sheet.dto.request.ViralizationUpdatePostRequest;
import GoogleData.sheet.dto.response.SheetResponse;
import GoogleData.sheet.model.AssignamentModel;
import GoogleData.sheet.model.PostVirModel;
import GoogleData.sheet.model.SaveAssignModel;
import GoogleData.sheet.model.UsersViralModel;
import GoogleData.sheet.model.ViralLinkModel;
import GoogleData.sheet.service.ViralizationService;
import GoogleData.sheet.utils.Utilities;

@Service("ViralizationImpl")
public class ViralizationImpl implements ViralizationService {
	private final Log log = LogFactory.getLog(getClass());
	
	@Value("${file.headers.viralization}")
    private String HEADERS_VIRALIZATION;
    @Value("${file.sheet.viralization.data}")
    private String FILE_VIRALIZATION_DATA;
    @Value("${file.sheet.viralization.log}")
    private String FILE_VIRALIZATION_LOG;
    @Value("${file.headers.viralization.log}")
    private String HEADERS_VIRALIZATION_LOG;
    @Value("${file.sheet.viralization.assign}")
    private String FILE_VIRALIZATION_ASSIGN;
    @Value("${file.sheet.viralization.assign.headers}")
    private String FILE_VIRALIZATION_ASSIGN_HEAD;
    
    @Autowired
    GoogleImpl googleImpl;
    @Autowired
    Utilities utilities;
	
	@Override
	public ResponseEntity<?> saveBinnacle(ViralizationRequest request) {
		log.info("###_Guardando acción en bitácora para VIRALIZACIÓN-_#####");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String LogsFile = (FILE_VIRALIZATION_LOG.equals("Bitacora")) ? "Bitácora" : FILE_VIRALIZATION_LOG;
			boolean update = false;
			
			//Validando si existe la hoja de logs ("Bitácora")
			boolean existSh = googleImpl.validateExistSheet(request.getSpreadsheet_id(),
					utilities.cleanNameSheet(LogsFile.trim().toLowerCase()));
			if(!existSh) {
				String[] HeadersFile = HEADERS_VIRALIZATION.split(",");
				String[] Headers_Log = HEADERS_VIRALIZATION_LOG.split(",");
				Headers_Log[2] = "Fecha asociación";
				Headers_Log[3] = "Fecha visualización";
				log.info("==> No existe la hoja de bitácora");
				googleImpl.createSheet(request.getSpreadsheet_id(), LogsFile.trim());
				
				String Headers_R = LogsFile.trim() + "!A1";
				List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
				List<Object> valHead = new ArrayList<Object>();
				for (int i = 0; i < Headers_Log.length; i++) {
					valHead.add(Headers_Log[i]);
				}
				valuesHeader.add(valHead);
				boolean addHeaders = googleImpl.addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
			}
			//Obteniendo los datos de la hoja de asignaciones
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), LogsFile);
			String RangeInsert = LogsFile.trim() + "!A" + String.valueOf(restGetRaw.objectResult.size() + 1);
			if(restGetRaw.objectResult.size()==1)
				update = true;
			restGetRaw.objectResult.remove(0);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			LocalDateTime dateRequest = LocalDateTime.parse(request.getDate(), formatter);
			
			//if(restGetRaw.objectResult.size() >= 1) {
				//Validar si la data de hoy contiene registros del usuario si no se agrega
				for (List<Object> element : restGetRaw.objectResult) {
					LocalDateTime dateFile = LocalDateTime.parse(element.get(2).toString(), formatter);
					if(dateFile.getDayOfMonth() == dateRequest.getDayOfMonth() && dateFile.getMonthValue() == dateRequest.getMonthValue() 
							&& dateFile.getYear() == dateRequest.getYear()) {
						if(request.getItems().get(0).getUser().equals(element.get(0).toString())) {
							break;
						}else {
							update = true;
						}
					}else {
						update = true;
					}
				}
			//}
			
			//Si el update es true se tienen que agregar los registros
			if(update) {
				//Insertar registros
				List<List<Object>> valuesUsers = new ArrayList<List<Object>>();
				for (UsersViralModel user : request.getItems()) {
					for (ViralLinkModel item : user.getItems()) {
						List<Object> valUser = new ArrayList<Object>();
						valUser.add(user.getUser());
						valUser.add(item.getLink());
						valUser.add(request.getDate());
						valUser.add("");
						valUser.add("No visitado");
						valuesUsers.add(valUser);
					}
				}
				boolean addItem = googleImpl.addHeadersSheet(valuesUsers, RangeInsert, request.getSpreadsheet_id());
				Thread.sleep(300);
			}

			map.put("code", 200);
			map.put("message", "OK");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		} catch (Exception ex) {
			log.error("########___Problemas al guardar en la bitácora");
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}
	
	/*@Override
	public ResponseEntity<?> saveBinnacleV2(ViralizationRequest request) {
		log.info("###_Guardando acción en bitácora para VIRALIZACIÓN-_#####");
		System.out.println(new Gson().toJson(request));
		Map<String, Object> map = new HashMap<String, Object>();
		try {			
			String[] HeadersFile = HEADERS_VIRALIZATION.split(",");
			String[] Headers_Log = HEADERS_VIRALIZATION_LOG.split(",");
			Headers_Log[2] = "Fecha asociación";
			Headers_Log[3] = "Fecha visualización";
			
			String LogsFile = (FILE_VIRALIZATION_LOG.equals("Bitacora")) ? "Bitácora" : FILE_VIRALIZATION_LOG;
			int numItem = 0;
			boolean update = false;
			
			//Validando si existe la hoja de logs ("Bitácora")
			boolean existSh = googleImpl.validateExistSheet(request.getSpreadsheet_id(),
					utilities.cleanNameSheet(LogsFile.trim().toLowerCase()));
			if(!existSh) {
				log.info("==> No existe la hoja de bitácora");
				googleImpl.createSheet(request.getSpreadsheet_id(), LogsFile.trim());
				
				String Headers_R = LogsFile.trim() + "!A1";
				List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
				List<Object> valHead = new ArrayList<Object>();
				for (int i = 0; i < Headers_Log.length; i++) {
					valHead.add(Headers_Log[i]);
				}
				valuesHeader.add(valHead);
				boolean addHeaders = googleImpl.addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
			}
			
			//Funcionalidad para agregar los usuarios a la hoja de "bitácora"
			//Revisar si existen registros de inserción del día de hoy, en caso de no existir se insertarán
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			LocalDateTime dateRequest = LocalDateTime.parse(request.getDate(), formatter);
			if(existSh) {
				//Validar que los registros existentes no sean de hoy
				List<String> listElements = googleImpl.getDataByColumn(request.getSpreadsheet_id(), Headers_Log[2],LogsFile.trim());
				listElements.remove(listElements.size()-1);
				for (String registerDate : listElements) {
					LocalDateTime dateFile = LocalDateTime.parse(registerDate, formatter);
					if(dateFile.getDayOfMonth() == dateRequest.getDayOfMonth() && dateFile.getMonthValue() == dateRequest.getMonthValue() 
							&& dateFile.getYear() == dateRequest.getYear()) {
						//Ya existe un registro del día de hoy, NO ACTUALIZAR
						//System.out.println("No actualizar los registros de la bitácora");
						update = false;
					}else {
						//Actualizar los registros
						//System.out.println("Actualizar los registros de la bitácora");
						update = true;
					}
				}
			}else {
				//Se tienen que agregar los datos por que el documento es nuevo
				update = true;
			}
			
			if(update) {
				//Insertar registros
				SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), LogsFile.trim());
				for (UsersViralModel user : request.getItems()) {
					for (ViralLinkModel item : user.getItems()) {
						numItem++;
						List<List<Object>> valuesUsers = new ArrayList<List<Object>>();
						List<Object> valUser = new ArrayList<Object>();
						valUser.add(user.getUser());
						valUser.add(item.getLink());
						valUser.add(request.getDate());
						valUser.add("");
						valUser.add("No visitado");
						valuesUsers.add(valUser);
						String num = utilities.numToLetter(numItem + 1);
						String PosItem = LogsFile + "!A" + numItem;
						boolean addItem = googleImpl.addHeadersSheet(valuesUsers, PosItem, request.getSpreadsheet_id());
						Thread.sleep(300);
					}
				}
			}
			map.put("code", 200);
			map.put("message", "OK");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		} catch (Exception ex) {
			log.error("########___Problemas al guardar en la bitácora");
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}*/
	
	public ResponseEntity<?> verifyBinnacle (VerifyDataBinnacleRequest request){
		log.info("####################__VERIFICANDO INFORMACIÓN DE LA HOJA DE BITÁCORA__##################");
		Map<String, Object> map = new HashMap<String, Object>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		try {
			//Verificando si existe la hoja de bitácora, si no regresar un 404
			//Validando si existe la hoja de logs ("Bitácora")
			String[] HeadersFile = HEADERS_VIRALIZATION.split(",");
			String[] Headers_Log = HEADERS_VIRALIZATION_LOG.split(",");
			Headers_Log[2] = "Fecha asociación";
			Headers_Log[3] = "Fecha visualización";
			int existDataNow = 0;
			
			String LogsFile = (FILE_VIRALIZATION_LOG.equals("Bitacora")) ? "Bitácora" : FILE_VIRALIZATION_LOG;
			boolean existSh = googleImpl.validateExistSheet(request.getSpreadsheet_id(),
					utilities.cleanNameSheet(LogsFile.trim().toLowerCase()));
			
			//Si la hoja es nueva regresar un 404
			if(!existSh) {
				log.info("==> No existe la hoja de bitácora");
				map.put("result",new ArrayList<UsersViralModel>());
				map.put("code", 404);
				map.put("message", "NOT EXIST");
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
				
			}else {
				//Si la hoja ya existe, regresar el objeto con los registros
				log.info("==> Obteniendo registros de la hoja de bitácora");
				SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), LogsFile);
				LocalDateTime dateRequest = LocalDateTime.parse(request.getDate(), formatter);
				List<UsersViralModel> resItems = new ArrayList<UsersViralModel>();
				
				int i = 0;
				for (List<Object> item : restGetRaw.objectResult) {
					if(i != 0) {
						UsersViralModel post = new UsersViralModel();
						ViralLinkModel link = new ViralLinkModel();
						boolean addUser = false;
						LocalDateTime dateFile = LocalDateTime.parse(item.get(2).toString(), formatter);
						if(dateFile.getDayOfMonth() == dateRequest.getDayOfMonth() && dateFile.getMonthValue() == dateRequest.getMonthValue() && 
								dateFile.getYear() == dateRequest.getYear()) 
						{
							existDataNow =  1;
							if(request.getUser().equals(item.get(0))) {
								//Ya existe un registro del día de hoy, AGREGAR
								//REGLA DE NEGOCIO--- El primer usuario se agrega, si el usuario anterior es diferente al nuevo se crea un objeto nuevo
								if(resItems.size()==0) {
									//El usuario es nuevo y se agrega el objeto completo
									post.setUser(item.get(0).toString());
									addUser = true;
									resItems.add(post);
								}else {
									for (UsersViralModel itemPost : resItems) {
										if(itemPost.getUser().equals(item.get(0))) {
											addUser = true;
										}else {
											addUser = false;
										}
									}
									if(!addUser) {
										//Agregar el usuario
										post.setUser(item.get(0).toString());
										resItems.add(post);
									}
								}
							}
						}
					}
					i++;
				}
				
				int posIt = 0;
				LocalDateTime dateReq = LocalDateTime.parse(request.getDate(), formatter);
				for (UsersViralModel element : resItems) {
					List<ViralLinkModel> Post = new ArrayList<ViralLinkModel>();
					for (List<Object> itemFile : restGetRaw.objectResult) {
						if(posIt != 0) {
							ViralLinkModel link = new ViralLinkModel();
							//Validar la fecha para obtener registros del día de hoy
							LocalDateTime dateFile = LocalDateTime.parse(itemFile.get(2).toString(), formatter);
							if(dateFile.getDayOfMonth() == dateReq.getDayOfMonth() && dateFile.getMonthValue() == dateReq.getMonthValue() && 
									dateFile.getYear() == dateReq.getYear()) {
								if(element.getUser().equals(itemFile.get(0).toString())){
									existDataNow =  2;
									link.setStatus(itemFile.get(4).toString().equals("No visitado") ? false : true);
									link.setLink(itemFile.get(1).toString());
									link.setDateDisplay(itemFile.get(3).toString());
									Post.add(link);
								}
							}
							element.setItems(Post);
						}
						posIt++;
					}
				}
				System.out.println("########################__TOTAL DE REGISTROS__##########################");
				//System.out.println(new Gson().toJson(resItems));
				
				
				if(existDataNow == 0) {
					map.put("code", 404);
					map.put("message", "NO EXISTE LA HOJA");

				}
				else if(existDataNow == 1) {
					map.put("code", 202);
					map.put("message", "EXISTE DATA DE HOY PERO NO DEL USUARIO SOLICITADO");
				}else if(existDataNow == 2) {
					map.put("code", 200);
					map.put("message", "OK");
				}
				map.put("result",resItems);

			}
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		} catch (Exception ex) {
			log.error("###############__PROBLEMAS AL OBTENER LOS DATOS DE LA HOJA DE BITÁCORA");
			log.error(ex.getMessage());
			map.put("result",new ArrayList<UsersViralModel>());
			map.put("code", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}
	
	public ResponseEntity<?> updatePost (ViralizationUpdatePostRequest request){
		log.info("#########################---ACTUALIZANDO EL POST DE VIRALIZACIÓN: ################");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//Validando si existe el post y encontrando la fila correspondiente
			log.info("==> Obteniendo registros de la hoja de bitácora");
			String LogsFile = (FILE_VIRALIZATION_LOG.equals("Bitacora")) ? "Bitácora" : FILE_VIRALIZATION_LOG;
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), LogsFile);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			LocalDateTime dateReq = LocalDateTime.parse(request.getDate(), formatter);
			
			int position = 0;
			for (List<Object> postFile : restGetRaw.objectResult) {
				if(position != 0) {
					if(postFile.get(0).toString().equals(request.getUser()) && postFile.get(1).toString().equals(request.getPost())) {
						//Validar que el post coincida con la fecha de hoy
						LocalDateTime dateFile = LocalDateTime.parse(postFile.get(2).toString(), formatter);
						if(dateFile.getDayOfMonth() == dateReq.getDayOfMonth() && dateFile.getMonthValue() == dateReq.getMonthValue() && 
								dateFile.getYear() == dateReq.getYear()) {
							//Actualizando elemento
							List<List<Object>> valuesItems = new ArrayList<List<Object>>();
							List<Object> valItem = new ArrayList<Object>();
							position++;
							String Range = LogsFile.trim() + "!D" + position;
							valItem.add(request.getDate());
							valItem.add("Visitado");
							valuesItems.add(valItem);
							googleImpl.updateAndReplaceData(valuesItems, Range, request.getSpreadsheet_id());
							break;
						}
					}
				}
				position++;
			}
			
			//Obteniendo los post del usuario
			VerifyDataBinnacleRequest reqElements = new VerifyDataBinnacleRequest();
			reqElements.setDate(request.getDate());
			reqElements.setSpreadsheet_id(request.getSpreadsheet_id());
			reqElements.setUser(request.getUser());
			ResponseEntity<?> elements = verifyBinnacle(reqElements);
			
			Map<String, Object> bodyMap = (Map<String, Object>) elements.getBody();
		    // Obtén el result (que se espera que sea un List<UsersViralModel>)
		    List<UsersViralModel> result = (List<UsersViralModel>) bodyMap.get("result");
			
			map.put("code", 200);
			map.put("message", "OK");
			map.put("result", result);
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		} catch (Exception ex) {
			log.error("###############__PROBLEMAS AL OBTENER LOS DATOS DE LA HOJA DE BITÁCORA");
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}

	public ResponseEntity<?> getDataAssignament (GetDataAssignamentRequest request){
		log.info("####################__OBTENIENDO DATA DE ASIGNACIONES__###################");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			int code = 200;
			String message = "OK";
			List<AssignamentModel> ListPost = new ArrayList<AssignamentModel>();
			boolean existSh = googleImpl.validateExistSheet(request.getSpreadsheet_id(),
					utilities.cleanNameSheet(FILE_VIRALIZATION_ASSIGN.toLowerCase()));
			
			//######--Si la hoja NO existe regresar 404
			if(!existSh) {
				code = 404;
				message = "NOT EXIST";
				map.put("code", code);
				map.put("message", message);
				map.put("result", ListPost);
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
			}
			else {
				//Obteniendo los datos de la hoja de asignaciones
				SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), FILE_VIRALIZATION_ASSIGN);
				LocalDateTime dateRequest = LocalDateTime.parse(request.getDate(), formatter);
				int i = 0;
				for (List<Object> item : restGetRaw.objectResult) {
					if (i != 0) {
						LocalDateTime dateFile = LocalDateTime.parse(item.get(1).toString(), formatter);
						if (dateFile.getDayOfMonth() == dateRequest.getDayOfMonth() && dateFile.getMonthValue() == dateRequest.getMonthValue()
								&& dateFile.getYear() == dateRequest.getYear()) {
							System.out.println("El post es: " + item.get(0).toString());
							AssignamentModel post = new AssignamentModel();
							post.setLink(item.get(0).toString());
							post.setDateAssign(item.get(1).toString());
							post.setMax_assign(Integer.parseInt(item.get(2).toString()));
							post.setCurrent_assign(Integer.parseInt(item.get(3).toString()));
							ListPost.add(post);
						}
					}
					i++;
				}
			}
			if(ListPost.size() == 0 )
				code = 202;
			
			map.put("code", code);
			map.put("message", message);
			map.put("result", ListPost);
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		} catch (Exception ex) {
			log.error("###############__PROBLEMAS AL OBTENER LOS DATOS DE LA HOJA DE 'ASIGNACIONES'");
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}

	public ResponseEntity<?> saveDataAssignament(SaveDataAssignamentRequest request) {
		log.info("####################__GUARDANDO DATOS DE ASIGNACIONES__###################");
		Map<String, Object> map = new HashMap<String, Object>();
		Integer code = 200;
		String message = "";
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			List<AssignamentModel> ListPost = new ArrayList<AssignamentModel>();
			boolean existSh = googleImpl.validateExistSheet(request.getSpreadsheet_id(),
					utilities.cleanNameSheet(FILE_VIRALIZATION_ASSIGN.toLowerCase()));
			
			if(!existSh) {
				//Crear la hoja de "asignacionesXusuario"
				String[] HeadersFile = FILE_VIRALIZATION_ASSIGN_HEAD.split(",");
				HeadersFile[1] = "Fecha de asignación";
				HeadersFile[2] = "Máximo de asignaciones";
				
				googleImpl.createSheet(request.getSpreadsheet_id(), FILE_VIRALIZATION_ASSIGN.trim());
				
				String Headers_R = FILE_VIRALIZATION_ASSIGN.trim() + "!A1";
				List<List<Object>> valuesHeader = new ArrayList<List<Object>>();
				List<Object> valHead = new ArrayList<Object>();
				for (int i = 0; i < HeadersFile.length; i++) {
					valHead.add(HeadersFile[i]);
				}
				valuesHeader.add(valHead);
				boolean addHeaders = googleImpl.addHeadersSheet(valuesHeader, Headers_R, request.getSpreadsheet_id());
			}
			//Obteniendo los datos de la hoja de asignaciones
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), FILE_VIRALIZATION_ASSIGN);
			String RangeInsert = FILE_VIRALIZATION_ASSIGN.trim() + "!A" + String.valueOf(restGetRaw.objectResult.size() + 1);
			List<List<Object>> valuesPost = new ArrayList<List<Object>>();
			if(request.getItems().size() > 0) {
				for (SaveAssignModel item : request.getItems()) {
					List<Object> valPost = new ArrayList<Object>();
					valPost.add(item.getLink());
					valPost.add(request.getDate());
					valPost.add(request.getMax_assign());
					valPost.add(item.getCurrent_assign());
					valuesPost.add(valPost);
				}
			}
			
			boolean addHeaders = googleImpl.addHeadersSheet(valuesPost, RangeInsert, request.getSpreadsheet_id());
			map.put("code", code);
			map.put("message", message);
			//map.put("result", message);
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		} catch (Exception ex) {
			log.error("###############__PROBLEMAS AL OBTENER LOS DATOS DE LA HOJA DE 'ASIGNACIONES'");
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			//map.put("result", message);
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}

	public ResponseEntity<?> updateDataAssignament(SaveDataAssignamentRequest request) {
		log.info("####################__GUARDANDO DATOS DE ASIGNACIONES__###################");
		Map<String, Object> map = new HashMap<String, Object>();
		Integer code = 200;
		String message = "";
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			LocalDateTime dateRequest = LocalDateTime.parse(request.getDate(), formatter);
			//Obteniendo los datos de la hoja de asignaciones
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), FILE_VIRALIZATION_ASSIGN);
			restGetRaw.objectResult.remove(0);
			
			Integer count = 0;
			for (List<Object> elementFile : restGetRaw.objectResult) {
				count++;
				for (SaveAssignModel elementRequest : request.getItems()) {
					LocalDateTime dateFile = LocalDateTime.parse(elementFile.get(1).toString(), formatter);
					if(elementFile.get(0).toString().equals(elementRequest.getLink())){
						if(dateFile.getDayOfMonth() == dateRequest.getDayOfMonth() && dateFile.getMonthValue() == dateRequest.getMonthValue() && 
								dateFile.getYear() == dateRequest.getYear()) 
						{
							List<List<Object>> valuesPost = new ArrayList<List<Object>>();
							List<Object> valPost = new ArrayList<Object>();
							valPost.add(elementRequest.getLink());
							valPost.add(elementFile.get(1));
							valPost.add(request.getMax_assign());
							Integer currAssign = Integer.parseInt(elementFile.get(3).toString()) +1;
							valPost.add(currAssign);
							System.out.println("El post se tiene que insertar: " + elementRequest.getLink());
							valuesPost.add(valPost);
							
							String RangeInsert = FILE_VIRALIZATION_ASSIGN + "!A" + String.valueOf(count + 1);
							System.out.println("");
							//boolean addHeaders = googleImpl.addHeadersSheet(valuesPost, RangeInsert, request.getSpreadsheet_id());
							googleImpl.updateAndReplaceData(valuesPost, RangeInsert, request.getSpreadsheet_id());
						}
					}
				}
			}
			
			/*if(valuesPost.size() > 0) {
				//Insertar elementos
				boolean addHeaders = googleImpl.addHeadersSheet(valuesPost, Headers_R, request.getSpreadsheet_id());
			}*/
			
			map.put("code", code);
			map.put("message", message);
			//map.put("result", message);
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		} catch (Exception ex) {
			log.error("###############__PROBLEMAS AL ACTUALIZAR LOS DATOS DE LA HOJA DE 'ASIGNACIONES'");
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}



	public ResponseEntity<?> updatePostMessage(UpdatePostMessageRequest request) {
		log.info("############--ACTUALIZANDO LOS POST updatePostMessage--###############");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//Buscar la colúmna Link
			SheetResponse restGetRaw = googleImpl.getDataSheetByFilter("RAW", request.getSpreadsheet_id(), FILE_VIRALIZATION_DATA);
			List<Object> headers = restGetRaw.objectResult.get(0);
			int pos = headers.indexOf("Link") +1;
			String letter = utilities.numToLetter(pos);
			

			
			for (PostVirModel post : request.getPost()) {
				int position = post.getId() + 1;
				String RangeInsert = FILE_VIRALIZATION_DATA + "!" + letter + (post.getId() + 1);
				List<List<Object>> valuesPost = new ArrayList<List<Object>>();
				List<Object> valPost = new ArrayList<Object>();
				valPost.add(post.getPost());
				valuesPost.add(valPost);
				googleImpl.updateAndReplaceData(valuesPost, RangeInsert, request.getSpreadsheet_id());
				Thread.sleep(500);
			}
			
			//googleImpl.updateAndReplaceDataByColumn(valuesPost, RangeInsert, request.getSpreadsheet_id());
			
			map.put("code", 200);
			map.put("message", "OK");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
			
		} catch (Exception ex) {
			log.error("###############---PROBLEMAS AL ACTUALIZAR LOS POST---###################");
			log.error(ex.getMessage());
			map.put("code", 500);
			map.put("message", "ERROR");
			ResponseEntity<?> res = utilities.getResponseEntity(map);
			return res;
		}
	}
	
}

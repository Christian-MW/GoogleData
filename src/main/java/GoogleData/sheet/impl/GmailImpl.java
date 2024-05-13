package GoogleData.sheet.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import GoogleData.sheet.dto.request.SendMailsListRequest;
import GoogleData.sheet.service.GmailService;
import GoogleData.sheet.utils.Utilities;

@Service("GmailImpl")
public class GmailImpl implements GmailService {
	private final Log log = LogFactory.getLog(getClass());
	@Value("${template.emails}")
	private String templateEmail;
	@Autowired
	GoogleImpl googleImpl;
    @Autowired
    Utilities utilities;
	
	@Override
	public ResponseEntity<?> sendMailsList(SendMailsListRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		// String passwordRemitente = "ewuw cypo clen dfup";

		String passEmail = "";
		if(request.getEmail().equals("noticiasdenegociosmx@gmail.com"))
			passEmail = "kqtv gxbn ghrw xchw";
		if (request.getEmail().equals("noticiasdemex@gmail.com"))
			passEmail = "ewuw cypo clen dfup";
		if (request.getEmail().equals("christian.garcia@mwgroup.com.mx"))
			passEmail = "iroa joap xgxx darv";

		final String PassEmail = passEmail;
		// String correoRemitente = "noticiasdemex@gmail.com";
		// String passwordRemitente = "ewuw cypo clen dfup";
		// String correoRemitente = "christian.garcia@mwgroup.com.mx";
		// String passwordRemitente = "iroa joap xgxx darv";

		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		// Crear una sesión de correo
		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(request.getEmail(), PassEmail);
			}
		});
		try {
			// Obtener listado de usuarios para enviar emails
			List<String> listUsers = googleImpl.getDataByColumn(request.getSpreadsheet_id(), "nombre", "Usuarios");
			List<String> listEmails = googleImpl.getDataByColumn(request.getSpreadsheet_id(), "email", "Usuarios");
			if (listUsers.size() != listEmails.size()) {
				map.put("operation", 500);
				map.put("message", "ERROR");
				ResponseEntity<?> res = utilities.getResponseEntity(map);
				return res;
			}

			// Obtener elementos cómo ASUNTO, CONTENIDO DEL MENSAJE Y LINK
			List<String> asunto = googleImpl.getDataByColumn(request.getSpreadsheet_id(), "Título", "Usuarios");
			List<String> contenido = googleImpl.getDataByColumn(request.getSpreadsheet_id(), "Cuerpo", "Usuarios");
			List<String> link = googleImpl.getDataByColumn(request.getSpreadsheet_id(), "Link", "Usuarios");
			// List<String> imagen = googleImpl.getDataByColumn(request.getSpreadsheet_id(),
			// "email", "Usuarios");

			String html = utilities.readFile("template/" + templateEmail);
			log.info("Template obtenido!!");

			Date Date = new Date();
			String dateS = new SimpleDateFormat("dd/MM/yyyy").format(Date);
			String month = utilities.getMonth(dateS);

			String[] arrDate = dateS.split("/");
			String dateFormat = arrDate[0] + " de " + month + " del " + arrDate[2];
			html = html.replace("{{date}}", dateFormat);
			html = html.replace("{{header}}", asunto.get(0));
			html = html.replace("{{body}}", contenido.get(0));
			html = html.replace("{{imagen}}", request.getImagen());
			html = html.replace("{{link}}", link.get(0));
			html = utilities.utfToUnicode(html, "");

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(request.getEmail()));

			try {
				for (int i = 0; i < listEmails.size(); i++) {
					long startTime = System.currentTimeMillis();
					if (listEmails.get(i) != "200") {
						// Validar si el elemento es un email
						Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
								+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
						Matcher mather = pattern.matcher(listEmails.get(i));
						if (mather.find() == true) {
							message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(listEmails.get(i)));
							message.setSubject(asunto.get(0));
							message.setContent(html, "text/html");
							// message.setText(html);
							try {
								Transport.send(message);
								System.out.println(i + " => Se ha enviado el email al usuario: " + listEmails.get(i));
								Thread.sleep(7000);
							} catch (Exception e) {
								System.out.println(i + " => CORREO CON ERROR: " + listEmails.get(i));
							}
						} else {
							System.out.println("El elemento: " + listEmails.get(i) + " no es un email");
						}

					}
					long endTime = System.currentTimeMillis() - startTime;
					// System.out.println(endTime);
				}
			} catch (Exception ex) {
				log.error("ERROR AL PROCESAR LOS MENSAJES");
				log.error(ex.getMessage());
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return null;
	}

}

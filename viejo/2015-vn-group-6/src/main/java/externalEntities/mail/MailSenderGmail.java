package externalEntities.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

import exceptions.model.ErrorEnvioEmail;

public enum MailSenderGmail implements MailSender {
	INSTANCIA;

	public void enviarMail(Mail mail) throws ErrorEnvioEmail {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", mail.getEmisor());
		props.put("mail.smtp.password", mail.getPassEmisor());
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(mail.getEmisor()));
			for (int i = 0; i < mail.getReceptores().size(); i++) {
				InternetAddress dir = new InternetAddress(mail.getReceptores().get(i));
				dir.validate();
				message.addRecipient(Message.RecipientType.TO, dir);
			}
			message.setSubject(mail.getTitulo());
			message.setText(mail.getCuerpo());

			Transport transport = session.getTransport("smtp");
			transport.connect(host, mail.getEmisor(), mail.getPassEmisor());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException ae) {
			throw new ErrorEnvioEmail("Error al Enviar el Mail. " + ae.getMessage());
		} catch (MessagingException me) {
			throw new ErrorEnvioEmail("Error al Enviar el Mail. " + me.getMessage());
		} catch (NullPointerException me) {
			throw new ErrorEnvioEmail("Error al Enviar el Mail. Alguno de los parametros del email es null");
		}
	}
}

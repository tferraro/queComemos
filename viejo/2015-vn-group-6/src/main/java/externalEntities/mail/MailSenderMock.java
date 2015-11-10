package externalEntities.mail;

import exceptions.model.ErrorEnvioEmail;

public class MailSenderMock implements MailSender {

	private String mensaje = "";

	@Override
	public void enviarMail(Mail mail) throws ErrorEnvioEmail {
		this.mensaje = mail.getCuerpo();
	}

	public String getMensaje() {
		return mensaje;
	}
}

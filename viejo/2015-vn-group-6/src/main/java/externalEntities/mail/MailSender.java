package externalEntities.mail;

import exceptions.model.ErrorEnvioEmail;

public interface MailSender {

	public void enviarMail(Mail mail) throws ErrorEnvioEmail;

}
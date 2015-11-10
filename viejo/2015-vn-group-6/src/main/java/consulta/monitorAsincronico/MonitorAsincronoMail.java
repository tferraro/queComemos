package consulta.monitorAsincronico;

import exceptions.model.ErrorEnvioEmail;
import exceptions.model.ErrorMonitorAsincronico;
import externalEntities.mail.Mail;
import externalEntities.mail.MailSender;
import businessModel.usuario.Usuario;

public class MonitorAsincronoMail implements MonitorAsincrono {

	private Usuario userDesignado = new Usuario();
	private MailSender gestorMail;

	public MonitorAsincronoMail(MailSender mail) {
		userDesignado.setNombre("jcontardo");
		gestorMail = mail;
	}

	@Override
	public void ejecutar(ConsultaAsincronica consulta) {
		if (consulta.getUser().mismoNombre(userDesignado))
			try {
				enviar(consulta.getUser().getMail(), consulta.getParametros(), consulta.getLista().size());
			} catch (ErrorEnvioEmail e) {
				throw new ErrorMonitorAsincronico(e.getMessage());
			}
	}

	private void enviar(String mailUser, String param, Integer size) throws ErrorEnvioEmail {
		Mail mailNuevo = crearMail(mailUser, param, size);
		gestorMail.enviarMail(mailNuevo);
	}

	private Mail crearMail(String mailUser, String param, Integer size) {
		Mail mailNuevo = new Mail();
		mailNuevo.agregarReceptor(mailUser);
		mailNuevo.setTitulo("Mensaje Automágico de aviso de Consulta");
		mailNuevo.setCuerpo("Este es un email enviado al usuario jcontardo por la realizacion de una consulta.\n"
				+ "Filtros <" + param + ">\nCantidad de Resultados: " + size);
		mailNuevo.mailDeAdmin(); // Setea los parametros del mail del admin
		return mailNuevo;
	}
}

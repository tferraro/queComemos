package consulta.monitorSincronico;

import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import businessModel.receta.Receta;
import businessModel.usuario.Usuario;
import persistencia.hibernate.PersistentEntity;

@Entity
@Table(name = "Monitores_Sincronicos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Tipo")
public abstract class MonitorSincronico extends PersistentEntity {
		
	public abstract void avisar(List<Receta> lista, Usuario user);

	public abstract void reiniciarEstadistica();
}

package businessModel.condicionMedica;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import persistencia.hibernate.PersistentEntity;
import businessModel.receta.Receta;
import businessModel.usuario.Usuario;

@Entity
@Table(name = "Condiciones_Medicas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Condicion")
public abstract class CondicionMedica extends PersistentEntity {

	abstract public void estadoValido(Usuario usr);

	abstract public void estaSubsanada(Usuario usr);

	abstract public void meEsAdecuadaLaReceta(Receta receta);

	public Boolean mismaCondicion(CondicionMedica condicion2) {
		return this.getClass().equals(condicion2.getClass());
	}
	
	public abstract String name();
}

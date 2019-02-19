package uo.ri.ui.admin.action.mechanic;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.MechanicCrudService;
import uo.ri.conf.ServiceFactory;

public class UpdateMechanicAction implements Action {

	/**
	 * Clase que permite actualizar un mecanico
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws BusinessException {

		// Pedir datos
		Long id = Console.readLong("Id del mecánico");
		String nombre = Console.readString("Nombre");
		String apellidos = Console.readString("Apellidos");

		MechanicDto mechanic = new MechanicDto();
		mechanic.id = id;
		mechanic.name = nombre;
		mechanic.surname = apellidos;

		MechanicCrudService service = ServiceFactory.getMechanicService();
		service.updateMechanic(mechanic);

		// Mostrar resultado
		Console.println("Mecánico actualizado");
	}

}

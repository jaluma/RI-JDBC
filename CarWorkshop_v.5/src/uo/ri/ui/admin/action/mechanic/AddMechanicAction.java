package uo.ri.ui.admin.action.mechanic;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.MechanicCrudService;
import uo.ri.conf.ServiceFactory;

public class AddMechanicAction implements Action {

	/**
	 * Clase que permite añadir un mecanico
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws BusinessException {

		// Pedir datos
		String dni = Console.readString("Dni");
		String nombre = Console.readString("Nombre");
		String apellidos = Console.readString("Apellidos");

		MechanicDto mechanic = new MechanicDto();
		mechanic.dni = dni;
		mechanic.name = nombre;
		mechanic.surname = apellidos;

		MechanicCrudService service = ServiceFactory.getMechanicService();
		service.addMechanic(mechanic);

		// Mostrar resultado
		Console.println("Nuevo mecánico añadido");
	}

}

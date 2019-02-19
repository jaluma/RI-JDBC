package uo.ri.ui.admin.action.mechanic;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.MechanicCrudService;
import uo.ri.conf.ServiceFactory;

public class DeleteMechanicAction implements Action {

	/**
	 * Clase que permite borrar un mecanico
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws BusinessException {
		Long idMecanico = Console.readLong("Id de mecánico");

		MechanicCrudService service = ServiceFactory.getMechanicService();
		service.deleteMechanic(idMecanico);
		Console.println("Se ha eliminado el mecánico");

	}

}

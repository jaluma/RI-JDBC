package uo.ri.ui.admin.action.mechanic;

import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.MechanicCrudService;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class ListMechanicsActiveAction implements Action {

	/**
	 * Listas los mecanicos activos
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws BusinessException {

		Console.println("\nListado de mecánicos\n");

		MechanicCrudService service = ServiceFactory.getMechanicService();
		printMechanic(service.findActiveMechanics());
	}

	/**
	 * Metodo que imprime los mecanicos
	 * 
	 * @param lista
	 */
	private void printMechanic(List<MechanicDto> lista) {
		for (MechanicDto mechanic : lista) {
			Printer.printMechanic(mechanic);
		}
	}

}

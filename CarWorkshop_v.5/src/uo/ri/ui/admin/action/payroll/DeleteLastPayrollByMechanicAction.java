package uo.ri.ui.admin.action.payroll;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.service.PayrollService;
import uo.ri.conf.ServiceFactory;

public class DeleteLastPayrollByMechanicAction implements Action {

	/**
	 * Borrar la ultima factura de un mecanico
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws Exception {
		PayrollService payrollService = ServiceFactory.getPayrollService();

		Long idMecanico = Console.readLong("Id de mecánico: ");

		payrollService.deleteLastPayrollForMechanicId(idMecanico);

		Console.println("Se ha eliminado la nómica correctamente");

	}

}

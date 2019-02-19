package uo.ri.ui.admin.action.payroll;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.service.PayrollService;
import uo.ri.conf.ServiceFactory;

public class DeleteForLastMonthPayrollAction implements Action {

	/**
	 * Borrar las nominas del ultimo mes
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws Exception {
		PayrollService payrollService = ServiceFactory.getPayrollService();

		int count = payrollService.deleteLastGenetaredPayrolls();

		Console.println("Se ha eliminado " + count + " nóminas del último mes.");

	}

}

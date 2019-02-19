package uo.ri.ui.admin.action.payroll;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.service.PayrollService;
import uo.ri.conf.ServiceFactory;

public class GeneratePayrollAction implements Action {

	/**
	 * Clase que genera las nominas
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws Exception {
		PayrollService payrollService = ServiceFactory.getPayrollService();
		int count = payrollService.generatePayrolls();

		Console.println("Se han generado " + count + " nóminas correctamente");
	}

}

package uo.ri.ui.admin;

import alb.util.menu.BaseMenu;
import uo.ri.ui.admin.action.payroll.DeleteForLastMonthPayrollAction;
import uo.ri.ui.admin.action.payroll.DeleteLastPayrollByMechanicAction;
import uo.ri.ui.admin.action.payroll.GeneratePayrollAction;
import uo.ri.ui.admin.action.payroll.ListPayrollAction;

public class GestionNominasMenu extends BaseMenu {

	/**
	 * Menu que permite generar las nominas
	 */
	public GestionNominasMenu() {
		menuOptions = new Object[][] { { "Administrador > Gestión de nóminas", null },

				{ "Generar nóminas", GeneratePayrollAction.class }, { "Consultar nóminas", ListPayrollAction.class },
				{ "Eliminar última nómina de un mecánico", DeleteLastPayrollByMechanicAction.class },
				{ "Eliminar todas las nóminas generadas en el último mes", DeleteForLastMonthPayrollAction.class }, };
	}

}

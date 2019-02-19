package uo.ri.ui.admin.action.categories;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.service.ContractCategoryCrudService;
import uo.ri.conf.ServiceFactory;

public class DeleteCategoryAction implements Action {

	/**
	 * Clase que permite borrar una categoria
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws Exception {
		Long id = Console.readLong("Id de la categoría: ");

		ContractCategoryCrudService service = ServiceFactory.getContractCategoryService();
		service.deleteContractCategory(id);

		Console.println("Categoría eliminada correctamente");

	}

}

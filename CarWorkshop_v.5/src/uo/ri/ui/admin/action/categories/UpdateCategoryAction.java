package uo.ri.ui.admin.action.categories;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.service.ContractCategoryCrudService;
import uo.ri.conf.ServiceFactory;

public class UpdateCategoryAction implements Action {

	/**
	 * Metodo que permite actualizar una categoria
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws Exception {
		Long id = Console.readLong("Id de la categoría: ");
		String nombre = Console.readString("Nombre de la categoría: ");
		double importeTrienios = Console.readDouble("Importe de los trienios: ");
		double plusProductividad = Console.readDouble("Porcertaje del plus de productividad (%): ");

		ContractCategoryDto category = new ContractCategoryDto();
		category.id = id;
		category.name = nombre;
		category.productivityPlus = plusProductividad / 100;
		category.trieniumSalary = importeTrienios;

		ContractCategoryCrudService service = ServiceFactory.getContractCategoryService();
		service.updateContractCategory(category);

		Console.println("Categoría actualizada correctamente");

	}

}

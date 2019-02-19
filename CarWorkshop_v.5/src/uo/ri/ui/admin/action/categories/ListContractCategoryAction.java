package uo.ri.ui.admin.action.categories;

import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.ContractCategoryCrudService;
import uo.ri.business.service.ContractCrudService;
import uo.ri.business.service.MechanicCrudService;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class ListContractCategoryAction implements Action {

	private ContractCrudService contractService;

	/**
	 * Clase que permite listar los mecanicos de una categoria
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws BusinessException {

		ContractCategoryCrudService categoryService = ServiceFactory.getContractCategoryService();
		MechanicCrudService mecanicoService = ServiceFactory.getMechanicService();
		contractService = ServiceFactory.getContractService();

		Long id = Console.readLong("Introduzca el id de la categoria: ");

		ContractCategoryDto categoria = categoryService.findContractCategoryById(id);
		List<MechanicDto> mecanicosActivos = mecanicoService.findActiveMechanics();

		if (categoria == null || categoria.name == null) {
			throw new BusinessException("La categoria no existe");
		}
		print(categoria, mecanicosActivos);
	}

	/**
	 * Metodo que permite imprimir los datos obtenidos
	 * 
	 * @param categoria
	 * @param mecanicos
	 * @throws BusinessException
	 */
	private void print(ContractCategoryDto categoria, List<MechanicDto> mecanicos) throws BusinessException {
		System.out.println("CATEGORIA SELECCIONADA\n");
		Printer.printContractCategory(categoria);
		double acumulado = 0;
		System.out.println("\nMECANICOS\n");
		for (MechanicDto mechanic : mecanicos) {
			List<ContractDto> contratos = contractService.findContractsByMechanicId(mechanic.id);
			for (ContractDto contract : contratos) {
				if (categoria.id == contract.categoryId) {
					Printer.printMechanic(mechanic);
					acumulado += contract.yearBaseSalary;
				}
			}
		}
		System.out.format("El acumulado de la categoria '%s' es '%.2f'\n", categoria.name, acumulado);
	}

}

package uo.ri.conf;

import uo.ri.business.service.ContractCategoryCrudService;
import uo.ri.business.service.ContractCrudService;
import uo.ri.business.service.InvoiceService;
import uo.ri.business.service.MechanicCrudService;
import uo.ri.business.service.PayrollService;
import uo.ri.business.service.implementation.ContractCategoryServiceImplementation;
import uo.ri.business.service.implementation.ContractServiceImplementation;
import uo.ri.business.service.implementation.InvoiceServiceImplementation;
import uo.ri.business.service.implementation.MechanicServiceImplementation;
import uo.ri.business.service.implementation.PayRollServiceImplementation;

public class ServiceFactory {

	public static MechanicCrudService getMechanicService() {
		return new MechanicServiceImplementation();
	}

	public static InvoiceService getInvoiceService() {
		return new InvoiceServiceImplementation();
	}

	public static ContractCategoryCrudService getContractCategoryService() {
		return new ContractCategoryServiceImplementation();
	}

	public static ContractCrudService getContractService() {
		return new ContractServiceImplementation();
	}

	public static PayrollService getPayrollService() {
		return new PayRollServiceImplementation();
	}

}

package uo.ri.conf;

import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.ContractCategoryGateway;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.InvoiceGateway;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.PaymentMeanGateway;
import uo.ri.persistence.PayrollGateway;
import uo.ri.persistence.implementatition.BreakdownGatewayImplementation;
import uo.ri.persistence.implementatition.ContractCategoryGatewayImplementation;
import uo.ri.persistence.implementatition.ContractGatewayImplementation;
import uo.ri.persistence.implementatition.InvoiceGatewayImplementation;
import uo.ri.persistence.implementatition.MechanicGatewayImplementation;
import uo.ri.persistence.implementatition.PaymentMeanGatewayImplementation;
import uo.ri.persistence.implementatition.PayrollGatewayImplementation;

public class PersistenceFactory {

	public static MechanicGateway getMechanicGateway() {
		return new MechanicGatewayImplementation();
	}

	public static InvoiceGateway getInvoiceGateway() {
		return new InvoiceGatewayImplementation();
	}

	public static BreakdownGateway getBreakdownGateway() {
		return new BreakdownGatewayImplementation();
	}

	public static PaymentMeanGateway getPaymentMeanGateway() {
		return new PaymentMeanGatewayImplementation();
	}

	public static ContractCategoryGateway getCategoryGateway() {
		return new ContractCategoryGatewayImplementation();
	}

	public static PayrollGateway getPayrollGateway() {
		return new PayrollGatewayImplementation();
	}

	public static ContractGateway getContractGateway() {
		return new ContractGatewayImplementation();
	}
}

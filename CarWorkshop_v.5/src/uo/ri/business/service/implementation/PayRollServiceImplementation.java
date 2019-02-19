package uo.ri.business.service.implementation;

import java.util.List;

import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.PayrollService;
import uo.ri.business.transaction.management.payroll.DeleteLastGenetaredPayrollsBusiness;
import uo.ri.business.transaction.management.payroll.DeleteLastPayrollForMechanicIdBusiness;
import uo.ri.business.transaction.management.payroll.FindAllPayrollsBusiness;
import uo.ri.business.transaction.management.payroll.FindPayrollByIdBusiness;
import uo.ri.business.transaction.management.payroll.FindPayrollsByMechanicIdBusiness;
import uo.ri.business.transaction.management.payroll.GeneratePayrolls;

public class PayRollServiceImplementation implements PayrollService {

	@Override
	public List<PayrollDto> findAllPayrolls() throws BusinessException {
		FindAllPayrollsBusiness find = new FindAllPayrollsBusiness();
		find.execute();
		return find.getList();
	}

	@Override
	public List<PayrollDto> findPayrollsByMechanicId(Long id) throws BusinessException {
		FindPayrollsByMechanicIdBusiness pay = new FindPayrollsByMechanicIdBusiness(id);
		pay.execute();
		return pay.getList();
	}

	@Override
	public PayrollDto findPayrollById(Long id) throws BusinessException {
		FindPayrollByIdBusiness pay = new FindPayrollByIdBusiness(id);
		pay.execute();
		return pay.getPayroll();
	}

	@Override
	public void deleteLastPayrollForMechanicId(Long id) throws BusinessException {
		DeleteLastPayrollForMechanicIdBusiness del = new DeleteLastPayrollForMechanicIdBusiness(id);
		del.execute();
	}

	@Override
	public int deleteLastGenetaredPayrolls() throws BusinessException {
		DeleteLastGenetaredPayrollsBusiness del = new DeleteLastGenetaredPayrollsBusiness();
		del.execute();
		return del.getNumberDelete();
	}

	@Override
	public int generatePayrolls() throws BusinessException {
		GeneratePayrolls gen = new GeneratePayrolls();
		gen.execute();
		return gen.getNumberGenerated();
	}

}

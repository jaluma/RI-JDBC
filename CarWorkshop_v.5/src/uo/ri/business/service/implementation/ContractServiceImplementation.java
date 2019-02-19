package uo.ri.business.service.implementation;

import java.util.Date;
import java.util.List;

import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.ContractCrudService;
import uo.ri.business.transaction.management.contract.AddContractBusiness;
import uo.ri.business.transaction.management.contract.DeleteContractBusiness;
import uo.ri.business.transaction.management.contract.FindContractByIdBusiness;
import uo.ri.business.transaction.management.contract.FindContractByMechanicIdBusiness;
import uo.ri.business.transaction.management.contract.FinishContractBusiness;
import uo.ri.business.transaction.management.contract.UpdateContractBusiness;

public class ContractServiceImplementation implements ContractCrudService {

	@Override
	public void addContract(ContractDto c) throws BusinessException {
		AddContractBusiness add = new AddContractBusiness(c);
		add.execute();
	}

	@Override
	public void updateContract(ContractDto dto) throws BusinessException {
		UpdateContractBusiness upd = new UpdateContractBusiness(dto);
		upd.execute();
	}

	@Override
	public void deleteContract(Long id) throws BusinessException {
		DeleteContractBusiness del = new DeleteContractBusiness(id);
		del.execute();
	}

	@Override
	public void finishContract(Long id, Date endDate) throws BusinessException {
		FinishContractBusiness fin = new FinishContractBusiness(id, endDate);
		fin.execute();
	}

	@Override
	public ContractDto findContractById(Long id) throws BusinessException {
		FindContractByIdBusiness find = new FindContractByIdBusiness(id);
		find.execute();
		return find.getContract();
	}

	@Override
	public List<ContractDto> findContractsByMechanicId(Long id) throws BusinessException {
		FindContractByMechanicIdBusiness find = new FindContractByMechanicIdBusiness(id);
		find.execute();
		return find.getList();
	}

}

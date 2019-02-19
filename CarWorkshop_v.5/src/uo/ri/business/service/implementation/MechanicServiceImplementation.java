package uo.ri.business.service.implementation;

import java.util.List;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.MechanicCrudService;
import uo.ri.business.transaction.management.mecanic.AddMechanicBusiness;
import uo.ri.business.transaction.management.mecanic.DeleteMechanicBusiness;
import uo.ri.business.transaction.management.mecanic.FindActiveMechanicsBusiness;
import uo.ri.business.transaction.management.mecanic.FindAllMechanicsBusiness;
import uo.ri.business.transaction.management.mecanic.FindMechanicByIdBusiness;
import uo.ri.business.transaction.management.mecanic.UpdateMechanicBusiness;

public class MechanicServiceImplementation implements MechanicCrudService {

	@Override
	public void updateMechanic(MechanicDto mechanic) throws BusinessException {
		UpdateMechanicBusiness update = new UpdateMechanicBusiness(mechanic);
		update.execute();
	}

	@Override
	public void addMechanic(MechanicDto mecanico) throws BusinessException {
		AddMechanicBusiness add = new AddMechanicBusiness(mecanico);
		add.execute();
	}

	@Override
	public void deleteMechanic(Long idMecanico) throws BusinessException {
		DeleteMechanicBusiness del = new DeleteMechanicBusiness(idMecanico);
		del.execute();
	}

	@Override
	public MechanicDto findMechanicById(Long id) throws BusinessException {
		FindMechanicByIdBusiness mechanicService = new FindMechanicByIdBusiness(id);
		return mechanicService.execute();
	}

	@Override
	public List<MechanicDto> findAllMechanics() throws BusinessException {
		FindAllMechanicsBusiness list = new FindAllMechanicsBusiness();
		return list.execute();
	}

	@Override
	public List<MechanicDto> findActiveMechanics() throws BusinessException {
		FindActiveMechanicsBusiness list = new FindActiveMechanicsBusiness();
		return list.execute();
	}

}

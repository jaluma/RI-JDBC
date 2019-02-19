package uo.ri.business.service.implementation;

import java.util.List;

import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.ContractCategoryCrudService;
import uo.ri.business.transaction.management.category.AddContractCategoryBusiness;
import uo.ri.business.transaction.management.category.DeleteContractCategoryBusiness;
import uo.ri.business.transaction.management.category.FindAllContractCategoriesBusiness;
import uo.ri.business.transaction.management.category.FindContractCategoryByIdBussines;
import uo.ri.business.transaction.management.category.UpdateContractCategoryBusiness;

public class ContractCategoryServiceImplementation implements ContractCategoryCrudService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.business.service.ContractCategoryCrudService#addContractCategory(uo.ri.
	 * business.dto.ContractCategoryDto)
	 */
	@Override
	public void addContractCategory(ContractCategoryDto dto) throws BusinessException {
		AddContractCategoryBusiness add = new AddContractCategoryBusiness(dto);
		add.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.business.service.ContractCategoryCrudService#deleteContractCategory(
	 * java.lang.Long)
	 */
	@Override
	public void deleteContractCategory(Long id) throws BusinessException {
		DeleteContractCategoryBusiness del = new DeleteContractCategoryBusiness(id);
		del.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.business.service.ContractCategoryCrudService#updateContractCategory(uo.
	 * ri.business.dto.ContractCategoryDto)
	 */
	@Override
	public void updateContractCategory(ContractCategoryDto dto) throws BusinessException {
		UpdateContractCategoryBusiness upd = new UpdateContractCategoryBusiness(dto);
		upd.execute();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.business.service.ContractCategoryCrudService#findContractCategoryById(
	 * java.lang.Long)
	 */
	@Override
	public ContractCategoryDto findContractCategoryById(Long id) throws BusinessException {
		FindContractCategoryByIdBussines find = new FindContractCategoryByIdBussines(id);
		find.execute();
		return find.getCategory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.business.service.ContractCategoryCrudService#findAllContractCategories(
	 * )
	 */
	@Override
	public List<ContractCategoryDto> findAllContractCategories() throws BusinessException {
		FindAllContractCategoriesBusiness find = new FindAllContractCategoriesBusiness();
		find.execute();
		return find.getList();
	}

}

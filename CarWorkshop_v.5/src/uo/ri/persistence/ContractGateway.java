package uo.ri.persistence;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.MechanicDto;

public interface ContractGateway extends SQLGateway {

	/**
	 * Añade un contrato
	 * 
	 * @param c
	 *            el contrato en cuestion
	 * @throws SQLException
	 *             si falla
	 */
	void addContract(ContractDto c) throws SQLException;

	/**
	 * Actualiza un contrato
	 * 
	 * @param dto
	 *            el contrato en cuestion
	 * @throws SQLException
	 *             si falla
	 */
	void updateContract(ContractDto dto) throws SQLException;

	/**
	 * Borra un contrato
	 * 
	 * @param dto
	 *            el contrato en cuestion
	 * @throws SQLException
	 *             si falla
	 */
	void deleteContract(ContractDto dto) throws SQLException;

	/**
	 * Finaliza un contrato
	 * 
	 * @param dto
	 *            el contrato en cuestion
	 * @param endDate
	 *            de cuando finaliza
	 * @throws SQLException
	 *             si falla
	 */
	void finishContract(ContractDto dto, Date endDate) throws SQLException;

	/**
	 * Buscar contrata a travez de un id
	 * 
	 * @param dto
	 *            a buscar
	 * @return el contrato
	 * @throws SQLException
	 *             si falla
	 */
	ContractDto findContractById(ContractDto dto) throws SQLException;

	/**
	 * Encuentra contratos de un mecanico
	 * 
	 * @param mechanic
	 *            a buscar
	 * @return lista de contratos
	 * @throws SQLException
	 *             si falla
	 */
	List<ContractDto> findContractsByMechanicId(MechanicDto mechanic) throws SQLException;
}

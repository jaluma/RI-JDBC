package uo.ri.persistence;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.MechanicDto;

public interface MechanicGateway extends SQLGateway {

	/**
	 * Añadir un mecanico
	 * 
	 * @param mechanic
	 *            a añadir
	 * @throws SQLException
	 *             si falla
	 */
	void add(MechanicDto mechanic) throws SQLException;

	/**
	 * Borrar un mecanico
	 * 
	 * @param mechanic
	 *            a borrar
	 * @throws SQLException
	 *             si falla
	 */
	void delete(MechanicDto mechanic) throws SQLException;

	/**
	 * Actualiza un mecanico
	 * 
	 * @param mechanic
	 *            a actualizar
	 * @throws SQLException
	 *             si falla
	 */
	void update(MechanicDto mechanic) throws SQLException;

	/**
	 * Lista los mecanicos
	 * 
	 * @return lista de mecanicos
	 * @throws SQLException
	 *             si falla
	 */
	List<MechanicDto> list() throws SQLException;

	/**
	 * Lista los mecanicos activos
	 * 
	 * @return lista de mecanicos
	 * @throws SQLException
	 *             si falla
	 */
	List<MechanicDto> listActive() throws SQLException;

	/**
	 * Encuentra un mecanico por su id
	 * 
	 * @param mechanic
	 *            a buscar
	 * @return mecanico si no existe
	 * @throws SQLException
	 *             si no hay o falla
	 */
	MechanicDto findMechanicById(MechanicDto mechanic) throws SQLException;

	/**
	 * Lista los mecanicos de una categoria
	 * 
	 * @param category
	 *            a buscar
	 * @return lista de mecanicos de una categoria
	 * @throws SQLException
	 *             si no hay o falla
	 */
	List<MechanicDto> listByCategoryId(ContractCategoryDto category) throws SQLException;

	/**
	 * Lista de mecanicos activos
	 * 
	 * @param payrollDate
	 *            a listar
	 * @return lista de mecanicos
	 * @throws SQLException
	 *             si no hay o falla
	 */
	List<MechanicDto> listActiveOrExt(Date payrollDate) throws SQLException;
}

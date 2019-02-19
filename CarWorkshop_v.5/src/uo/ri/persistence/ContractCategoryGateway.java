package uo.ri.persistence;

import java.sql.SQLException;
import java.util.List;

import uo.ri.business.dto.ContractCategoryDto;

public interface ContractCategoryGateway extends SQLGateway {

	/**
	 * Añade una categoria
	 * 
	 * @param category
	 *            a añadir
	 * @throws SQLException
	 *             si falla
	 */
	void add(ContractCategoryDto category) throws SQLException;

	/**
	 * Borra una categoria
	 * 
	 * @param category
	 *            a borrar
	 * @throws SQLException
	 *             si falla
	 */
	void delete(ContractCategoryDto category) throws SQLException;

	/**
	 * Actualiza una categoria
	 * 
	 * @param category
	 *            a actualizar
	 * @throws SQLException
	 *             si falla
	 */
	void update(ContractCategoryDto category) throws SQLException;

	/**
	 * Listar las categorias
	 * 
	 * @return lista de todas las categorias
	 * @throws SQLException
	 *             si falla
	 */
	List<ContractCategoryDto> listAllCategories() throws SQLException;

	/**
	 * Encontrar una categoria por una id
	 * 
	 * @param category
	 *            a buscar
	 * @return encuentra una categoria y la devuelve
	 * @throws SQLException
	 *             si falla
	 */
	ContractCategoryDto findCategoryById(Long category) throws SQLException;

}

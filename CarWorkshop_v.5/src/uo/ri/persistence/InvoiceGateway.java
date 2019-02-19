package uo.ri.persistence;

import java.sql.SQLException;
import java.util.Map;

import uo.ri.business.dto.InvoiceDto;

public interface InvoiceGateway extends SQLGateway {

	/**
	 * Crea una factura
	 * 
	 * @param invoice
	 *            a crear
	 * @return facturas
	 * @throws SQLException
	 *             si falla
	 */
	long crearFactura(InvoiceDto invoice) throws SQLException;

	/**
	 * Encuentra una factura
	 * 
	 * @param invoice
	 *            a buscar con un number
	 * @return factura
	 * @throws SQLException
	 *             si falla
	 */
	InvoiceDto findInvoice(InvoiceDto invoice) throws SQLException;

	/**
	 * Encuentra una factura
	 * 
	 * @param invoice
	 *            a buscar con un id
	 * @return factura
	 * @throws SQLException
	 *             si falla
	 */
	InvoiceDto findInvoicebyId(InvoiceDto invoice) throws SQLException;

	/**
	 * Obtiene el ultimo numero de factura
	 * 
	 * @return numero de la ultima factura
	 * @throws SQLException
	 *             si falla
	 */
	long getUltimoNumeroFactura() throws SQLException;

	/**
	 * Actualizar el importe de una averia
	 * 
	 * @param invoice
	 *            actualiza el importe de una averia
	 * @throws SQLException
	 *             si falla
	 */
	void actualizarImporteAveria(InvoiceDto invoice) throws SQLException;

	/**
	 * Aádir los cargos
	 * 
	 * @param invoice
	 *            de añadir
	 * @param cargos
	 *            cargos a añaadir
	 * @throws SQLException
	 *             si falla
	 */
	void addCargos(InvoiceDto invoice, Map<Long, Double> cargos) throws SQLException;
}

package uo.ri.persistence.implementatition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.InvoiceGateway;

public class InvoiceGatewayImplementation implements InvoiceGateway {
	private Connection connection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.SQLGateway#setConnection(java.sql.Connection)
	 */
	@Override
	public void setConnection(Connection conn) {
		connection = conn;
	}

	/**
	 * Devuelve el valor de la clave generada
	 * 
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	private long getGeneratedKey(long numeroFactura) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FACTURA_RECUPERAR_CLAVE_GENERADA"));
			pst.setLong(1, numeroFactura);
			rs = pst.executeQuery();
			rs.next();

			return rs.getLong(1);

		} finally {
			Jdbc.close(rs, pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.InvoiceGateway#crearFactura(uo.ri.business.dto.InvoiceDto)
	 */
	@Override
	public long crearFactura(InvoiceDto invoice) throws SQLException {
		PreparedStatement pst = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FACTURA_INSERTAR_FACTURA"));
			pst.setLong(1, invoice.number);
			pst.setDate(2, new java.sql.Date(invoice.date.getTime()));
			pst.setDouble(3, invoice.vat);
			pst.setDouble(4, invoice.total);
			pst.setString(5, "SIN_ABONAR");

			pst.executeUpdate();

			return getGeneratedKey(invoice.number); // Id de la nueva factura generada

		} finally {
			Jdbc.close(pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.InvoiceGateway#getUltimoNumeroFactura()
	 */
	@Override
	public long getUltimoNumeroFactura() throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FACTURA_ULTIMO_NUMERO_FACTURA"));
			rs = pst.executeQuery();

			if (rs.next()) {
				return rs.getLong(1) + 1; // +1, el siguiente
			} else { // todavía no hay ninguna
				return 1L;
			}
		} finally {
			Jdbc.close(rs, pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.InvoiceGateway#actualizarImporteAveria(uo.ri.business.dto.
	 * InvoiceDto)
	 */
	@Override
	public void actualizarImporteAveria(InvoiceDto invoice) throws SQLException {
		PreparedStatement pst = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FACTURA_UPDATE_IMPORTE_AVERIA"));
			pst.setDouble(1, invoice.total);
			pst.setLong(2, invoice.id);
			pst.executeUpdate();
		} finally {
			Jdbc.close(pst);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.InvoiceGateway#findInvoice(uo.ri.business.dto.InvoiceDto)
	 */
	@Override
	public InvoiceDto findInvoice(InvoiceDto invoice) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;

		InvoiceDto invoiceR;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_LIQUIDAR_FACTURA_RECUPERAR_FACTURA"));
			pst.setLong(1, invoice.number);

			rs = pst.executeQuery();

			rs.next();

			invoiceR = new InvoiceDto();
			invoiceR.id = rs.getLong(1);
			invoiceR.number = rs.getLong(2);
			invoiceR.date = rs.getDate(3);
			invoiceR.vat = rs.getLong(4);
			invoiceR.total = rs.getLong(5);
			invoiceR.status = rs.getString(6);

		} finally {
			Jdbc.close(rs, pst);
		}

		return invoiceR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.InvoiceGateway#findInvoice(uo.ri.business.dto.InvoiceDto)
	 */
	@Override
	public InvoiceDto findInvoicebyId(InvoiceDto invoice) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;

		InvoiceDto invoiceR;

		try {

			pst = connection
					.prepareStatement(Conf.getInstance().getProperty("SQL_LIQUIDAR_FACTURA_RECUPERAR_FACTURA_BY_ID"));
			pst.setLong(1, invoice.id);

			rs = pst.executeQuery();

			rs.next();

			invoiceR = new InvoiceDto();
			invoiceR.id = rs.getLong(1);
			invoiceR.number = rs.getLong(2);
			invoiceR.date = rs.getDate(3);
			invoiceR.vat = rs.getLong(4);
			invoiceR.total = rs.getLong(5);
			invoiceR.status = rs.getString(6);

		} finally {
			Jdbc.close(rs, pst);
		}

		return invoiceR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.InvoiceGateway#añadirCargos(uo.ri.business.dto.InvoiceDto,
	 * java.util.Map)
	 */
	@Override
	public void addCargos(InvoiceDto invoice, Map<Long, Double> cargos) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(Conf.getInstance().getProperty("SQL_LIQUIDAR_FACTURA_INSERTAR_PAGO"));

			for (long idMedio : cargos.keySet()) {
				ps.setDouble(1, cargos.get(idMedio));
				ps.setLong(2, invoice.id);
				ps.setLong(3, idMedio);

				ps.executeUpdate();
			}

			actualizarCargosYFactura(invoice, cargos);

		} finally {
			Jdbc.close(ps);
		}

	}

	/**
	 * Actualiza los cargos de la factura
	 * 
	 * @param invoice
	 * @param cargos
	 * @throws SQLException
	 */
	private void actualizarCargosYFactura(InvoiceDto invoice, Map<Long, Double> cargos) throws SQLException {
		PreparedStatement ps = null, ps1 = null;
		try {
			ps = connection
					.prepareStatement(Conf.getInstance().getProperty("SQL_LIQUIDAR_FACTURA_ACTUALIZAR_MEDIO_PAGO"));
			ps1 = connection
					.prepareStatement(Conf.getInstance().getProperty("SQL_LIQUIDAR_FACTURA_ACTUALIZAR_ESTAOD_FACTURA"));
			ps1.setString(1, "ABONADA");

			for (long idMedio : cargos.keySet()) {
				ps.setDouble(1, cargos.get(idMedio));
				ps.setLong(2, idMedio);

				ps1.setLong(2, invoice.id);

				ps.executeUpdate();
				ps1.executeUpdate();
			}
		} finally {
			Jdbc.close(ps);
		}

	}

}

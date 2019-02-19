package uo.ri.persistence.implementatition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.BreakdownGateway;

public class BreakdownGatewayImplementation implements BreakdownGateway {

	private Connection connection;

	@Override
	public void setConnection(Connection conn) {
		connection = conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.BreakdownGateway#FindInvoiceForId(long)
	 */
	@Override
	public InvoiceDto findInvoiceForId(long id) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		InvoiceDto invoice = new InvoiceDto();

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FACTURA_VERIFICAR_ESTADO_AVERIA"));

			pst.setLong(1, id);

			rs = pst.executeQuery();
			rs.next();

			invoice.id = id;
			invoice.status = rs.getString(1);

			rs.close();
		} finally {
			Jdbc.close(rs, pst);
		}
		return invoice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.BreakdownGateway#cambiarEstadoAverias(java.util.List,
	 * java.lang.String)
	 */
	@Override
	public void cambiarEstadoAverias(List<Long> idsAveria, String status) throws SQLException {
		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FACTURA_ACTUALIZAR_ESTADO_AVERIA"));

			for (Long idAveria : idsAveria) {
				pst.setString(1, status);
				pst.setLong(2, idAveria);

				pst.executeUpdate();
			}
		} finally {
			Jdbc.close(pst);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.BreakdownGateway#vincularAveriasConFactura(long,
	 * java.util.List)
	 */
	@Override
	public void vincularAveriasConFactura(long idFactura, List<Long> idsAveria) throws SQLException {

		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FACTURA_VINCULAR_AVERIA_FACTURA"));

			for (Long idAveria : idsAveria) {
				pst.setLong(1, idFactura);
				pst.setLong(2, idAveria);

				pst.executeUpdate();
			}
		} finally {
			Jdbc.close(pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.BreakdownGateway#consultaImporteRepuestos(uo.ri.business.
	 * dto.BreakdownDto)
	 */
	@Override
	public double consultaImporteRepuestos(BreakdownDto breakdown) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FACTURA_IMPORTE_REPUESTOS"));
			pst.setLong(1, breakdown.id);

			rs = pst.executeQuery();
			if (rs.next() == false) {
				return 0.0; // La averia puede no tener repuestos
			}

			return rs.getDouble(1);

		} finally {
			Jdbc.close(rs, pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.BreakdownGateway#consultaImporteManoObra(uo.ri.business.dto
	 * .BreakdownDto)
	 */
	@Override
	public double consultaImporteManoObra(BreakdownDto breakdown) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FACTURA_IMPORTE_MANO_OBRA"));
			pst.setLong(1, breakdown.id);

			rs = pst.executeQuery();
			rs.next();

			return rs.getDouble(1);

		} finally {
			Jdbc.close(rs, pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.BreakdownGateway#ReparacionesNoFacturadasUnClienteBusiness(
	 * uo.ri.business.dto.ClientDto)
	 */
	@Override
	public List<BreakdownDto> reparacionesNoFacturadasUnClienteBusiness(ClientDto cliente) throws SQLException {

		// Procesar
		PreparedStatement pst = null;
		ResultSet rs = null;
		BreakdownDto breakdown;
		List<BreakdownDto> list = new ArrayList<>();

		try {

			pst = connection
					.prepareStatement(Conf.getInstance().getProperty("SQL_REPARACIONES_NO_FACTURADAS_DE_UN_CLIENTE"));
			pst.setString(1, cliente.dni);

			rs = pst.executeQuery();

			while (rs.next()) {
				breakdown = new BreakdownDto();
				breakdown.id = rs.getLong(1);
				breakdown.date = rs.getDate(2);
				breakdown.status = rs.getString(3);
				breakdown.total = rs.getDouble(4);
				breakdown.description = rs.getString(5);
				list.add(breakdown);
			}

		} finally {
			Jdbc.close(rs, pst);
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.BreakdownGateway#getCountBreakdownFinishedByMechanic(uo.ri.
	 * business.dto.MechanicDto, java.util.Date, java.util.Date)
	 */
	@Override
	public Double getImporteAveriasTotales(MechanicDto m, Date startMonthDate, Date finishMonthDate)
			throws SQLException {

		// Procesar
		PreparedStatement pst = null;
		ResultSet rs = null;
		Double importe;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SUM_IMPORTE_BREAKDOWN"));
			pst.setLong(1, m.id);
			pst.setDate(2, new java.sql.Date(startMonthDate.getTime()));
			pst.setDate(3, new java.sql.Date(finishMonthDate.getTime()));

			rs = pst.executeQuery();

			rs.next();

			importe = rs.getDouble(1);

		} finally {
			Jdbc.close(rs, pst);
		}

		return importe;
	}

}

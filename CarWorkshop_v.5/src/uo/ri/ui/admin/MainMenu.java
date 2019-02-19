package uo.ri.ui.admin;

import alb.util.menu.BaseMenu;

public class MainMenu extends BaseMenu {

	/**
	 * Clase de un menu principal
	 */
	public MainMenu() {
		menuOptions = new Object[][] { { "Administrador", null }, { "Gestión de mecánicos", MecanicosMenu.class },
				{ "Gestión de repuestos", RepuestosMenu.class },
				{ "Gestión de tipos de vehículo", TiposVehiculoMenu.class },
				{ "Gestión de nominas", GestionNominasMenu.class },
				{ "Gestión de categorias", GestionCategoriasMenu.class } };
	}

	public static void main(String[] args) {
		new MainMenu().execute();
	}

}

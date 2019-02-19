package uo.ri.ui.admin;

import alb.util.menu.BaseMenu;
import uo.ri.ui.admin.action.categories.AddCategoryAction;
import uo.ri.ui.admin.action.categories.DeleteCategoryAction;
import uo.ri.ui.admin.action.categories.ListContractCategoryAction;
import uo.ri.ui.admin.action.categories.UpdateCategoryAction;

public class GestionCategoriasMenu extends BaseMenu {

	/**
	 * Menu ge gestion de categorias
	 */
	public GestionCategoriasMenu() {
		menuOptions = new Object[][] { { "Administrador > Gestión de categorias", null },

				{ "Añadir categoria", AddCategoryAction.class },
				{ "Listar mecánicos dada su categoria", ListContractCategoryAction.class },
				{ "Eliminar categorías", DeleteCategoryAction.class },
				{ "Modificar categoria", UpdateCategoryAction.class }, };
	}

}

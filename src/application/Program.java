package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();

		System.out.println("==== Test 1: findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);

		System.out.println("\n==== Test 2: findByDepartment ====");

		// Aqui eu to instanciando um departamento com id = 2
		Department department = new Department(2, null);
		/*
		 * Aqui na lista eu to procurando por todos os vendedores que pertencem aquele
		 * departamento
		 */
		List<Seller> list = sellerDao.findByDepartment(department);
		// Como saporra é uma lista uso o "forEach" do java para loopar a lista
		for (Seller obj : list) {
			System.out.println(obj);
		}
	}

}

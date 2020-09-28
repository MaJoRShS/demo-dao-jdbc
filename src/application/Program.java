package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();

		Scanner sc = new Scanner(System.in);
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

		System.out.println("\n==== Test 3: findaLL ====");

		// Aqui eu to reaproveitando a lista que já foi instanciada no método anterior.
		list = sellerDao.findAll();
		for (Seller obj : list) {
			System.out.println(obj);
		}

		System.out.println("\n==== Test 4: Seller Insert ====");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted! New id = " + newSeller.getId());

		System.out.println("\n==== Test 5: Seller Update ====");
		seller = sellerDao.findById(1);
		seller.setName("Martha Wayne");
		sellerDao.update(seller);
		System.out.println("Update Completed !");

		System.out.println("\n==== Test 6: Seller Delete ====");
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete Completed!");

		sc.close();

	}

}

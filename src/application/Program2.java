package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		DepartmentDao DepartmentDao = DaoFactory.createDepartmentDao();

		Scanner sc = new Scanner(System.in);
		System.out.println("==== Test 1: findById ====");
		Department department = DepartmentDao.findById(3);
		System.out.println(department);

		
		System.out.println("\n==== Test 2: findaLL ====");
		List<Department> list  = DepartmentDao.findAll();
		for (Department obj : list) {
			System.out.println(obj);
		}

	
		System.out.println("\n==== Test 3: Department Insert ====");
		Department newDepartment = new Department(null, "Musica");
		DepartmentDao.insert(newDepartment);
		System.out.println("Inserted! New id = " + newDepartment.getId());
		
		
		
		System.out.println("\n==== Test 4: Department Update ====");
		department = DepartmentDao.findById(8);
		department.setName("Anal");
		DepartmentDao.update(department);
		System.out.println("Update Completed !");
		
		

		System.out.println("\n==== Test 5: Department Delete ====");
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		DepartmentDao.deleteById(id);
		System.out.println("Delete Completed!");

		sc.close();

	}

}

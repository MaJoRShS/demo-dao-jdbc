package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	void insert(Seller obj);

	void update(Seller obj);

	void deleteById(Integer obj);

	Seller findById(Integer id);

	List<Seller> findAll();
	
	//Inclução de mais uma implementação na interface
	List<Seller> findByDepartment(Department department);

}

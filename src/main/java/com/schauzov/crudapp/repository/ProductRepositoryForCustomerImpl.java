package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.model.Product;

import javax.persistence.*;
import java.util.Set;

public class ProductRepositoryForCustomerImpl implements ProductRepositoryForCustomer {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Set<Product> findProductsWithLocaleAndCurrency(String locale, String currency) {


//        @Override
//        public List getEmployeesMaxSalary() {
//            return em.createQuery("from Employees where salary = (select max(salary) from Employees )", Employees.class)
//                    .getResultList();
//        }
    }
}

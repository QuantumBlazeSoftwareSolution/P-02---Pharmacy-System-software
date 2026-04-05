package com.qb.app.model.CRUD;

import com.qb.app.model.JPATransaction;
import com.qb.app.model.entity.Stock;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class StockCRUD {

    public static List<Stock> getStock() {
        return JPATransaction.runInTransaction((em) -> {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Stock> cq = cb.createQuery(Stock.class);
            Root<Stock> stockTable = cq.from(Stock.class);

            return em.createQuery(cq).getResultList();
        });
    }
}

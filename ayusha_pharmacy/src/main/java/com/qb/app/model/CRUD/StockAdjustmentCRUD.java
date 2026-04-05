package com.qb.app.model.CRUD;

import com.qb.app.model.JPATransaction;
import com.qb.app.model.entity.StockAdjustment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

/**
 *
 * @author Vihanga
 */
public class StockAdjustmentCRUD {

    public static List<StockAdjustment> getThisMonthStockAdjustments() {
        return JPATransaction.runInTransaction((em) -> {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<StockAdjustment> cq = cb.createQuery(StockAdjustment.class);
            Root<StockAdjustment> root = cq.from(StockAdjustment.class);

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            java.util.Date monthStart = cal.getTime();

            cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            cal.set(java.util.Calendar.SECOND, 59);
            cal.set(java.util.Calendar.MILLISECOND, 999);
            java.util.Date monthEnd = cal.getTime();

            cq.select(root)
                    .where(cb.between(root.get("dateTime"), monthStart, monthEnd));

            List<StockAdjustment> adjustments = em.createQuery(cq).getResultList();

            return adjustments != null ? adjustments : java.util.Collections.emptyList();
        });
    }
}

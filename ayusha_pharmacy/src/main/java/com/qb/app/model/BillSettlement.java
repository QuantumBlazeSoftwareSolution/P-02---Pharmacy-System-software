package com.qb.app.model;

import com.qb.app.controllers.beans.PaymentResult;

public class BillSettlement {

    public static PaymentResult calculateSettlement(
            double billAmount,
            double cashPaid,
            double cardPaid
    ) {
        if (billAmount <= 0) {
            return new PaymentResult(0, 0, 0, false);
        }

        if (cashPaid < 0 || cardPaid < 0) {
            return new PaymentResult(0, 0, 0, false);
        }

        double totalPaid = cashPaid + cardPaid;

        if (totalPaid < billAmount) {
            return new PaymentResult(0, 0, 0, false);
        }

        // Card is settled first
        double cardSettled = Math.min(cardPaid, billAmount);

        // Cash takes the remaining bill amount
        double cashSettled = billAmount - cardSettled;

        // Change given back to customer
        double change = totalPaid - billAmount;

        return new PaymentResult(cashSettled, cardSettled, change, true);
    }
}

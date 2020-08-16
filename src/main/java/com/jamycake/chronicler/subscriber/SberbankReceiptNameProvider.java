package com.jamycake.chronicler.subscriber;

public class SberbankReceiptNameProvider {

    public static final String PAYMENT = "Оплата";
    public static final String RECONCILIATION_OF_RESULTS = "Сверка итогов";
    public static final String UNKNOWN_RECEIPT_TYPE = "Unknown receipt type";

    public String getName(String receiptContent){

        String [] receiptStrings = receiptContent.split("[\n\r]+");

        if (receiptStrings[5].contains(PAYMENT)){
            return getPaymentReceiptName(receiptStrings);
        }
        if (receiptStrings[5].contains(RECONCILIATION_OF_RESULTS)){
            return getReconciliationReceiptName(receiptStrings);
        } else {
            return UNKNOWN_RECEIPT_TYPE;
        }
    }

    private String getPaymentReceiptName(String[] receiptStrings) {
        final int RECEIPT_INDICATOR_INDEX = 4;
        final int RECEIPT_SUM_INDEX = 12;

        String name = receiptStrings[RECEIPT_INDICATOR_INDEX] + receiptStrings[RECEIPT_SUM_INDEX];
        return makeReplaces(name);
    }

    private String getReconciliationReceiptName(String[] receiptStrings) {
        final int TOTAL_OPERATION_INDEX = 16;
        final int TOTAL_SUM_INDEX = 15;

        String name = RECONCILIATION_OF_RESULTS + " " +
                receiptStrings[TOTAL_OPERATION_INDEX] + "-" +
                receiptStrings[TOTAL_SUM_INDEX];
        return makeReplaces(name);
    }

    private String makeReplaces(String str){
        return str
                .replaceAll(":", "-")
                .replaceAll(" +", " ");
    }
}

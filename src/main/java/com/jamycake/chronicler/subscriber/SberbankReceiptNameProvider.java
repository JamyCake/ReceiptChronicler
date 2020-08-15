package com.jamycake.chronicler.subscriber;

public class SberbankReceiptNameProvider {

    public static final String PAYMENT = "Оплата";
    public static final String RECONCILIATION_OF_RESULTS = "Сверка итогов";
    public static final String UNKNOWN_RECEIPT_TYPE = "Unknown receipt type";

    public String getName(String [] receiptStrings){
        if (receiptStrings[5].contains(PAYMENT)){
            String name = receiptStrings[4] + receiptStrings[12];
            return makeReplaces(name);
        }
        if (receiptStrings[5].contains(RECONCILIATION_OF_RESULTS)){
            String name = RECONCILIATION_OF_RESULTS + " " +
                    receiptStrings[17] + "-" +
                    receiptStrings [16];
            return makeReplaces(name);
        } else {
            return UNKNOWN_RECEIPT_TYPE;
        }
    }

    private String makeReplaces(String str){
        return str
                .replaceAll(":", "-")
                .replaceAll(" +", " ");
    }
}

package io.github.halleypay;

public class HalleyWallet {

    private String codeCountry, userPhone, appKey;

    public HalleyWallet() {

    }

    public void configure(String codeCountry, String userPhone, String appKey) {
        this.codeCountry = codeCountry;
        this.userPhone = userPhone;
        this.appKey = appKey;
    }
}

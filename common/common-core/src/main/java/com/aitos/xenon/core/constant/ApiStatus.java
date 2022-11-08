package com.aitos.xenon.core.constant;



public enum ApiStatus implements ConstantsCode {

    /**
     * 操作成功
     */
    SUCCESS(0, "success"),
    /**
     * 操作失败
     */
    FAILED(1, "failed"),
    //鉴权相关

    VALIDATE_FAILED(1001,"Checkout failure"),

    VALIDATE_TIMESTAMP_NOEMPTY(1001,"Timestamp can not be empty"),
    VALIDATE_TIMESTAMP_EXPIRED(1002,"Timestamp has expired"),
    VALIDATE_TIMESTAMP_FORMAT(1003,"Timestamp format error"),

    VALIDATE_CLIENTID_NOEMPTY(1004,"client_id can't be empty"),
    VALIDATE_CLIENTID_EXPIRED(1005,"client_id timestamp has expired"),
    VALIDATE_CLIENTID_FAILED(1006,"client_id Incorrect"),

    VALIDATE_SIGN_NOEMPTY(1007,"sign can't be empty"),
    VALIDATE_SIGN_FAILED(1008,"sign Incorrect"),



    //系统错误
    ERROR(2000,"system abnormality"),
    TIMEOUT(2001,"overtime"),
    PARAMETER_FORMATE_ERROR(2002,"Parameter format error"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED(2003,"Parameter format error"),
    FORBIDDEN(2004,"No access rights, please contact the administrator for authorization"),
    REPEAT_RECORD(2005,"record repeat"),

    //业务错误
    /**
     * 设备相关
     */
    BUSINESS_DEVICE_ID_EXISTED(3001,"device id is existed"),

    BUSINESS_DEVICE_PAYER_SIGN_ERROR(3002,"payer sign error"),

    BUSINESS_DEVICE_OWNER_SIGN_ERROR(3003,"owner sign error"),

    BUSINESS_DEVICE_MINER_SIGN_ERROR(3004,"miner sign error"),

    BUSINESS_DEVICE_NOT_EXISTED(3005,"device  is not existed"),

    BUSINESS_DEVICE_NO_ONBOARD(3006,"miner did not do onboard"),
    BUSINESS_DEVICE_TERMINATE(3007,"miner has been terminated"),
    BUSINESS_DEVICE_BOUND(3008,"device is bound"),


    BUSINESS_AIRDROPDEVICE_NOT_EXISTED(3009,"airdrop device is not exist"),
    BUSINESS_AIRDROPDEVICE_EXISTED(3010,"airdrop device is existed"),
    BUSINESS_AIRDROPDEVICE_CLAIMED(3011,"airdrop device is claimed"),
    BUSINESS_AIRDROPDEVICE_CLAIM_EXPIRED(3012,"airdrop device is claim expired"),
    BUSINESS_OWNER_BIND_APPLY_SIGN_ERROR(3013,"The signature of the owner binding application is incorrect"),
    BUSINESS_DEVICE_MAKER_INFO_ERROR(3014,"maker information error"),
    BUSINESS_DEVICE_GMAE_MINER_TOTAL_NUMBER(3015,"Total miner exceeds limit"),
    BUSINESS_DEVICE_GMAE_MINER_SINGLE_NUMBER(3016,"The amount of miners that a single user can apply for exceeds the limit"),
    BUSINESS_NFT_CASTING_FAILED(3017,"nft casting failed"),
    BUSINESS_DEVICE_GMAE_MINER_SINGLE_NUMBER_FOR_EMAIL(3018,"The amount of miners that a single email can apply for exceeds the limit"),
    CLAIM_GAMING_MINER_CODE_MISMATCH(3019,"claim code Mismatch"),


    // 账户相关
    BUSINESS_ACCOUNT_EXISTED(4001,"account is existed"),
    BUSINESS_ACCOUNT_NOT_EXIST(4002,"account is not exist"),
    BUSINESS_PAYER_ACCOUNT_NOT_EXIST(4003,"payer account is not exist"),
    BUSINESS_PAYER_ACCOUNT_NOT_ENOUGH(4004,"payer account not enough"),
    BUSINESS_TRANSACTION_FAIL(4005,"transaction failed"),
    BUSINESS_TRANSACTION_SIGN_ERROR(4006,"transaction signature error"),
    BUSINESS_OWNER_ACCOUNT_NOT_EXIST(4007,"owner account is not exist"),
    BUSINESS_FOUNDATION_SIGN_ERROR(4008,"foundation signature error"),
    BUSINESS_ACCOUNT_REWARD_SEARCH_DAY_RANGE_ERROR(4009,"account reward days range error"),
    BUSINESS_ACCOUNT_MAKER_NOT_EXIST(4010,"maker account is not exist"),
    BUSINESS_ACCOUNT_SIGN_ERROR(4011,"maker account signature error"),
    BUSINESS_ACCOUNT_BLANCE_INSUFFICIENT(4012,"Insufficient account balance"),
    BUSINESS_ACCOUNT_NONCE_MISMATCH_PATTERN(4013,"nonce pattern mismatch"),


    // pogg相关
    BUSINESS_POGG_REPORT_SIGN_ERROR(4101,"pogg report sign error"),

    BUSINESS_POGG_RESPONSE_SIGN_ERROR(4102,"pogg response sign error"),

    BUSINESS_POGG_CHALLENGE_EXPIRED(4103,"pogg challenge expired"),

    BUSINESS_POGG_CHALLENGE_NO_HIT(4104,"pogg challenge no hit"),

    // 空投相关
    BUSINESS_AIRDROP_NOT_ACTIVE(6001,"airdrop not active"),
   ;


    ApiStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}

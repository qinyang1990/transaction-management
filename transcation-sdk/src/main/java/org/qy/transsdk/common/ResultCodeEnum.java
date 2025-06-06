package org.qy.transsdk.common;

/**
 * 异常时的状态码，主要为了多语言切换
 *
 * @author qinyang
 * @date 2025/6/5 14:05
 */
public enum ResultCodeEnum {

    parameter_transtype,
    parameter_transid,
    parameter_clientid,
    parameter_accountid,
    parameter_amount,
    parameter_page,

    accountid_not_exist,
    transid_not_exist,

    trans_amount_negative,
    trans_bal_trans_type,
    trans_bal_lack,
    trans_id_exist,
    trans_bal_no_modify,

    page_size_too_large,

    fail_db,
    fail_other;

    public static int getCode(ResultCodeEnum codeEnum) {
        return codeEnum.ordinal()+1;
    }
}

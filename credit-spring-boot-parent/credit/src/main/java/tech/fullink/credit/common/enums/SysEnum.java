package tech.fullink.credit.common.enums;

import lombok.Getter;

@Getter
public enum SysEnum {
    TASKSCHEDULER("TASKSCHEDULER", "任务中心", "01"),

    BIZCENTER("BIZCENTER", "业务中心", "02"),

    BILLCENTER("BILLCENTER", "账单中心", "03"),

    PAYCENTER("PAYCENTER", "支付中心", "04"),

    CFSCENTER("CFSCENTER", "综管台", "05"),

    CIF("CIF", "客户体系", "06"),

    ACCOUNT("ACCOUNT", "账务", "07"),

    ACCOUNTTING("ACCOUNTTING", "会计", "08"),

    ACCOUNTQUERY("ACCOUNTQUERY", "账务查询", "09"),

    MARKET("MARKET", "营销中心", "11"),

    AUTOCREDIT("AUTOCREDIT", "自动授信贷款", "12"),

    MSGCENTER("MSGCENTER", "消息中心", "15"),

    CREDITFRONT("CREDITFRONT", "微前置", "16"),

    COMMON("COMMON", "通用服务", "17"),

    LIMIT("LIMIT", "授信额度管理", "19"),

    CHECK("CHECK", "对账", "21"),

    MXAUTH("MXAUTH", "认证服务", "23"),

    MXAUTH_NOTIFY("MXAUTH_NOTIFY", "魔蝎消息分发服务", "25"),

    MXAUTH_APIGATE("MXAUTH_APIGATE", "MXAUTH认证api", "27"),

    CREDIT_TRADE_LOAN("CREDIT_TRADE_LOAN", "借款服务", "29"),

    CREDIT_TRADE_REPAY("CREDIT_TRADE_REPAY", "还款服务", "30"),

    CREDIT_TRADE_QUERY("CREDIT_TRADE_QUERY", "查询服务", "31"),

    PAY_CORE("PAY_CORE", "支付核心", "32"),

    CREDIT_TRADE_LOAN_GATE("CREDIT_TRADE_LOAN_GATE", "借款前置", "33"),

    CIF_GATEWAY("CIF_GATEWAY", "客户网关", "41"),

    NOTIFICATION("NOTIFICATION", "通知中心", "34"),

    CREDIT_BILL("CREDIT_BILL", "账单服务", "35"),

    CIF_GATE("CIF_GATE", "客户体系前置", "36"),

    OLAP("OLAP", "大盘统计", "37"),

    ACCQUIRING("ACCQUIRING", "收单", "40"),

    PAYDAYLOAN_GATE("PAYDAYLOAN_GATE", "PAYDAYLOAN_GATE", "43"),

    RISK("RISK", "风控", "42"),

    AGREEMENT("AGREEMENT", "协议服务", "45"),;

    /**
     * 英文说明
     */
    private String egname;

    /**
     * 说明
     */
    private String desc;

    /**
     * 编号
     */
    private String number;

    /**
     * 私有构造方法
     *
     * @param egname
     * @param desc
     * @param number
     */
    SysEnum(String egname, String desc, String number) {
        this.egname = egname;
        this.desc = desc;
        this.number = number;
    }

    public static SysEnum getEnumByEgname(String egname) {

        for (SysEnum e : SysEnum.values()) {
            if (e.egname.equals(egname)) {
                return e;
            }
        }
        return null;
    }

}

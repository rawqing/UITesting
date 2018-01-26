package com.yq.milk.constant;

import static com.trubuzz.trubuzz.test.R.string.accrued_cash_explain;
import static com.trubuzz.trubuzz.test.R.string.available_funds_explain;
import static com.trubuzz.trubuzz.test.R.string.buying_power_explain;
import static com.trubuzz.trubuzz.test.R.string.equity_with_loan_value_explain;
import static com.trubuzz.trubuzz.test.R.string.excess_liquidity_explain;
import static com.trubuzz.trubuzz.test.R.string.gross_position_value_explain;
import static com.trubuzz.trubuzz.test.R.string.init_margin_req_explain;
import static com.trubuzz.trubuzz.test.R.string.maint_margin_req_explain;
import static com.trubuzz.trubuzz.test.R.string.net_liquidation_explain;
import static com.trubuzz.trubuzz.test.R.string.today_portfolio_explain;
import static com.trubuzz.trubuzz.test.R.string.total_amount_explain;
import static com.trubuzz.trubuzz.test.R.string.total_pnl_explain;
import static com.trubuzz.trubuzz.utils.God.getString;

/**
 * Created by king on 2016/10/19.
 */

public enum Nouns {
    /*购买力*/                 buying_power_n(getString(buying_power_explain)),
    /*净清算值*/               net_liquidation_n(getString(net_liquidation_explain)),
    /*净累计利息*/             accrued_cash_n(getString(accrued_cash_explain)),
    /*有贷款价值的资产*/       equity_with_loan_value_n(getString(equity_with_loan_value_explain)),
    /*股票+期权*/              gross_position_value_n(getString(gross_position_value_explain)),
    /*初始准备金*/             init_margin_req_n(getString(init_margin_req_explain)),
    /*维持准备金*/             maint_margin_req_n(getString(maint_margin_req_explain)),
    /*剩余流动性*/             excess_liquidity_n(getString(excess_liquidity_explain)),
    /*持仓总额*/               total_amount_n(getString(total_amount_explain)),
    /*可用资金*/               available_funds_n(getString(available_funds_explain)),
    /*总收益*/                 total_pnl_n(getString(total_pnl_explain)),
    /*当日收益*/               today_portfolio_n(getString(today_portfolio_explain)),
    ;






    private final String value;

    Nouns(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

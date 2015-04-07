package com.gs.thrift.result;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GenThriftResult {

    private GenThriftFlag flag;
    private String error;

    public GenThriftFlag getFlag() {
        return flag;
    }

    public void setFlag(GenThriftFlag flag) {
        this.flag = flag;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

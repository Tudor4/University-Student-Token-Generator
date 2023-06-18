package com.cspub.licenta.Server;

import org.springframework.stereotype.Component;

@Component
public class IssueNftDto {
    public String name;
    public String ticker;

    public IssueNftDto() {}

    public IssueNftDto(String name, String ticker) {
        this.name = name;
        this.ticker = ticker;
    }
    @Override
    public String toString() {
        return "IssueNftDto{" +
                "name='" + name + '\'' +
                ", ticker='" + ticker + '\'' +
                '}';
    }
}

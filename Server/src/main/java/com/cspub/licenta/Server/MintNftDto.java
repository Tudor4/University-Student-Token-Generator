package com.cspub.licenta.Server;

public class MintNftDto {
    public String name;
    public String sendToAddress;

    public MintNftDto() {}

    public MintNftDto(String name, String sendToAddress) {
        this.name = name;
        this.sendToAddress = sendToAddress;
    }

    @Override
    public String toString() {
        return "MintNftDto{" +
                "name='" + name + '\'' +
                ", sendToAddress='" + sendToAddress + '\'' +
                '}';
    }
}

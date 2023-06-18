package com.cspub.licenta.Server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.binary.Hex;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class Controller {
    @Autowired
    MainService service;

    @PostMapping("/issue-nft")
    public ResponseEntity issue_nft(@RequestBody IssueNftDto issueNftDto) throws IOException {
        System.out.println(issueNftDto);
        System.out.println(Hex.encodeHexString(issueNftDto.name.getBytes(StandardCharsets.UTF_8)));
        String result = service.issue_collection("0x" + Hex.encodeHexString(
                issueNftDto.name.getBytes(StandardCharsets.UTF_8)),
                "0x" + Hex.encodeHexString(issueNftDto.ticker.getBytes(StandardCharsets.UTF_8)));
        System.out.println(result);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/mint-nft")
    public ResponseEntity mint_nft(@RequestBody MintNftDto mintNftDto) throws IOException, InterruptedException {
        System.out.println(mintNftDto);
        String result = service.mintNft("0x" + Hex.encodeHexString(
                mintNftDto.name.getBytes(StandardCharsets.UTF_8)),
                "0x" + Hex.encodeHexString(mintNftDto.sendToAddress.getBytes(StandardCharsets.UTF_8)));
        System.out.println(result);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/get-nfts")
    public ResponseEntity get_nfts(@RequestBody GetNftsDto getNftsDto) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(
                "https://testnet-api.multiversx.com/accounts/"
                        + getNftsDto.address + "/nfts", String.class);
        return response;
    }

}

package com.cspub.licenta.Server;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class MainService {
    public Map<String, String> loginData = new HashMap<>();
    public Map<String, String> userAddress = new HashMap<>();

    public int login(String username, String password) {
        if (loginData.containsKey(username)) {
            if (loginData.get(username).equals(password))
                return 1;
            else
                return 0;
        } else
            return -1;
    }

    public int register(String username, String password, String address) {
        if (loginData.containsKey(username))
            return 0;
        loginData.put(username, password);
        userAddress.put(username, address);
        return 1;
    }

    public String issue_collection(String name, String ticker) throws IOException {
        try {
            String[] command = {
                    "mxpy",
                    "contract",
                    "call",
                    "erd1qqqqqqqqqqqqqpgqnut80yd4ylsaz4m8nqxh0lw50af3hv0l0s4qldwdnj",
                    "--function",
                    "issue_nft",
                    "--value",
                    "50000000000000000",
                    "--recall-nonce",
                    "--pem",
                    "~/facultate/licenta/ping-pong/wallet/wallet-owner.pem",
                    "--gas-limit",
                    "60000000",
                    "--arguments",
                    name,
                    ticker,
                    "--send"
            };
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process proc = processBuilder.start();

            BufferedReader input =
                    new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader error =
                    new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = error.readLine()) != null) {
                System.out.println(line);
            }
            proc.waitFor();
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String mintNft(String name, String address) throws IOException, InterruptedException {
        try {
            String[] command = {
                    "mxpy",
                    "contract",
                    "call",
                    "erd1qqqqqqqqqqqqqpgqnut80yd4ylsaz4m8nqxh0lw50af3hv0l0s4qldwdnj",
                    "--function",
                    "mint_nft",
                    "--recall-nonce",
                    "--pem",
                    "~/facultate/licenta/ping-pong/wallet/wallet-owner.pem",
                    "--gas-limit",
                    "60000000",
                    "--arguments",
                    name,
                    address,
                    "--send"
            };
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process proc = processBuilder.start();

            BufferedReader input =
                    new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader error =
                    new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = error.readLine()) != null) {
                System.out.println(line);
            }
            proc.waitFor();
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
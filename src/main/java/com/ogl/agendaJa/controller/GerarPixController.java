package com.ogl.agendaJa.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GerarPixController {

    private String gerarPixPayload(String chavePix, String nomeRecebedor, String cidade, String valor) {
        nomeRecebedor = removerAcentos(nomeRecebedor);
        if (nomeRecebedor.length() > 25) {
            nomeRecebedor = nomeRecebedor.substring(0, 25);
        }
        cidade = removerAcentos(cidade);

        String gui = "BR.GOV.BCB.PIX";
        String infoChave = "01" + String.format("%02d", chavePix.length()) + chavePix;
        String merchantAccountInfo = "26" + String.format("%02d", 4 + gui.length() + infoChave.length()) + "00" + String.format("%02d", gui.length()) + gui + infoChave;

        String payload =
                "000201" +                           // formato payload
                        merchantAccountInfo +               // info conta recebedor
                        "52040000" +                         // categoria
                        "5303986" +                          // moeda transacao
                        "54" + String.format("%02d", valor.length()) + valor + // valor trasacao
                        "5802BR" +                           // cod. pais
                        "59" + String.format("%02d", nomeRecebedor.length()) + nomeRecebedor + // nome de quem recebe
                        "60" + String.format("%02d", cidade.length()) + cidade +               // cidade de quem recebe
                        "62" + "07" + "05" + "03" + "***";  // txid

        String crc = calcularCRC16(payload + "6304");
        return payload + "6304" + crc;
    }

    private String calcularCRC16(String input) {
        int polinomio = 0x1021;
        int resultado = 0xFFFF;

        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            resultado ^= (b << 8);
            for (int i = 0; i < 8; i++) {
                if ((resultado & 0x8000) != 0) {
                    resultado = (resultado << 1) ^ polinomio;
                } else {
                    resultado <<= 1;
                }
            }
        }

        return String.format("%04X", resultado & 0xFFFF);
    }

    private String removerAcentos(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    @GetMapping("/negocio/qrcode-pix")
    public ResponseEntity<byte[]> gerarQrCodePix(@RequestParam String chave, @RequestParam String nome, @RequestParam String cidade, @RequestParam String valor) throws Exception {
        String cidadeFormatada = Normalizer.normalize(cidade.split("[,-]")[0], Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        String valorFormatado = valor.replace(",", ".");
        String tipoChave;
        if (chave.contains("@")) {
            tipoChave = "email";
        } else if (chave.startsWith("+55")) {
            chave = chave.replaceAll("\\s+", "");
        } else {
            chave = chave.replaceAll("[.-]", "");
        }
        String payload = gerarPixPayload(chave, nome, cidadeFormatada, valorFormatado);

        BitMatrix matrix = new MultiFormatWriter().encode(payload, BarcodeFormat.QR_CODE, 300, 300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

}
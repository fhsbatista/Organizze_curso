package com.mindfree.fbatista.organizze.helper;

import java.text.SimpleDateFormat;

/**
 * Created by fbatista on 20/03/18.
 */

public class DateCustom {

    public static String dataAtual(){

        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateAtual = simpleDateFormat.format(data);

        return dateAtual;

    }

    public static String getPeriodo(String data){

        String[] posicoes = data.split("/");
        String mes = posicoes[1];
        String ano = posicoes[2];

        String periodo = mes + ano;

        return periodo;

    }
}

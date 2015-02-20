/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.util;

import java.util.Arrays;

/**
 *
 * @author j2cf
 */
public class Auxiliary {
	//separa a string recebida do git em um array, contendo o nome, qtde de commmits e o e-mail
	public static String[] getSplittedLine(String line){
		line = line.trim();
		String[] datas = line.split("\t");
		String[] result = {	datas[1].substring(0, datas[1].indexOf("<")), 
							datas[1].substring(datas[1].indexOf("<") + 1, datas[1].length() - 1),
							datas[0]
		};
		return result;
	}
	
}

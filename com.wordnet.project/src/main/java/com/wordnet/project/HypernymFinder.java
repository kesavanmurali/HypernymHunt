package com.wordnet.project;

import java.util.LinkedList;
import java.util.List;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;

public class HypernymFinder {

	private static final String INPUT = "vibration";
	private static final String INPUT1 = "vibration";
	private static final String INPUT2 = "marriage";
	private static PointerTargetNodeList directHypernyms;
	private static List<String> synsetExtract = new LinkedList<String>();
	
	private static List<String> synsetExtract1 = new LinkedList<String>();
	private static List<String> synsetExtract2 = new LinkedList<String>();

	public static void getFirstHypernym(List<String> synsetExtract, Dictionary dictionary, String word) throws JWNLException {
		for (POS pos : POS.getAllPOS()) {
			IndexWord indexWord = dictionary.getIndexWord(pos, word);
			if (indexWord != null) {
				List<Synset> synsetList = indexWord.getSenses();
				directHypernyms = PointerUtils.getDirectHypernyms(synsetList.get(0));
				for (PointerTargetNode firsthypernym : directHypernyms) {
					Synset firstHypernym = firsthypernym.getSynset();
					for (Word word1 : firstHypernym.getSynset().getWords()) {
						String hyperStr = word1.getLemma();
						if (!synsetExtract.contains(hyperStr)) {
							synsetExtract.add(hyperStr);
							getFirstHypernym(synsetExtract, dictionary, hyperStr);
							return;
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) throws JWNLException {

		Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		getFirstHypernym(synsetExtract, dictionary, INPUT);
		
		System.out.print("Inherited Hypernyms are: " + INPUT);
		for(String hypernym : synsetExtract) {
			System.out.print("--->" + hypernym);
		}
		System.out.println();

		getFirstHypernym(synsetExtract1, dictionary, INPUT1);
		getFirstHypernym(synsetExtract2, dictionary, INPUT2);
		for(String hypernym1 : synsetExtract1) {
			for(String hypernym2 : synsetExtract2) {
				if(hypernym1.equals(hypernym2)) {
					System.out.println("The common hypernym is: "+ hypernym1);
					return;
				}
			}
		}
		System.out.println("No common hypernym is found.");
	}

}

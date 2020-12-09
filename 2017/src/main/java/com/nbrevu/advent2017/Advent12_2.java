package com.nbrevu.advent2017;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.koloboke.collect.set.IntSet;
import com.koloboke.collect.set.hash.HashIntSets;

public class Advent12_2 {
	private final static String IN_FILE="Advent12.txt";
	
	private final static Pattern SPLITTER=Pattern.compile(", ");
	
	private static List<IntSet> parseMap(List<String> content)	{
		List<IntSet> result=new ArrayList<>();
		for (String s:content)	{
			IntSet connected=HashIntSets.newMutableSet();
			String[] split1=s.split(" <-> ");
			if ((split1.length!=2)||!Integer.toString(result.size()).equals(split1[0])) throw new IllegalArgumentException("Can't parse.");
			SPLITTER.splitAsStream(split1[1]).mapToInt(Integer::parseInt).forEach(connected::add);
			result.add(connected);
		}
		return result;
	}
	
	private static BitSet getGroup(List<IntSet> connexions,int startGroup)	{
		BitSet result=new BitSet(connexions.size());
		Queue<Integer> pending=new ArrayDeque<>();
		pending.add(startGroup);
		while (!pending.isEmpty())	{
			int value=pending.poll();
			if (result.get(value)) continue;
			result.set(value);
			pending.addAll(connexions.get(value));
		}
		return result;
	}
	
	private static int countGroups(List<IntSet> connexions)	{
		BitSet pending=new BitSet(connexions.size());
		pending.set(0,connexions.size(),true);
		int value=0;
		int result=0;
		while (pending.cardinality()>0)	{
			value=pending.nextSetBit(value);
			BitSet group=getGroup(connexions,value);
			pending.andNot(group);
			++result;
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException	{
		URL file=Resources.getResource(IN_FILE);
		List<String> content=Resources.readLines(file,Charsets.UTF_8);
		List<IntSet> connexions=parseMap(content);
		int result=countGroups(connexions);
		System.out.println(result);
	}
}

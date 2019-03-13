package ru.lermosk;

import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodOfUndeterminedCoefficients {
    private MethodOfUndeterminedCoefficients() { }

    public static String minimization(List<String> zeros, List<String> ones){

    List<Pair<String,String>> zeroCoefficients=findZeroCoefficients(zeros);
    List<List<Pair<String,String>>>   systemOfEquations=makeSystemOfEquations(ones);
    List<Pair<String,String>> answerOnSystemOfEquations=new ArrayList<>();
    for (int i = 0; i < systemOfEquations.size(); i++) {
       systemOfEquations.get(i).removeAll(zeroCoefficients);
    }
    Collections.sort(systemOfEquations, new Comparator<List<Pair<String, String>>>() {
        @Override
        public int compare(List<Pair<String, String>> o1, List<Pair<String, String>> o2) {
            return Integer.valueOf(o1.get(0).getKey().length()).compareTo(Integer.valueOf(o2.get(0).getKey().length()));
        }
    });
    answerOnSystemOfEquations.add(systemOfEquations.get(0).get(0));
    for (int i = 1; i < systemOfEquations.size(); i++) {
        boolean isContainsOne=false;
        for (Pair<String,String> coef: answerOnSystemOfEquations) {
            if(systemOfEquations.get(i).contains(coef)) isContainsOne=true;
        }
        if (!isContainsOne) {
            answerOnSystemOfEquations.add(systemOfEquations.get(i).get(0));
        }
    }
      return decode(answerOnSystemOfEquations);
}
private static String decode(List<Pair<String, String>> coefficients){
    String resalt="";
    for(Pair<String,String> coef: coefficients){
        for (int i = 0; i <coef.getKey().length() ; i++) {
           if(coef.getKey().charAt(i)=='0') resalt+="-x"+coef.getValue().charAt(i);
                   else resalt+="x"+coef.getValue().charAt(i);
        }
        resalt+="+";
    }
    return resalt.substring(0,resalt.length()-1);
}
private static List<List<Pair<String,String>>>  makeSystemOfEquations(List<String> ones){
    List<List<Pair<String,String>>>   systemOfEquations=new ArrayList<>();
    for(String str:ones){
        systemOfEquations.add(makeCoefficients(str));
    }
    return systemOfEquations;
}

private static List<Pair<String,String>> findZeroCoefficients(List<String> zeros){
    List<Pair<String,String>> zeroCoefficients=new ArrayList<>();
for(String str: zeros) {
    for (Pair<String,String> coef : makeCoefficients(str)) {
        if(!zeroCoefficients.contains(coef)) zeroCoefficients.add(coef);
    }
}
    return zeroCoefficients;
}
private static List<Pair<String,String>> makeCoefficients(String str){
    List<Pair<String,String>> coefficients=new ArrayList<>();
    try {
        String content = new Scanner(new File("lowindexes.txt")).useDelimiter("\\Z").next();
        List<String> lowIndexes=new ArrayList<>();
        Pattern pattern = Pattern.compile("([\\d][\\d]*),");
        Matcher m = pattern.matcher(content);
        while (m.find()) {
            lowIndexes.add(m.group(1));
        }
        for(String lowIndex: lowIndexes){
            String upperIndex="";
            for (int i = 0; i < lowIndex.length(); i++) {
                Integer indexOfVariable=Integer.parseInt(String.valueOf(lowIndex.charAt(i)))-1;
                upperIndex+=str.charAt(indexOfVariable);
            }
            coefficients.add(new Pair<>(upperIndex,lowIndex));
        }
    }
    catch (IOException e){
        e.printStackTrace();
    }
    return coefficients;
}
    public static void main(String[] args) {
        try {
            String content = new Scanner(new File(args[0])).useDelimiter("\\Z").next();
            String content2 = new Scanner(new File(args[1])).useDelimiter("\\Z").next();
            List<String> zeros=new ArrayList<>();
            List<String> ones=new ArrayList<>();
            Pattern pattern = Pattern.compile("([\\d][\\d]*),");
            Matcher m = pattern.matcher(content);
            Matcher m2 = pattern.matcher(content2);
            while (m.find()) {
                zeros.add(Integer.toBinaryString(Integer.parseInt(m.group(1))));
            }
            while (m2.find()) {
                ones.add(Integer.toBinaryString(Integer.parseInt(m2.group(1))));
            }
            for (int i = 0; i < zeros.size(); i++) {
                String zero="00000";
                zeros.set(i, zero.substring(0,6-zeros.get(i).length())+zeros.get(i));
                ones.set(i, zero.substring(0,6-ones.get(i).length())+ones.get(i));
            }
            System.out.println(MethodOfUndeterminedCoefficients.minimization(zeros, ones));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

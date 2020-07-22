package com.example.fixparsing;

import java.util.LinkedHashMap;
import java.util.Map;

public class FIXMessageHandling {

    /**
     * 把输入的fixmessage拆分成键值对，并将键值对存入linkedHashMap中
     */
    public Map<String, String> TagValue(String fixmessage) {
        Map<String, String> tagvalue = new LinkedHashMap<>();  //保证插入顺序
        if(isValid(fixmessage)){
            String[] arrsplit = fixmessage.split("\002");
            for(String strsplit : arrsplit){
                String[] arrSplitEqual;
                arrSplitEqual = strsplit.split("=");
                tagvalue.put(arrSplitEqual[0],arrSplitEqual[1]);
            }
        }

        return tagvalue;
    }

    boolean isValid(String input) {
        String[] arrsplit = input.split("\002");
        for(String strsplit : arrsplit){
            if(hasUniqueEqual(strsplit)){
                String[] arrSplitEqual = strsplit.split("=");
                if(validTag(arrSplitEqual[0])){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        return false;
    }

    public boolean hasUniqueEqual(String s){
        int count = 0;
        char[] sarray = s.toCharArray();
        for(char ss : sarray){
            if(ss == '='){
                count++;
            }
        }
        if(count==1)
            return true;
        return false;
    }

    public boolean validTag(String tag){
        String validTag[] = {"7","8","9","10","16","34","35","36","43","45","49","52","56","97","98",
                "108","112","122","123","371","372","373","380","464","554","789","925","1128","1137",
                "1400","1402","1404","1409","11","14","17","18","19","22","31","32","37","38","39","40",
                "41","44","48","54","58","59","60","77","102","103","132","133","134","135","150","151",
                "207","295","298","300","378","379","390","434","447","448","452","453","487","528","529",
                "530","531","532","537","552","553","571","574","576","577","751","828","856","880","893",
                "923","924","939","1003","1090","1093","1115","1123","1166","1300","1328","1369","1511",
                "1512","1535","1538","1539","1610","1611","1612","1613","1614","1615","1616","1656","1671",
                "1691","1692","1693","1770","1771","1772","1773","1774","1775","1776","1777","1778","1779",
                "1780","1812","1813","1814","1867","1868","1869","1870","5681"};
        int length = validTag.length;
        while(length-- > 0){
            if(tag.equals(validTag[length])){
                return true;
            }
        }
        return false;
    }


    public StringBuilder Translate(String fixmessage) {
        Map<String, String> tagvalue = TagValue(fixmessage);
        StringBuilder out = new StringBuilder();
        MapDef mapDef = new MapDef();

        for(String key : tagvalue.keySet()){
            String tagTrans, valueTrans, tv;
            ParsedMessage parsedMessage = new ParsedMessage();
            if(mapDef.getTagFlag(key)){
                tagTrans = mapDef.getTagMessageMap().get(key);
                valueTrans = mapDef.getValueMessageMap().get(key).apply(tagvalue.get(key));
            }else{
                tagTrans = mapDef.getTagMessageMap().get(key);
                valueTrans = tagvalue.get(key);
            }
            parsedMessage.setTagParsing(tagTrans);
            parsedMessage.setValueParsing(valueTrans);
            tv = parsedMessage.getTagParsing() + ":" + parsedMessage.getValueParsing();
            out.append(tv).append("\n");
            //System.out.println(parsedMessage.getTagParsing()+"为"+parsedMessage.getValueParsing());
        }

        return out;

    }

}

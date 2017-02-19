package com.sureone.igs;
public class IgsHelper {
    public static IgsField calField(String line,String name,String next) {
        int idx = line.indexOf(name);
        if(idx==-1) return null;
        int idx2=line.length()-1;
        if(next!=null) {
            idx2 = line.indexOf(next);
            if(idx2==-1) return null;
        }
        return new IgsField(idx,idx2);
    }
    public static Integer elementI(char[] line,int index,char del1,char del2) {
        String s = elementS(line,index,del1,del2);
        if(s==null) return null;
        char c = s.charAt(0);
        if((c>='0' && c<='9')||(c=='-')) {
            return Integer.valueOf(s);
        }
        return null;
    }
    public static String elementS(String line,int index,char del1,char del2) {
        char[] cs = line.toCharArray();
        return elementS(cs,index,del1,del2);
    }
    public static String elementS(String line,int index,char del1) {
        char[] cs = line.toCharArray();
        return elementS(cs,index,del1,(char)0);
    }
    public static String elementS(char[] line,int index,char del1) {
        return elementS(line,index,del1,(char)0);
    }
    public static String elementS(char[] line,int index,char del1,char del2) {
        int len = line.length;
        int idx = index;
        int i;
        int sdx=-1;
        int slen=0;
        //ignore trailing white spaces
        while(line[len-1]<33 && len>0)
            len--;
        if(del2==0) {
            // ... no right delimiter
            // element("a b c", 0, " ") -> "a"
            // element("  a b c", 0, " ") -> "a"  spaces at the beginning are skipped
            // element("a b c", 1, " ") -> "b"
            // element(" a  b  c", 1, " ") -> "b", though two spaces between "a" and "b"
            i=0;
            //skip the spaces at the beginning
            while(i<len && line[i]==' ')
                i++;
            for(; idx!=-1 && i<len ; i++) {
                // skip multiple (delimiters) spaces before wanted sequence starts
                while(idx>0 && line[i]==' ' && i<len-1 && line[i+1]==' ')
                    i++;
                if(line[i]==del1)
                    idx--;
                else if(idx==0) {
                    if(sdx==-1) sdx=i;
                    slen++;
                }

            }
        } else {
            // ... with right delimiter
            // element("a b c", 0, " ", " ") -> "b"
            // element("(a) (b c)", 0, "(", ")") -> "a"
            // element("(a) (b c)", 1, "(", ")") -> "b c"
            // element("(a) (b c)", 0, " ", ")") -> "(b c"
            // element("(a) (b c)", 1, " ", ")") -> "c"
            // element("(a) (b c)", 1, "(", "EOL") -> "b c)"
            i=0;
            //skip the spaces at the beginning
            while(i<len && line[i]==' ')
                i++;
            //seek left delimiter
            idx++;
            for (; idx != -1 && i < len; i++) {
                // skip multiple (delimiters) spaces before wanted sequence starts
                while (idx > 0 && line[i] == ' ' && i < len-1 && line[i+1] == ' ') i++;
                if ((idx != 0 && line[i] == del1) || (idx == 0 && line[i] == del2)) {
                    idx--;
                } else if (idx == 0) {
                    if(sdx==-1 || line[i]==del1) {
                        slen=0;
                        sdx=i;
                    }
                    slen++;
                }
            }
        }
        String sout = null;
        if(slen>0) sout = new String(line,sdx,slen).trim();
        return sout;
    }
}

package com.sureone;
/*
AB: Add Black: locations of Black stones to be placed on the board prior to the first move.
AW: Add White: locations of White stones to be placed on the board prior to the first move.
AN: Annotations: name of the person commenting the game.
AP: Application: application that was used to create the SGF file (e.g. CGOban2,...).
B: a move by Black at the location specified by the property value.
BR: Black Rank: rank of the Black player.
BT: Black Team: name of the Black team.
C: Comment: a comment.
CP: Copyright: copyright information. See Kifu Copyright Discussion.
DT: Date: date of the game.
EV: Event: name of the event (e.g. 58th Honinbo Title Match).
FF: File format: version of SGF specification governing this SGF file.
GM: Game: type of game represented by this SGF file. A property value of 1 refers to Go.
GN: Game Name: name of the game record.
HA: Handicap: the number of handicap stones given to Black. Placment of the handicap stones are set using the AB property.
KM: Komi: komi.
ON: Opening: information about the opening (fuseki), rarely used in any file.
OT: Overtime: overtime system.
PB: Black Name: name of the black player.
PC: Place: place where the game was played (e.g.: Tokyo).
PW: White Name: name of the white player.
RE: Result: result, usually in the format "B+R" (Black wins by resign) or "B+3.5" (black wins by 3.5 moku).
RO: Round: round (e.g.: 5th game).
RU: Rules: ruleset (e.g.: Japanese).
SO: Source: source of the SGF file.
SZ: Size: size of the board, non square boards are supported.
TM: Time limit: time limit in seconds.
US: User: name of the person who created the SGF file.
W: a move by White at the location specified by the property value.
WR: White Rank: rank of the White player.
WT: White Team: name of the White team.
There is no strict checking of the contents of these tags, so it is possible to put any text into the result tag for example.
 */

/*
 * The official specification can be found here:

 http://www.red-bean.com/sgf/

other info:
 */
import java.util.ArrayList;
public class SgfParser {
    public String mData;
    public SgfParser(String s) {
        mData = s;
        mLastTags = new ArrayList<SgfTag>();
        mH=new SgfHeader();

    }
    public SgfHeader mH = null;


    public boolean is_end=false;
    public boolean is_start=false;

    int mCursor=0;
    int mPosOfBodyStart=0;
    int mCurTag=0;


    ArrayList<SgfTag> mLastTags = null;


    public boolean parseHeader() {
        char[] c = mData.toCharArray();
        char[] ct = new char[2];
        StringBuffer v= new StringBuffer();
        String k="";
        int j;
        boolean bError=false;
        boolean bHeaderEnd=false;
        for (int i = 0; i < c.length; i++) {
            switch(c[i]) {
            case '(':
                break;
            case '[':
                //skip '['
                i++;
                v.delete(0, v.length());
                while(c[i]!=']' && i<c.length) {
                    if(c[i]!='\r' || c[i]!='\n' || c[i]!=';' || c[i]!='[')
                        v.append(c[i]);
                    i++;
                }

                if(i==c.length) {
                    bError=true;
                    break;
                }
                if(v.length()==0) {
                    v.append("NA");
                }
                if(k.compareTo("EV")==0) {
                    mH.EV=new String(v);
                }
                if(k.compareTo("DT")==0) {
                    mH.DT=new String(v);
                }
                if(k.compareTo("RE")==0) {
                    mH.RE=new String(v);
                }
                if(k.compareTo("PB")==0) {
                    mH.PB=new String(v);
                }
                if(k.compareTo("PW")==0) {
                    mH.PW=new String(v);
                }
                if(k.compareTo("SZ")==0) {
                    mH.SZ=new String(v);
                }
                if(k.compareTo("BR")==0) {
                    mH.BR=new String(v);
                }
                if(k.compareTo("WR")==0) {
                    mH.WR=new String(v);
                }
                if(k.compareTo("ID")==0) {
                    String s = new String(v);
                    mH.ID=Integer.parseInt(s);
                }

                break;
            case ';':
                i++;
                while(i<c.length) {
                    if(c[i]=='\r' || c[i]=='\n') {
                        i++;
                    } else {
                        break;
                    }
                }
                if(c[i]=='B') {
                    bHeaderEnd=true;
                } else {
                    i--;
                }
                break;
            case '\r':
            case '\n':
                break;
            default:
                ct[0]=c[i];
                if(ct[0]=='C' && (i+1)<c.length && c[i+1]=='[') {
                    k="C";
                    break;
                }
                if((i+1)>=c.length) {
                    bError=true;
                    break;
                }
                ct[1]=c[i+1];
                i=i+1;
                if((i+1)>c.length) {
                    bError=true;
                    break;
                }
                k=new String(ct);
                break;
            }
            if(bError==true) {
                break;
            }
            if(bHeaderEnd==true) {
                break;
            }

        }
        return !bError;
    }

    public boolean begin() {
        mLastTags.clear();
        mCurTag=0;

        this.mCursor=0;
        //skipHeader
        int j;
        char[] c = mData.toCharArray();
        int i=0;
        char[] t = new char[2];
        while(i<c.length) {
            if(c[i]==';') {
                j=i;
                i++;
                while(i<c.length) {
                    if(c[i]=='\r' || c[i]=='\n') {
                        i++;
                    } else
                        break;
                }
                t[0]=c[i];
                t[1]=c[i+1];
                if((t[0]=='B' && (t[1]=='['))) {
                    mCursor=j;
                    mPosOfBodyStart=i;
                    return true;
                }
            }
            i++;
        }

        return false;
    }

    boolean isCommentBegin(int i,char[] c) {
        if(i+1<c.length && c[i]=='C' && c[i+1]=='[') {
            return true;
        }
        return false;
    }
    //each tag should start with ';' or 'B' or 'C'
    public boolean next(SgfTag tag) {
        if(mCurTag<mLastTags.size()) {
            mCurTag++;
            SgfTag t = mLastTags.get(mCurTag-1);
            tag.t=t.t;
            tag.v=t.v;
            return true;
        }

        int i = mCursor;
        char[] c = mData.toCharArray();
        boolean bError=false;
        boolean bIsTag=false;
        while(c[i]!=';' && c[i]!=')' && (isCommentBegin(i,c)==false) && i<c.length)
            i++;
        while(i<c.length) {
            switch (c[i]) {
            case 'C':
                mCursor=i;
                bIsTag=true;
                break;
            case ';':
                i++;
                while(i<c.length) {
                    if(c[i]=='\r' || c[i]=='\n') {
                        i++;
                    } else
                        break;
                }
                if(c[i]=='B' || c[i]=='W') {
                    mCursor=i;
                    bIsTag=true;
                    break;
                }
            case ')':
                mCursor=i;
                is_end=true;
                tag.t=SgfTag.TAG_END;
                return true;
            default:
                break;
            }
            if(bIsTag==true) break;
            i++;
        }
        boolean b = (parseTag(tag));
        if(b==true && tag.t!=SgfTag.TAG_END) {
            mCurTag++;
            mLastTags.add(tag);
        }
        return b;

    }

    public boolean parseTag(SgfTag tag) {
        int i = mCursor;
        char[] c = mData.toCharArray();
        StringBuffer sbuf = new StringBuffer();
        boolean bError=false;
        int iFindLR=0;
        if(c[i]=='B' || c[i]=='W') {
            if(c[i]=='B' && c[i+1]=='[')
                tag.t = SgfTag.TAG_B;
            if(c[i]=='W' && c[i+1]=='[')
                tag.t =  SgfTag.TAG_W;
        } else if(c[i]=='C') {
            tag.t=SgfTag.TAG_C;
        } else {
            mCursor=i;
            return false;
        }
        while(c[i]!='[' && i<c.length)
            i++;
        if(i==c.length) {
            mCursor=i;
            is_end=true;
            return false;
        }

        //skip [
        i++;
        //skip 0x0d 0x0A just after '["
        while(i<c.length && (c[i]==0x0D || c[i]==0x0A)) {
            i++;
        }
        while(i<c.length && c[i]!=']') {
            //skip the continues "0x0D 0x0A"
            if(c[i]==0x0D || c[i]==0x0A) iFindLR++;
            if(iFindLR>=2 && c[i]!=0x0D && c[i]!=0x0A) iFindLR=0;
            if(iFindLR<=2)
                sbuf.append(c[i]);
            i++;
        }

        //sometime force close here
        if(i==c.length) return false;

        tag.v=new String(sbuf);
        //skip ]
        i++;

        while(c[i]=='\r' || c[i]=='\n')
            i++;
        mCursor=i;
        return !bError;
    }

    public boolean prev(SgfTag tag) {
        int i = mCursor;
        char[] c = mData.toCharArray();
        boolean bError=false;
        while(c[i]!='[' && i>mPosOfBodyStart) {
            i--;
        }

        if(mCurTag<=mLastTags.size()) {
            mCurTag--;
            if(mCurTag>0) {
                SgfTag t = mLastTags.get(mCurTag-1);
                tag.t=t.t;
                tag.v=t.v;
                return true;
            } else
                mCurTag=0;
        }

        if(i==mPosOfBodyStart) {
            tag.t=SgfTag.TAG_START;
            return true;
        }

        //skip [
        i--;
        while(c[i]!=']') {
            i--;
        }
        //skip ]
        i++;
        return (parseTag(tag));

    }

    public boolean eof() {
        return is_end;
    }
    void testString(String str) {
        try {
            if (str != null) {
                byte[] bs = str.getBytes();
                printBytes(bs, "len="+bs.length);
                /*
                String ch_name = new String(mData.getBytes(), "GB2312");
                printBytes(ch_name.getBytes(), "ch_name ");
                String bb = new String(ch_name.getBytes("GB2312"));
                printBytes(bb.getBytes(), "bb ");

                printCharArray(mData);
                */
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static int printCharArray(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        printStr("chararray = ");
        for (int i = 0; i < c.length; i++) {
            len++;
            System.out.print(c[i]);
        }
        printlnStr(",len="+len);
        return len;
    }
    public static void printlnStr(String str) {
        System.out.println(str);
    }
    public static void printStr(String str) {
        System.out.print(str);
    }
    public static void printBytes(byte[] array, String name) {
        printStr(name + " = ");
        for (int k = 0; k < array.length; k++) {
            printStr("0x" + byteToHex(array[k]) + " ");
        }
        printlnStr("");
    }
    static public String byteToHex(byte b) {
        // Returns hex String representation of byte b
        char hexDigit[] = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
        return new String(array);
    }

    static public String charToHex(char c) {
        // Returns hex String representation of char c
        byte hi = (byte) (c >>> 8);
        byte lo = (byte) (c & 0xff);
        return byteToHex(hi) + byteToHex(lo);
    }
}

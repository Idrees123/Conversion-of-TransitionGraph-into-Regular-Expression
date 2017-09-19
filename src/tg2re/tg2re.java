/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tg2re;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 */
class trans {

    String word;
    int n_state;

    void show() {
        System.out.print("{" + word + "," + n_state + "}");

    }
}

class incoming {

    int row, col;

    incoming(int r, int c) {
        row = r;
        col = c;

    }

}

class TransG {

    ArrayList<trans>[] TT;
    int noofstate;
    int[] IS;
    int[] FS;

    char[] inp = {'a', 'b'};

    TransG(int n) {
        noofstate = n;
        TT = new ArrayList[n + 2];
        initTT();

    }

 
    void initTT() {
        Scanner in = new Scanner(System.in);
        for (int a = 0; a < noofstate; a++) {
            System.out.println("enter no. of transition @ state " + (a));
            int i = in.nextInt();
            int j;

             TT[a] = new ArrayList<>(i);

           
            for (int b = 0; b < i; b++) {
//                System.out.println(TT[a].size());
                System.out.println("TT[" + a + "]" + "[" + b + "]-------------->");
                boolean matchch = false;
                String s;
                while (true) {
                    System.out.println("enter word");
                    s = in.next();
                    Pattern p = Pattern.compile("(a|b)*");
                    Matcher m = p.matcher(s);
                    if (m.matches()) {
                        break;
                    }

                }

                trans temp = new trans();
                temp.word = s;
                System.out.println("enter next state");
                int st = in.nextInt();
                temp.n_state = st;
                TT[a].add(temp);

            }

        }
         System.out.println("enter no. of initial states");
        int s = in.nextInt();
        IS = new int[s];
        for (int i = 0; i < s; i++) {
            System.out.println("enter inital state#" + (i + 1));
            IS[i] = in.nextInt();
        }
        System.out.println("enter no. of final states");
        s = in.nextInt();
        FS = new int[s];
        for (int i = 0; i < s; i++) {
            System.out.println("enter final state#" + (i + 1));
            FS[i] = in.nextInt();
        }

        //make new initial state
        TT[noofstate] = new ArrayList<trans>(IS.length);
        for (int i = 0; i < IS.length; i++) {
            //TT[noofstate][i] = new trans();
            trans temp = new trans();
            temp.word = "";
            temp.n_state = IS[i];
            TT[noofstate].add(temp);
        }
        IS = new int[1];
        IS[0] = noofstate;
        //make new final state
        TT[noofstate + 1] = new ArrayList<trans>(0);
        for (int i = 0; i < FS.length; i++) {
            trans temp = new trans();
            temp.word = "";
            temp.n_state = noofstate + 1;
            TT[FS[i]].add(temp);

        }
        FS = new int[1];
        FS[0] = noofstate + 1;
    }

    void conversion() {

        for (int j = 0; j < TT.length - 2; j++) {
            for (int i = 0; i < TT.length - 1; i++) {
                remEdge(i);
            }
            remclosure(j);
             remState(j);
            //remEdge(noofstate);
            // remclosure(noofstate);

        }
        remEdge(noofstate);
        remclosure(noofstate);

    }

    String getRE() {
        conversion();
        return TT[noofstate].get(0).word;

    }

    private void remclosure(int state) {
        outer:
        for (int i = 0; i < TT[state].size(); i++) {
            if (TT[state].get(i).n_state == state) {
                for (int j = 0; j < TT[state].size(); j++) {
                    if (TT[state].size() == 1) {
                        break outer;
                    }

                    if (j == i) {
                        continue;
                    }
                    TT[state].get(j).word = "(" + TT[state].get(i).word + ")*" + TT[state].get(j).word;

                }

                TT[state].remove(TT[state].get(i));
            }

        }

    }

private void remState(int state) {
        ArrayList<incoming> inc = new ArrayList<incoming>();
        for (int i = 0; i < TT.length - 1; i++) {
            for (int j = 0; j < TT[i].size(); j++) {
                if (TT[i].get(j).n_state == state) {
                    incoming t = new incoming(i, j);
                    inc.add(t);
                }
            }

        }
        for (int i = 0; i < inc.size(); i++) {
            for (int j = 0; j < TT[state].size(); j++) {

                trans t = new trans();
                t.n_state = TT[state].get(j).n_state;
                show();
                t.word = TT[inc.get(i).row].get(inc.get(i).col).word + TT[state].get(j).word;
                TT[inc.get(i).row].add(t);
            }
            TT[inc.get(i).row].remove(TT[inc.get(i).row].get(inc.get(i).col));
        }
        TT[state].clear();
    }

    private void remEdge(int state) {
        for (int i = 0; i < TT[state].size(); i++) {
            int dest = TT[state].get(i).n_state;
            for (int j = i + 1; j < TT[state].size(); j++) {
                if (dest == TT[state].get(j).n_state) {
                    TT[state].get(i).word = "(" + TT[state].get(i).word + "|" + TT[state].get(j).word + ")";
                    TT[state].remove(TT[state].get(j));
                    j--;
                }
            }

        }

    }

    void show() {
        for (int a = 0; a < TT.length; a++) {
            System.out.print("state" + a + " ");
             for (int b = 0; b < TT[a].size(); b++) {
                TT[a].get(b).show();
            }
            System.out.println("");

        }

    }

}

public class tg2re {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("enter total no. of states");
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        TransG t = new TransG(n);
       // t.initTT();
  //      t.conversion();
       // t.show();
   String re = t.getRE();
       // System.out.println(re);
//        t.show();
      Pattern p = Pattern.compile(re);
      Matcher m = p.matcher("bababaaaabbbabbaaaaabbbba");
      System.out.println(m.matches());

    }

}

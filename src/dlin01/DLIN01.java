/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlin01;

import static java.lang.System.nanoTime;

/**
 *
 * @author jhpark
 */
public class DLIN01 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long ls, le;
        long iter = 500;

//        Net nn1 = new Net(2,5,2,3);
        Net nn1 = new Net(2,1000,100,100,3);
        //nn1.initWeight();
        
        double[][] ins1 = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0},
        };
        
        double[][] outs1 = {
            {0.0, 0.0, 0.0},
            {0.0, 1.0, 1.0},
            {0.0, 1.0, 1.0},
            {1.0, 1.0, 0.0},
        };
        
//        double[][] outs1 = {
//            {0.0},
//            {0.0},
//            {0.0},
//            {1.0},
//        };

        double d0,d1,d2,dE=1000.;
        nn1.disp();
        ls = nanoTime();
        for(long k=0;k<=iter;k++) {
//          System.out.println(k+"th learning. err="+dE);
            System.out.printf("%d / %d\n",k,iter);
//          if (dE<1e-4) break;
//          dE = 0;
            for(int n=0;n<4;n++){
                nn1.calcOutput(ins1[n]);
                nn1.calcDelta(outs1[n]);
                nn1.updateWeight();
                
//                int id = nn1.iNumLayer-1;
//                d0 = nn1.aLayer[id].vDelta[0]*nn1.aLayer[id].vDelta[0];
//                d1 = nn1.aLayer[id].vDelta[1]*nn1.aLayer[id].vDelta[1];
//                d2 = nn1.aLayer[id].vDelta[2]*nn1.aLayer[id].vDelta[2];
//                dE = (d0+d1+d2)/2;
            }
        }
        le = nanoTime();
        System.out.println((double)(le-ls)/1e9+" sec");
        nn1.disp();
        for(int n=0;n<4;n++){
            nn1.calcOutput(ins1[n]);
            nn1.calcDelta(outs1[n]);
            nn1.dispInOut();
        }
    }
    
}

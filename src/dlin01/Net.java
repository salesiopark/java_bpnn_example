/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlin01;

/**
 *
 * @author jhpark
 */

public class Net {
    public int iNumLayer;
    public Layer[] aLayer;
    
    public Net(int ... aNumNode) {
        //System.out.println("##"+aNumNode.length);
        iNumLayer = aNumNode.length;
        aLayer = new Layer[iNumLayer];
        
        aLayer[0] = new Layer(aNumNode[0]);
        for(int k=1;k<iNumLayer;k++) {
            aLayer[k] = new Layer(aNumNode[k], aLayer[k-1]);
        }

        // 연결돤 후방 레이어를 설정한다.
        for(int k=0;k<iNumLayer-1;k++) {
            aLayer[k].setNextLayer(aLayer[k+1]);
        }
        
        initWeight();
    }

    public void initWeight() {
        for(int k=1;k<aLayer.length;k++)
            aLayer[k].initWeight();
    }
    
    // double 넷의 출력 계산
    public void calcOutput(double[] vDbl){
        aLayer[0].setOutputVec(vDbl);//입력벡터를 입력층에 저장
        for (int k=1;k<iNumLayer;k++)
            aLayer[k].calcOutput();
    }

    public void disp(){
         String str = "";
         for (int k=0;k<iNumLayer;k++) {
             str += aLayer[k];
             str += "\n";
         }
         System.out.println(str);
     }
    
     public void dispInOut(){
         String str = "(";
            for (int n=0; n<aLayer[0].iNumNode; n++) {
                str += String.format("%.3f, ",aLayer[0].vOutput[n]);
            }
            str += ") -> (";
            for (int n=0; n<aLayer[iNumLayer-1].iNumNode; n++) {
                str += String.format("%.3f (t:%.3f), "
                        ,aLayer[iNumLayer-1].vOutput[n]
                        ,aLayer[iNumLayer-1].vTarget[n]);
            }
         System.out.print(str+")\n");
     }

    public void calcDelta(double[] vDbl){
        aLayer[iNumLayer-1].vTarget = vDbl;//목포값을 입력층에 저장
        for (int k=iNumLayer-1;k>0;k--) //입력층(k==0)은 계산할 필요가 없다
            aLayer[k].calcDelta();
    }

    public void updateWeight(){
        for (int k=1;k<iNumLayer;k++) //입력층은 갱신할 가중치가 없다.
                aLayer[k].updateWeight();
    }

//    public void updateWeightDbl(){
//        for (int k=1;k<iNumLayer;k++) //입력층은 갱신할 가중치가 없다.
//                aLayer[k].updateWeightDbl();
//    }
}

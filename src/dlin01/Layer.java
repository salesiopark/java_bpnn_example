package dlin01;

import java.util.Random;

public class Layer {

    public static int iCnt = 0;

    public int id;
    public int iNumNode;
    public Layer layerPrev = null;
    public Layer layerNext = null;

    public double[][] mWeight = null;// matrix of weights
    public double[] vOutput = null; //각 노드의 출력값
    public double[] vTarget = null;
    public double[] vDelta = null;
    //public double[]     vError = null;

    public boolean isInputLayer() {
        return layerPrev == null;
    }

    public boolean isOutputLayer() {
        return layerNext == null;
    }

    public Layer(int iNumNode) {
        this.id = iCnt++;//고유 아이디 부여
        this.iNumNode = iNumNode;

        vOutput = new double[iNumNode];
        vTarget = new double[iNumNode];
        //vError = new double[iNumNode];
    }

    public Layer(int iNumNode, Layer layerIn) {
        this(iNumNode);
        this.layerPrev = layerIn;

        this.vDelta = new double[iNumNode];
        //(가중치로 가정한)바이어스를 위해서 +1개 더 생성
        this.mWeight = new double[iNumNode][layerIn.iNumNode + 1];
    }

    public void setNextLayer(Layer layer) {
        this.layerNext = layer;
        this.vTarget = null;//출력 노드가 아니므로 메모리 반환
        //this.vError = null;//출력 노드가 아니므로 메모리 반환
    }

    public void initWeight() {
        Random rand = new Random();
        for (int n = 0; n < iNumNode; n++) { // node index (row in mW)
            for (int c = 0; c < layerPrev.iNumNode + 1; c++) {
                mWeight[n][c] = rand.nextDouble()-0.5;
            }
        }
    }

    public void setOutputVec(double[] vec) {
        vOutput = vec;
    }

    public void setOutput(int k, double val) {
        vOutput[k] = val;
    }

    public String toString() {
        String str = String.format("layer id:%d, %d nodes, ", id, iNumNode);

        if (isInputLayer()) {
            str += String.format("to layer #%d\n", layerNext.id);
        } else if (isOutputLayer()) {
            str += String.format("from layer #%d\n", layerPrev.id);
        } else {
            str += String.format("from layer #%d to #%d\n", layerPrev.id, layerNext.id);
        }

        for (int n = 0; n < iNumNode; n++) {
            if (!isInputLayer()) {
                str += String.format("(o:%.3f, d:%.3f)", vOutput[n], vDelta[n]);
                for (int c = 0; c < layerPrev.iNumNode + 1; c++) {
                    if (c == layerPrev.iNumNode) {
                        str += String.format("%.3f (bias)\n", mWeight[n][c]);
                    } else {
                        str += String.format("%.3f, ", mWeight[n][c]);
                    }
                }
            } else {
                str += String.format("(o:%.3f)", vOutput[n]);
            }
        }
        return str;
    }

    public void calcOutput() {
        double dSum, dTmp;
        for (int n = 0; n < iNumNode; n++) {
            dSum = 0;
            //입력*가중치들의 합
            for (int pn = 0; pn < layerPrev.iNumNode; pn++) {
                dSum += mWeight[n][pn] * layerPrev.vOutput[pn];
            }
            //(가중치로 가장한)바이어스를 더함
            dSum += mWeight[n][layerPrev.iNumNode];

            //if (isOutputLayer()) {
            //    vOutput[n] = dSum;//선형함수
            //} else { //                vOutput[n] = 1/(1+exp(-dSum));  //sigmoid함수
                vOutput[n] = afunc(dSum);  // sigmoid함수
            //}
        }
    }

    public void calcDelta() {
        double dSum;
        if (isOutputLayer()) {
            for (int n = 0; n < iNumNode; n++) {
//                vDelta[n] = vTarget[n] - vOutput[n];//*선기울기==1
                vDelta[n] = (vTarget[n] - vOutput[n])*dafunc(vOutput[n]);//*선기울기==1
            }
        } else {
            for (int n = 0; n < iNumNode; n++) {
                dSum = 0;
                for (int k = 0; k < layerNext.iNumNode; k++) {
                    dSum += layerNext.vDelta[k] * layerNext.mWeight[k][n];
                }
                vDelta[n] = dSum * dafunc(vOutput[n]);
            }
        }
    }

    public void updateWeight() {
        for (int n = 0; n < iNumNode; n++) {
            for (int k = 0; k < layerPrev.iNumNode; k++) {
                mWeight[n][k] += lrate * vDelta[n] * layerPrev.vOutput[k];
            }
            //(바이어스)가중치 업데이트
            mWeight[n][layerPrev.iNumNode] += lrate * vDelta[n];//*1.0
        }
    }

    public double lrate = 0.1; //learning rate

    public double afunc(double x) {//phi
        //return 1 / (1 + Math.exp(-x));
        if (x<0) return 0.0; else return x;
    }

    public double dafunc(double phi) {
        //return phi * (1 - phi);
        if (phi<0) return 0.0; else return 1.0;
    }

}

enum AFTYPE { // type of activation function
    SIGMOID,
    LINEAR,
}
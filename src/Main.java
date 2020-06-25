import java.util.*;

public class Main {
    // y = 20x + 95

    public static int MSE(int a, int b, int[] x, int[] y){
        int mse = 0;
        for(int i = 0 ; i < y.length ; i ++){
            mse += Math.pow(y[i] - (a*x[i] + b), 2);
            // System.out.println(mse);
        }
        return mse;
    }

    public static int[] init() {
        Random r = new Random();
        int[] arr = new int[4];
        for(int i = 0; i < 4; i++) {
            arr[i] = r.nextInt(51);
        }
        return arr;
    }

    public static int[] selection(int[] a, int[] x, int[] y) {
        int sum = 0;
        int[] f = new int[a.length];            // 적합도 저장
        Arrays.fill(f, Integer.MAX_VALUE);

        for(int i = 0 ; i < a.length ; i++) {
            for(int j = 90 ; j <= 110 ; j++){
                // 가장 좋은 b 찾고 거기에 매칭되는 a 해서 f[i]에 그 a랑 b랑 MSE 한거 저장
                f[i] = Math.min(f[i], MSE(a[i], j, x, y));
            }
            sum += f[i];
        }

        for (int i = 0 ; i < f.length ; i++) {
            f[i] = sum - f[i];              // MSE가 낮을수록 우열을 만들려고 뒤집어줌
        }

        sum = 0;
        for (int i = 0; i < f.length; i++) {
            sum += f[i];
        }

        double[] ratio = new double[a.length];
        for(int i = 0 ; i < ratio.length ; i++) {
            if(i == 0) ratio[i] = (double)f[i] / (double)sum;
            else ratio[i] = ratio[i-1] + (double)f[i] / (double)sum;
        }

        int[] sx = new int[a.length];
        Random r = new Random();
        for(int i = 0; i < a.length ; i++) {
            double p = r.nextDouble();
            if(p < ratio[0]) sx[i] = a[0];
            else if(p < ratio[1]) sx[i] = a[1];
            else if(p < ratio[2]) sx[i] = a[2];
            else sx[i] = a[3];
        }

        System.out.print("selection: ");
        for (int i = 0; i <4; i++) {
            System.out.printf("%d ", sx[i]);
        }
        System.out.println();

        return sx;
    }

    public static String int2String(String x) {
        return String.format("%8s", x).replace(' ', '0');
    }

    public static String[] crossOver(int[] x) {
        String[] arr = new String[x.length];
        for(int i=0; i<x.length; i+=2) {
            String bit1 = int2String(Integer.toBinaryString(x[i]));
            String bit2 = int2String(Integer.toBinaryString(x[i+1]));

            arr[i] = bit1.substring(0, 4) + bit2.substring(4, 8);
            arr[i+1] = bit2.substring(0, 4) + bit1.substring(4, 8);
        }

        System.out.print("crossover: ");
        for (int i = 0; i < 4; i++) {
            System.out.printf("%s ", arr[i]);
        }
        System.out.println();
        return arr;
    }

    public static int invert(String x) {
        Random r = new Random();
        int a = Integer.parseInt(x, 2);
        for(int i=0; i<x.length(); i++) {
            // 돌연변이 발생 확률
            double p = (double)1/ (double)50;
            if(r.nextDouble() < p) {
                a = 1 << i ^ a;
            }
        }
        return a;
    }

    public static int[] mutation(String[] x) {
        int[] arr = new int[x.length];
        for (int i=0; i<x.length; i++) {
            arr[i] = invert(x[i]);
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] a = init();

        // 요일 (일 월오전 월오후 화오전 화오후 ... 토오전 토오후)
        int[] x = new int[13];
        for (int i = 0 ; i < 13; i++) {
            x[i] = i;
        }

        // 무값 노이즈 섞어서 값 넣음
        // 0번은 구매가격이 되냐 ?
        int[] y = new int[13];
        for(int i = 0 ; i < 13 ; i++){
            Random r = new Random();
            y[i] = 20*x[i] + 95 + (int)(20*r.nextGaussian());
            System.out.println(y[i]);
        }

        int result_a = 0;
        int result_b = 0;
        for(int i = 0; i < 1000 ; i++) {
            int[] selec = selection(a, x, y);
            String[] cross = crossOver(selec);
            int[] mut = mutation(cross);

            int[] mse = new int[mut.length];
            int min = Integer.MAX_VALUE;
            for(int j = 0 ; j < mut.length ; j++) {

                int kk = 0;
                for(int k = 90 ; k <= 110 ; k++){
                    mse[j] = MSE(a[j], k, x, y);
                    if(min > mse[j]){
                        min = mse[j];
                        result_a = a[j];
                        result_b = k;
                    }
                }

            }
            System.out.print("mutation: ");
            for (int j = 0; j < 4; j++) {
                System.out.printf("%d ", a[j]);
            }
            System.out.println();
            System.out.println(result_a + "x + " + result_b);

            a = mut;
        }

    }
}

package abodx3.sar.emproject.audiomaneger;

import android.content.Context;

import java.util.List;

import linc.com.amplituda.Amplituda;

public class AmplitudExtractor {

    int[] result;

    public int[] getAmplituds(Context context, String path) {
        Amplituda amplituda = new Amplituda(context);
        amplituda.fromPath(path).amplitudesAsList(this::AsintArray);
        return result;
    }

    void AsintArray(List<Integer> list) {
        result = new int[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i) * 8 + 1;
        }
    }

}

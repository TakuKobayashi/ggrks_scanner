package kobayashi.taku.com.ggrks_scanner;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.gms.vision.text.internal.client.TextRecognizerOptions;
import com.google.android.gms.vision.text.internal.client.zzg;

public class CostumTextRecognizer extends Detector<TextBlock> {
	// マイフレーム呼ばれるのでここに検出処理を記述する
	@Override
	public SparseArray<TextBlock> detect(Frame frame) {
		Log.d(Config.TAG, "detect");
		return null;
	}
}

package animaciones;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.administrador.R;

public class scale_anim {

    public void animar(Context a, View v){
        Animation sol= AnimationUtils.loadAnimation(a, R.anim.scale);
        v.startAnimation(sol);
    }
}

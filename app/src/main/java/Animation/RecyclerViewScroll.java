package Animation;

import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewScroll extends RecyclerView.OnScrollListener{
    private static final float HIDE_THRESHOLD = 20;
    private static final float SHOW_THRESHOLD = 10;
    private int scrollDist = 0;
    private boolean isVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy){
        super.onScrolled(recyclerView, dx, dy);//Super se refiere al constructor del padre
        if (isVisible && scrollDist > HIDE_THRESHOLD){
            hide();
            scrollDist = 0;
            isVisible = false;
        }
        //Desplazamiento hacia arriba
        else if(!isVisible && scrollDist < -SHOW_THRESHOLD){
            show();

            scrollDist = 0;
            isVisible = true;
        }
        // Ya sea que desplazarse hacia arriba o hacia abajo,
        // calcular la distancia de desplazamiento
        if((isVisible && dy > 0) || (!isVisible && dy < 0)){
            scrollDist += dy;
        }
    }
    public abstract void show();

    public abstract void hide();
}


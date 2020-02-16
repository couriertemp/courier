package uz.courier.ui.components;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import uz.courier.R;

public class Toolbar extends ConstraintLayout {
    public Toolbar(Context context, AttributeSet attributeSet) {
        super(context);

        final Context tmpContext = context;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.toolbar, this);

    }
}

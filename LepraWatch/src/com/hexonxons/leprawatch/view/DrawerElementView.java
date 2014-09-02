package com.hexonxons.leprawatch.view;

import com.hexonxons.leprawatch.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DrawerElementView extends RelativeLayout
{
    // Drawer list element text.
    public TextView primaryText     = null;
    // Drawer list element secondary text.
    public TextView secondaryText   = null;
    // Drawer list element icon.
    public ImageView icon           = null;
    
    public DrawerElementView(Context context)
    {
        super(context);
    }
    
    public DrawerElementView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public DrawerElementView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        
        primaryText = (TextView) findViewById(R.id.drawer_element_primary_text);
        secondaryText = (TextView) findViewById(R.id.drawer_element_secondary_text);
        icon = (ImageView) findViewById(R.id.drawer_element_icon);
    }
}

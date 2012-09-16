package de.zieren.rot13;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Allows to set a text offset that will be brought into view on draw.
 *
 * @author jz
 */
public class ScrollingEditText extends EditText {
  /** This text offset will be brought into view on draw. */
  protected int offsetToView = 0;

  public ScrollingEditText(Context context) {
    super(context);
  }
  public ScrollingEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  public ScrollingEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setOffsetToView(int offsetToView) {
    this.offsetToView = offsetToView;
  }

  @Override
  public boolean onPreDraw() {
    assert offsetToView < getText().length();
    // This needs to be called after layout:
    bringPointIntoView(offsetToView);
    return super.onPreDraw();
  }
}

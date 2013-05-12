package de.zieren.rot13;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

// TODO(jz): Make text black (or rather, default text color) despite being non-editable.

/**
 * Allows to set a text offset that will be brought into view on draw.
 *
 * @author jz
 */
public class ScrollingTextView extends TextView {
  /** This text offset will be brought into view on draw. */
  protected int offsetToView = 0;

  public ScrollingTextView(Context context) {
    super(context);
  }
  public ScrollingTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  public ScrollingTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setOffsetToView(int offsetToView) {
    this.offsetToView = offsetToView;
  }

  @Override
  public boolean onPreDraw() {
	// TODO(jz): Verify that this is really necessary.
    assert offsetToView < getText().length();
    // This needs to be called after layout:
    bringPointIntoView(offsetToView);
    return super.onPreDraw();
  }
}

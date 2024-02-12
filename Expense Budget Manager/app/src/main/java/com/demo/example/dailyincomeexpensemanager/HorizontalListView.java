package com.demo.example.dailyincomeexpensemanager;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import java.util.LinkedList;
import java.util.Queue;


public class HorizontalListView extends AdapterView<ListAdapter> {
    protected ListAdapter mAdapter;
    protected int mCurrentX;
    private GestureDetector mGesture;
    protected int mNextX;
    private OnItemClickListener mOnItemClicked;
    private OnItemLongClickListener mOnItemLongClicked;
    private OnItemSelectedListener mOnItemSelected;
    protected Scroller mScroller;
    public boolean mAlwaysOverrideTouch = true;
    private int mLeftViewIndex = -1;
    private int mRightViewIndex = 0;
    private int mMaxX = Integer.MAX_VALUE;
    private int mDisplayOffset = 0;
    private Queue<View> mRemovedViewQueue = new LinkedList();
    private boolean mDataChanged = false;
    private DataSetObserver mDataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            synchronized (HorizontalListView.this) {
                HorizontalListView.this.mDataChanged = true;
            }
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }

        @Override
        public void onInvalidated() {
            HorizontalListView.this.reset();
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }
    };
    private GestureDetector.OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {
        @Override

        public boolean onDown(MotionEvent motionEvent) {
            return HorizontalListView.this.onDown(motionEvent);
        }

        @Override

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return HorizontalListView.this.onFling(motionEvent, motionEvent2, f, f2);
        }

        @Override

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            synchronized (HorizontalListView.this) {
                HorizontalListView.this.mNextX += (int) f;
            }
            HorizontalListView.this.requestLayout();
            return true;
        }

        @Override

        public boolean onSingleTapConfirmed(MotionEvent e) {
            Rect viewRect = new Rect();
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int left = child.getLeft();
                int right = child.getRight();
                int top = child.getTop();
                int bottom = child.getBottom();
                viewRect.set(left, top, right, bottom);

                if (viewRect.contains((int) e.getX(), (int) e.getY())) {
                    if (mOnItemClicked != null) {
                        mOnItemClicked.onItemClick(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    if (mOnItemSelected != null) {
                        mOnItemSelected.onItemSelected(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    break;
                }

            }
            return true;
        }

        @Override

        public void onLongPress(MotionEvent motionEvent) {
            int childCount = HorizontalListView.this.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = HorizontalListView.this.getChildAt(i);
                if (isEventWithinView(motionEvent, childAt)) {
                    if (HorizontalListView.this.mOnItemLongClicked != null) {
                        HorizontalListView.this.mOnItemLongClicked.onItemLongClick(HorizontalListView.this, childAt, HorizontalListView.this.mLeftViewIndex + 1 + i, HorizontalListView.this.mAdapter.getItemId(HorizontalListView.this.mLeftViewIndex + 1 + i));
                        return;
                    } else {
                        return;
                    }
                }
            }
        }

        private boolean isEventWithinView(MotionEvent motionEvent, View view) {
            Rect rect = new Rect();
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            rect.set(i, i2, view.getWidth() + i, view.getHeight() + i2);
            return rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
        }
    };

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int i) {
    }

    public HorizontalListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private synchronized void initView() {
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
        this.mDisplayOffset = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mMaxX = Integer.MAX_VALUE;
        this.mScroller = new Scroller(getContext());
        this.mGesture = new GestureDetector(getContext(), this.mOnGesture);
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelected = onItemSelectedListener;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClicked = onItemClickListener;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClicked = onItemLongClickListener;
    }

    @Override
    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setAdapter(ListAdapter listAdapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mAdapter = listAdapter;
        this.mAdapter.registerDataSetObserver(this.mDataObserver);
        reset();
    }

    
    public synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    private void addAndMeasureChild(View view, int i) {
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-1, -1);
        }
        addViewInLayout(view, i, layoutParams, true);
        view.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
    }

    @Override
    protected synchronized void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mAdapter != null) {
            if (this.mDataChanged) {
                int i5 = this.mCurrentX;
                initView();
                removeAllViewsInLayout();
                this.mNextX = i5;
                this.mDataChanged = false;
            }
            if (this.mScroller.computeScrollOffset()) {
                this.mNextX = this.mScroller.getCurrX();
            }
            if (this.mNextX <= 0) {
                this.mNextX = 0;
                this.mScroller.forceFinished(true);
            }
            if (this.mNextX >= this.mMaxX) {
                this.mNextX = this.mMaxX;
                this.mScroller.forceFinished(true);
            }
            int i6 = this.mCurrentX - this.mNextX;
            removeNonVisibleItems(i6);
            fillList(i6);
            positionItems(i6);
            this.mCurrentX = this.mNextX;
            if (!this.mScroller.isFinished()) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        HorizontalListView.this.requestLayout();
                    }
                });
            }
        }
    }

    private void fillList(int i) {
        View childAt = getChildAt(getChildCount() - 1);
        int i2 = 0;
        fillListRight(childAt != null ? childAt.getRight() : 0, i);
        View childAt2 = getChildAt(0);
        if (childAt2 != null) {
            i2 = childAt2.getLeft();
        }
        fillListLeft(i2, i);
    }

    private void fillListRight(int i, int i2) {
        while (i + i2 < getWidth() && this.mRightViewIndex < this.mAdapter.getCount()) {
            View view = this.mAdapter.getView(this.mRightViewIndex, this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(view, -1);
            i += view.getMeasuredWidth();
            if (this.mRightViewIndex == this.mAdapter.getCount() - 1) {
                this.mMaxX = (this.mCurrentX + i) - getWidth();
            }
            if (this.mMaxX < 0) {
                this.mMaxX = 0;
            }
            this.mRightViewIndex++;
        }
    }

    private void fillListLeft(int i, int i2) {
        while (i + i2 > 0 && this.mLeftViewIndex >= 0) {
            View view = this.mAdapter.getView(this.mLeftViewIndex, this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(view, 0);
            i -= view.getMeasuredWidth();
            this.mLeftViewIndex--;
            this.mDisplayOffset -= view.getMeasuredWidth();
        }
    }

    private void removeNonVisibleItems(int i) {
        View childAt = getChildAt(0);
        while (childAt != null && childAt.getRight() + i <= 0) {
            this.mDisplayOffset += childAt.getMeasuredWidth();
            this.mRemovedViewQueue.offer(childAt);
            removeViewInLayout(childAt);
            this.mLeftViewIndex++;
            childAt = getChildAt(0);
        }
        View childAt2 = getChildAt(getChildCount() - 1);
        while (childAt2 != null && childAt2.getLeft() + i >= getWidth()) {
            this.mRemovedViewQueue.offer(childAt2);
            removeViewInLayout(childAt2);
            this.mRightViewIndex--;
            childAt2 = getChildAt(getChildCount() - 1);
        }
    }

    private void positionItems(int i) {
        if (getChildCount() > 0) {
            this.mDisplayOffset += i;
            int i2 = this.mDisplayOffset;
            int i3 = 0;
            while (i3 < getChildCount()) {
                View childAt = getChildAt(i3);
                int measuredWidth = childAt.getMeasuredWidth() + i2;
                childAt.layout(i2, 0, measuredWidth, childAt.getMeasuredHeight());
                i3++;
                i2 = measuredWidth;
            }
        }
    }

    public synchronized void scrollTo(int i) {
        this.mScroller.startScroll(this.mNextX, 0, i - this.mNextX, 0);
        requestLayout();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return this.mGesture.onTouchEvent(motionEvent) | super.dispatchTouchEvent(motionEvent);
    }

    protected boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        synchronized (this) {
            this.mScroller.fling(this.mNextX, 0, (int) (-f), 0, 0, this.mMaxX, 0, 0);
        }
        requestLayout();
        return true;
    }

    protected boolean onDown(MotionEvent motionEvent) {
        this.mScroller.forceFinished(true);
        return true;
    }
}

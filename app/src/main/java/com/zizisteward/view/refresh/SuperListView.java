package com.zizisteward.view.refresh;

import com.lsjr.zizisteward.utils.ViewUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

public class SuperListView extends ListView implements OnScrollListener, GestureDetector.OnGestureListener,
		BasePulldownRefershListHeader.OnAllowReadyRefreshListener {
	public static int rollbackDuration = 400;
	private int lastTotalItemCount;
	private int boundariesPosition = -1;
	private boolean init;
	private boolean pulldown;
	private boolean full;
	private Scroller rollBackScroller;
	private GestureDetector gestureDetector;
	private OnScrollListener onScrollListener;
	protected BasePulldownRefershListHeader pulldownRefershListHeader;
	private BaseLoadMoreListFooter loadMoreListFooter;
	private OnRefreshListener onRefreshListener;
	private OnLoadMoreListener onLoadMoreListener;
	protected boolean refreshing;
	protected boolean loadMoring;
	protected boolean isLoadFull = false;

	public SuperListView(Context context) {
		super(context);
		setPulldownRefershListHeader(new PulldownRefreshListHeader(context));
		setLoadMoreListFooter(new LoadMoreListFooter(context));
		init();
	}

	public SuperListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPulldownRefershListHeader(new PulldownRefreshListHeader(context));
		setLoadMoreListFooter(new LoadMoreListFooter(context));
		init();
	}

	public SuperListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setPulldownRefershListHeader(new PulldownRefreshListHeader(context));
		setLoadMoreListFooter(new LoadMoreListFooter(context));
		init();
	}

	public boolean isFull() {
		return full;
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	public void setIsLoadFull(boolean full) {
		this.isLoadFull = full;
	}

	private void init() {
		rollBackScroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());
		gestureDetector = new GestureDetector(getContext(), this);

		init = true;
		setOnScrollListener(this);
		init = false;
	}

	@Override
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		if (init) {
			super.setOnScrollListener(onScrollListener);
		} else {
			this.onScrollListener = onScrollListener;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (pulldownRefershListHeader != null && onRefreshListener != null) {
			gestureDetector.onTouchEvent(ev);
			if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
				upEventHandle();
			}
		}
		return super.onTouchEvent(ev);
	}

	private void upEventHandle() {
		if (getFirstVisiblePosition() == 0) {
			if (onRefreshListener != null
					&& pulldownRefershListHeader.getState() == BasePulldownRefershListHeader.State.READY_REFRESH) {
				refreshing = true;
				pulldownRefershListHeader.readyRefreshToRefreshingState();
				onRefreshListener.onRefresh();
			}
			tryRollbackPulldownRefreshListHeader();
		}
	}

	private void tryRollbackPulldownRefreshListHeader(int begainHeight, int endHeight) {
		rollBackScroller.abortAnimation();
		rollBackScroller.startScroll(0, begainHeight, 0, endHeight - begainHeight, rollbackDuration);
		invalidate();
	}

	private void tryRollbackPulldownRefreshListHeader(int begainHeight) {
		tryRollbackPulldownRefreshListHeader(begainHeight, pulldownRefershListHeader.getRollbackFinalHeight());
	}

	private void tryRollbackPulldownRefreshListHeader() {
		tryRollbackPulldownRefreshListHeader(pulldownRefershListHeader.getContentView().getHeight());
	}

	public void finishRefresh() {
		if (pulldownRefershListHeader != null && refreshing) {
			pulldownRefershListHeader.toggleToRefreshingToNormalState();
			if (getFirstVisiblePosition() == 0) {
				if ((pulldownRefershListHeader.getTop() + getPaddingTop()) == getTop()) {
					tryRollbackPulldownRefreshListHeader();
				} else {
					int begainHeight = (pulldownRefershListHeader.getContentViewHeight()
							+ pulldownRefershListHeader.getTop()) - getPaddingTop();
					pulldownRefershListHeader.updateHeight(begainHeight);
					setSelection(0);
					tryRollbackPulldownRefreshListHeader(begainHeight);
				}
			} else {
				pulldownRefershListHeader.updateHeight(pulldownRefershListHeader.getRollbackFinalHeight());
				invalidate();
			}
			refreshing = false;
		}
	}

	public void finishLoadMore() {
		if (loadMoreListFooter != null && loadMoring) {
			loadMoreListFooter.toggleToNormalState();
			loadMoring = false;
		}
	}

	public boolean refresh() {
		if (pulldownRefershListHeader != null && onRefreshListener != null
				&& pulldownRefershListHeader.getState() == BasePulldownRefershListHeader.State.NORMAL && isNoAction()) {
			if (getWidth() > 0) {
				refreshing = true;
				setSelection(0);
				pulldownRefershListHeader.setState(BasePulldownRefershListHeader.State.NORMAL_TO_REFRESHING);
				tryRollbackPulldownRefreshListHeader(pulldownRefershListHeader.getContentView().getHeight(),
						pulldownRefershListHeader.getContentViewHeight());
				isLoadFull = false;
				onRefreshListener.onRefresh();
			} else {
				getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						getHandler().postDelayed(new Runnable() {
							@Override
							public void run() {
								pulldownRefershListHeader.onReadyRefreshToRefresingState();
								refresh();
							}
						}, 100);
						ViewUtil.removeOnGlobalLayoutListener(getViewTreeObserver(), this);
					}
				});
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean loadMore() {
		if (loadMoreListFooter != null && onLoadMoreListener != null && full
				&& loadMoreListFooter.getState() == BaseLoadMoreListFooter.State.NOMRAL && isNoAction()) {
			loadMoring = true;
			loadMoreListFooter.toggleToLoadingState();
			onLoadMoreListener.onLoadMore();
			setSelection(getCount() - 1);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void computeScroll() {
		if (pulldownRefershListHeader != null && rollBackScroller.computeScrollOffset()) {
			pulldownRefershListHeader.updateHeight(rollBackScroller.getCurrY());
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (pulldownRefershListHeader != null) {
			pulldownRefershListHeader.setOnAllowReadyRefreshListener(this);
			addHeaderView(pulldownRefershListHeader);
		}

		if (loadMoreListFooter != null) {
			addFooterView(loadMoreListFooter);
		}
		super.setAdapter(adapter);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (onScrollListener != null) {
			onScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (loadMoreListFooter != null) {
			setFooterDividersEnabled(full = visibleItemCount != totalItemCount); // �����б��Ƿ�������趨�Ƿ���Ҫ��ʾ�б�β�ָ���
			if (!full && loadMoreListFooter.getContentView().getVisibility() == View.VISIBLE) {
				loadMoreListFooter.getContentView().setVisibility(View.GONE);
			}
			if (onLoadMoreListener != null && full && isNoAction()) {
				if (boundariesPosition == -1) {
					if (getLastVisiblePosition() == totalItemCount - 1 - getFooterViewsCount()
							&& loadMoreListFooter.getState() == BaseLoadMoreListFooter.State.NOMRAL) {
						if (!isLoadFull) {
							loadMoring = true;
							boundariesPosition = getLastVisiblePosition();
							loadMoreListFooter.toggleToLoadingState(); // ״̬
							onLoadMoreListener.onLoadMore();
						} else {
							loadMoreListFooter.onToggleToLoadAllState();
						}
					}
				} else {
					if (totalItemCount != lastTotalItemCount || getLastVisiblePosition() < boundariesPosition) {
						boundariesPosition = -1;
					}
				}
				lastTotalItemCount = totalItemCount;
			}
		}

		if (onScrollListener != null) {
			onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	private boolean isNoAction() {
		return !refreshing && !loadMoring;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		if (rollBackScroller.computeScrollOffset()) {
			rollBackScroller.abortAnimation();
		}
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		pulldown = distanceY < 0;
		if (getFirstVisiblePosition() == 0) {
			if (pulldown) {
				pulldownRefershListHeader.updateHeightOffset((int) distanceY);
			} else {
				if (pulldownRefershListHeader.getContentView().getHeight() > pulldownRefershListHeader
						.getRollbackFinalHeight()) {
					pulldownRefershListHeader.updateHeightOffset((int) distanceY);
					setSelection(0);
				}
			}
			if (!pulldown) {
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public boolean isAllowReadyRefresh() {
		return onRefreshListener != null && isNoAction();
	}

	public OnRefreshListener getOnRefreshListener() {
		return onRefreshListener;
	}

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	public OnLoadMoreListener getOnLoadMoreListener() {
		return onLoadMoreListener;
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		this.onLoadMoreListener = onLoadMoreListener;
	}

	public BasePulldownRefershListHeader getPulldownRefershListHeader() {
		return pulldownRefershListHeader;
	}

	public void setPulldownRefershListHeader(BasePulldownRefershListHeader pulldownRefershListHeader) {
		this.pulldownRefershListHeader = pulldownRefershListHeader;
	}

	public BaseLoadMoreListFooter getLoadMoreListFooter() {
		return loadMoreListFooter;
	}

	public void setLoadMoreListFooter(BaseLoadMoreListFooter loadMoreListFooter) {
		this.loadMoreListFooter = loadMoreListFooter;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

	public void setRes_loading(int res_loading) {
		pulldownRefershListHeader.setRes_loading(res_loading);
	}

	public void setNomoreLoad(boolean isNomoreLoad) {
		pulldownRefershListHeader.setNomoreLoad(isNomoreLoad);
	}

}
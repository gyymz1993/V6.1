package com.zizisteward.view.refresh;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.utils.ViewUtil;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

public abstract class BasePulldownRefershListHeader extends LinearLayout {
	public static final float OFFSET_RADIO = 0.4f; // ����ƫ�ƣ�ģ�������Ƥ��ĸо���
	private int contentViewHeight; // ���ݸ߶�
	private int contentViewMinHeight; // ������С�߶�
	public State state; // ״̬
	private View contentView; // ������ͼ
	private OnAllowReadyRefreshListener onAllowReadyRefreshListener;
	
	
	protected int res_normal = R.string.superlistview_header_hint_normal;
	protected int res_loading = R.string.superlistview_header_hint_loading;
	protected int res_ready = R.string.superlistview_header_hint_ready;
	
	protected boolean isNomoreLoad = false;
	
	public void setNomoreLoad(boolean isNomoreLoad) {
		this.isNomoreLoad = isNomoreLoad;
	}

	public void setRes_loading(int res_loading) {
		this.res_loading = res_loading;
	}

	public BasePulldownRefershListHeader(Context context) {
		super(context);
		setClickable(true);
		setOrientation(LinearLayout.HORIZONTAL); // ��������
		setGravity(Gravity.BOTTOM); // �����ݾ��ڵײ�
		state = State.NORMAL; // ��ʼ��״̬
		setLayoutParams(new AbsListView.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		/* ��ʼ��������ͼ */
		contentView = onGetContentView();
		contentViewHeight = ViewUtil.getMeasuredHeight(contentView); // �������ݵĸ߶�
		contentView.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, contentViewMinHeight)); // ����������ͼ�ĸ߶�Ϊ��С�߶�
		addView(contentView); // ��������ͼ��ӵ�����ˢ�²�����
	}

	/**
	 * ���¸߶�
	 * 
	 * @param newHeight
	 */
	public void updateHeight(int newHeight) {
		/* ����µĸ߶�С�ڻع������ո߶Ⱦͽ�������Ϊ�ع������ո߶ȣ�Ȼ����¸߶� */
		if (newHeight < getRollbackFinalHeight()) {
			newHeight = getRollbackFinalHeight();
		}
		ViewUtil.setViewHeight(contentView, newHeight);

		/* ���ݵ�ǰ��ͬ��״̬�Լ��µĸ߶ȣ�������ͬ�Ĵ��� */
		if (state == State.NORMAL) { // �����ǰ״̬������
			if (newHeight >= contentViewHeight
					&& (onAllowReadyRefreshListener == null || onAllowReadyRefreshListener
							.isAllowReadyRefresh())) { // ����µĸ߶ȴ���ԭʼ�߶ȣ�˵����Ҫ������״̬����׼��ˢ��״̬��
				state = State.READY_REFRESH; // ����״̬Ϊ׼��ˢ��
				onNormalToReadyRefreshState(); // ������Ӧ�ص������޸Ľ�����ʾ
			}
		} else if (state == State.READY_REFRESH) { // �����ǰ״̬��׼��ˢ��
			if (newHeight < contentViewHeight) { // ����µĸ߶�С��ԭʼ�߶ȣ�˵����Ҫ��׼��ˢ��״̬��Ϊ����״̬��
				state = State.NORMAL; // ����״̬Ϊ����
				onReadyRefreshToNormalState(); // ������Ӧ�ص������޸Ľ�����ʾ
			}
		} else if (state == State.REFRESHING_TO_NORMAL) { // ������ˢ���б�Ϊ����
			if (newHeight <= contentViewMinHeight) { // ����Ѿ��ع�����С�߶���
				state = State.NORMAL; // ����״̬Ϊ����
				onRefreshingToNormalState(); // ������Ӧ�ص������޸Ľ�����ʾ
			}
		} else if (state == State.NORMAL_TO_REFRESHING) { // ���������������״ֱ̬�ӱ�Ϊˢ����״̬
			if (newHeight >= contentViewHeight) { // ����µĸ߶ȴ���ԭʼ�߶ȣ�˵����Ҫ������״̬����׼��ˢ��״̬��
				setState(State.REFRESHING);
				onNormalToRefreshingState();
			}
		}
	}

	/**
	 * ���������ķ�ʽ���¸߶�
	 * 
	 * @param newOffSetHeight
	 */
	public void updateHeightOffset(int newOffSetHeight) {
		updateHeight((int) (contentView.getHeight() - (newOffSetHeight * OFFSET_RADIO)));
	}

	/**
	 * ��׼��ˢ�±�Ϊˢ����״̬
	 */
	public void readyRefreshToRefreshingState() {
		setState(State.REFRESHING);
		onReadyRefreshToRefresingState();
	}

	/**
	 * �л���ˢ����ת��Ϊ����״̬
	 */
	public void toggleToRefreshingToNormalState() {
		setState(State.REFRESHING_TO_NORMAL);
	}

	/**
	 * ��ȡ�ع��߶�
	 * 
	 * @return �ع��߶ȣ�����ˢ����״̬ʱ�ع��߶���ԭʼ�߶ȣ�����һ������С�߶�
	 */
	public int getRollbackFinalHeight() {
		return state == State.REFRESHING ? contentViewHeight
				: contentViewMinHeight; // �������ˢ�¾ͻع���ԭʼ�߶ȣ�����ع�����С�߶�
	}

	/**
	 * ��ȡ������ͼ
	 * 
	 * @return ������ͼ
	 */
	public abstract View onGetContentView();

	/**
	 * ������״̬��Ϊ׼��ˢ��״̬
	 */
	public abstract void onNormalToReadyRefreshState();

	/**
	 * ��׼��ˢ��״̬��Ϊ����״̬
	 */
	public abstract void onReadyRefreshToNormalState();

	/**
	 * ��׼��ˢ��״̬��Ϊˢ����״̬
	 */
	public abstract void onReadyRefreshToRefresingState();

	/**
	 * ��ˢ����״̬��Ϊ����״̬
	 */
	public abstract void onRefreshingToNormalState();

	/**
	 * ������״̬��Ϊˢ����״̬
	 */
	public abstract void onNormalToRefreshingState();

	/**
	 * �л�������״̬
	 */
	public abstract void onToggleToNormalState();

	/**
	 * �л���ˢ����״̬
	 */
	public abstract void onToggleToRefreshingState();

	/**
	 * ��ȡ״̬
	 * 
	 * @return ״̬
	 */
	public State getState() {
		return state;
	}

	/**
	 * ����״̬״̬
	 * 
	 * @param state
	 *            ״̬
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * ��ȡ������ͼ�ĸ߶�
	 * 
	 * @return ������ͼ�ĸ߶�
	 */
	public int getContentViewHeight() {
		return contentViewHeight;
	}

	/**
	 * ����������ͼ�ĸ߶�
	 * 
	 * @param contentHeight
	 *            ������ͼ�ĸ߶�
	 */
	public void setContentViewHeight(int contentHeight) {
		this.contentViewHeight = contentHeight;
	}

	/**
	 * ��ȡ������ͼ����С�߶�
	 * 
	 * @return the contentMinHeight ������ͼ����С�߶�
	 */
	public int getContentViewMinHeight() {
		return contentViewMinHeight;
	}

	/**
	 * ����������ͼ����С�߶�
	 * 
	 * @param contentViewMinHeight
	 *            ������ͼ����С�߶�
	 */
	public void setContentViewMinHeight(int contentViewMinHeight) {
		this.contentViewMinHeight = contentViewMinHeight;
	}

	/**
	 * ��ȡ������ͼ
	 * 
	 * @return the contentView ������ͼ
	 */
	public View getContentView() {
		return contentView;
	}

	/**
	 * ����������ͼ
	 * 
	 * @param contentView
	 *            ������ͼ
	 */
	public void setContentView(View contentView) {
		this.contentView = contentView;
	}

	/**
	 * @return the onAllowReadyRefreshListener
	 */
	public OnAllowReadyRefreshListener getOnAllowReadyRefreshListener() {
		return onAllowReadyRefreshListener;
	}

	/**
	 * @param onAllowReadyRefreshListener
	 *            the onAllowReadyRefreshListener to set
	 */
	public void setOnAllowReadyRefreshListener(
			OnAllowReadyRefreshListener onAllowReadyRefreshListener) {
		this.onAllowReadyRefreshListener = onAllowReadyRefreshListener;
	}

	/**
	 * ״̬
	 */
	public enum State {
		/**
		 * ����
		 */
		NORMAL,
		/**
		 * ׼��ˢ��
		 */
		READY_REFRESH,
		/**
		 * ˢ����
		 */
		REFRESHING,
		/**
		 * ˢ���е�����
		 */
		REFRESHING_TO_NORMAL,
		/**
		 * ������ˢ����
		 */
		NORMAL_TO_REFRESHING;
	}

	public interface OnAllowReadyRefreshListener {
		public boolean isAllowReadyRefresh();
	}
}
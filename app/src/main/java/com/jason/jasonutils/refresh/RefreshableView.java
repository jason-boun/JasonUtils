package com.jason.jasonutils.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jason.jasonutils.R;

/**
 * 可进行下拉刷新的自定义控件。
 */
public class RefreshableView extends LinearLayout implements OnTouchListener {

	/**
	 * 下拉刷新的回调接口
	 */
	private PullToRefreshListener mListener;

	/**
	 * 下拉头的View
	 */
	private View header;

	/**
	 * 需要去下拉刷新的ListView
	 */
	private ListView listView;

	/**
	 * 刷新时显示的进度条
	 */
	private ProgressBar progressBar;

	/**
	 * 指示下拉和释放的箭头
	 */
	private ImageView arrow;

	/**
	 * 指示下拉和释放的文字描述
	 */
	private TextView description;

	/**
	 * 上次更新时间的文字描述
	 */
	private TextView updateAt;

	/**
	 * 下拉头的布局参数
	 */
	private MarginLayoutParams headerLayoutParams;

	/**
	 * 为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分
	 */
	private int mId = -1;

	/**
	 * 下拉头的高度
	 */
	private int hideHeaderHeight;

	/**
	 * 当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
	 * STATUS_REFRESHING 和 STATUS_REFRESH_FINISHED
	 */
	public HeaderStatus currentStatus = HeaderStatus.FINISHED;;

	/**
	 * 记录上一次的状态是什么，避免进行重复操作
	 */
	private HeaderStatus lastStatus = currentStatus;

	/**
	 * 手指按下时的屏幕纵坐标
	 */
	private float yDown;

	/**
	 * 在被判定为滚动之前用户手指可以移动的最大值。
	 */
	private int touchSlop;

	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce;

	/**
	 * 当前是否可以下拉，只有ListView滚动到头的时候才允许下拉
	 */
	private boolean ableToPull;

	/**
	 * 下拉刷新控件的构造函数，会在运行时动态添加一个下拉头的布局。
	 * 
	 * @param context
	 * @param attrs
	 */
	public RefreshableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		header = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_view, null, true);
		arrow = (ImageView) header.findViewById(R.id.arrow);
		progressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
		description = (TextView) header.findViewById(R.id.description);
		updateAt = (TextView) header.findViewById(R.id.updated_at);
		
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		RefreshViewUtil.refreshUpdatedAtValue(updateAt, mId);
		setOrientation(VERTICAL);
		addView(header, 0);
	}

	/**
	 * 进行一些关键性的初始化操作，比如：将下拉头向上偏移进行隐藏，给ListView注册touch事件。
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			hideHeaderHeight = -header.getHeight();
			headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
			headerLayoutParams.topMargin = hideHeaderHeight;
			listView = (ListView) getChildAt(1);
			listView.setOnTouchListener(this);
			loadOnce = true;
		}
	}

	/**
	 * 当ListView被触摸时调用，其中处理了各种下拉刷新的具体逻辑。
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		setIsAbleToPull(event);
		if (ableToPull) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				yDown = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				float yMove = event.getRawY();
				int distance = (int) (yMove - yDown);
				// 如果手指是下滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
				if (distance <= 0 && headerLayoutParams.topMargin <= hideHeaderHeight) {
					return false;
				}
				if (distance < touchSlop) {
					return false;
				}
				if (currentStatus != HeaderStatus.REFRESHING) {
					if (headerLayoutParams.topMargin > 0) {
						currentStatus = HeaderStatus.RELEASE;
					} else {
						currentStatus = HeaderStatus.PULL;
					}
					// 通过偏移下拉头的topMargin值，来实现下拉效果
					headerLayoutParams.topMargin = (distance / 2) + hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
				}
				break;
			case MotionEvent.ACTION_UP:
			default:
				if (currentStatus == HeaderStatus.RELEASE) {
					// 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
					new RefreshingTask(this, header,headerLayoutParams,mListener).execute();
				} else if (currentStatus == HeaderStatus.PULL) {
					// 松手时如果是下拉状态，就去调用隐藏下拉头的任务
					new HideHeaderTask(this,header,headerLayoutParams).execute();
				}
				break;
			}
			// 时刻记得更新下拉头中的信息
			if (currentStatus == HeaderStatus.PULL
					|| currentStatus == HeaderStatus.RELEASE) {
				updateHeaderView();
				// 当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
				listView.setPressed(false);
				listView.setFocusable(false);
				listView.setFocusableInTouchMode(false);
				lastStatus = currentStatus;
				// 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
				return true;
			}
		}
		return false;
	}

	/**
	 * 给下拉刷新控件注册一个监听器。
	 * 
	 * @param listener
	 *            监听器的实现。
	 * @param id
	 *            为了防止不同界面的下拉刷新在上次更新时间上互相有冲突， 请不同界面在注册下拉刷新监听器时一定要传入不同的id。
	 */
	public void setOnRefreshListener(PullToRefreshListener listener, int id) {
		mListener = listener;
		mId = id;
	}

	/**
	 * 当所有的刷新逻辑完成后，记录调用一下，否则你的ListView将一直处于正在刷新状态。
	 */
	public void finishRefreshing() {
		currentStatus = HeaderStatus.FINISHED;
		RefreshViewUtil.updateRefreshTimeStamp(this.getContext(), mId);
		new HideHeaderTask(this,header,headerLayoutParams).execute();
	}

	/**
	 * 根据当前ListView的滚动状态来设定 {@link #ableToPull}
	 * 的值，每次都需要在onTouch中第一个执行，这样可以判断出当前应该是滚动ListView，还是应该进行下拉。
	 * 
	 * @param event
	 */
	private void setIsAbleToPull(MotionEvent event) {
		View firstChild = listView.getChildAt(0);
		if (firstChild != null) {
			int firstVisiblePos = listView.getFirstVisiblePosition();
			if (firstVisiblePos == 0 && firstChild.getTop() == 0) {
				if (!ableToPull) {
					yDown = event.getRawY();
				}
				// 如果首个元素的上边缘，距离父布局值为0，就说明ListView滚动到了最顶部，此时应该允许下拉刷新
				ableToPull = true;
			} else {
				if (headerLayoutParams.topMargin != hideHeaderHeight) {
					headerLayoutParams.topMargin = hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
				}
				ableToPull = false;
			}
		} else {
			// 如果ListView中没有元素，也应该允许下拉刷新
			ableToPull = true;
		}
	}

	/**
	 * 更新下拉头中的信息。
	 */
	public void updateHeaderView() {
		if (lastStatus != currentStatus) {
			if (currentStatus == HeaderStatus.PULL) {
				description.setText("下拉可以刷新");
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == HeaderStatus.RELEASE) {
				description.setText("下拉可以刷新");
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == HeaderStatus.REFRESHING) {
				description.setText("正在刷新…");
				progressBar.setVisibility(View.VISIBLE);
				arrow.clearAnimation();
				arrow.setVisibility(View.GONE);
			}
			RefreshViewUtil.refreshUpdatedAtValue(updateAt, mId);
		}
	}

	/**
	 * 根据当前的状态来旋转箭头。
	 */
	private void rotateArrow() {
		float pivotX = arrow.getWidth() / 2f;
		float pivotY = arrow.getHeight() / 2f;
		float fromDegrees = 0f;
		float toDegrees = 0f;
		if (currentStatus == HeaderStatus.PULL) {
			fromDegrees = 180f;
			toDegrees = 360f;
		} else if (currentStatus == HeaderStatus.RELEASE) {
			fromDegrees = 0f;
			toDegrees = 180f;
		}
		RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
		animation.setDuration(100);
		animation.setFillAfter(true);
		arrow.startAnimation(animation);
	}

}

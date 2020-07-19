package com.giants3.android;




import java.util.ArrayList;


public class ActionRepeatController {
	public static final String TAG="ActionRepeatController";
	public static final int MAX_REPEAT_TIME = 1000 * 20;
	public static final int DEFAULT_INSTANCE_LIVE_TIME = 1000;
	
	private static final Integer DEFAULT_OBJECT = Integer.valueOf(0);
	
	private static ActionRepeatController INSTANCE = null;
	private ArrayList<objectActionInfo> objectActionInfoList = null;
	private long instanceLiveTime = DEFAULT_INSTANCE_LIVE_TIME;
	
	
	public static void disableAction(int actionId, int disableTimeInMillisecond) {
		GetInstance().updateLiveTime(disableTimeInMillisecond);
		GetInstance().changeLiveTime();
		
		objectActionInfo objectActionInfo = GetInstance().getObjectActionInfo(DEFAULT_OBJECT, actionId);
		if (objectActionInfo == null) {
			objectActionInfo = GetInstance().createObjectActionInfo(DEFAULT_OBJECT, actionId, disableTimeInMillisecond);
			GetInstance().addToObjectActionInfoList(objectActionInfo);
			return;
		} 
		if (objectActionInfo.isActionEnable()) {
			objectActionInfo.updateCurrentActionTime();
			objectActionInfo.updateRepeatEnableTime(disableTimeInMillisecond);
		}
	}
	
	/*
	 *  判断 同一个 actionId 动作 能否运行执行
	 *  repeatDisableInMillisecond 一个动作操作后 在规定时间内不允许再执行
	 */
	public static boolean isActionEnable(int actionId, int repeatDisableInMillisecond) {
		return isActionEnable(DEFAULT_OBJECT, actionId, repeatDisableInMillisecond);
	}
	
	public static boolean isActionEnableInOneSecond(int actionId) {
		return isActionEnable(DEFAULT_OBJECT, actionId, 1000);
	}
	
	public static boolean isActionEnableInTwoSecond(int actionId) {
		return isActionEnable(DEFAULT_OBJECT, actionId, 2000);
	}
	
	/*
	 *  判断当前 对象 同一个 actionId 动作  在规定时间 repeatDisableInMillisecond 内是否重复
	 *  由于对象会被 INSTANCE 持有一段时间，所以暂时不开放对象进行比较
	 */
	private static boolean isActionEnable(Object obj, int actionId, int repeatDisableInMillisecond) {
		synchronized (DEFAULT_OBJECT) {
			if (repeatDisableInMillisecond <= 0 || obj == null || repeatDisableInMillisecond > MAX_REPEAT_TIME) {

				return true;
			}
			GetInstance().updateLiveTime(repeatDisableInMillisecond);
			GetInstance().changeLiveTime();
			
			objectActionInfo objectActionInfo = GetInstance().getObjectActionInfo(obj, actionId);
			if (objectActionInfo == null) {
				objectActionInfo = GetInstance().createObjectActionInfo(obj, actionId, repeatDisableInMillisecond);
				GetInstance().addToObjectActionInfoList(objectActionInfo);
				return true;
			} 
			if (objectActionInfo.isActionEnable()) {
				objectActionInfo.updateCurrentActionTime();
				objectActionInfo.updateRepeatEnableTime(repeatDisableInMillisecond);
				return true;
			}
			return false;
		}
	}
	
	public static boolean isInstanceExistForTest() {
		return INSTANCE != null;
	}
	
	public static void clearInstanceForTest() {
		INSTANCE = null;
	}
	
	
	private static ActionRepeatController GetInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ActionRepeatController();
			INSTANCE.startDestoryThread();
		}
		return INSTANCE;
	}
	
	private void updateLiveTime(int currentRepeatDisableInMillisecond) {
		// 控制器存在时间设置成 当前需要管理的时间的 5 倍
		if (currentRepeatDisableInMillisecond * 5 > instanceLiveTime) {
			instanceLiveTime = currentRepeatDisableInMillisecond * 2;
		}
	}
	
	private final boolean isLiveTimeChange(long lastLiveTive) {
		return INSTANCE.getLiveTime() > lastLiveTive;
	}
	
	private final void changeLiveTime() {
		instanceLiveTime += 1;
	}
	
	private void addToObjectActionInfoList(objectActionInfo objectActionInfo){
		if (objectActionInfo != null && objectActionInfoList != null) {
			objectActionInfoList.add(objectActionInfo);
		}
	}
	
	private objectActionInfo createObjectActionInfo(Object object, int actionId, int repeatDisableInMillisecond) {
		return new objectActionInfo(object, actionId, repeatDisableInMillisecond);
	}
	
	private ActionRepeatController() {
		objectActionInfoList = new ArrayList<objectActionInfo>();
		objectActionInfoList.clear();

	}
	
	private objectActionInfo getObjectActionInfo(Object object, int actionId) {
		int count = objectActionInfoList.size();
		for (int i = 0; i < count; i++) {
			if (objectActionInfoList.get(i).isCurrentAction(object, actionId)) {
				return objectActionInfoList.get(i);
			}
		}
		return null;
	}
	
	private long getLiveTime() {
		return instanceLiveTime;
	}
	
	private void startDestoryThread() {
		new Thread() {
			@Override
			public void run() {
				if (INSTANCE == null) {
					return;
				}
				long liveTime = -1;
				do {
					synchronized (DEFAULT_OBJECT) {
						if (liveTime == -1) {
							liveTime = INSTANCE.getLiveTime();
						} else if (INSTANCE.isLiveTimeChange(liveTime)) {
							liveTime = INSTANCE.getLiveTime();
						} else {
							liveTime = 0;
							INSTANCE = null;
						}
					}
					try {
						if (liveTime > 0) {
							Thread.sleep(liveTime);
						}
					} catch (InterruptedException e) {

					}
				} while (liveTime > 0);
			}
		}.start();	
	}

	
	class objectActionInfo {
		public Object object;
		public int actionId;
		public int repeatDisableInMillisecond;
		public long lastActionTime = 0;
		
		public objectActionInfo(Object object, int actionId, int repeatDisableInMillisecond) {
			this.object = object;
			this.actionId = actionId;
			this.repeatDisableInMillisecond = repeatDisableInMillisecond;
			this.lastActionTime = System.currentTimeMillis();
		}
		
		public boolean isEmptyObjectActionInfo() {
			return object == null;
		}
		
		public boolean isCurrentAction(Object object, int actionId) {
			return this.object == object && this.actionId == actionId;
		}
		
		public void updateRepeatEnableTime(int newRepeatTime) {
			repeatDisableInMillisecond = newRepeatTime;
		}
		
		public void updateCurrentActionTime() {
			lastActionTime = System.currentTimeMillis();
		}
		
		public boolean isActionEnable() {
			if (isEmptyObjectActionInfo()) {
				return true;
			}
			return System.currentTimeMillis() - lastActionTime > repeatDisableInMillisecond;
		}
	}
}

package voc.cn.cnvoccoin.network;

public class ServerTime {
    private static volatile ServerTime sInstance;
    private TimeStamp mTimeStamp;

    public static ServerTime getDefault() {
        if (sInstance == null) {
            synchronized (ServerTime.class) {
                if (sInstance == null) {
                    sInstance = new ServerTime();
                }
            }
        }
        return sInstance;
    }

    private ServerTime() {
        mTimeStamp = new TimeStamp();
    }

    public long getTimeStamp() {
        synchronized (mTimeStamp) {
            if (mTimeStamp.lastSyncMsec < 0) {
                return System.currentTimeMillis();
            } else {
                return System.currentTimeMillis() - mTimeStamp.lastSyncMsec + mTimeStamp.serverMsec;
            }
        }
    }

    public void syncTimeStamp(long serverMsec) {
        synchronized (mTimeStamp) {
            mTimeStamp.serverMsec = serverMsec;
            mTimeStamp.lastSyncMsec = System.currentTimeMillis();
        }
    }

    private class TimeStamp {
        long lastSyncMsec = -1;
        long serverMsec = -1;
    }
}

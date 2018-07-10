package voc.cn.cnvoccoin.entity;

public class NextDateEntity {
    private String accessKey;
    private String appId;
    private String eventId;
    private Data data;

    public NextDateEntity(String accessKey, String appId, String eventId, Data data) {
        this.accessKey = accessKey;
        this.appId = appId;
        this.eventId = eventId;
        this.data = data;
    }


    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String tokenId;
        private String deviceId;
        private String ip;
        private long timestamp;
        private String signupPlatform;
        private String phone;
        private String inviteTokenId;
        private String accountName;

        public Data() {
        }

        public Data(String tokenId, String deviceId, String ip, long timestamp, String signupPlatform, String phone) {
            this.tokenId = tokenId;
            this.deviceId = deviceId;
            this.ip = ip;
            this.timestamp = timestamp;
            this.signupPlatform = signupPlatform;
            this.phone = phone;
        }

        public Data(String tokenId, String deviceId, String ip, long timestamp, String signupPlatform, String phone, String inviteTokenId, String accountName) {
            this.tokenId = tokenId;
            this.deviceId = deviceId;
            this.ip = ip;
            this.timestamp = timestamp;
            this.signupPlatform = signupPlatform;
            this.phone = phone;
            this.inviteTokenId = inviteTokenId;
            this.accountName = accountName;
        }

        public Data(String tokenId, String deviceId, String ip, long timestamp, String signupPlatform, String phone, String accountName) {
            this.tokenId = tokenId;
            this.deviceId = deviceId;
            this.ip = ip;
            this.timestamp = timestamp;
            this.signupPlatform = signupPlatform;
            this.phone = phone;
            this.accountName = accountName;
        }

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getSignupPlatform() {
            return signupPlatform;
        }

        public void setSignupPlatform(String signupPlatform) {
            this.signupPlatform = signupPlatform;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getInviteTokenId() {
            return inviteTokenId;
        }

        public void setInviteTokenId(String inviteTokenId) {
            this.inviteTokenId = inviteTokenId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }
    }
}

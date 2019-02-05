/**
 * Modified MIT License
 * <p>
 * Copyright 2018 OneSignal
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * 1. The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * 2. All copies of substantial portions of the Software may only be used in connection
 * with services provided by OneSignal.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.onesignal;

import org.json.JSONObject;

import androidx.annotation.NonNull;

public class OSEmailSubscriptionState implements Cloneable {

    OSObservable<Object, OSEmailSubscriptionState> observable;
    private String emailUserId;
    private String emailAddress;

    OSEmailSubscriptionState(boolean asFrom) {
        observable = new OSObservable<>("changed", false);

        if (asFrom) {
            emailUserId = OneSignalPrefs.getString(OneSignalPrefs.PREFS_ONESIGNAL,
                    OneSignalPrefs.PREFS_ONESIGNAL_EMAIL_ID_LAST, null);
            emailAddress = OneSignalPrefs.getString(OneSignalPrefs.PREFS_ONESIGNAL,
                    OneSignalPrefs.PREFS_ONESIGNAL_EMAIL_ADDRESS_LAST, null);
        } else {
            emailUserId = OneSignal.getEmailId();
            emailAddress = OneSignalStateSynchronizer.getEmailStateSynchronizer().getRegistrationId();
        }
    }

    void clearEmailAndId() {
        boolean changed = emailUserId != null || emailAddress != null;
        emailUserId = null;
        emailAddress = null;
        if (changed)
            observable.notifyChange(this);
    }

    public String getEmailUserId() {
        return emailUserId;
    }

    void setEmailUserId(@NonNull String id) {
        boolean changed = !id.equals(emailUserId);
        emailUserId = id;
        if (changed)
            observable.notifyChange(this);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    void setEmailAddress(@NonNull String email) {
        boolean changed = !email.equals(emailAddress);
        emailAddress = email;
        if (changed)
            observable.notifyChange(this);
    }

    public boolean getSubscribed() {
        return emailUserId != null && emailAddress != null;
    }

    void persistAsFrom() {
        OneSignalPrefs.saveString(OneSignalPrefs.PREFS_ONESIGNAL,
                OneSignalPrefs.PREFS_ONESIGNAL_EMAIL_ID_LAST, emailUserId);
        OneSignalPrefs.saveString(OneSignalPrefs.PREFS_ONESIGNAL,
                OneSignalPrefs.PREFS_ONESIGNAL_EMAIL_ADDRESS_LAST, emailAddress);
    }

    boolean compare(OSEmailSubscriptionState from) {
        return !(emailUserId != null ? emailUserId : "").equals(from.emailUserId != null ? from.emailUserId : "")
                || !(emailAddress != null ? emailAddress : "").equals(from.emailAddress != null ? from.emailAddress : "");
    }

    protected Object clone() {
        try {
            return super.clone();
        } catch (Throwable t) {
        }
        return null;
    }

    public JSONObject toJSONObject() {
        JSONObject mainObj = new JSONObject();

        try {
            if (emailUserId != null)
                mainObj.put("emailUserId", emailUserId);
            else
                mainObj.put("emailUserId", JSONObject.NULL);

            if (emailAddress != null)
                mainObj.put("emailAddress", emailAddress);
            else
                mainObj.put("emailAddress", JSONObject.NULL);

            mainObj.put("subscribed", getSubscribed());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return mainObj;
    }

    @Override
    public String toString() {
        return toJSONObject().toString();
    }
}

package com.funnysec.richardtang.androidkiller4j.core.device.process;

import com.android.annotations.NonNull;
import com.android.annotations.concurrency.GuardedBy;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProcessMessageReceiverTask implements Runnable {

    private static final String COMMAND = "top";

    private static final int DEVICE_POLL_INTERVAL_MSEC = 1000;

    private final IDevice mDevice;
    private final ProcessMessageOutputReceiver mReceiver;
    private final ProcessMessageParser mParser;
    private final AtomicBoolean mCancelled;

    @GuardedBy("this")
    private final Set<ProcessMessageListener> mListeners = new HashSet<ProcessMessageListener>();

    public ProcessMessageReceiverTask(@NonNull IDevice device) {
        mDevice = device;

        mReceiver = new ProcessMessageOutputReceiver();
        mParser = new ProcessMessageParser();
        mCancelled = new AtomicBoolean();
    }

    @Override
    public void run() {
        while (!mDevice.isOnline()) {
            try {
                Thread.sleep(DEVICE_POLL_INTERVAL_MSEC);
            } catch (InterruptedException e) {
                return;
            }
        }

        try {
            mDevice.executeShellCommand(COMMAND, mReceiver, 0);
        } catch (Exception e) {
            System.out.println("进程监听失败");
        }
    }

    public void stop() {
        mCancelled.set(true);
    }

    private class ProcessMessageOutputReceiver extends MultiLineReceiver {

        public ProcessMessageOutputReceiver() {
            setTrimLine(false);
        }

        @Override
        public boolean isCancelled() {
            return mCancelled.get();
        }

        @Override
        public void processNewLines(@NonNull String[] lines) {
            if (!mCancelled.get()) {
                processLogLines(lines);
            }
        }

        private void processLogLines(String[] lines) {
            List<ProcessMessage> newMessages = mParser.processLogLines(lines);
            if (!newMessages.isEmpty()) {
                notifyListeners(newMessages);
            }
        }
    }

    public synchronized void addLogCatListener(ProcessMessageListener l) {
        mListeners.add(l);
    }

    public synchronized void removeLogCatListener(ProcessMessageListener l) {
        mListeners.remove(l);
    }

    private synchronized void notifyListeners(List<ProcessMessage> messages) {
        for (ProcessMessageListener l : mListeners) {
            l.log(messages);
        }
    }
}
